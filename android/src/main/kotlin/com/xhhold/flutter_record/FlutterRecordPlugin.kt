package com.xhhold.flutter_record

import AudioStatus
import CodecConfig
import OutputConfig
import RecordCode
import RecordConfig
import RecordFlutter
import RecordInfo
import RecordPlatform
import RecordResult
import ServiceInfo
import VideoStatus
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import androidx.core.content.getSystemService
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

typealias OnActivityResultListener = (requestCode: Int, resultCode: Int, data: Intent?) -> Unit

class FlutterRecordPlugin : FlutterPlugin, ActivityAware, ActivityResultListener, RecordPlatform,
    CoroutineScope, ServiceConnection {
    private val coroutineScopeContext = SupervisorJob() + Dispatchers.Main.immediate
    private var context: Context? = null
    private var activity: FlutterActivity? = null
    private var recordFlutter: RecordFlutter? = null
    private var serviceController: RecordService.RecordServiceController? = null
    private var mediaProjectionResult: Pair<Int, Intent>? = null
    private val mediaProjectionManager by lazy { activity?.getSystemService<MediaProjectionManager>() }

    private val activityResults = ConcurrentHashMap<Int, OnActivityResultListener>()
    private val requestCodeGenerator = AtomicInteger(0)

    @Volatile
    private var requestingMediaProjection = false

    override val coroutineContext: CoroutineContext get() = coroutineScopeContext

    companion object {
        const val TAG = "FlutterRecordPlugin"
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        context = binding.applicationContext
        RecordPlatform.setUp(binding.binaryMessenger, this)
        recordFlutter = RecordFlutter(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        RecordPlatform.setUp(binding.binaryMessenger, null)
        recordFlutter = null
        context = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        binding.addActivityResultListener(this)
        activity = binding.activity as FlutterActivity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity as FlutterActivity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        val activityResult = activityResults.remove(requestCode)
        activityResult?.invoke(requestCode, resultCode, data)
        return activityResult != null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        serviceController = service as RecordService.RecordServiceController
        serviceController?.onError = ::onError
        serviceController?.onInfo = ::onInfo
        serviceController?.onAudioStatus = ::onAudioStatus
        serviceController?.onVideoStatus = ::onVideoStatus
        recordFlutter?.onUpdateService(ServiceInfo(ServiceState.CONNECTED)) {}
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        recordFlutter?.onUpdateService(ServiceInfo(ServiceState.DISCONNECTED)) {}
        serviceController?.onError = null
        serviceController?.onInfo = null
        serviceController?.onAudioStatus = null
        serviceController?.onVideoStatus = null
        serviceController = null
    }

    private suspend fun requestMediaProjection(): Pair<Int, Intent?>? {
        if (requestingMediaProjection) {
            return null
        }
        requestingMediaProjection = true
        val intent = mediaProjectionManager?.createScreenCaptureIntent()
        val requestCode = requestCodeGenerator.getAndIncrement()
        if (requestCode >= 1000) {
            requestCodeGenerator.set(0)
        }
        val data = withCoroutine {
            activityResults[requestCode] = { _, resultCode, data ->
                if (this.context.isActive) {
                    runCatching {
                        resume(Pair(resultCode, data))
                    }
                }
            }
            activity?.startActivityForResult(intent, requestCode)
        }.getOrNull()
        activityResults.remove(requestCode)
        requestingMediaProjection = false
        return data
    }

    private fun startRecordService() {
        context?.let {
            val intent = Intent(it, RecordService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.startForegroundService(intent)
            } else {
                it.startService(intent)
            }
            it.bindService(intent, this, Context.BIND_IMPORTANT)
        }
    }

    override fun connect(): RecordResult {
        return try {
            startRecordService()
            RecordResult(code = RecordCode.SUCCESS)
        } catch (e: Exception) {
            RecordResult(code = RecordCode.SERVICESTARTFAILED, message = e.message)
        }
    }

    override fun request(callback: (Result<RecordResult>) -> Unit) {
        launch {
            try {
                val result = requestMediaProjection()
                val resultCode = result?.first
                val intent = result?.second
                if (resultCode == null && intent == null) {
                    callback(Result.success(RecordResult(code = RecordCode.REQUESTFAILED)))
                } else if (resultCode == Activity.RESULT_OK && intent != null) {
                    mediaProjectionResult = Pair(resultCode, intent)
                    callback(Result.success(RecordResult(code = RecordCode.SUCCESS)))
                } else {
                    callback(
                        Result.success(
                            RecordResult(
                                code = RecordCode.ERROR,
                                externalCode = resultCode?.toLong(),
                                message = "Request media projection failed"
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                callback(
                    Result.success(
                        RecordResult(
                            code = RecordCode.REQUESTFAILED, message = e.message
                        )
                    )
                )
            }
        }
    }

    override fun refreshState() {
        serviceController?.refreshState();
    }

    override fun setCodecConfig(config: CodecConfig): RecordResult {
        return serviceController?.setCodecConfig(config) ?: RecordResult(
            code = RecordCode.SERVICEDISCONNECTED, message = "Record service not connected"
        )
    }

    override fun setOutputConfig(config: OutputConfig): RecordResult {
        return serviceController?.setOutputConfig(config) ?: RecordResult(
            code = RecordCode.SERVICEDISCONNECTED, message = "Record service not connected"
        )

    }

    override fun setRecordConfig(config: RecordConfig): RecordResult {
        return serviceController?.setRecordConfig(config) ?: RecordResult(
            code = RecordCode.SERVICEDISCONNECTED, message = "Record service not connected"
        )
    }


    override fun startRecord(): RecordResult {
        if (mediaProjectionResult == null) {
            return RecordResult(
                code = RecordCode.REQUESTFAILED, message = "Request media projection failed"
            )
        }
        return serviceController?.startRecord(mediaProjectionResult!!) ?: RecordResult(
            code = RecordCode.SERVICEDISCONNECTED, message = "Record service not connected"
        )
    }

    override fun pauseRecord(): RecordResult {
        return serviceController?.pauseRecord() ?: RecordResult(
            code = RecordCode.SERVICEDISCONNECTED, message = "Record service not connected"
        )
    }


    override fun stopRecord(): RecordResult {
        return serviceController?.stopRecord() ?: RecordResult(
            code = RecordCode.SERVICEDISCONNECTED, message = "Record service not connected"
        )
    }

    private fun onError(error: RecordResult) {
        launch(Dispatchers.Main) {
            recordFlutter?.onError(error) {}
        }
    }

    private fun onInfo(info: RecordInfo) {
        launch(Dispatchers.Main) {
            recordFlutter?.onUpdateInfo(info) {}
        }
    }

    private fun onAudioStatus(status: AudioStatus) {
        launch(Dispatchers.Main) {
            recordFlutter?.onUpdateAudioStatus(status) {}
        }
    }

    private fun onVideoStatus(status: VideoStatus) {
        launch(Dispatchers.Main) {
            recordFlutter?.onUpdateVideoStatus(status) {}
        }
    }

}

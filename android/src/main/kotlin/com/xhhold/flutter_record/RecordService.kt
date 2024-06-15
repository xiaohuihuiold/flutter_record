package com.xhhold.flutter_record

import AudioStatus
import CodecConfig
import OutputConfig
import RecordConfig
import RecordInfo
import RecordResult
import VideoStatus
import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat

typealias OnRecordError = (RecordResult) -> Unit
typealias OnRecordInfo = (RecordInfo) -> Unit
typealias OnAudioStatus = (AudioStatus) -> Unit
typealias OnVideoStatus = (VideoStatus) -> Unit

class RecordService : Service() {
    private val controller by lazy { RecordServiceController() }
    private val notificationManager by lazy { RecordNotificationManager(this) }
    private val recordManager by lazy {
        RecordManager(
            this,
            onInfo = ::onInfo,
            onError = ::onError,
            onAudioStatus = ::onAudioStatus,
            onVideoStatus = ::onVideoStatus,
        )
    }

    private var audioStatus: AudioStatus? = null
    private var videoStatus: VideoStatus? = null

    companion object {
        const val TAG = "RecordService"
    }

    private fun startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            var permission =
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION
                )
            if (permission == PackageManager.PERMISSION_DENIED) {
                stopSelf()
                return
            }
            permission =
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.FOREGROUND_SERVICE_MICROPHONE
                )
            if (permission == PackageManager.PERMISSION_DENIED) {
                stopSelf()
                return
            }
        }
        notificationManager.start()
    }

    override fun onCreate() {
        super.onCreate()
        startNotification()
    }

    override fun onBind(intent: Intent?): IBinder = controller

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationManager.stop()
    }

    private fun onError(result: RecordResult) {
        controller.onError?.invoke(result)
    }

    private fun onInfo(info: RecordInfo) {
        controller.onInfo?.invoke(info)
    }

    private fun onAudioStatus(status: AudioStatus) {
        audioStatus = status
        controller.onAudioStatus?.invoke(status)
    }

    private fun onVideoStatus(status: VideoStatus) {
        videoStatus = status
        controller.onVideoStatus?.invoke(status)
        updateNotification()
    }

    private fun updateNotification() {
        val audioStatus = this.audioStatus
        val videoStatus = this.videoStatus
        if (audioStatus == null || videoStatus == null) {
            notificationManager.update("准备就绪")
        } else {
            notificationManager.update("FPS:${videoStatus.fps} \t音频:${(audioStatus.bitrate / 1000).toInt()}kbps \t视频:${(videoStatus.bitrate / 1000).toInt()}kbps")
        }
    }

    inner class RecordServiceController : Binder() {
        var onError: OnRecordError? = null
        var onInfo: OnRecordInfo? = null
        var onAudioStatus: OnAudioStatus? = null
        var onVideoStatus: OnVideoStatus? = null

        fun refreshState() {
            recordManager.refreshState()
        }

        fun setCodecConfig(config: CodecConfig): RecordResult {
            recordManager.setCodecConfig(config)
            return RecordResult(RecordCode.SUCCESS)
        }

        fun setOutputConfig(config: OutputConfig): RecordResult {
            recordManager.setOutputConfig(config)
            return RecordResult(RecordCode.SUCCESS)
        }

        fun setRecordConfig(config: RecordConfig): RecordResult {
            recordManager.setRecordConfig(config)
            return RecordResult(RecordCode.SUCCESS)
        }

        fun startRecord(result: Pair<Int, Intent>): RecordResult {
            return recordManager.startRecord(result)
        }

        fun pauseRecord(): RecordResult {
            recordManager.pauseRecord()
            return RecordResult(RecordCode.SUCCESS)
        }

        fun stopRecord(): RecordResult {
            recordManager.stopRecord()
            return RecordResult(RecordCode.SUCCESS)
        }
    }
}
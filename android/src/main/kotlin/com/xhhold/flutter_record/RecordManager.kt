package com.xhhold.flutter_record

import AudioStatus
import CodecConfig
import OutputConfig
import RecordConfig
import RecordInfo
import RecordResult
import RecordState
import VideoStatus
import android.content.Context
import android.content.Intent
import android.media.MediaCodec
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.util.DisplayMetrics
import android.util.Size
import android.view.WindowManager
import androidx.core.content.getSystemService
import java.nio.ByteBuffer

class RecordManager(
    private val context: Context,
    private val onInfo: OnRecordInfo,
    private val onError: OnRecordError,
    private val onAudioStatus: OnAudioStatus,
    private val onVideoStatus: OnVideoStatus,
) : ScreenRecorder.ScreenCallback, AudioRecorder.AudioCallback {

    private val mediaProjectionManager by lazy { context.getSystemService<MediaProjectionManager>() }
    private var codecConfig: CodecConfig? = null
    private var outputConfig: OutputConfig? = null
    private var recordConfig = RecordConfig(
        videoEnabled = true,
        systemAudioEnabled = false,
        micEnabled = false,
        videoCropStyle = VideoCropStyle.BLACK
    )

    private var screenRecorder: ScreenRecorder? = null
    private var audioRecorder: AudioRecorder? = null
    private var videoOutput: RtspOutput? = null

    private var audioBitrate: Int = 0
    private var videoBitrate: Int = 0
    private var fps: Int = 0

    @Volatile
    var recordState = RecordState.IDLE
        private set

    companion object {
        const val TAG = "RecordManager"
    }

    private fun getScreenSize(): Size {
        val windowManager = context.getSystemService<WindowManager>()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager?.maximumWindowMetrics
            val bounds = windowMetrics?.bounds
            Size(bounds?.width() ?: 0, bounds?.height() ?: 0)
        } else {
            val displayMetrics = DisplayMetrics()
            val display = windowManager?.defaultDisplay
            display?.getMetrics(displayMetrics)
            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels
            Size(width, height)
        }
    }

    fun refreshState() {
        updateInfo()
    }

    fun setCodecConfig(config: CodecConfig) {
        codecConfig = config
    }

    fun setOutputConfig(config: OutputConfig) {
        outputConfig = config
    }

    fun setRecordConfig(config: RecordConfig) {
        recordConfig = config
        updateInfo()
    }

    fun startRecord(result: Pair<Int, Intent>): RecordResult {
        if (recordState != RecordState.STOPPED && recordState != RecordState.IDLE) {
            return RecordResult(RecordCode.RECORDRUNNING)
        }
        val codecConfig = this.codecConfig ?: return RecordResult(RecordCode.CODECCONFIGFAILED)
        // TODO: 释放mediaProjection
        val mediaProjection =
            mediaProjectionManager?.getMediaProjection(result.first, result.second)
                ?: return RecordResult(RecordCode.MEDIAPROJECTIONFAILED)
        screenRecorder = ScreenRecorder(
            mediaProjection, codecConfig.videoConfig, getScreenSize(), this, onError
        )
        audioRecorder = AudioRecorder(
            mediaProjection, codecConfig.audioConfig, this, onError
        )
        screenRecorder?.startRecord()
        audioRecorder?.startRecord()
        recordState = RecordState.RECORDING
        updateInfo()
        val outputConfig = this.outputConfig ?: return RecordResult(RecordCode.OUTPUTCONFIGFAILED)
        videoOutput = RtspOutput(outputConfig, codecConfig) { fps, audioBitrate, videoBitrate ->
            this.fps = fps
            this.audioBitrate = audioBitrate
            this.videoBitrate = videoBitrate
            onAudioStatus(AudioStatus(audioBitrate.toLong()))
            onVideoStatus(VideoStatus(fps.toLong(), videoBitrate.toLong()))
        }
        videoOutput?.connect()
        return RecordResult(RecordCode.SUCCESS)
    }

    fun pauseRecord() {
        screenRecorder?.pauseRecord()
        recordState = RecordState.PAUSED
        updateInfo()
    }

    fun stopRecord() {
        screenRecorder?.stopRecord()
        screenRecorder = null
        audioRecorder?.stopRecord()
        audioRecorder = null
        videoOutput?.disconnect()
        videoOutput = null
        recordState = RecordState.STOPPED
        updateInfo()
    }

    private fun updateInfo() {
        onInfo(RecordInfo(recordState, recordConfig))
    }

    override fun onAACData(buffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        videoOutput?.pushAudio(buffer, bufferInfo)
    }

    override fun onVideoData(buffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        videoOutput?.pushVideo(buffer, bufferInfo)
    }

    override fun onSPSPPS(sps: ByteBuffer, pps: ByteBuffer) {
        videoOutput?.setSPSPPS(sps, pps)
    }
}
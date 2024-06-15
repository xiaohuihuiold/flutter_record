package com.xhhold.flutter_record

import RecordResult
import VideoCodecConfig
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaCodec
import android.media.MediaCodec.BufferInfo
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.os.Build
import android.util.Size
import android.view.Surface
import java.nio.ByteBuffer

class ScreenRecorder(
    private val mediaProjection: MediaProjection,
    private val videoCodecConfig: VideoCodecConfig,
    private val screenSize: Size,
    private val screenCallback: ScreenCallback,
    private val onError: OnRecordError
) : Thread() {

    private var mediaCodec: MediaCodec? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var inputSurface: Surface? = null

    @Volatile
    var recordState = RecordState.IDLE
        private set

    companion object {
        const val TAG = "ScreenRecorder"
    }

    override fun run() {
        super.run()
        prepareEncoder()
        runRender()
    }

    private fun prepareEncoder() {
        try {
            val configSize = videoCodecConfig.size
            val videoSize = if (configSize != null) Size(
                configSize.width.toInt(), configSize.height.toInt()
            ) else screenSize
            val mediaFormat = MediaFormat.createVideoFormat(
                MediaFormat.MIMETYPE_VIDEO_AVC, videoSize.width, videoSize.height
            )
            val bitRateMode = when (videoCodecConfig.bitRateMode) {
                BitRateMode.CQ -> MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CQ
                BitRateMode.VBR -> MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR
                BitRateMode.CBR -> MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR
                BitRateMode.FD -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR_FD
                } else {
                    MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR
                }
            }
            mediaFormat.setInteger(
                MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
            )
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, videoCodecConfig.bitRate.toInt())
            mediaFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, bitRateMode)
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, videoCodecConfig.frameRate.toInt())
            mediaFormat.setInteger(
                MediaFormat.KEY_I_FRAME_INTERVAL, videoCodecConfig.iframeInterval.toInt()
            )
            mediaFormat.setInteger(MediaFormat.KEY_PROFILE, videoCodecConfig.profile.toInt())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mediaFormat.setInteger(MediaFormat.KEY_LEVEL, videoCodecConfig.profileLevel.toInt())
            }
            val mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
            this.mediaCodec = mediaCodec
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            mediaCodec.setCallback(object : MediaCodec.Callback() {
                override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                }

                override fun onOutputBufferAvailable(
                    codec: MediaCodec, index: Int, info: BufferInfo
                ) {
                    val buffer = codec.getOutputBuffer(index)
                    if (buffer != null) {
                        screenCallback.onVideoData(buffer, info)
                    }
                    codec.releaseOutputBuffer(index, false)
                }

                override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                    codec.reset()
                    onError(RecordResult(RecordCode.CODECERROR, message = e.message))
                }

                override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
                    val sps = format.getByteBuffer("csd-0")
                    val pps = format.getByteBuffer("csd-1")
                    if (sps != null && pps != null) {
                        screenCallback.onSPSPPS(sps, pps)
                    }
                }
            })
            inputSurface = mediaCodec.createInputSurface()
            mediaCodec.start()
            virtualDisplay = mediaProjection.createVirtualDisplay(
                TAG,
                videoSize.width,
                videoSize.height,
                1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                inputSurface,
                null,
                null
            )
        } catch (e: Exception) {
            stopRecord()
            onError(RecordResult(RecordCode.CODECCONFIGFAILED, message = e.message))
        }
    }

    private fun runRender() {

    }

    fun startRecord() {
        if (recordState != RecordState.IDLE) {
            return
        }
        recordState = RecordState.RECORDING
        start()
    }

    fun pauseRecord() {
        recordState = RecordState.PAUSED
    }

    fun stopRecord() {
        recordState = RecordState.STOPPED
        release()
    }

    private fun release() {
        mediaCodec?.stop()
        mediaCodec?.release()
        mediaCodec = null
        virtualDisplay?.release()
        virtualDisplay = null
        mediaProjection.stop()
    }

    interface ScreenCallback {
        fun onVideoData(buffer: ByteBuffer, bufferInfo: BufferInfo)
        fun onSPSPPS(sps: ByteBuffer, pps: ByteBuffer)
    }
}
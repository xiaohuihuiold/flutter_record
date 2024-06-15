package com.xhhold.flutter_record

import AudioCodecConfig
import RecordResult
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioPlaybackCaptureConfiguration
import android.media.AudioRecord
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.os.Build
import java.nio.ByteBuffer

class AudioRecorder(
    private val mediaProjection: MediaProjection,
    private val audioCodecConfig: AudioCodecConfig,
    private val audioCallback: AudioCallback,
    private val onError: OnRecordError
) : Thread() {

    private var mediaCodec: MediaCodec? = null
    private var systemRecord: AudioRecord? = null

    @Volatile
    var recordState = RecordState.IDLE
        private set

    companion object {
        const val TAG = "AudioRecorder"
    }

    override fun run() {
        super.run()
        prepareEncoder()
        startMediaCodec()
    }

    private fun prepareEncoder() {
        try {
            val sampleRateInHz = audioCodecConfig.sampleRate.toInt()
            val bitRate = audioCodecConfig.bitRate.toInt()
            val channelCount = audioCodecConfig.channelCount.toInt()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val audioPlaybackCaptureConfiguration = AudioPlaybackCaptureConfiguration
                    .Builder(mediaProjection)
                    .addMatchingUsage(AudioAttributes.USAGE_UNKNOWN)
                    .addMatchingUsage(AudioAttributes.USAGE_MEDIA)
                    .addMatchingUsage(AudioAttributes.USAGE_GAME)
                    .addMatchingUsage(AudioAttributes.USAGE_ALARM)
                    .addMatchingUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
                systemRecord = AudioRecord.Builder()
                    .setAudioPlaybackCaptureConfig(audioPlaybackCaptureConfiguration)
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setSampleRate(sampleRateInHz)
                            .setChannelMask(if (channelCount == 1) AudioFormat.CHANNEL_IN_MONO else AudioFormat.CHANNEL_IN_STEREO)
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .build()
                    )
                    .build()
            }
            if (systemRecord != null) {
                val mediaFormat =
                    MediaFormat.createAudioFormat(
                        MediaFormat.MIMETYPE_AUDIO_AAC,
                        sampleRateInHz,
                        channelCount
                    )
                        .apply {
                            setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
                            setInteger(
                                MediaFormat.KEY_AAC_PROFILE,
                                MediaCodecInfo.CodecProfileLevel.AACObjectLC
                            )
                        }
                mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
                mediaCodec?.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
                mediaCodec?.setCallback(object : MediaCodec.Callback() {
                    override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                        val buffer = codec.getInputBuffer(index)
                        buffer?.clear()
                        if (buffer == null) {
                            return
                        }
                        val size = systemRecord?.read(buffer, buffer.capacity())
                        if (size != null && size > 0) {
                            codec.queueInputBuffer(index, 0, size, System.nanoTime() / 1000, 0)
                        }
                    }

                    override fun onOutputBufferAvailable(
                        codec: MediaCodec,
                        index: Int,
                        info: MediaCodec.BufferInfo
                    ) {
                        val buffer = codec.getOutputBuffer(index)
                        if (buffer != null) {
                            audioCallback.onAACData(buffer, info)
                        }
                        codec.releaseOutputBuffer(index, false)
                    }

                    override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                        codec.reset()
                        onError(RecordResult(RecordCode.CODECERROR, message = e.message))
                    }

                    override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
                    }
                })
            }
            if (mediaCodec == null) {
                stopRecord()
                onError(RecordResult(RecordCode.CODECCONFIGFAILED, message = "mediaCodec is null"))
            }
        } catch (e: Exception) {
            stopRecord()
            onError(RecordResult(RecordCode.CODECCONFIGFAILED, message = e.message))
        }
    }

    private fun startMediaCodec() {
        try {
            systemRecord?.startRecording()
            mediaCodec?.start()
        } catch (e: Exception) {
            stopRecord()
            onError(RecordResult(RecordCode.CODECERROR, message = e.message))
        }
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
        mediaCodec?.reset()
        mediaCodec?.release()
        mediaCodec = null
        mediaProjection.stop()

        if (systemRecord?.state != AudioRecord.STATE_UNINITIALIZED) {
            systemRecord?.stop()
            systemRecord?.release()
            systemRecord = null
        }
    }

    interface AudioCallback {
        fun onAACData(buffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo)
    }
}
package com.xhhold.flutter_record

import CodecConfig
import OutputConfig
import android.media.MediaCodec
import com.pedro.rtsp.rtsp.RtspClient
import com.pedro.rtsp.utils.ConnectCheckerRtsp
import java.nio.ByteBuffer
import java.util.Timer
import kotlin.concurrent.timer

typealias OnStatusUpdate = (fps: Int, audioBitrate: Int, videoBitrate: Int) -> Unit

class RtspOutput(
    private val outputConfig: OutputConfig,
    private val codecConfig: CodecConfig,
    private val onStatusUpdate: OnStatusUpdate,
) : ConnectCheckerRtsp {
    private val rtspClient = RtspClient(this)
    private var secondTimer: Timer? = null

    @Volatile
    private var audioBitrateCount: Int = 0

    @Volatile
    private var videoBitrateCount: Int = 0

    @Volatile
    private var fpsCount: Int = 0

    @Volatile
    private var canStreaming = false

    companion object {
        const val TAG = "MediaClient"
    }

    fun connect() {
        rtspClient.setLogs(false)
        when (outputConfig.protocol) {
            Protocol.TCP -> rtspClient.setProtocol(com.pedro.rtsp.rtsp.Protocol.TCP)
            Protocol.UDP -> rtspClient.setProtocol(com.pedro.rtsp.rtsp.Protocol.UDP)
        }
        rtspClient.setOnlyAudio(false)
        rtspClient.setOnlyVideo(false)
        rtspClient.setAudioInfo(
            codecConfig.audioConfig.sampleRate.toInt(),
            codecConfig.audioConfig.channelCount.toInt() == 2
        )
        rtspClient.setCheckServerAlive(true)
        rtspClient.connect(outputConfig.uri, true)
        secondTimer = timer(period = 1000) {
            onStatusUpdate(fpsCount, audioBitrateCount, videoBitrateCount)
            audioBitrateCount = 0
            videoBitrateCount = 0
            fpsCount = 0
        }
    }

    fun disconnect() {
        rtspClient.disconnect()
    }

    private fun release() {
        canStreaming = false
        secondTimer?.cancel()
    }

    fun setSPSPPS(sps: ByteBuffer, pps: ByteBuffer) {
        canStreaming = true
        rtspClient.setVideoInfo(sps = sps, pps = pps, vps = null)
    }

    fun pushVideo(buffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        if (!rtspClient.isStreaming || !canStreaming) {
            return
        }
        fpsCount++
        videoBitrateCount += bufferInfo.size * 8
        rtspClient.sendVideo(buffer, bufferInfo)
    }

    fun pushAudio(buffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        if (!rtspClient.isStreaming || !canStreaming) {
            return
        }
        audioBitrateCount += bufferInfo.size * 8
        rtspClient.sendAudio(buffer, bufferInfo)
    }

    override fun onAuthErrorRtsp() {

    }

    override fun onAuthSuccessRtsp() {
    }

    override fun onConnectionFailedRtsp(reason: String) {
        release()
    }

    override fun onConnectionStartedRtsp(rtspUrl: String) {
    }

    override fun onConnectionSuccessRtsp() {
    }

    override fun onDisconnectRtsp() {
        release()
    }

    override fun onNewBitrateRtsp(bitrate: Long) {
    }
}
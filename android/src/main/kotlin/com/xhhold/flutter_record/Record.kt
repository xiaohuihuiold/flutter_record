// Autogenerated from Pigeon (v14.0.0), do not edit directly.
// See also: https://pub.dev/packages/pigeon


import android.util.Log
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MessageCodec
import io.flutter.plugin.common.StandardMessageCodec
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

private fun wrapResult(result: Any?): List<Any?> {
  return listOf(result)
}

private fun wrapError(exception: Throwable): List<Any?> {
  if (exception is FlutterError) {
    return listOf(
      exception.code,
      exception.message,
      exception.details
    )
  } else {
    return listOf(
      exception.javaClass.simpleName,
      exception.toString(),
      "Cause: " + exception.cause + ", Stacktrace: " + Log.getStackTraceString(exception)
    )
  }
}

private fun createConnectionError(channelName: String): FlutterError {
  return FlutterError("channel-error",  "Unable to establish connection on channel: '$channelName'.", "")}

/**
 * Error class for passing custom error details to Flutter via a thrown PlatformException.
 * @property code The error code.
 * @property message The error message.
 * @property details The error details. Must be a datatype supported by the api codec.
 */
class FlutterError (
  val code: String,
  override val message: String? = null,
  val details: Any? = null
) : Throwable()

enum class RecordState(val raw: Int) {
  IDLE(0),
  RECORDING(1),
  PAUSED(2),
  STOPPED(3);

  companion object {
    fun ofRaw(raw: Int): RecordState? {
      return values().firstOrNull { it.raw == raw }
    }
  }
}

enum class ServiceState(val raw: Int) {
  DISCONNECTED(0),
  CONNECTED(1);

  companion object {
    fun ofRaw(raw: Int): ServiceState? {
      return values().firstOrNull { it.raw == raw }
    }
  }
}

enum class BitRateMode(val raw: Int) {
  CQ(0),
  CBR(1),
  VBR(2),
  FD(3);

  companion object {
    fun ofRaw(raw: Int): BitRateMode? {
      return values().firstOrNull { it.raw == raw }
    }
  }
}

enum class Protocol(val raw: Int) {
  UDP(0),
  TCP(1);

  companion object {
    fun ofRaw(raw: Int): Protocol? {
      return values().firstOrNull { it.raw == raw }
    }
  }
}

enum class VideoCropStyle(val raw: Int) {
  WHITE(0),
  BLACK(1),
  BLUR(2),
  TRANSPARENT(3);

  companion object {
    fun ofRaw(raw: Int): VideoCropStyle? {
      return values().firstOrNull { it.raw == raw }
    }
  }
}

enum class RecordCode(val raw: Int) {
  SUCCESS(0),
  ERROR(1),
  SERVICEDISCONNECTED(2),
  REQUESTFAILED(3),
  MEDIAPROJECTIONFAILED(4),
  RECORDRUNNING(5),
  INVALIDARGUMENT(6),
  CODECCONFIGFAILED(7),
  CODECERROR(8),
  SERVICESTARTFAILED(9),
  OUTPUTCONFIGFAILED(10);

  companion object {
    fun ofRaw(raw: Int): RecordCode? {
      return values().firstOrNull { it.raw == raw }
    }
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class AudioStatus (
  val bitrate: Long

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): AudioStatus {
      val bitrate = list[0].let { if (it is Int) it.toLong() else it as Long }
      return AudioStatus(bitrate)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      bitrate,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class VideoStatus (
  val fps: Long,
  val bitrate: Long

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): VideoStatus {
      val fps = list[0].let { if (it is Int) it.toLong() else it as Long }
      val bitrate = list[1].let { if (it is Int) it.toLong() else it as Long }
      return VideoStatus(fps, bitrate)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      fps,
      bitrate,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class RecordInfo (
  val state: RecordState,
  val recordConfig: RecordConfig

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): RecordInfo {
      val state = RecordState.ofRaw(list[0] as Int)!!
      val recordConfig = RecordConfig.fromList(list[1] as List<Any?>)
      return RecordInfo(state, recordConfig)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      state.raw,
      recordConfig.toList(),
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class ServiceInfo (
  val state: ServiceState

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): ServiceInfo {
      val state = ServiceState.ofRaw(list[0] as Int)!!
      return ServiceInfo(state)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      state.raw,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class CodecConfig (
  val audioConfig: AudioCodecConfig,
  val videoConfig: VideoCodecConfig

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): CodecConfig {
      val audioConfig = AudioCodecConfig.fromList(list[0] as List<Any?>)
      val videoConfig = VideoCodecConfig.fromList(list[1] as List<Any?>)
      return CodecConfig(audioConfig, videoConfig)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      audioConfig.toList(),
      videoConfig.toList(),
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class AudioCodecConfig (
  val sampleRate: Long,
  val bitRate: Long,
  val channelCount: Long

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): AudioCodecConfig {
      val sampleRate = list[0].let { if (it is Int) it.toLong() else it as Long }
      val bitRate = list[1].let { if (it is Int) it.toLong() else it as Long }
      val channelCount = list[2].let { if (it is Int) it.toLong() else it as Long }
      return AudioCodecConfig(sampleRate, bitRate, channelCount)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      sampleRate,
      bitRate,
      channelCount,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class VideoSize (
  val width: Long,
  val height: Long

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): VideoSize {
      val width = list[0].let { if (it is Int) it.toLong() else it as Long }
      val height = list[1].let { if (it is Int) it.toLong() else it as Long }
      return VideoSize(width, height)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      width,
      height,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class VideoCodecConfig (
  val size: VideoSize? = null,
  val bitRate: Long,
  val bitRateMode: BitRateMode,
  val frameRate: Long,
  val iframeInterval: Long,
  val profile: Long,
  val profileLevel: Long

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): VideoCodecConfig {
      val size: VideoSize? = (list[0] as List<Any?>?)?.let {
        VideoSize.fromList(it)
      }
      val bitRate = list[1].let { if (it is Int) it.toLong() else it as Long }
      val bitRateMode = BitRateMode.ofRaw(list[2] as Int)!!
      val frameRate = list[3].let { if (it is Int) it.toLong() else it as Long }
      val iframeInterval = list[4].let { if (it is Int) it.toLong() else it as Long }
      val profile = list[5].let { if (it is Int) it.toLong() else it as Long }
      val profileLevel = list[6].let { if (it is Int) it.toLong() else it as Long }
      return VideoCodecConfig(size, bitRate, bitRateMode, frameRate, iframeInterval, profile, profileLevel)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      size?.toList(),
      bitRate,
      bitRateMode.raw,
      frameRate,
      iframeInterval,
      profile,
      profileLevel,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class OutputConfig (
  val uri: String,
  val protocol: Protocol

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): OutputConfig {
      val uri = list[0] as String
      val protocol = Protocol.ofRaw(list[1] as Int)!!
      return OutputConfig(uri, protocol)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      uri,
      protocol.raw,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class RecordConfig (
  val videoEnabled: Boolean,
  val micEnabled: Boolean,
  val systemAudioEnabled: Boolean,
  val videoCropStyle: VideoCropStyle

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): RecordConfig {
      val videoEnabled = list[0] as Boolean
      val micEnabled = list[1] as Boolean
      val systemAudioEnabled = list[2] as Boolean
      val videoCropStyle = VideoCropStyle.ofRaw(list[3] as Int)!!
      return RecordConfig(videoEnabled, micEnabled, systemAudioEnabled, videoCropStyle)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      videoEnabled,
      micEnabled,
      systemAudioEnabled,
      videoCropStyle.raw,
    )
  }
}

/** Generated class from Pigeon that represents data sent in messages. */
data class RecordResult (
  val code: RecordCode,
  val externalCode: Long? = null,
  val message: String? = null

) {
  companion object {
    @Suppress("UNCHECKED_CAST")
    fun fromList(list: List<Any?>): RecordResult {
      val code = RecordCode.ofRaw(list[0] as Int)!!
      val externalCode = list[1].let { if (it is Int) it.toLong() else it as Long? }
      val message = list[2] as String?
      return RecordResult(code, externalCode, message)
    }
  }
  fun toList(): List<Any?> {
    return listOf<Any?>(
      code.raw,
      externalCode,
      message,
    )
  }
}

@Suppress("UNCHECKED_CAST")
private object RecordPlatformCodec : StandardMessageCodec() {
  override fun readValueOfType(type: Byte, buffer: ByteBuffer): Any? {
    return when (type) {
      128.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          AudioCodecConfig.fromList(it)
        }
      }
      129.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          CodecConfig.fromList(it)
        }
      }
      130.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          OutputConfig.fromList(it)
        }
      }
      131.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          RecordConfig.fromList(it)
        }
      }
      132.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          RecordResult.fromList(it)
        }
      }
      133.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          VideoCodecConfig.fromList(it)
        }
      }
      134.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          VideoSize.fromList(it)
        }
      }
      else -> super.readValueOfType(type, buffer)
    }
  }
  override fun writeValue(stream: ByteArrayOutputStream, value: Any?)   {
    when (value) {
      is AudioCodecConfig -> {
        stream.write(128)
        writeValue(stream, value.toList())
      }
      is CodecConfig -> {
        stream.write(129)
        writeValue(stream, value.toList())
      }
      is OutputConfig -> {
        stream.write(130)
        writeValue(stream, value.toList())
      }
      is RecordConfig -> {
        stream.write(131)
        writeValue(stream, value.toList())
      }
      is RecordResult -> {
        stream.write(132)
        writeValue(stream, value.toList())
      }
      is VideoCodecConfig -> {
        stream.write(133)
        writeValue(stream, value.toList())
      }
      is VideoSize -> {
        stream.write(134)
        writeValue(stream, value.toList())
      }
      else -> super.writeValue(stream, value)
    }
  }
}

/** Generated interface from Pigeon that represents a handler of messages from Flutter. */
interface RecordPlatform {
  fun connect(): RecordResult
  fun request(callback: (Result<RecordResult>) -> Unit)
  fun refreshState()
  fun setCodecConfig(config: CodecConfig): RecordResult
  fun setOutputConfig(config: OutputConfig): RecordResult
  fun setRecordConfig(config: RecordConfig): RecordResult
  fun startRecord(): RecordResult
  fun pauseRecord(): RecordResult
  fun stopRecord(): RecordResult

  companion object {
    /** The codec used by RecordPlatform. */
    val codec: MessageCodec<Any?> by lazy {
      RecordPlatformCodec
    }
    /** Sets up an instance of `RecordPlatform` to handle messages through the `binaryMessenger`. */
    @Suppress("UNCHECKED_CAST")
    fun setUp(binaryMessenger: BinaryMessenger, api: RecordPlatform?) {
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_record.RecordPlatform.connect", codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            var wrapped: List<Any?>
            try {
              wrapped = listOf<Any?>(api.connect())
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_record.RecordPlatform.request", codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            api.request() { result: Result<RecordResult> ->
              val error = result.exceptionOrNull()
              if (error != null) {
                reply.reply(wrapError(error))
              } else {
                val data = result.getOrNull()
                reply.reply(wrapResult(data))
              }
            }
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_record.RecordPlatform.refreshState", codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            var wrapped: List<Any?>
            try {
              api.refreshState()
              wrapped = listOf<Any?>(null)
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_record.RecordPlatform.setCodecConfig", codec)
        if (api != null) {
          channel.setMessageHandler { message, reply ->
            val args = message as List<Any?>
            val configArg = args[0] as CodecConfig
            var wrapped: List<Any?>
            try {
              wrapped = listOf<Any?>(api.setCodecConfig(configArg))
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_record.RecordPlatform.setOutputConfig", codec)
        if (api != null) {
          channel.setMessageHandler { message, reply ->
            val args = message as List<Any?>
            val configArg = args[0] as OutputConfig
            var wrapped: List<Any?>
            try {
              wrapped = listOf<Any?>(api.setOutputConfig(configArg))
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_record.RecordPlatform.setRecordConfig", codec)
        if (api != null) {
          channel.setMessageHandler { message, reply ->
            val args = message as List<Any?>
            val configArg = args[0] as RecordConfig
            var wrapped: List<Any?>
            try {
              wrapped = listOf<Any?>(api.setRecordConfig(configArg))
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_record.RecordPlatform.startRecord", codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            var wrapped: List<Any?>
            try {
              wrapped = listOf<Any?>(api.startRecord())
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_record.RecordPlatform.pauseRecord", codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            var wrapped: List<Any?>
            try {
              wrapped = listOf<Any?>(api.pauseRecord())
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_record.RecordPlatform.stopRecord", codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            var wrapped: List<Any?>
            try {
              wrapped = listOf<Any?>(api.stopRecord())
            } catch (exception: Throwable) {
              wrapped = wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
    }
  }
}
@Suppress("UNCHECKED_CAST")
private object RecordFlutterCodec : StandardMessageCodec() {
  override fun readValueOfType(type: Byte, buffer: ByteBuffer): Any? {
    return when (type) {
      128.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          AudioStatus.fromList(it)
        }
      }
      129.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          RecordConfig.fromList(it)
        }
      }
      130.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          RecordInfo.fromList(it)
        }
      }
      131.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          RecordResult.fromList(it)
        }
      }
      132.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          ServiceInfo.fromList(it)
        }
      }
      133.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          VideoStatus.fromList(it)
        }
      }
      else -> super.readValueOfType(type, buffer)
    }
  }
  override fun writeValue(stream: ByteArrayOutputStream, value: Any?)   {
    when (value) {
      is AudioStatus -> {
        stream.write(128)
        writeValue(stream, value.toList())
      }
      is RecordConfig -> {
        stream.write(129)
        writeValue(stream, value.toList())
      }
      is RecordInfo -> {
        stream.write(130)
        writeValue(stream, value.toList())
      }
      is RecordResult -> {
        stream.write(131)
        writeValue(stream, value.toList())
      }
      is ServiceInfo -> {
        stream.write(132)
        writeValue(stream, value.toList())
      }
      is VideoStatus -> {
        stream.write(133)
        writeValue(stream, value.toList())
      }
      else -> super.writeValue(stream, value)
    }
  }
}

/** Generated class from Pigeon that represents Flutter messages that can be called from Kotlin. */
@Suppress("UNCHECKED_CAST")
class RecordFlutter(private val binaryMessenger: BinaryMessenger) {
  companion object {
    /** The codec used by RecordFlutter. */
    val codec: MessageCodec<Any?> by lazy {
      RecordFlutterCodec
    }
  }
  fun onUpdateService(infoArg: ServiceInfo, callback: (Result<Unit>) -> Unit) {
    val channelName = "dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateService"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(infoArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else {
          callback(Result.success(Unit))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
  fun onUpdateInfo(infoArg: RecordInfo, callback: (Result<Unit>) -> Unit) {
    val channelName = "dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateInfo"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(infoArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else {
          callback(Result.success(Unit))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
  fun onUpdateAudioStatus(statusArg: AudioStatus, callback: (Result<Unit>) -> Unit) {
    val channelName = "dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateAudioStatus"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(statusArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else {
          callback(Result.success(Unit))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
  fun onUpdateVideoStatus(statusArg: VideoStatus, callback: (Result<Unit>) -> Unit) {
    val channelName = "dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateVideoStatus"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(statusArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else {
          callback(Result.success(Unit))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
  fun onError(errorArg: RecordResult, callback: (Result<Unit>) -> Unit) {
    val channelName = "dev.flutter.pigeon.flutter_record.RecordFlutter.onError"
    val channel = BasicMessageChannel<Any?>(binaryMessenger, channelName, codec)
    channel.send(listOf(errorArg)) {
      if (it is List<*>) {
        if (it.size > 1) {
          callback(Result.failure(FlutterError(it[0] as String, it[1] as String, it[2] as String?)))
        } else {
          callback(Result.success(Unit))
        }
      } else {
        callback(Result.failure(createConnectionError(channelName)))
      } 
    }
  }
}

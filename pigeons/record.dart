import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/record.dart',
  dartOptions: DartOptions(),
  kotlinOut: 'android/src/main/kotlin/com/xhhold/flutter_record/Record.kt',
  kotlinOptions: KotlinOptions(),
  dartPackageName: 'flutter_record',
))
class PigeonConfig {}

enum RecordState {
  idle,
  recording,
  paused,
  stopped,
}

class AudioStatus {
  final int bitrate;

  AudioStatus({required this.bitrate});
}

class VideoStatus {
  final int fps;
  final int bitrate;

  VideoStatus({required this.fps, required this.bitrate});
}

class RecordInfo {
  final RecordState state;
  final RecordConfig recordConfig;

  RecordInfo({
    required this.state,
    required this.recordConfig,
  });
}

enum ServiceState {
  disconnected,
  connected,
}

class ServiceInfo {
  final ServiceState state;

  ServiceInfo({required this.state});
}

class CodecConfig {
  final AudioCodecConfig audioConfig;
  final VideoCodecConfig videoConfig;

  CodecConfig({required this.audioConfig, required this.videoConfig});
}

class AudioCodecConfig {
  final int sampleRate;
  final int bitRate;
  final int channelCount;

  AudioCodecConfig({
    required this.sampleRate,
    required this.bitRate,
    required this.channelCount,
  });
}

class VideoSize {
  final int width;
  final int height;

  const VideoSize({required this.width, required this.height});
}

enum BitRateMode {
  cq,
  cbr,
  vbr,
  fd,
}

class VideoCodecConfig {
  final VideoSize? size;
  final int bitRate;
  final BitRateMode bitRateMode;
  final int frameRate;
  final int iframeInterval;
  final int profile;
  final int profileLevel;

  VideoCodecConfig({
    this.size,
    required this.bitRate,
    this.bitRateMode = BitRateMode.vbr,
    required this.frameRate,
    this.iframeInterval = 1,
    this.profile = 0x10000,
    this.profileLevel = 0x800,
  });
}

enum Protocol {
  udp,
  tcp,
}

class OutputConfig {
  final String uri;
  final Protocol protocol;

  OutputConfig({
    required this.uri,
    this.protocol = Protocol.tcp,
  });
}

enum VideoCropStyle {
  white,
  black,
  blur,
  transparent,
}

class RecordConfig {
  final bool videoEnabled;
  final bool micEnabled;
  final bool systemAudioEnabled;
  final VideoCropStyle videoCropStyle;

  RecordConfig({
    required this.videoEnabled,
    required this.micEnabled,
    required this.systemAudioEnabled,
    this.videoCropStyle = VideoCropStyle.black,
  });
}

enum RecordCode {
  success,
  error,
  serviceDisconnected,
  requestFailed,
  mediaProjectionFailed,
  recordRunning,
  invalidArgument,
  codecConfigFailed,
  codecError,
  serviceStartFailed,
  outputConfigFailed,
}

class RecordResult {
  final RecordCode code;
  final int? externalCode;
  final String? message;

  RecordResult({required this.code, this.externalCode, this.message});
}

@HostApi()
abstract class RecordPlatform {
  RecordResult connect();

  @async
  RecordResult request();

  void refreshState();

  RecordResult setCodecConfig(CodecConfig config);

  RecordResult setOutputConfig(OutputConfig config);

  RecordResult setRecordConfig(RecordConfig config);

  RecordResult startRecord();

  RecordResult pauseRecord();

  RecordResult stopRecord();
}

@FlutterApi()
abstract class RecordFlutter {
  void onUpdateService(ServiceInfo info);

  void onUpdateInfo(RecordInfo info);

  void onUpdateAudioStatus(AudioStatus status);

  void onUpdateVideoStatus(VideoStatus status);

  void onError(RecordResult error);
}

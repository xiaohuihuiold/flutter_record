import 'dart:async';

import 'src/record.dart';

export 'src/record.dart';

class RecordManager extends RecordFlutter {
  static final _platform = RecordPlatform();

  static final instance = RecordManager._();

  ServiceInfo _serviceInfoValue = ServiceInfo(state: ServiceState.disconnected);

  ServiceInfo get serviceInfoValue => _serviceInfoValue;

  RecordResult? _errorValue;

  RecordResult? get errorValue => _errorValue;

  RecordInfo _infoValue = RecordInfo(
    state: RecordState.idle,
    recordConfig: RecordConfig(
      videoEnabled: true,
      micEnabled: false,
      systemAudioEnabled: false,
      videoCropStyle: VideoCropStyle.black,
    ),
  );

  RecordInfo get infoValue => _infoValue;

  AudioStatus _audioStatusValue = AudioStatus(bitrate: 0);

  AudioStatus get audioStatusValue => _audioStatusValue;

  VideoStatus _videoStatusValue = VideoStatus(fps: 0, bitrate: 0);

  VideoStatus get videoStatusValue => _videoStatusValue;
  final _error = StreamController<RecordResult>.broadcast();
  final _info = StreamController<RecordInfo>.broadcast();
  final _serviceInfo = StreamController<ServiceInfo>.broadcast();
  final _audioStatus = StreamController<AudioStatus>.broadcast();
  final _videoStatus = StreamController<VideoStatus>.broadcast();

  Stream<RecordResult> get error => _error.stream;

  Stream<RecordInfo> get info => _info.stream;

  Stream<ServiceInfo> get serviceInfo => _serviceInfo.stream;

  Stream<AudioStatus> get audioStatus => _audioStatus.stream;

  Stream<VideoStatus> get videoStatus => _videoStatus.stream;

  RecordManager._() {
    RecordFlutter.setup(this);
  }

  Future<RecordResult> connect() async {
    return await _platform.connect();
  }

  Future<RecordResult> request() async {
    return await _platform.request();
  }

  Future<void> refreshState() async {
    await _platform.refreshState();
  }

  Future<RecordResult> setCodecConfig(CodecConfig config) async {
    return await _platform.setCodecConfig(config);
  }

  Future<RecordResult> setOutputConfig(OutputConfig config) async {
    return await _platform.setOutputConfig(config);
  }

  Future<RecordResult> setRecordConfig(RecordConfig config) async {
    return await _platform.setRecordConfig(config);
  }

  Future<RecordResult> startRecord() async {
    return await _platform.startRecord();
  }

  Future<RecordResult> pauseRecord() async {
    return await _platform.pauseRecord();
  }

  Future<RecordResult> stopRecord() async {
    return await _platform.stopRecord();
  }

  @override
  void onError(RecordResult error) {
    _errorValue = error;
    _error.add(error);
  }

  @override
  void onUpdateAudioStatus(AudioStatus status) {
    _audioStatusValue = status;
    _audioStatus.add(status);
  }

  @override
  void onUpdateService(ServiceInfo info) {
    if (info.state == ServiceState.connected) {
      refreshState();
    }
    _serviceInfoValue = info;
    _serviceInfo.add(info);
  }

  @override
  void onUpdateInfo(RecordInfo info) {
    _infoValue = info;
    _info.add(info);
  }

  @override
  void onUpdateVideoStatus(VideoStatus status) {
    _videoStatusValue = status;
    _videoStatus.add(status);
  }
}

class VideoCodecProfiles {
  static const avcProfileBaseline = 0x01;
  static const avcProfileMain = 0x02;
  static const avcProfileExtended = 0x04;
  static const avcProfileHigh = 0x08;
  static const avcProfileHigh10 = 0x10;
  static const avcProfileHigh422 = 0x20;
  static const avcProfileHigh444 = 0x40;
  static const avcProfileConstrainedBaseline = 0x10000;
  static const avcProfileConstrainedHigh = 0x80000;
  static const avcLevel1 = 0x01;
  static const avcLevel1b = 0x02;
  static const avcLevel11 = 0x04;
  static const avcLevel12 = 0x08;
  static const avcLevel13 = 0x10;
  static const avcLevel2 = 0x20;
  static const avcLevel21 = 0x40;
  static const avcLevel22 = 0x80;
  static const avcLevel3 = 0x100;
  static const avcLevel31 = 0x200;
  static const avcLevel32 = 0x400;
  static const avcLevel4 = 0x800;
  static const avcLevel41 = 0x1000;
  static const avcLevel42 = 0x2000;
  static const avcLevel5 = 0x4000;
  static const avcLevel51 = 0x8000;
  static const avcLevel52 = 0x10000;
  static const avcLevel6 = 0x20000;
  static const avcLevel61 = 0x40000;
  static const avcLevel62 = 0x80000;
}

extension RrcordConfigExtension on RecordConfig {
  RecordConfig copyWith({
    bool? videoEnabled,
    bool? micEnabled,
    bool? systemAudioEnabled,
    VideoCropStyle? videoCropStyle,
  }) {
    return RecordConfig(
      videoEnabled: videoEnabled ?? this.videoEnabled,
      micEnabled: micEnabled ?? this.micEnabled,
      systemAudioEnabled: systemAudioEnabled ?? this.systemAudioEnabled,
      videoCropStyle: videoCropStyle ?? this.videoCropStyle,
    );
  }
}

// Autogenerated from Pigeon (v14.0.0), do not edit directly.
// See also: https://pub.dev/packages/pigeon
// ignore_for_file: public_member_api_docs, non_constant_identifier_names, avoid_as, unused_import, unnecessary_parenthesis, prefer_null_aware_operators, omit_local_variable_types, unused_shown_name, unnecessary_import, no_leading_underscores_for_local_identifiers

import 'dart:async';
import 'dart:typed_data' show Float64List, Int32List, Int64List, Uint8List;

import 'package:flutter/foundation.dart' show ReadBuffer, WriteBuffer;
import 'package:flutter/services.dart';

PlatformException _createConnectionError(String channelName) {
  return PlatformException(
    code: 'channel-error',
    message: 'Unable to establish connection on channel: "$channelName".',
  );
}

List<Object?> wrapResponse({Object? result, PlatformException? error, bool empty = false}) {
  if (empty) {
    return <Object?>[];
  }
  if (error == null) {
    return <Object?>[result];
  }
  return <Object?>[error.code, error.message, error.details];
}

enum RecordState {
  idle,
  recording,
  paused,
  stopped,
}

enum ServiceState {
  disconnected,
  connected,
}

enum BitRateMode {
  cq,
  cbr,
  vbr,
  fd,
}

enum Protocol {
  udp,
  tcp,
}

enum VideoCropStyle {
  white,
  black,
  blur,
  transparent,
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

class AudioStatus {
  AudioStatus({
    required this.bitrate,
  });

  int bitrate;

  Object encode() {
    return <Object?>[
      bitrate,
    ];
  }

  static AudioStatus decode(Object result) {
    result as List<Object?>;
    return AudioStatus(
      bitrate: result[0]! as int,
    );
  }
}

class VideoStatus {
  VideoStatus({
    required this.fps,
    required this.bitrate,
  });

  int fps;

  int bitrate;

  Object encode() {
    return <Object?>[
      fps,
      bitrate,
    ];
  }

  static VideoStatus decode(Object result) {
    result as List<Object?>;
    return VideoStatus(
      fps: result[0]! as int,
      bitrate: result[1]! as int,
    );
  }
}

class RecordInfo {
  RecordInfo({
    required this.state,
    required this.recordConfig,
  });

  RecordState state;

  RecordConfig recordConfig;

  Object encode() {
    return <Object?>[
      state.index,
      recordConfig.encode(),
    ];
  }

  static RecordInfo decode(Object result) {
    result as List<Object?>;
    return RecordInfo(
      state: RecordState.values[result[0]! as int],
      recordConfig: RecordConfig.decode(result[1]! as List<Object?>),
    );
  }
}

class ServiceInfo {
  ServiceInfo({
    required this.state,
  });

  ServiceState state;

  Object encode() {
    return <Object?>[
      state.index,
    ];
  }

  static ServiceInfo decode(Object result) {
    result as List<Object?>;
    return ServiceInfo(
      state: ServiceState.values[result[0]! as int],
    );
  }
}

class CodecConfig {
  CodecConfig({
    required this.audioConfig,
    required this.videoConfig,
  });

  AudioCodecConfig audioConfig;

  VideoCodecConfig videoConfig;

  Object encode() {
    return <Object?>[
      audioConfig.encode(),
      videoConfig.encode(),
    ];
  }

  static CodecConfig decode(Object result) {
    result as List<Object?>;
    return CodecConfig(
      audioConfig: AudioCodecConfig.decode(result[0]! as List<Object?>),
      videoConfig: VideoCodecConfig.decode(result[1]! as List<Object?>),
    );
  }
}

class AudioCodecConfig {
  AudioCodecConfig({
    required this.sampleRate,
    required this.bitRate,
    required this.channelCount,
  });

  int sampleRate;

  int bitRate;

  int channelCount;

  Object encode() {
    return <Object?>[
      sampleRate,
      bitRate,
      channelCount,
    ];
  }

  static AudioCodecConfig decode(Object result) {
    result as List<Object?>;
    return AudioCodecConfig(
      sampleRate: result[0]! as int,
      bitRate: result[1]! as int,
      channelCount: result[2]! as int,
    );
  }
}

class VideoSize {
  VideoSize({
    required this.width,
    required this.height,
  });

  int width;

  int height;

  Object encode() {
    return <Object?>[
      width,
      height,
    ];
  }

  static VideoSize decode(Object result) {
    result as List<Object?>;
    return VideoSize(
      width: result[0]! as int,
      height: result[1]! as int,
    );
  }
}

class VideoCodecConfig {
  VideoCodecConfig({
    this.size,
    required this.bitRate,
    required this.bitRateMode,
    required this.frameRate,
    required this.iframeInterval,
    required this.profile,
    required this.profileLevel,
  });

  VideoSize? size;

  int bitRate;

  BitRateMode bitRateMode;

  int frameRate;

  int iframeInterval;

  int profile;

  int profileLevel;

  Object encode() {
    return <Object?>[
      size?.encode(),
      bitRate,
      bitRateMode.index,
      frameRate,
      iframeInterval,
      profile,
      profileLevel,
    ];
  }

  static VideoCodecConfig decode(Object result) {
    result as List<Object?>;
    return VideoCodecConfig(
      size: result[0] != null
          ? VideoSize.decode(result[0]! as List<Object?>)
          : null,
      bitRate: result[1]! as int,
      bitRateMode: BitRateMode.values[result[2]! as int],
      frameRate: result[3]! as int,
      iframeInterval: result[4]! as int,
      profile: result[5]! as int,
      profileLevel: result[6]! as int,
    );
  }
}

class OutputConfig {
  OutputConfig({
    required this.uri,
    required this.protocol,
  });

  String uri;

  Protocol protocol;

  Object encode() {
    return <Object?>[
      uri,
      protocol.index,
    ];
  }

  static OutputConfig decode(Object result) {
    result as List<Object?>;
    return OutputConfig(
      uri: result[0]! as String,
      protocol: Protocol.values[result[1]! as int],
    );
  }
}

class RecordConfig {
  RecordConfig({
    required this.videoEnabled,
    required this.micEnabled,
    required this.systemAudioEnabled,
    required this.videoCropStyle,
  });

  bool videoEnabled;

  bool micEnabled;

  bool systemAudioEnabled;

  VideoCropStyle videoCropStyle;

  Object encode() {
    return <Object?>[
      videoEnabled,
      micEnabled,
      systemAudioEnabled,
      videoCropStyle.index,
    ];
  }

  static RecordConfig decode(Object result) {
    result as List<Object?>;
    return RecordConfig(
      videoEnabled: result[0]! as bool,
      micEnabled: result[1]! as bool,
      systemAudioEnabled: result[2]! as bool,
      videoCropStyle: VideoCropStyle.values[result[3]! as int],
    );
  }
}

class RecordResult {
  RecordResult({
    required this.code,
    this.externalCode,
    this.message,
  });

  RecordCode code;

  int? externalCode;

  String? message;

  Object encode() {
    return <Object?>[
      code.index,
      externalCode,
      message,
    ];
  }

  static RecordResult decode(Object result) {
    result as List<Object?>;
    return RecordResult(
      code: RecordCode.values[result[0]! as int],
      externalCode: result[1] as int?,
      message: result[2] as String?,
    );
  }
}

class _RecordPlatformCodec extends StandardMessageCodec {
  const _RecordPlatformCodec();
  @override
  void writeValue(WriteBuffer buffer, Object? value) {
    if (value is AudioCodecConfig) {
      buffer.putUint8(128);
      writeValue(buffer, value.encode());
    } else if (value is CodecConfig) {
      buffer.putUint8(129);
      writeValue(buffer, value.encode());
    } else if (value is OutputConfig) {
      buffer.putUint8(130);
      writeValue(buffer, value.encode());
    } else if (value is RecordConfig) {
      buffer.putUint8(131);
      writeValue(buffer, value.encode());
    } else if (value is RecordResult) {
      buffer.putUint8(132);
      writeValue(buffer, value.encode());
    } else if (value is VideoCodecConfig) {
      buffer.putUint8(133);
      writeValue(buffer, value.encode());
    } else if (value is VideoSize) {
      buffer.putUint8(134);
      writeValue(buffer, value.encode());
    } else {
      super.writeValue(buffer, value);
    }
  }

  @override
  Object? readValueOfType(int type, ReadBuffer buffer) {
    switch (type) {
      case 128: 
        return AudioCodecConfig.decode(readValue(buffer)!);
      case 129: 
        return CodecConfig.decode(readValue(buffer)!);
      case 130: 
        return OutputConfig.decode(readValue(buffer)!);
      case 131: 
        return RecordConfig.decode(readValue(buffer)!);
      case 132: 
        return RecordResult.decode(readValue(buffer)!);
      case 133: 
        return VideoCodecConfig.decode(readValue(buffer)!);
      case 134: 
        return VideoSize.decode(readValue(buffer)!);
      default:
        return super.readValueOfType(type, buffer);
    }
  }
}

class RecordPlatform {
  /// Constructor for [RecordPlatform].  The [binaryMessenger] named argument is
  /// available for dependency injection.  If it is left null, the default
  /// BinaryMessenger will be used which routes to the host platform.
  RecordPlatform({BinaryMessenger? binaryMessenger})
      : __pigeon_binaryMessenger = binaryMessenger;
  final BinaryMessenger? __pigeon_binaryMessenger;

  static const MessageCodec<Object?> pigeonChannelCodec = _RecordPlatformCodec();

  Future<RecordResult> connect() async {
    const String __pigeon_channelName = 'dev.flutter.pigeon.flutter_record.RecordPlatform.connect';
    final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
      __pigeon_channelName,
      pigeonChannelCodec,
      binaryMessenger: __pigeon_binaryMessenger,
    );
    final List<Object?>? __pigeon_replyList =
        await __pigeon_channel.send(null) as List<Object?>?;
    if (__pigeon_replyList == null) {
      throw _createConnectionError(__pigeon_channelName);
    } else if (__pigeon_replyList.length > 1) {
      throw PlatformException(
        code: __pigeon_replyList[0]! as String,
        message: __pigeon_replyList[1] as String?,
        details: __pigeon_replyList[2],
      );
    } else if (__pigeon_replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (__pigeon_replyList[0] as RecordResult?)!;
    }
  }

  Future<RecordResult> request() async {
    const String __pigeon_channelName = 'dev.flutter.pigeon.flutter_record.RecordPlatform.request';
    final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
      __pigeon_channelName,
      pigeonChannelCodec,
      binaryMessenger: __pigeon_binaryMessenger,
    );
    final List<Object?>? __pigeon_replyList =
        await __pigeon_channel.send(null) as List<Object?>?;
    if (__pigeon_replyList == null) {
      throw _createConnectionError(__pigeon_channelName);
    } else if (__pigeon_replyList.length > 1) {
      throw PlatformException(
        code: __pigeon_replyList[0]! as String,
        message: __pigeon_replyList[1] as String?,
        details: __pigeon_replyList[2],
      );
    } else if (__pigeon_replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (__pigeon_replyList[0] as RecordResult?)!;
    }
  }

  Future<void> refreshState() async {
    const String __pigeon_channelName = 'dev.flutter.pigeon.flutter_record.RecordPlatform.refreshState';
    final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
      __pigeon_channelName,
      pigeonChannelCodec,
      binaryMessenger: __pigeon_binaryMessenger,
    );
    final List<Object?>? __pigeon_replyList =
        await __pigeon_channel.send(null) as List<Object?>?;
    if (__pigeon_replyList == null) {
      throw _createConnectionError(__pigeon_channelName);
    } else if (__pigeon_replyList.length > 1) {
      throw PlatformException(
        code: __pigeon_replyList[0]! as String,
        message: __pigeon_replyList[1] as String?,
        details: __pigeon_replyList[2],
      );
    } else {
      return;
    }
  }

  Future<RecordResult> setCodecConfig(CodecConfig config) async {
    const String __pigeon_channelName = 'dev.flutter.pigeon.flutter_record.RecordPlatform.setCodecConfig';
    final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
      __pigeon_channelName,
      pigeonChannelCodec,
      binaryMessenger: __pigeon_binaryMessenger,
    );
    final List<Object?>? __pigeon_replyList =
        await __pigeon_channel.send(<Object?>[config]) as List<Object?>?;
    if (__pigeon_replyList == null) {
      throw _createConnectionError(__pigeon_channelName);
    } else if (__pigeon_replyList.length > 1) {
      throw PlatformException(
        code: __pigeon_replyList[0]! as String,
        message: __pigeon_replyList[1] as String?,
        details: __pigeon_replyList[2],
      );
    } else if (__pigeon_replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (__pigeon_replyList[0] as RecordResult?)!;
    }
  }

  Future<RecordResult> setOutputConfig(OutputConfig config) async {
    const String __pigeon_channelName = 'dev.flutter.pigeon.flutter_record.RecordPlatform.setOutputConfig';
    final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
      __pigeon_channelName,
      pigeonChannelCodec,
      binaryMessenger: __pigeon_binaryMessenger,
    );
    final List<Object?>? __pigeon_replyList =
        await __pigeon_channel.send(<Object?>[config]) as List<Object?>?;
    if (__pigeon_replyList == null) {
      throw _createConnectionError(__pigeon_channelName);
    } else if (__pigeon_replyList.length > 1) {
      throw PlatformException(
        code: __pigeon_replyList[0]! as String,
        message: __pigeon_replyList[1] as String?,
        details: __pigeon_replyList[2],
      );
    } else if (__pigeon_replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (__pigeon_replyList[0] as RecordResult?)!;
    }
  }

  Future<RecordResult> setRecordConfig(RecordConfig config) async {
    const String __pigeon_channelName = 'dev.flutter.pigeon.flutter_record.RecordPlatform.setRecordConfig';
    final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
      __pigeon_channelName,
      pigeonChannelCodec,
      binaryMessenger: __pigeon_binaryMessenger,
    );
    final List<Object?>? __pigeon_replyList =
        await __pigeon_channel.send(<Object?>[config]) as List<Object?>?;
    if (__pigeon_replyList == null) {
      throw _createConnectionError(__pigeon_channelName);
    } else if (__pigeon_replyList.length > 1) {
      throw PlatformException(
        code: __pigeon_replyList[0]! as String,
        message: __pigeon_replyList[1] as String?,
        details: __pigeon_replyList[2],
      );
    } else if (__pigeon_replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (__pigeon_replyList[0] as RecordResult?)!;
    }
  }

  Future<RecordResult> startRecord() async {
    const String __pigeon_channelName = 'dev.flutter.pigeon.flutter_record.RecordPlatform.startRecord';
    final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
      __pigeon_channelName,
      pigeonChannelCodec,
      binaryMessenger: __pigeon_binaryMessenger,
    );
    final List<Object?>? __pigeon_replyList =
        await __pigeon_channel.send(null) as List<Object?>?;
    if (__pigeon_replyList == null) {
      throw _createConnectionError(__pigeon_channelName);
    } else if (__pigeon_replyList.length > 1) {
      throw PlatformException(
        code: __pigeon_replyList[0]! as String,
        message: __pigeon_replyList[1] as String?,
        details: __pigeon_replyList[2],
      );
    } else if (__pigeon_replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (__pigeon_replyList[0] as RecordResult?)!;
    }
  }

  Future<RecordResult> pauseRecord() async {
    const String __pigeon_channelName = 'dev.flutter.pigeon.flutter_record.RecordPlatform.pauseRecord';
    final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
      __pigeon_channelName,
      pigeonChannelCodec,
      binaryMessenger: __pigeon_binaryMessenger,
    );
    final List<Object?>? __pigeon_replyList =
        await __pigeon_channel.send(null) as List<Object?>?;
    if (__pigeon_replyList == null) {
      throw _createConnectionError(__pigeon_channelName);
    } else if (__pigeon_replyList.length > 1) {
      throw PlatformException(
        code: __pigeon_replyList[0]! as String,
        message: __pigeon_replyList[1] as String?,
        details: __pigeon_replyList[2],
      );
    } else if (__pigeon_replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (__pigeon_replyList[0] as RecordResult?)!;
    }
  }

  Future<RecordResult> stopRecord() async {
    const String __pigeon_channelName = 'dev.flutter.pigeon.flutter_record.RecordPlatform.stopRecord';
    final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
      __pigeon_channelName,
      pigeonChannelCodec,
      binaryMessenger: __pigeon_binaryMessenger,
    );
    final List<Object?>? __pigeon_replyList =
        await __pigeon_channel.send(null) as List<Object?>?;
    if (__pigeon_replyList == null) {
      throw _createConnectionError(__pigeon_channelName);
    } else if (__pigeon_replyList.length > 1) {
      throw PlatformException(
        code: __pigeon_replyList[0]! as String,
        message: __pigeon_replyList[1] as String?,
        details: __pigeon_replyList[2],
      );
    } else if (__pigeon_replyList[0] == null) {
      throw PlatformException(
        code: 'null-error',
        message: 'Host platform returned null value for non-null return value.',
      );
    } else {
      return (__pigeon_replyList[0] as RecordResult?)!;
    }
  }
}

class _RecordFlutterCodec extends StandardMessageCodec {
  const _RecordFlutterCodec();
  @override
  void writeValue(WriteBuffer buffer, Object? value) {
    if (value is AudioStatus) {
      buffer.putUint8(128);
      writeValue(buffer, value.encode());
    } else if (value is RecordConfig) {
      buffer.putUint8(129);
      writeValue(buffer, value.encode());
    } else if (value is RecordInfo) {
      buffer.putUint8(130);
      writeValue(buffer, value.encode());
    } else if (value is RecordResult) {
      buffer.putUint8(131);
      writeValue(buffer, value.encode());
    } else if (value is ServiceInfo) {
      buffer.putUint8(132);
      writeValue(buffer, value.encode());
    } else if (value is VideoStatus) {
      buffer.putUint8(133);
      writeValue(buffer, value.encode());
    } else {
      super.writeValue(buffer, value);
    }
  }

  @override
  Object? readValueOfType(int type, ReadBuffer buffer) {
    switch (type) {
      case 128: 
        return AudioStatus.decode(readValue(buffer)!);
      case 129: 
        return RecordConfig.decode(readValue(buffer)!);
      case 130: 
        return RecordInfo.decode(readValue(buffer)!);
      case 131: 
        return RecordResult.decode(readValue(buffer)!);
      case 132: 
        return ServiceInfo.decode(readValue(buffer)!);
      case 133: 
        return VideoStatus.decode(readValue(buffer)!);
      default:
        return super.readValueOfType(type, buffer);
    }
  }
}

abstract class RecordFlutter {
  static const MessageCodec<Object?> pigeonChannelCodec = _RecordFlutterCodec();

  void onUpdateService(ServiceInfo info);

  void onUpdateInfo(RecordInfo info);

  void onUpdateAudioStatus(AudioStatus status);

  void onUpdateVideoStatus(VideoStatus status);

  void onError(RecordResult error);

  static void setup(RecordFlutter? api, {BinaryMessenger? binaryMessenger}) {
    {
      final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateService', pigeonChannelCodec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        __pigeon_channel.setMessageHandler(null);
      } else {
        __pigeon_channel.setMessageHandler((Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateService was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final ServiceInfo? arg_info = (args[0] as ServiceInfo?);
          assert(arg_info != null,
              'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateService was null, expected non-null ServiceInfo.');
          try {
            api.onUpdateService(arg_info!);
            return wrapResponse(empty: true);
          } on PlatformException catch (e) {
            return wrapResponse(error: e);
          }          catch (e) {
            return wrapResponse(error: PlatformException(code: 'error', message: e.toString()));
          }
        });
      }
    }
    {
      final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateInfo', pigeonChannelCodec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        __pigeon_channel.setMessageHandler(null);
      } else {
        __pigeon_channel.setMessageHandler((Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateInfo was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final RecordInfo? arg_info = (args[0] as RecordInfo?);
          assert(arg_info != null,
              'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateInfo was null, expected non-null RecordInfo.');
          try {
            api.onUpdateInfo(arg_info!);
            return wrapResponse(empty: true);
          } on PlatformException catch (e) {
            return wrapResponse(error: e);
          }          catch (e) {
            return wrapResponse(error: PlatformException(code: 'error', message: e.toString()));
          }
        });
      }
    }
    {
      final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateAudioStatus', pigeonChannelCodec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        __pigeon_channel.setMessageHandler(null);
      } else {
        __pigeon_channel.setMessageHandler((Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateAudioStatus was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final AudioStatus? arg_status = (args[0] as AudioStatus?);
          assert(arg_status != null,
              'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateAudioStatus was null, expected non-null AudioStatus.');
          try {
            api.onUpdateAudioStatus(arg_status!);
            return wrapResponse(empty: true);
          } on PlatformException catch (e) {
            return wrapResponse(error: e);
          }          catch (e) {
            return wrapResponse(error: PlatformException(code: 'error', message: e.toString()));
          }
        });
      }
    }
    {
      final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateVideoStatus', pigeonChannelCodec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        __pigeon_channel.setMessageHandler(null);
      } else {
        __pigeon_channel.setMessageHandler((Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateVideoStatus was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final VideoStatus? arg_status = (args[0] as VideoStatus?);
          assert(arg_status != null,
              'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onUpdateVideoStatus was null, expected non-null VideoStatus.');
          try {
            api.onUpdateVideoStatus(arg_status!);
            return wrapResponse(empty: true);
          } on PlatformException catch (e) {
            return wrapResponse(error: e);
          }          catch (e) {
            return wrapResponse(error: PlatformException(code: 'error', message: e.toString()));
          }
        });
      }
    }
    {
      final BasicMessageChannel<Object?> __pigeon_channel = BasicMessageChannel<Object?>(
          'dev.flutter.pigeon.flutter_record.RecordFlutter.onError', pigeonChannelCodec,
          binaryMessenger: binaryMessenger);
      if (api == null) {
        __pigeon_channel.setMessageHandler(null);
      } else {
        __pigeon_channel.setMessageHandler((Object? message) async {
          assert(message != null,
          'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onError was null.');
          final List<Object?> args = (message as List<Object?>?)!;
          final RecordResult? arg_error = (args[0] as RecordResult?);
          assert(arg_error != null,
              'Argument for dev.flutter.pigeon.flutter_record.RecordFlutter.onError was null, expected non-null RecordResult.');
          try {
            api.onError(arg_error!);
            return wrapResponse(empty: true);
          } on PlatformException catch (e) {
            return wrapResponse(error: e);
          }          catch (e) {
            return wrapResponse(error: PlatformException(code: 'error', message: e.toString()));
          }
        });
      }
    }
  }
}

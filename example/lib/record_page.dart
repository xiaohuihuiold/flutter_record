import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_record/flutter_record.dart';
import 'package:permission_handler/permission_handler.dart';

class RecordPage extends StatefulWidget {
  const RecordPage({super.key});

  @override
  State<RecordPage> createState() => _RecordPageState();
}

class _RecordPageState extends State<RecordPage> {
  StreamSubscription? _serviceInfoSubscription;
  StreamSubscription? _recordInfoSubscription;

  ServiceInfo _serviceInfoValue = RecordManager.instance.serviceInfoValue;
  RecordInfo _recordInfoValue = RecordManager.instance.infoValue;

  void _onUpdateServiceInfo(ServiceInfo info) {
    _serviceInfoValue = info;
    if (info.state == ServiceState.connected) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
        content: Text('服务已连接'),
      ));
    }
    if (!mounted) return;
    setState(() {});
  }

  void _onUpdateInfo(RecordInfo info) {
    _recordInfoValue = info;
    if (!mounted) return;
    setState(() {});
  }

  void _subscribe() {
    _serviceInfoSubscription =
        RecordManager.instance.serviceInfo.listen(_onUpdateServiceInfo);
    _recordInfoSubscription = RecordManager.instance.info.listen(_onUpdateInfo);
  }

  void _unsubscribe() {
    _serviceInfoSubscription?.cancel();
    _recordInfoSubscription?.cancel();
  }

  void _connectService() async {
    if (await Permission.microphone.status.isDenied) {
      if (await Permission.microphone.request().isDenied) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text('请授予麦克风权限'),
        ));
        return;
      }
    }
    if (await Permission.notification.status.isDenied) {
      if (await Permission.notification.request().isDenied) {
        ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
          content: Text('请授予通知权限'),
        ));
        return;
      }
    }
    final result = await RecordManager.instance.connect();
    if (result.code != RecordCode.success) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('服务连接失败: ${result.message}'),
      ));
    }
  }

  Future<void> _onStartRecord() async {
    final result = await RecordManager.instance.request();
    if (result.code != RecordCode.success) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('请求失败: ${result.message}'),
      ));
      return;
    }
    await RecordManager.instance.setRecordConfig(_recordInfoValue.recordConfig);
    await RecordManager.instance.setCodecConfig(CodecConfig(
      videoConfig: VideoCodecConfig(
        size: VideoSize(width: 1920, height: 1080),
        bitRate: 5 * 1000 * 1000,
        bitRateMode: BitRateMode.vbr,
        frameRate: 60,
        iframeInterval: 1,
        profile: VideoCodecProfiles.avcProfileConstrainedBaseline,
        profileLevel: VideoCodecProfiles.avcLevel4,
      ),
      audioConfig: AudioCodecConfig(
        sampleRate: 44100,
        bitRate: 64 * 1000,
        channelCount: 2,
      ),
    ));
    await RecordManager.instance.setOutputConfig(OutputConfig(
      uri: 'rtsp://192.168.1.25:8554/live/test01',
      protocol: Protocol.tcp,
    ));
    final startResult = await RecordManager.instance.startRecord();
    if (startResult.code != RecordCode.success) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('开始录制失败: ${startResult.message}'),
      ));
    }
  }

  Future<void> _onStopRecord() async {}

  void _onVideoChanged(bool value) async {
    final result = await RecordManager.instance.setRecordConfig(
      _recordInfoValue.recordConfig.copyWith(videoEnabled: value),
    );
    if (result.code != RecordCode.success) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('设置失败: ${result.message}'),
      ));
    }
  }

  void _onSystemAudioChanged(bool value) async {
    final result = await RecordManager.instance.setRecordConfig(
      _recordInfoValue.recordConfig.copyWith(systemAudioEnabled: value),
    );
    if (result.code != RecordCode.success) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('设置失败: ${result.message}'),
      ));
    }
  }

  void _onMicChanged(bool value) async {
    final result = await RecordManager.instance.setRecordConfig(
      _recordInfoValue.recordConfig.copyWith(micEnabled: value),
    );
    if (result.code != RecordCode.success) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(
        content: Text('设置失败: ${result.message}'),
      ));
    }
  }

  @override
  void initState() {
    super.initState();
    _subscribe();
  }

  @override
  void dispose() {
    super.dispose();
    _unsubscribe();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('RECORD'),
      ),
      bottomNavigationBar: RecordStatusBar(),
      body: SingleChildScrollView(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ServiceStateInfo(
              serviceInfo: _serviceInfoValue,
              onConnect: _connectService,
            ),
            if (_serviceInfoValue.state == ServiceState.connected) ...[
              const CategoryTitle(title: Text('录制设置')),
              RecordSettingInfo(
                recordInfo: _recordInfoValue,
                onStart: _onStartRecord,
                onStop: _onStopRecord,
                onVideoChanged: _onVideoChanged,
                onSystemAudioChanged: _onSystemAudioChanged,
                onMicChanged: _onMicChanged,
              ),
            ],
          ],
        ),
      ),
    );
  }
}

class RecordSettingInfo extends StatelessWidget {
  final RecordInfo recordInfo;
  final VoidCallback onStart;
  final VoidCallback onStop;

  final ValueChanged<bool> onVideoChanged;
  final ValueChanged<bool> onSystemAudioChanged;
  final ValueChanged<bool> onMicChanged;

  const RecordSettingInfo({
    super.key,
    required this.recordInfo,
    required this.onVideoChanged,
    required this.onStart,
    required this.onStop,
    required this.onSystemAudioChanged,
    required this.onMicChanged,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: [
        ListTile(
          title: const Text('录制状态'),
          subtitle: Text(recordInfo.state.toString()),
          trailing: recordInfo.state == RecordState.recording
              ? FilledButton(
                  onPressed: onStop,
                  child: const Text('停止录制'),
                )
              : ElevatedButton(
                  onPressed: onStart,
                  child: const Text('开始录制'),
                ),
        ),
        ListTile(
          title: const Text('录制视频'),
          subtitle: Text(
              recordInfo.recordConfig.videoEnabled == true ? '已开启' : '已关闭'),
          trailing: Switch(
            value: recordInfo.recordConfig.videoEnabled == true,
            onChanged: onVideoChanged,
          ),
        ),
        ListTile(
          title: const Text('录制系统音频'),
          subtitle: Text(recordInfo.recordConfig.systemAudioEnabled == true
              ? '已开启'
              : '已关闭'),
          trailing: Switch(
            value: recordInfo.recordConfig.systemAudioEnabled == true,
            onChanged: onSystemAudioChanged,
          ),
        ),
        ListTile(
          title: const Text('录制麦克风音频'),
          subtitle:
              Text(recordInfo.recordConfig.micEnabled == true ? '已开启' : '已关闭'),
          trailing: Switch(
            value: recordInfo.recordConfig.micEnabled == true,
            onChanged: onMicChanged,
          ),
        ),
      ],
    );
  }
}

class ServiceStateInfo extends StatelessWidget {
  final ServiceInfo serviceInfo;
  final VoidCallback onConnect;

  const ServiceStateInfo({
    super.key,
    required this.serviceInfo,
    required this.onConnect,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      title: const Text('服务状态'),
      subtitle: Text(serviceInfo.state.toString()),
      trailing: Switch(
        value: serviceInfo.state == ServiceState.connected,
        onChanged: (value) {
          if (value) {
            onConnect();
          }
        },
      ),
    );
  }
}

class CategoryTitle extends StatelessWidget {
  final Widget title;

  const CategoryTitle({super.key, required this.title});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      color: Theme.of(context).splashColor,
      child: DefaultTextStyle(
        style: Theme.of(context).textTheme.titleSmall!,
        child: title,
      ),
    );
  }
}

class RecordStatusBar extends StatefulWidget {
  const RecordStatusBar({super.key});

  @override
  State<RecordStatusBar> createState() => _RecordStatusBarState();
}

class _RecordStatusBarState extends State<RecordStatusBar> {
  StreamSubscription? _audioStatusSubscription;
  StreamSubscription? _videoStatusSubscription;

  AudioStatus _audioStatusValue = RecordManager.instance.audioStatusValue;
  VideoStatus _videoStatusValue = RecordManager.instance.videoStatusValue;

  void _onAudioStatus(AudioStatus status) {
    _audioStatusValue = status;
    if (!mounted) return;
    setState(() {});
  }

  void _onVideoStatus(VideoStatus status) {
    _videoStatusValue = status;
    if (!mounted) return;
    setState(() {});
  }

  void _subscribe() {
    _audioStatusSubscription =
        RecordManager.instance.audioStatus.listen(_onAudioStatus);
    _videoStatusSubscription =
        RecordManager.instance.videoStatus.listen(_onVideoStatus);
  }

  void _unsubscribe() {
    _audioStatusSubscription?.cancel();
    _videoStatusSubscription?.cancel();
  }

  @override
  void initState() {
    super.initState();
    _subscribe();
  }

  @override
  void dispose() {
    super.dispose();
    _unsubscribe();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      color: Colors.black,
      child: DefaultTextStyle(
        style: Theme.of(context)
            .textTheme
            .bodyMedium!
            .copyWith(color: Colors.white),
        child: Row(
          children: [
            Text(
              'FPS: ${_videoStatusValue.fps}',
              style: const TextStyle(color: Colors.green),
            ),
            const SizedBox(width: 8),
            DefaultTextStyle(
              style: Theme.of(context).textTheme.bodyMedium!.copyWith(
                  fontSize:
                      Theme.of(context).textTheme.bodyMedium!.fontSize! / 2),
              child: Column(
                mainAxisSize: MainAxisSize.min,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'A: ${_audioStatusValue.bitrate ~/ 1000}kbps',
                    style: const TextStyle(color: Colors.blue),
                  ),
                  const SizedBox(width: 8),
                  Text(
                    'V: ${_videoStatusValue.bitrate ~/ 1000}kbps',
                    style: const TextStyle(color: Colors.red),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

import 'dart:async';

import 'package:flutter/services.dart';

class ImeiPlugin {
  static const MethodChannel _channel = const MethodChannel('imei_plugin');

  static Future<String> get imei1 async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> getImei({int index = 0}) async {
    final String imei =
        await _channel.invokeMethod("getImei", {"index": index});
    return imei;
  }
}

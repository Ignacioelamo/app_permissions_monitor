import 'package:app_permissions_monitor_example/FileManager.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:app_permissions_monitor/app_permissions_monitor.dart';
import 'package:workmanager/workmanager.dart';
import 'package:permission_handler/permission_handler.dart';



@pragma('vm:entry-point') // Mandatory if the App is obfuscated or using Flutter 3.1+
void callbackDispatcher() {
  Workmanager().executeTask((task, inputData) async {
    if (kDebugMode) {
      print("Native called background task: $task");
    } //simpleTask will be emitted here.

    try {
      // Get the installed apps permission statuses
      List<dynamic> appsPermissions = await AppPermissionsMonitor().getInstalledAppsPermissionStatuses();
      print("Esto funciona babyyy");
      var content = "Background task executed at ${DateTime.now().toString()}\n";
      FileManager().writeToFile('prueba.txt', content);


    } catch (e) {
      if (kDebugMode) {
        print('Error initializing FlutterBackgroundServiceAndroid: $e');
      }
    }

    return Future.value(true);
  });
}

Future<void> _requestPermissions() async {
  if (await Permission.notification.request().isDenied) {
    await Permission.notification.request();
  }
}

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  _requestPermissions();
  FileManager().createFile('prueba.txt');


  Workmanager().initialize(
      callbackDispatcher, // The top level function, aka callbackDispatcher
      isInDebugMode: true // If enabled it will post a notification whenever the task is running. Handy for debugging tasks
  );

  Workmanager().registerPeriodicTask("task-identifier", "simplePeriodicTask", frequency: const Duration(seconds: 2));
  Workmanager().registerOneOffTask("Initialization", "Initialization_task", initialDelay: Duration(seconds: 5));

  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _appPermissionsMonitorPlugin = AppPermissionsMonitor();

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _appPermissionsMonitorPlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel(
      'com.example.android_video_multiple_activity/second_activity');

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Method Channel Example'),
      ),
      body: Center(
        child: Column(
          children: [
            ElevatedButton(
              onPressed: () async {
                try {
                  await platform.invokeMethod('openSecondActivity1');
                } on PlatformException catch (e) {
                  print('Failed to open second activity: ${e.message}');
                }
              },
              child: Text('Open Second Activity'),
            ),
            ElevatedButton(
              onPressed: () async {
                try {
                  await platform.invokeMethod('openSecondActivity2');
                } on PlatformException catch (e) {
                  print('Failed to open second activity: ${e.message}');
                }
              },
              child: Text('Open Second Activity 2'),
            ),
          ],
        ),
      ),
    );
  }
}

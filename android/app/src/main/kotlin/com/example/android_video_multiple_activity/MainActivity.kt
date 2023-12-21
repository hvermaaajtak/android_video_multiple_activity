package com.example.android_video_multiple_activity

import android.content.Intent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private val CHANNEL = "com.example.android_video_multiple_activity/second_activity"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        methodChannel.setMethodCallHandler { call, result ->
            when (call.method) {
                "openSecondActivity1" -> {
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
                    result.success("Second Activity opened successfully")
                }
                "openSecondActivity2" -> {
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
                    result.success("Second Activity opened successfully2")
                }

                else -> result.notImplemented()
            }
        }
    }
}

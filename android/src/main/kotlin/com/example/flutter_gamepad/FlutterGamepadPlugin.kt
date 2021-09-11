package com.example.flutter_gamepad

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/**
 * The flutter_gamepad plugin class that is registered with the framework.
 */
class FlutterGamepadPlugin : MethodCallHandler {
    companion object {
        var isTv: Boolean? = null

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            // Detect if we are running on a TV.
            val context = registrar.context()
            val manager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            isTv = manager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION

            // Set up Flutter platform channels.
            val methodChannel = MethodChannel(registrar.messenger(), "com.rainway.flutter_gamepad/methods")
            methodChannel.setMethodCallHandler(FlutterGamepadPlugin())
            val eventChannel = EventChannel(registrar.messenger(), "com.rainway.flutter_gamepad/events")
            eventChannel.setStreamHandler(GamepadStreamHandler)
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (call.method == "gamepads") {
            result.success(allGamepadInfoDictionaries())
        } else if (call.method == "enableDebugMode") {
            GamepadStreamHandler.enableDebugMode(true)
        } else if (call.method == "disableDebugMode") {
            GamepadStreamHandler.enableDebugMode(false)
        } else {
            result.notImplemented()
        }
    }
}

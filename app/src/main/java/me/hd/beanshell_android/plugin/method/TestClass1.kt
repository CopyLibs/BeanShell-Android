package me.hd.beanshell_android.plugin.method

import android.content.Context
import me.hd.beanshell_android.plugin.log.PluginLogger

class TestClass1(val ctx: Context) {
    fun log(msg: Any) {
        PluginLogger.writeLog(ctx, msg.toString())
    }
}

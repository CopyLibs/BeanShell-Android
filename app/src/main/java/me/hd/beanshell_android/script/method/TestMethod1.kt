package me.hd.beanshell_android.script.method

import android.content.Context
import me.hd.beanshell_android.log.LogManager

class TestMethod1(val context: Context) {
    fun log(msg: Any) {
        LogManager.writeLog(context, msg.toString())
    }
}

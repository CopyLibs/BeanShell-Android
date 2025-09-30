package me.hd.beanshell_android.log

import android.content.Context
import java.io.File

object LogManager {
    fun readLog(context: Context): String {
        val logFile = File(context.filesDir, "script.log")
        return if (logFile.exists()) {
            logFile.readText()
        } else {
            "Log file does not exist."
        }
    }

    fun writeLog(context: Context, string: String) {
        val logFile = File(context.filesDir, "script.log")
        logFile.appendText(string + "\n")
    }
}

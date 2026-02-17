package me.hd.beanshell_android.plugin.log

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PluginLogger {
    private const val LOG_FILE_NAME = "script.log"

    private fun getLogFile(ctx: Context): File {
        return File(ctx.filesDir, LOG_FILE_NAME)
    }

    fun writeLog(ctx: Context, msg: String) {
        val logFile = getLogFile(ctx)
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        val formatMsg = "[$timestamp] $msg"
        logFile.appendText("$formatMsg\n")
    }

    fun readLog(ctx: Context): String {
        val logFile = getLogFile(ctx)
        return if (logFile.exists()) {
            logFile.readText()
        } else {
            "日志文件不存在"
        }
    }

    fun clearLog(ctx: Context) {
        val logFile = getLogFile(ctx)
        logFile.writeText("")
    }
}

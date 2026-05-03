package io.github.copylibs.bsh.plugin.method

import android.content.Context
import io.github.copylibs.bsh.plugin.log.PluginLogger

class LogClass(val ctx: Context) {
    fun log(msg: Any) {
        PluginLogger.writeLog(ctx, msg.toString())
    }
}

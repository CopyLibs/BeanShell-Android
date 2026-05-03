package io.github.copylibs.bsh.plugin.module.log

import android.content.Context
import io.github.copylibs.bsh.plugin.log.PluginLogger

class LogModuleApi(val ctx: Context) {
    fun log(msg: Any) {
        PluginLogger.writeLog(ctx, msg.toString())
    }
}

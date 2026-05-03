package io.github.copylibs.bsh.plugin

import android.content.Context
import bsh.BshMethod
import io.github.copylibs.bsh.plugin.method.LogClass

object PluginManager {
    fun getPlugin(ctx: Context): Plugin {
        return Plugin().apply {
            registerVariable(this, ctx)
            registerMethod(this, ctx)
        }
    }

    private fun registerVariable(plugin: Plugin, ctx: Context) {
        plugin.setVariable("ctx", ctx)
        plugin.setVariable("tag", "BeanShell")
    }

    private fun registerMethod(plugin: Plugin, ctx: Context) {
        val logInstance = LogClass(ctx)
        plugin.setMethod(
            BshMethod("log", arrayOf(Any::class.java)) { args ->
                logInstance.log(args[0])
            }
        )
        plugin.setMethod(
            BshMethod("loadJava", arrayOf(String::class.java)) { args ->
                plugin.source(args[0].toString())
            }
        )
        plugin.setMethod(
            BshMethod("loadDex", arrayOf(String::class.java)) { args ->
                plugin.loadDex(args[0].toString())
            }
        )
        plugin.setMethod(
            BshMethod("loadJar", arrayOf(String::class.java)) { args ->
                plugin.loadJar(args[0].toString())
            }
        )
        plugin.setMethod(
            BshMethod("loadAar", arrayOf(String::class.java)) { args ->
                plugin.loadAar(args[0].toString())
            }
        )
    }
}

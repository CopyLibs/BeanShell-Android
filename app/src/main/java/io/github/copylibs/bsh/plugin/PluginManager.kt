package io.github.copylibs.bsh.plugin

import android.content.Context
import bsh.BshMethod
import io.github.copylibs.bsh.plugin.method.TestClass1
import io.github.copylibs.bsh.plugin.method.TestClass2

object PluginManager {
    fun getPlugin(context: Context): Plugin {
        return Plugin().apply {
            registerVariable(this, context)
            registerMethod(this, context)
        }
    }

    private fun registerVariable(plugin: Plugin, context: Context) {
        plugin.setVariable("context", context)
        plugin.setVariable("tag", "BeanShell")
    }

    private fun registerMethod(plugin: Plugin, context: Context) {
        val testClass1 = TestClass1(context)
        val testClass2 = TestClass2
        plugin.setMethod(
            BshMethod("log", arrayOf(Any::class.java)) { args ->
                testClass1.log(args[0])
            }
        )
        plugin.setMethod(
            BshMethod("print", arrayOf(Any::class.java)) { args ->
                testClass2.print(args[0])
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

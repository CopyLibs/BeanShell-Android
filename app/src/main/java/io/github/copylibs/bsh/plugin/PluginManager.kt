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
        // 实例方法
        val testClass1 = TestClass1(context)
        val logMethod = TestClass1::class.java.getDeclaredMethod("log", Any::class.java)
        val logBshMethod = BshMethod(logMethod, testClass1)
        plugin.setMethod(logBshMethod)

        // 静态方法
        val testClass2 = TestClass2
        val printMethod = TestClass2::class.java.getDeclaredMethod("print", Any::class.java)
        val printBshMethod = BshMethod(printMethod, testClass2)
        plugin.setMethod(printBshMethod)

        val loadDexMethod = plugin::class.java.getDeclaredMethod("loadDex", String::class.java)
        val loadDexBshMethod = BshMethod(loadDexMethod, plugin)
        plugin.setMethod(loadDexBshMethod)

        val loadJarMethod = plugin::class.java.getDeclaredMethod("loadJar", String::class.java)
        val loadJarBshMethod = BshMethod(loadJarMethod, plugin)
        plugin.setMethod(loadJarBshMethod)

        val loadAarMethod = plugin::class.java.getDeclaredMethod("loadAar", String::class.java)
        val loadAarBshMethod = BshMethod(loadAarMethod, plugin)
        plugin.setMethod(loadAarBshMethod)
    }
}

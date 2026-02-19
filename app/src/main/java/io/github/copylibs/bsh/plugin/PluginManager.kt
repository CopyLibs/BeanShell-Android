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
        val method1 = BshMethod(logMethod, testClass1)
        plugin.setMethod(method1)

        // 静态方法
        val testClass2 = TestClass2
        val printMethod = TestClass2::class.java.getDeclaredMethod("print", Any::class.java)
        val method2 = BshMethod(printMethod, testClass2)
        plugin.setMethod(method2)
    }
}

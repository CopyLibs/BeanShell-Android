package me.hd.beanshell_android.script

import android.content.Context
import bsh.BshMethod
import me.hd.beanshell_android.script.method.TestMethod1
import me.hd.beanshell_android.script.method.TestMethod2

object ScriptManager {
    fun getScript(context: Context): Script {
        return Script().apply {
            registerVariable(this, context)
            registerMethod(this, context)
        }
    }

    private fun registerVariable(script: Script, context: Context) {
        script.setVariable("context", context)
        script.setVariable("tag", "BeanShell")
    }

    private fun registerMethod(script: Script, context: Context) {
        // 实例方法
        val method1 = BshMethod(TestMethod1::class.java.getDeclaredMethod("log", Any::class.java), TestMethod1(context))
        script.setMethod(method1)
        // 静态方法
        val method2 = BshMethod(TestMethod2::class.java.getDeclaredMethod("print", Any::class.java), TestMethod2)
        script.setMethod(method2)
    }
}

package me.hd.beanshell_android

import bsh.BshMethod
import bsh.Interpreter
import org.junit.Test

class UnitTest {
    @Test
    fun bshEvalTest() {
        class PluginMethod {
            fun log(tag: String, msg: String) {
                println("[$tag] $msg")
            }
        }
        Interpreter().apply {
            nameSpace.setVariable("TAG", "BeanShell", false)
            nameSpace.setMethod(BshMethod(PluginMethod::class.java.getMethod("log", String::class.java, String::class.java), PluginMethod()))
        }.eval(
            """
                log(TAG, "Hello World")
            """.trimIndent()
        )
    }
}
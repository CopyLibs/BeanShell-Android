package io.github.copylibs.bsh.plugin

import android.content.Context
import bsh.BshMethod
import bsh.Interpreter
import bsh.loader.BshLoaderHelper
import io.github.copylibs.bsh.plugin.module.log.LogModule

class Plugin(val ctx: Context) {
    private val interpreter = Interpreter()

    init {
        registerVariables()
        registerMethods()
        installModules()
    }

    private fun registerVariables() {
        interpreter.nameSpace.apply {
            setVariable("ctx", ctx)
            setVariable("tag", "BeanShell")
        }
    }

    private fun registerMethods() {
        interpreter.nameSpace.apply {
            setMethod(
                BshMethod("loadJava", arrayOf(String::class.java)) { args ->
                    val path = args[0] as String
                    interpreter.source(path)
                }
            )
            setMethod(
                BshMethod("loadDex", arrayOf(String::class.java)) { args ->
                    val dexPath = args[0] as String
                    val clsLoader = BshLoaderHelper.getLoaderByDex(dexPath, Plugin::class.java.classLoader)
                    interpreter.addClassLoader(clsLoader)
                }
            )
            setMethod(
                BshMethod("loadJar", arrayOf(String::class.java)) { args ->
                    val jarPath = args[0] as String
                    val clsLoader = BshLoaderHelper.getLoaderByJar(jarPath, Plugin::class.java.classLoader)
                    interpreter.addClassLoader(clsLoader)
                }
            )
            setMethod(
                BshMethod("loadAar", arrayOf(String::class.java)) { args ->
                    val aarPath = args[0] as String
                    val clsLoader = BshLoaderHelper.getLoaderByAar(aarPath, Plugin::class.java.classLoader)
                    interpreter.addClassLoader(clsLoader)
                }
            )
        }
    }

    private fun installModules() {
        interpreter.installModule(LogModule(ctx))
    }

    fun eval(code: String) {
        interpreter.eval(code)
    }
}

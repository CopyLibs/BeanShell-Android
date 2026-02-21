package io.github.copylibs.bsh.plugin

import bsh.BshMethod
import bsh.Interpreter
import bsh.loader.BshLoaderHelper

class Plugin {
    private val interpreter = Interpreter()

    /**
     * 导入 Class
     */
    fun importClass(className: String) {
        interpreter.nameSpace.importClass(className)
    }

    /**
     * 导入 Package
     */
    fun importPackage(packageName: String) {
        interpreter.nameSpace.importPackage(packageName)
    }

    /**
     * 设置 Variable
     */
    fun setVariable(name: String, value: Any) {
        interpreter.set(name, value)
    }

    /**
     * 设置 Method
     */
    fun setMethod(method: BshMethod) {
        interpreter.nameSpace.setMethod(method)
    }

    /**
     * 执行 Code
     */
    fun eval(code: String) {
        interpreter.eval(code)
    }

    /**
     * 执行 Path
     */
    fun source(path: String) {
        interpreter.source(path)
    }

    /**
     * 添加 ClassLoader
     */
    fun addClassLoader(loader: ClassLoader) {
        interpreter.addClassLoader(loader)
    }

    /**
     * 加载 Dex
     */
    fun loadDex(path: String) {
        val clsLoader = BshLoaderHelper.getLoaderByDex(path, Plugin::class.java.classLoader)
        addClassLoader(clsLoader)
    }

    /**
     * 加载 Jar
     */
    fun loadJar(path: String) {
        val clsLoader = BshLoaderHelper.getLoaderByJar(path, Plugin::class.java.classLoader)
        addClassLoader(clsLoader)
    }
}

package me.hd.beanshell_android.plugin

import bsh.BshMethod
import bsh.Interpreter
import bsh.classpath.BshLoaderManager

class Plugin {
    private val interpreter = Interpreter()

    /**
     * 导入类
     */
    fun importClass(className: String) {
        interpreter.nameSpace.importClass(className)
    }

    /**
     * 导入包
     */
    fun importPackage(packageName: String) {
        interpreter.nameSpace.importPackage(packageName)
    }

    /**
     * 设置变量
     */
    fun setVariable(name: String, value: Any) {
        interpreter.set(name, value)
    }

    /**
     * 设置方法
     */
    fun setMethod(method: BshMethod) {
        interpreter.nameSpace.setMethod(method)
    }

    /**
     * 执行代码
     */
    fun eval(code: String) {
        interpreter.eval(code)
    }

    /**
     * 执行文件
     */
    fun source(path: String) {
        interpreter.source(path)
    }

    /**
     * 加载Dex
     */
    fun loadDex(path: String) {
        val loader = BshLoaderManager.getDexLoader(path, Plugin::class.java.classLoader)
        BshLoaderManager.addLoader(loader)
    }
}

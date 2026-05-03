package io.github.copylibs.bsh.plugin.module.log

import android.content.Context
import bsh.BshMethod
import bsh.Interpreter
import bsh.module.BshModule

class LogModule(ctx: Context) : BshModule {
    override fun getId(): String {
        return "Log"
    }

    private val api = LogModuleApi(ctx)

    override fun install(interpreter: Interpreter) {
        interpreter.nameSpace.apply {
            setMethod(BshMethod("log", arrayOf(Any::class.java)) { args ->
                api.log(args[0].toString())
            })
        }
    }
}

package io.github.copylibs.bsh.ui.activity

import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import io.github.copylibs.bsh.databinding.ActivityMainBinding
import io.github.copylibs.bsh.plugin.PluginManager
import io.github.copylibs.bsh.plugin.log.PluginLogger
import io.github.copylibs.bsh.ui.base.ActivityBase
import io.github.rosemoe.sora.langs.java.JavaLanguage

class MainActivity : ActivityBase<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.codeToolbar.menu.apply {
            add("运行").apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                setOnMenuItemClickListener {
                    val context = this@MainActivity
                    val code = binding.codeEditor.text.toString()
                    runCatching {
                        PluginManager.getPlugin(context).eval(code)
                    }.onFailure {
                        PluginLogger.writeLog(context, "运行异常: $it")
                        it.printStackTrace()
                    }
                    true
                }
            }
            add("撤销").apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                setOnMenuItemClickListener {
                    binding.codeEditor.undo()
                    true
                }
            }
            add("重做").apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                setOnMenuItemClickListener {
                    binding.codeEditor.redo()
                    true
                }
            }
        }
        binding.codeEditor.apply {
            typefaceText = Typeface.createFromAsset(context.assets, "font/mono.ttf")
            setEditorLanguage(JavaLanguage())
        }
    }
}

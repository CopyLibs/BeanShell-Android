package io.github.copylibs.bsh.ui.fragment.page

import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import io.github.copylibs.bsh.databinding.FragmentCodeBinding
import io.github.copylibs.bsh.plugin.PluginManager
import io.github.copylibs.bsh.plugin.log.PluginLogger
import io.github.copylibs.bsh.ui.base.FragmentBase
import io.github.rosemoe.sora.langs.java.JavaLanguage

class CodeFragment : FragmentBase<FragmentCodeBinding>(
    FragmentCodeBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.codeToolbar.apply {
            menu.add("运行").apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                setOnMenuItemClickListener {
                    runCatching {
                        PluginManager.getPlugin(requireContext()).eval(binding.codeEditor.text.toString())
                    }.onFailure {
                        PluginLogger.writeLog(requireContext(), "运行异常: $it")
                        it.printStackTrace()
                    }
                    true
                }
            }
            menu.add("撤销").apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                setOnMenuItemClickListener {
                    binding.codeEditor.undo()
                    true
                }
            }
            menu.add("重做").apply {
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

package io.github.copylibs.bsh.ui.activity

import android.graphics.Typeface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.qiplat.sweeteditor.EditorTheme
import com.qiplat.sweeteditor.LanguageConfiguration
import com.qiplat.sweeteditor.core.Document
import com.qiplat.sweeteditor.core.foundation.CurrentLineRenderMode
import com.qiplat.sweeteditor.core.foundation.FoldArrowMode
import io.github.copylibs.bsh.databinding.ActivityMainBinding
import io.github.copylibs.bsh.plugin.Plugin
import io.github.copylibs.bsh.plugin.log.PluginLogger
import io.github.copylibs.bsh.ui.base.ActivityBase

class MainActivity : ActivityBase<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {
    private var isLogPanelExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        renderLogPanel()
        refreshLog()
    }

    private fun initView() {
        binding.toolbar.menu.apply {
            add("运行").apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                setOnMenuItemClickListener {
                    val context = this@MainActivity
                    val code = binding.codeEditor.document?.text.toString()
                    runCatching {
                        Plugin(context).eval(code)
                    }.onFailure {
                        PluginLogger.writeLog(context, "运行异常: $it")
                        it.printStackTrace()
                        expandLogPanel()
                    }
                    refreshLog()
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
            applyTheme(EditorTheme.light())
            setLanguageConfiguration(
                LanguageConfiguration.Builder("bsh")
                    .addAutoClosingPair("\"", "\"")
                    .addAutoClosingPair("(", ")")
                    .addAutoClosingPair("{", "}")
                    .addAutoClosingPair("[", "]")
                    .setInsertSpaces(true)
                    .build()
            )
            settings.apply {
                setEditorTextSize(28f)
                setTypeface(Typeface.createFromAsset(context.assets, "font/mono.ttf"))
                setFoldArrowMode(FoldArrowMode.AUTO)
                setGutterSticky(true)
                setCurrentLineRenderMode(CurrentLineRenderMode.BORDER)
                setCompositionEnabled(true)
            }
            loadDocument(Document(""))
        }
        binding.logToggleButton.setOnClickListener {
            toggleLogPanel()
        }
        binding.logClearButton.setOnClickListener {
            PluginLogger.clearLog(this)
            refreshLog()
        }
    }

    private fun renderLogPanel() {
        binding.logToggleButton.text = if (isLogPanelExpanded) "收起" else "展开"
        binding.logPanelBody.visibility = if (isLogPanelExpanded) View.VISIBLE else View.GONE
    }

    private fun refreshLog() {
        binding.logTextView.text = PluginLogger.readLog(this).ifEmpty { "暂无日志" }
        binding.logPanelBody.post {
            binding.logPanelBody.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun toggleLogPanel() {
        isLogPanelExpanded = !isLogPanelExpanded
        renderLogPanel()
        if (isLogPanelExpanded) refreshLog()
    }

    private fun expandLogPanel() {
        isLogPanelExpanded = true
        renderLogPanel()
        refreshLog()
    }
}

package me.hd.beanshell_android.ui.fragment.page

import android.os.Bundle
import android.view.View
import me.hd.beanshell_android.databinding.FragmentLogBinding
import me.hd.beanshell_android.plugin.log.PluginLogger
import me.hd.beanshell_android.ui.base.FragmentBase

class LogFragment : FragmentBase<FragmentLogBinding>(
    FragmentLogBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        refreshLog()
    }

    override fun onResume() {
        super.onResume()
        refreshLog()
    }

    private fun refreshLog() {
        binding.logTvContent.text = PluginLogger.readLog(requireContext())
    }
}

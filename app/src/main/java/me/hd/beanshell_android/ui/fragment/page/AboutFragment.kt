package me.hd.beanshell_android.ui.fragment.page

import android.os.Bundle
import android.view.View
import me.hd.beanshell_android.databinding.FragmentAboutBinding
import me.hd.beanshell_android.ui.base.FragmentBase

class AboutFragment : FragmentBase<FragmentAboutBinding>(
    FragmentAboutBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
    }
}

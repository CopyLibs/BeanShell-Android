package io.github.copylibs.bsh.ui.fragment.page

import android.os.Bundle
import android.view.View
import io.github.copylibs.bsh.databinding.FragmentAboutBinding
import io.github.copylibs.bsh.ui.base.FragmentBase

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

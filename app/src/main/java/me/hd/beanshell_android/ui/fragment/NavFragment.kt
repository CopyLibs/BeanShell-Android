package me.hd.beanshell_android.ui.fragment

import android.os.Bundle
import android.view.View
import me.hd.beanshell_android.R
import me.hd.beanshell_android.databinding.FragmentNavBinding
import me.hd.beanshell_android.ui.adapter.MVP2Adapter
import me.hd.beanshell_android.ui.base.FragmentBase
import me.hd.beanshell_android.ui.fragment.page.AboutFragment
import me.hd.beanshell_android.ui.fragment.page.CodeFragment
import me.hd.beanshell_android.ui.fragment.page.LogFragment

class NavFragment : FragmentBase<FragmentNavBinding>(
    FragmentNavBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.navViewPager2.apply {
            isUserInputEnabled = false
            val fragmentPages = listOf(
                CodeFragment() to R.id.navCodeFragment,
                LogFragment() to R.id.navLogFragment,
                AboutFragment() to R.id.navAboutFragment
            )
            adapter = MVP2Adapter(requireActivity(), fragmentPages.map { it.first })
            binding.navBottomNav.setOnItemSelectedListener { item ->
                val position = fragmentPages.indexOfFirst { it.second == item.itemId }
                if (position != -1) {
                    setCurrentItem(position, false)
                }
                true
            }
        }
    }
}

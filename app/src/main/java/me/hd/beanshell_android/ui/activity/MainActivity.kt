package me.hd.beanshell_android.ui.activity

import android.os.Bundle
import me.hd.beanshell_android.databinding.ActivityMainBinding
import me.hd.beanshell_android.ui.base.ActivityBase

class MainActivity : ActivityBase<ActivityMainBinding>(
    ActivityMainBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

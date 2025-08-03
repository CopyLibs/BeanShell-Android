package me.hd.beanshell_android

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import bsh.BshMethod
import bsh.Interpreter
import me.hd.beanshell_android.databinding.ActivityMainBinding

@SuppressLint("SetTextI18n")
class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    fun log(str: String) {
        binding.tvLog.text = "[${System.currentTimeMillis()}] $str"
    }

    private fun initView() {
        binding.btnRun.setOnClickListener {
            runCatching {
                Interpreter().apply {
                    nameSpace.setMethod(BshMethod(MainActivity::class.java.getMethod("log", String::class.java), this@MainActivity))
                }.eval(binding.edtCode.text.toString())
            }.onFailure { e ->
                Log.e("BeanShell", "Run Failed", e)
            }
        }
    }
}
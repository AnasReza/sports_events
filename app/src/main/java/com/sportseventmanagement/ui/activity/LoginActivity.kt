package com.sportseventmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var create_one: TextView? = null
    private var forgot_text: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        create_one = findViewById(R.id.create_one)
        forgot_text = findViewById(R.id.forgot_text)


        create_one!!.setOnClickListener(this)
        forgot_text!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.create_one -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            R.id.forgot_text -> {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }
        }
    }
}
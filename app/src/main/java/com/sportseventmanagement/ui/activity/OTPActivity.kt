package com.sportseventmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class OTPActivity:AppCompatActivity(), View.OnClickListener {

    private var verify_layout:LinearLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_otp)

        init()
    }

    private fun init() {
        verify_layout=findViewById(R.id.verify_layout)

        verify_layout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.verify_layout->{
                startActivity(Intent(this,NewPasswordActivity::class.java))
            }
        }
    }
}
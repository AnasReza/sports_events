package com.sportseventmanagement.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.OTPActivity

class ForgotPasswordActivity :AppCompatActivity(), View.OnClickListener {
    private var send_email:LinearLayout?=null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_forgot)
        
        init()
    }

    private fun init() {
        send_email=findViewById(R.id.send_email)
        
        send_email!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.send_email->{
                startActivity(Intent(this, OTPActivity::class.java))
            }
        }
    }
}
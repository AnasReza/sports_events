package com.sportseventmanagement.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class NewPasswordActivity : AppCompatActivity(), View.OnClickListener {
    private var reset_password:LinearLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_new_password)

        init()
    }

    private fun init() {
        reset_password=findViewById(R.id.reset_password)

        reset_password!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.reset_password->{
                startActivity(Intent(this, ResetConfirmationActivity::class.java))
            }
        }
    }
}
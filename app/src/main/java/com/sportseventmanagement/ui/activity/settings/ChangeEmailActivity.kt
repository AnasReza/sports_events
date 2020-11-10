package com.sportseventmanagement.ui.activity.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class ChangeEmailActivity : AppCompatActivity(), View.OnClickListener {
    private var back_button:ImageView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_change_email)
        init()
    }

    private fun init(){
        back_button=findViewById(R.id.back_button)

        back_button!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
                finish()
            }
        }
    }
}
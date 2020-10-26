package com.sportseventmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class VerifyAccountActivity : AppCompatActivity(), View.OnClickListener {
    private var next_screen:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_verify_account)

        init()
    }

    private fun init() {
        next_screen=findViewById(R.id.next_screen)

        next_screen!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.next_screen->{
                startActivity(Intent(this,AccountConfirmationActivity::class.java))
            }
        }
    }
}
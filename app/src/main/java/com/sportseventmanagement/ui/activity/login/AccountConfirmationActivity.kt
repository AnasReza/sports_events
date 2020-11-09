package com.sportseventmanagement.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.HomeActivity

class AccountConfirmationActivity : AppCompatActivity(), View.OnClickListener {

    private var verify_layout: Button?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_account_confirm)

        init()
    }

    private fun init() {
        verify_layout=findViewById(R.id.verify_layout)

        verify_layout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.verify_layout->{
                startActivity(Intent(this@AccountConfirmationActivity, HomeActivity::class.java))
            }
        }
    }
}
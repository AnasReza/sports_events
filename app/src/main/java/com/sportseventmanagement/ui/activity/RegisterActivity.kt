package com.sportseventmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private var create_account:LinearLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        create_account=findViewById(R.id.create_account)

        create_account!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.create_account->{
                startActivity(Intent(this,VerifyAccountActivity::class.java))
            }
        }
    }
}
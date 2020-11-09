package com.sportseventmanagement.ui.activity.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class PersonalInfoActivity :AppCompatActivity(), View.OnClickListener{
    private var changeEmail: TextView? =null
    private var changePassword: TextView?=null
    private var changeUsername: TextView?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_info)
        init()
    }
    private fun init(){
        changeEmail =findViewById(R.id.changeEmail)
        changePassword=findViewById(R.id.changePassword)
        changeUsername=findViewById(R.id.changeUsername)
        changeEmail!!.setOnClickListener(this)
        changePassword!!.setOnClickListener(this)
        changeUsername!!.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        when (v!!.id) {
            R.id.changeEmail -> {
                startActivity(Intent(this@PersonalInfoActivity, ChangeEmailActivity::class.java))
            }
            R.id.changePassword -> {
                startActivity(Intent(this@PersonalInfoActivity, ChangePasswordActivity::class.java))
            }
            R.id.changeUsername -> {
                startActivity(Intent(this@PersonalInfoActivity, ChangeUserNameActivity::class.java))
            }
        }

    }
}
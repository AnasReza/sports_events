package com.sportseventmanagement.ui.activity.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class PersonalInfoActivity :AppCompatActivity(), View.OnClickListener{
    private var changeEmail: TextView? =null


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
        changeEmail!!.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        when (v!!.id) {
            R.id.changeEmail -> {
                startActivity(Intent(this@PersonalInfoActivity, ChangeEmailActivity::class.java))
            }
        }

    }
}
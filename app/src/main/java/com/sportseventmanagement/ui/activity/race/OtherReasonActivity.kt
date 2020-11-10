package com.sportseventmanagement.ui.activity.race

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class OtherReasonActivity : AppCompatActivity(), View.OnClickListener {
private var submit:RelativeLayout?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_other_reason)

        init()
    }

    private fun init() {
        submit=findViewById(R.id.submit)


        submit!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.submit->{
                startActivity(Intent(this@OtherReasonActivity,OtherReasonActivity::class.java))
                finishAffinity()
            }
        }
        }
}
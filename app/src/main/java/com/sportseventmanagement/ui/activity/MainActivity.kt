package com.sportseventmanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import com.sportseventmanagement.R

class MainActivity : AppCompatActivity() {
    private var cdt:CountDownTimer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        cdt=object:CountDownTimer(2000,500){
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                startActivity(Intent(this@MainActivity,OnBoardingActivity::class.java))
            }

        }
        cdt!!.start()

    }
}
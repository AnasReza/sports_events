package com.sportseventmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.utility.Preferences

class MainActivity : AppCompatActivity() {
    private var cdt: CountDownTimer? = null
    private var pref:Preferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        pref= Preferences(this)

        cdt = object : CountDownTimer(2000, 500) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                val login=pref!!.getLogin()
                if(login!!){
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this@MainActivity, OnBoardingActivity::class.java))
                    finish()
                }

            }

        }
        cdt!!.start()

    }
}
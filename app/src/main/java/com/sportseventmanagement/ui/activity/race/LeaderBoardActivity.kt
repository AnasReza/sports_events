package com.sportseventmanagement.ui.activity.race

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class LeaderBoardActivity : AppCompatActivity(), View.OnClickListener {
private var backbutton:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_leaderboard)
        init();
    }

    private fun init(){
        backbutton=findViewById(R.id.back_button)

        backbutton!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
                finishAffinity()

            }
        }
    }
}
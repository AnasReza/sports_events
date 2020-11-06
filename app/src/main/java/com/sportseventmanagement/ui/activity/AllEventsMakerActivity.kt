package com.sportseventmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class AllEventsMakerActivity: AppCompatActivity(), View.OnClickListener {
private var foxLayout: RelativeLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_all_event_maker)

        init()
    }

    private fun init() {
        foxLayout=findViewById(R.id.foxLayout)

        foxLayout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.foxLayout->{
                startActivity(Intent(this@AllEventsMakerActivity,EventMakerDetailsActivity::class.java))
            }
        }
    }
}
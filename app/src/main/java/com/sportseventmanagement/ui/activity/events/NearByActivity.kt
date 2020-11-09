package com.sportseventmanagement.ui.activity.events

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.sportseventmanagement.R

class NearByActivity : AppCompatActivity(), View.OnClickListener {
    private var first_card: CardView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_nearby_event)

        init()
    }

    private fun init() {

        first_card=findViewById(R.id.first_card)

        first_card!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.first_card->{
                startActivity(Intent(this@NearByActivity, EventDetailActivity::class.java))
            }
        }
    }
}
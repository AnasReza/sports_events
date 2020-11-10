package com.sportseventmanagement.ui.activity.events

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.sportseventmanagement.R

class AllEventsActivity : AppCompatActivity(), View.OnClickListener {
    private var back_button: ImageView?=null
    private var first_card:CardView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_all_events)

        init()
    }

    private fun init() {
        first_card=findViewById(R.id.first_card)
        back_button=findViewById(R.id.back_button)



        back_button!!.setOnClickListener(this)
        first_card!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.first_card->{
                startActivity(Intent(this@AllEventsActivity, EventDetailActivity::class.java))
            }
            R.id.back_button -> {
                finish()
            }
        }
    }
}
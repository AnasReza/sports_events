package com.sportseventmanagement.ui.activity.settings

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.race.StartRaceActivity

class PaymentActivity() : AppCompatActivity(),
    View.OnClickListener {
    private var addcard:RelativeLayout?=null
    private var card1:LinearLayout?=null
    private var back_button: ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_payment)
        init()
    }
private fun init(){
    addcard=findViewById(R.id.add_card)
    card1=findViewById(R.id.card1)
    back_button=findViewById(R.id.back_button)
    back_button!!.setOnClickListener(this)
    addcard!!.setOnClickListener(this)
    card1!!.setOnClickListener(this)

}

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.add_card->{
                startActivity(Intent(this@PaymentActivity, AddCardActivity::class.java))
            }
            R.id.card1->{
                startActivity(Intent(this@PaymentActivity, StartRaceActivity::class.java))
            }
            R.id.back_button -> {
                finish()
            }
        }
    }

}
package com.sportseventmanagement.ui.activity.settings

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.sportseventmanagement.R

class MyAwardsActivity :AppCompatActivity(), View.OnClickListener {
private var first_card:CardView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_my_awards)

        init()
    }

    private fun init() {
        first_card=findViewById(R.id.first_card)


        first_card!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.first_card->{
showCustomDialog()
            }
        }
    }

    private fun showCustomDialog() {
        val dialog=Dialog(this@MyAwardsActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_award)

        val close_layout:RelativeLayout=dialog.findViewById(R.id.delete_layout)

        close_layout!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}
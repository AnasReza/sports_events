package com.sportseventmanagement.ui.activity.settings

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.race.LeaderBoardActivity

class MyAwardsActivity : AppCompatActivity(), View.OnClickListener {
    private var first_card: CardView? = null
    private var back_button: ImageView? = null
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
        first_card = findViewById(R.id.first_card)
        back_button = findViewById(R.id.back_button)

        first_card!!.setOnClickListener(this)
        back_button!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.first_card -> {
                showCustomDialog()
            }
            R.id.back_button -> {
                finish()
            }
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this@MyAwardsActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        var window=dialog.window
        window!!.setLayout(100,100)
        dialog.setContentView(R.layout.dialog_award)

        val close_layout: RelativeLayout = dialog.findViewById(R.id.delete_layout)
        val leadership: CardView = dialog.findViewById(R.id.leaderboard_card)

        close_layout!!.setOnClickListener { dialog.dismiss() }

        leadership!!.setOnClickListener {
            startActivity(
                Intent(
                    this@MyAwardsActivity,
                    LeaderBoardActivity::class.java
                )
            )
        }
        dialog.show()

    }
}
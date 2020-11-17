package com.sportseventmanagement.ui.activity.race

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.sportseventmanagement.R

class ReadyRaceActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mapFragment: SupportMapFragment? = null
    private var start_race:RelativeLayout?=null
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready_race)

        init()
    }

    private fun init() {
        mapFragment= supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        start_race=findViewById(R.id.start_race_button)


        mapFragment!!.getMapAsync(this)
        start_race!!.setOnClickListener(this)

    }

    override fun onMapReady(map: GoogleMap?) {
        Log.i("Anas", "MAP IS READY")
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.start_race_button->{
                startActivity(Intent(this@ReadyRaceActivity,FinishRaceActivity::class.java))
            }
        }
    }

}
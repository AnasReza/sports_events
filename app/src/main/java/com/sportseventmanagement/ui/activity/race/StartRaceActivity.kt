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
import kotlinx.android.synthetic.main.activity_all_events.*

class StartRaceActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mainScroll: ScrollView?=null
    private var mapFragment: SupportMapFragment? = null
    private var transparent_image: ImageView?=null
    private var startRace: RelativeLayout?=null
    private var backbutton: ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_race)

        init()
    }

    private fun init() {
        mapFragment= supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mainScroll=findViewById(R.id.mainScroll)
        transparent_image=findViewById(R.id.transparent_image)
        startRace=findViewById(R.id.start_race)
        backbutton=findViewById(R.id.back_button)
        mapFragment!!.getMapAsync(this)
        startRace!!.setOnClickListener(this)
        back_button.setOnClickListener(this)

        transparent_image!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                var action: Int = event!!.action
                return when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Disallow ScrollView to intercept touch events.
                        mainScroll!!.requestDisallowInterceptTouchEvent(true)
                        // Disable touch on transparent view
                        false
                    }
                    MotionEvent.ACTION_UP -> {
                        // Allow ScrollView to intercept touch events.
                        mainScroll!!.requestDisallowInterceptTouchEvent(false)
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        mainScroll!!.requestDisallowInterceptTouchEvent(true)
                        false
                    }
                    else -> true
                }
                return false
            }

        })


    }

    override fun onMapReady(map: GoogleMap?) {
        Log.i("Anas", "MAP IS READY")
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.start_race->{
                startActivity(Intent(this@StartRaceActivity,ReadyRaceActivity::class.java))
            }
            R.id.back_button->{
                finish()
            }
        }
    }

}
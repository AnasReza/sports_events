package com.sportseventmanagement.ui.activity.events

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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.settings.PaymentActivity

class EventDetailActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mainScroll: ScrollView? = null
    private var mapFragment: SupportMapFragment? = null
    private var transparent_image: ImageView? = null
    private var To_Pay: RelativeLayout? = null
    private var back_button: ImageView? = null
    private var event_maker: TextView? = null
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        init()
    }

    private fun init() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mainScroll = findViewById(R.id.mainScroll)
        transparent_image = findViewById(R.id.transparent_image)
        To_Pay = findViewById(R.id.ToPay)
        back_button = findViewById(R.id.back_button)
        event_maker = findViewById(R.id.event_maker)


        mapFragment!!.getMapAsync(this)
        To_Pay!!.setOnClickListener(this)
        back_button!!.setOnClickListener(this)
        event_maker!!.setOnClickListener(this)
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
        when (v!!.id) {
            R.id.ToPay -> {
               // startActivity(Intent(this@EventDetailActivity, PaymentActivity::class.java))
            }
            R.id.back_button -> {
                finish()
            }
            R.id.event_maker -> {
                startActivity(
                    Intent(
                        this@EventDetailActivity,
                        EventMakerDetailsActivity::class.java
                    )
                )
            }
        }
    }

}
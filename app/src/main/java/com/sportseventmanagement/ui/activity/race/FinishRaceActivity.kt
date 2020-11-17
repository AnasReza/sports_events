package com.sportseventmanagement.ui.activity.race

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.HomeActivity
import kotlin.math.roundToInt


class FinishRaceActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mapFragment: SupportMapFragment? = null
    private var transparent_image: ImageView? = null
    private var finish_race: RelativeLayout? = null
    private var info_layout2: RelativeLayout? = null
    private var info_layout: RelativeLayout? = null
    private var details_layout: LinearLayout? = null
    private var timeLayout: LinearLayout? = null
    private var distanceCoveredLayout: RelativeLayout? = null
    private var percentageLayout: LinearLayout? = null
    private var up_down_layout: RelativeLayout? = null
    private var up_down: ImageView? = null
    private var quitText: TextView? = null
    private var selectedOption: Int = 0
    private var check: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_race)

        init()
    }

    private fun init() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        transparent_image = findViewById(R.id.transparent_image)
        details_layout = findViewById(R.id.details_layout)
        up_down_layout = findViewById(R.id.up_down_layout)
        up_down = findViewById(R.id.up_down)
        quitText = findViewById(R.id.quitText)
        finish_race = findViewById(R.id.finish_race)
        info_layout2 = findViewById(R.id.info_layout2)
        info_layout = findViewById(R.id.info_layout)
        finish_race = findViewById(R.id.finish_race)
        timeLayout = findViewById(R.id.timeLayout)
        distanceCoveredLayout = findViewById(R.id.distanceCoveredLayout)
        percentageLayout = findViewById(R.id.percentageLayout)

        up_down!!.setImageResource(R.drawable.ic_down)

        mapFragment!!.getMapAsync(this)

        up_down_layout!!.setOnClickListener(this)
        quitText!!.setOnClickListener(this)
        finish_race!!.setOnClickListener(this)


    }

    override fun onMapReady(map: GoogleMap?) {
        map!!.uiSettings.isCompassEnabled = true

//        map!!.addMarker(
//            MarkerOptions()
//                .position(LatLng(24.8607, 67.0011))
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.path_track))
//        )
//        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(24.8607, 67.0011,),19F))
     //   map!!.animateCamera(CameraUpdateFactory.zoomTo(19F))
    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.up_down_layout -> {
                if (check) {
                    var params = info_layout!!.layoutParams
                    params.height = 100
                    info_layout!!.layoutParams = params
                    var params1 = details_layout!!.layoutParams
                    params1.height = 0
                    details_layout!!.layoutParams = params1

                    // info_layout!!.setBackgroundColor(Color.WHITE)
                    up_down!!.setImageResource(R.drawable.ic_up)
                    timeLayout!!.visibility = View.GONE
                    distanceCoveredLayout!!.visibility = View.GONE
                    percentageLayout!!.visibility = View.GONE
                    info_layout2!!.visibility = View.GONE
                    // details_layout!!.visibility=View.GONE
                    check = false
                } else {
                    var params = info_layout!!.layoutParams
                    params.height = 280
                    info_layout!!.layoutParams = params

                    var params1 = details_layout!!.layoutParams
                    params1.height = 250
                    details_layout!!.layoutParams = params1
                    // info_layout!!.setBackgroundColor(Color.TRANSPARENT)
                    up_down!!.setImageResource(R.drawable.ic_down)
                    timeLayout!!.visibility = View.VISIBLE
                    distanceCoveredLayout!!.visibility = View.VISIBLE
                    percentageLayout!!.visibility = View.VISIBLE
                    info_layout2!!.visibility = View.VISIBLE
                    //   details_layout!!.visibility=View.VISIBLE
                    check = true
                }

            }

            R.id.quitText -> {
                showAlertDialog()
            }
            R.id.finish_race -> {
                showFinishDialog()
            }
        }
    }

    private fun showFinishDialog() {
        val dialog = Dialog(this@FinishRaceActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_award)

        val close_layout: RelativeLayout = dialog.findViewById(R.id.delete_layout)
        val leaderboard: CardView = dialog.findViewById(R.id.leaderboard_card)

        close_layout.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this@FinishRaceActivity, LeaderBoardActivity::class.java))
        }
        leaderboard.setOnClickListener {
            startActivity(
                Intent(
                    this@FinishRaceActivity,
                    LeaderBoardActivity::class.java
                )
            )
        }
        dialog.show()
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this@FinishRaceActivity)
        alertDialog.setTitle("")
        alertDialog.setCancelable(false)
        alertDialog.setMessage("Are you sure you want to finish the race, you won't be able to undo it?")
        alertDialog.setPositiveButton(
            "Yes"
        ) { p0, p1 -> showCustomDialog() }
        alertDialog.setNegativeButton("No") { p0, p1 -> }
        alertDialog.show()

    }


    private fun showCustomDialog() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val dialog = Dialog(this@FinishRaceActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_reason)
        var window=dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        var radioGroup: RadioGroup = dialog.findViewById(R.id.radioGroup)
        val finishText: TextView = dialog.findViewById(R.id.finish_race)
        var radioButton: RadioButton

        radioGroup!!.setOnCheckedChangeListener { p0, selectedButtonId ->
            try {
                Log.e("Anas", "$selectedButtonId  selectedID")
                selectedOption = selectedButtonId

            } catch (e: NullPointerException) {
            }

        }

        finishText.setOnClickListener {

            radioButton = dialog.findViewById(selectedOption)
            val text = radioButton!!.text.toString()
            if (text != "Other") {

                startActivity(Intent(this@FinishRaceActivity, HomeActivity::class.java))
                finishAffinity()
            } else {
                startActivity(Intent(this@FinishRaceActivity, OtherReasonActivity::class.java))
                finishAffinity()
            }
        }
        dialog.show()
    }


}
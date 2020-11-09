package com.sportseventmanagement.ui.activity.race

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.sportseventmanagement.R
import kotlin.math.roundToInt


class FinishRaceActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mapFragment: SupportMapFragment? = null
    private var transparent_image: ImageView? = null
    private var details_layout: LinearLayout? = null
    private var up_down_layout: RelativeLayout? = null
    private var quitText: TextView? = null
    private var selectedOption: Int = 0


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
        quitText = findViewById(R.id.quitText)


        mapFragment!!.getMapAsync(this)
        up_down_layout!!.setOnClickListener(this)
        quitText!!.setOnClickListener(this)


    }

    override fun onMapReady(map: GoogleMap?) {
        Log.i("Anas", "MAP IS READY")
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.up_down_layout -> {
            }

            R.id.quitText -> {
                showAlertDialog()


            }
        }
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
        dialog.window!!.setLayout(width - 20, (height * 0.41).roundToInt())
        var radioGroup:RadioGroup = dialog.findViewById(R.id.radioGroup)
        val finishText: TextView = dialog.findViewById(R.id.finish_race)
        var radioButton:RadioButton

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
                Log.e("Anas", "$text")
            } else {
                Log.e("Anas", "$text other is pressed")
            }
        }
        dialog.show()
    }
}
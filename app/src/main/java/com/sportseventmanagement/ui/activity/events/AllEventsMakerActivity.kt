package com.sportseventmanagement.ui.activity.events

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.model.AllEventsMakersModel
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AllEventsMakerActivity : AppCompatActivity(), View.OnClickListener,
    AllEventsMakersModel.AllEventMakers {
    private var back_button: ImageView? = null
    private var eventLayout: LinearLayout? = null

    private var model: AllEventsMakersModel? = null
    private var pref: Preferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_all_event_maker)

        init()
    }

    private fun init() {
        pref = Preferences(this)
        model = AllEventsMakersModel(this, this)

        back_button = findViewById(R.id.back_button)
        eventLayout = findViewById(R.id.eventLayout)

        model!!.onAllEventMaker(pref!!.getToken()!!)

        back_button!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
                finish()
            }
        }
    }

    override fun onAllEventMaker(response: String) {
        val json = JSONObject(response)

        val message = json.getString("message")
        val success = json.getBoolean("success")

        if (success) {
            val data = json.getJSONObject("data")
            val eventMakers = data.getJSONArray("eventMakers")
            val inflator = LayoutInflater.from(this)
            for (x in 0 until eventMakers.length()) {
                var eventMakerDetails = eventMakers.getJSONObject(x)
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val c = Calendar.getInstance()
                val joinedAt = inputFormat.parse(eventMakerDetails.getString("createdAt"))
                var newView = inflator!!.inflate(R.layout.event_maker_item, null)
                var makerImage: ImageView = newView.findViewById(R.id.maker_icon)
                var makerName: TextView = newView.findViewById(R.id.event_maker_name)
                var joined_date: TextView = newView.findViewById(R.id.joined_date)
                var events_number: TextView = newView.findViewById(R.id.events_number)

                c.time = joinedAt
                var month = c.get(Calendar.MONTH) + 1

                makerName!!.text = eventMakerDetails.getString("businessName")
                events_number!!.text = "${eventMakerDetails.getInt("totalEvents")} Events"
                joined_date!!.text =
                    "Joined On ${c.get(Calendar.DAY_OF_MONTH)}/$month/${c.get(Calendar.YEAR)}"
                Picasso.with(this@AllEventsMakerActivity).load(eventMakerDetails.getString("logo"))
                    .into(makerImage)
                newView.setOnClickListener {
                    startActivity(
                        Intent(
                            this@AllEventsMakerActivity,
                            EventMakerDetailsActivity::class.java
                        ).putExtra("json", eventMakerDetails.toString())
                    )
                }

                eventLayout!!.addView(newView)
            }
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
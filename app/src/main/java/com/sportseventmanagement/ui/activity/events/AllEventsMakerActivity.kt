package com.sportseventmanagement.ui.activity.events

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
    private var search_bar: EditText? = null

    private var model: AllEventsMakersModel? = null
    private var pref: Preferences? = null
    private var mainJson: String = ""

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
        search_bar = findViewById(R.id.search_bar)

        model!!.onAllEventMaker(pref!!.getToken()!!)


        search_bar!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Anas", "$chars on click  ${chars!!.length}")
                if (mainJson != "") {
                    //search_bar!!.isEnabled=true
                    searchJson(chars)
                }
            }

            private fun searchJson(chars: CharSequence) {
                val json = JSONObject(mainJson)

                val message = json.getString("message")
                val success = json.getBoolean("success")
                eventLayout!!.removeAllViews()
                if (success) {
                    if (chars.isNotEmpty()) {
                        val data = json.getJSONObject("data")
                        val eventMakers = data.getJSONArray("eventMakers")
                        val inflator = LayoutInflater.from(this@AllEventsMakerActivity)
                        for (x in 0 until eventMakers.length()) {
                            var eventMakerDetails = eventMakers.getJSONObject(x)
                            var name = eventMakerDetails.getString("businessName")
                            if (name.contains(chars)) {
                                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                                val c = Calendar.getInstance()
                                val joinedAt =
                                    inputFormat.parse(eventMakerDetails.getString("createdAt"))
                                var newView = inflator!!.inflate(R.layout.event_maker_item, null)
                                var makerImage: ImageView = newView.findViewById(R.id.maker_icon)
                                var makerName: TextView =
                                    newView.findViewById(R.id.event_maker_name)
                                var joined_date: TextView = newView.findViewById(R.id.joined_date)
                                var events_number: TextView =
                                    newView.findViewById(R.id.events_number)

                                c.time = joinedAt
                                var month = c.get(Calendar.MONTH) + 1

                                makerName!!.text = name
                                events_number!!.text =
                                    "${eventMakerDetails.getInt("totalEvents")} Events"
                                joined_date!!.text =
                                    "Joined On ${c.get(Calendar.DAY_OF_MONTH)}/$month/${
                                        c.get(
                                            Calendar.YEAR
                                        )
                                    }"
                                Picasso.with(this@AllEventsMakerActivity)
                                    .load(eventMakerDetails.getString("logo"))
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
                        }

                    } else {
                        readingJson(mainJson)
                    }

                } else {
                    Toast.makeText(this@AllEventsMakerActivity, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        back_button!!.setOnClickListener(this)
    }

    fun readingJson(response: String) {
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


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
                finish()
            }
        }
    }

    override fun onAllEventMaker(response: String) {
        mainJson = response
        readingJson(response)
    }
}
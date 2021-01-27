package com.sportseventmanagement.ui.activity.events

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.sportseventmanagement.R
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AllEventsActivity : AppCompatActivity(), View.OnClickListener {
    private var card_list: LinearLayout?=null
    private var search_bar: EditText?=null

    private var json: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_all_events)

        init()
    }

    private fun init() {
       json = intent.getStringExtra("allEventJson")!!

        card_list=findViewById(R.id.card_list)
        search_bar=findViewById(R.id.search_bar)

        search_bar!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Anas", "$chars on click  ${chars!!.length}")
                searchJson(chars)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        readingJson(json)
    }
    private fun searchJson(chars: CharSequence) {
        card_list!!.removeAllViews()
        if (chars.isNotEmpty()) {
            val json = JSONObject(json)
            val data = json.getJSONObject("data")
            val eventArray = data.getJSONArray("events")

            if (eventArray.length() > 0) {
                for (x in 0 until eventArray.length()) {
                    val desSub: String
                    val inflator = LayoutInflater.from(this)
                    var newView = inflator!!.inflate(R.layout.event_card_view, null)
                    val dataJSON = eventArray.getJSONObject(x)
                    val titleText = dataJSON.getString("title")
                    if (titleText.contains(chars)) {
                        val imageURL = dataJSON.getString("picture")
                        val participatesList = dataJSON.getJSONArray("participates")
                        val totalPartcipants = dataJSON.getInt("totalParticipants")
                        val descriptionString = dataJSON.getString("description")
                        val startTimeData = dataJSON.getJSONObject("startTime")
                        val endTimeData = dataJSON.getJSONObject("endTime")
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        val c = Calendar.getInstance()
                        val c1 = Calendar.getInstance()
                        val c2 = Calendar.getInstance()
                        val c3 = Calendar.getInstance()
                        val sdf = SimpleDateFormat("MMM")
                        val startTimeFrom = inputFormat.parse(startTimeData.getString("from"))
                        val startTimeTo = inputFormat.parse(startTimeData.getString("to"))
                        val endTimeFrom = inputFormat.parse(endTimeData.getString("from"))
                        val endTimeTo = inputFormat.parse(endTimeData.getString("to"))
                        c.time = startTimeFrom
                        c1.time = startTimeTo
                        c2.time = endTimeFrom
                        c3.time = endTimeTo

                        desSub = if (descriptionString.length > 50) {
                            "${descriptionString.substring(0, 50)}..."
                        } else {
                            descriptionString
                        }

                        val title: TextView = newView.findViewById(R.id.title)
                        val details: TextView = newView.findViewById(R.id.details)
                        val description: TextView = newView.findViewById(R.id.descriptionText)
                        val event_image: ImageView = newView.findViewById(R.id.event_image)
                        val price: TextView = newView.findViewById(R.id.price)
                        val participantsText: TextView = newView.findViewById(R.id.participantsText)
                        val startTimeText: TextView = newView.findViewById(R.id.startTime)
                        val endTimeText: TextView = newView.findViewById(R.id.endTime)

                        newView.layoutParams =
                            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500)
                        title.text = titleText
                        details.text =
                            "${dataJSON.getString("category")} • ${dataJSON.getString("routeLength")} KM • ${
                                dataJSON.getString(
                                    "startingAreaAddress"
                                )
                            }"
                        description.text = desSub
                        price.text = "$${dataJSON.getString("price")}"
                        participantsText.text = "${participatesList.length()}/$totalPartcipants"
                        startTimeText.text =
                            "${startTimeFrom.date} ${sdf.format(c.time)} ${startTimeFrom.hours}:${startTimeFrom.minutes} " +
                                    "To ${startTimeTo.date} ${sdf.format(c1.time)} ${startTimeTo.hours}:${startTimeTo.minutes}"

                        endTimeText.text =
                            "${endTimeFrom.date} ${sdf.format(c2.time)} ${endTimeFrom.hours}:${endTimeFrom.minutes} " +
                                    "To ${endTimeTo.date} ${sdf.format(c3.time)} ${endTimeTo.hours}:${endTimeTo.minutes}"

                        event_image.setImageDrawable(null)
                        Picasso.with(this).load(imageURL).into(event_image)

                        newView.setOnClickListener {
                            startActivity(
                                Intent(
                                    this,
                                    EventDetailActivity::class.java
                                ).putExtra("details", eventArray[x].toString())
                            )
                        }

                        card_list!!.addView(newView)
                    }


                }
            }
        } else {
            readingJson(json)
        }

    }
    private fun readingJson(json: String) {
        val json = JSONObject(json)
        val data = json.getJSONObject("data")
        val eventArray = data.getJSONArray("events")
        if (eventArray.length() > 0) {
            for (x in 0 until eventArray.length()) {
                val desSub: String
                val inflator = LayoutInflater.from(this)
                var newView = inflator!!.inflate(R.layout.event_card_view, null)
                val dataJSON = eventArray.getJSONObject(x)
                val imageURL = dataJSON.getString("picture")
                val participatesList = dataJSON.getJSONArray("participates")
                val totalPartcipants = dataJSON.getInt("totalParticipants")
                val descriptionString = dataJSON.getString("description")
                val startTimeData = dataJSON.getJSONObject("startTime")
                val endTimeData = dataJSON.getJSONObject("endTime")
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val c = Calendar.getInstance()
                val c1 = Calendar.getInstance()
                val c2 = Calendar.getInstance()
                val c3 = Calendar.getInstance()
                val sdf = SimpleDateFormat("MMM")
                val startTimeFrom = inputFormat.parse(startTimeData.getString("from"))
                val startTimeTo = inputFormat.parse(startTimeData.getString("to"))
                val endTimeFrom = inputFormat.parse(endTimeData.getString("from"))
                val endTimeTo = inputFormat.parse(endTimeData.getString("to"))
                c.time = startTimeFrom
                c1.time = startTimeTo
                c2.time = endTimeFrom
                c3.time = endTimeTo

                desSub = if (descriptionString.length > 50) {
                    "${descriptionString.substring(0, 50)}..."
                } else {
                    descriptionString
                }

                val title: TextView = newView.findViewById(R.id.title)
                val details: TextView = newView.findViewById(R.id.details)
                val description: TextView = newView.findViewById(R.id.descriptionText)
                val event_image: ImageView = newView.findViewById(R.id.event_image)
                val price: TextView = newView.findViewById(R.id.price)
                val participantsText: TextView = newView.findViewById(R.id.participantsText)
                val startTimeText: TextView = newView.findViewById(R.id.startTime)
                val endTimeText: TextView = newView.findViewById(R.id.endTime)

                newView.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500)
                title.text = dataJSON.getString("title")
                details.text =
                    "${dataJSON.getString("category")} • ${dataJSON.getString("routeLength")} KM • ${
                        dataJSON.getString(
                            "startingAreaAddress"
                        )
                    }"
                description.text = desSub
                price.text = "$${dataJSON.getString("price")}"
                participantsText.text = "${participatesList.length()}/$totalPartcipants"
                startTimeText.text =
                    "${startTimeFrom.date} ${sdf.format(c.time)} ${startTimeFrom.hours}:${startTimeFrom.minutes} " +
                            "To ${startTimeTo.date} ${sdf.format(c1.time)} ${startTimeTo.hours}:${startTimeTo.minutes}"

                endTimeText.text =
                    "${endTimeFrom.date} ${sdf.format(c2.time)} ${endTimeFrom.hours}:${endTimeFrom.minutes} " +
                            "To ${endTimeTo.date} ${sdf.format(c3.time)} ${endTimeTo.hours}:${endTimeTo.minutes}"

                event_image.setImageDrawable(null)
                Picasso.with(this).load(imageURL).into(event_image)

                newView.setOnClickListener {
                    startActivity(
                        Intent(
                            this,
                            EventDetailActivity::class.java
                        ).putExtra("details", eventArray[x].toString())
                    )
                }

                card_list!!.addView(newView)

            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
        }
    }
}
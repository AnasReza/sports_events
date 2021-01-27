package com.sportseventmanagement.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R
import com.sportseventmanagement.model.FetchEventModel
import com.sportseventmanagement.ui.activity.race.StartRaceActivity
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ActiveEventsFragment() : Fragment(), FetchEventModel.Event {
    private var height: Int= 0
    private var active_layout: LinearLayout? = null

    private var model: FetchEventModel? = null
    private var pref: Preferences? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_active_events, null)
        init(view)
        return view
    }

    private fun init(view: View) {
        model = FetchEventModel(this, requireActivity())
        pref = Preferences(requireActivity())
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        active_layout = view.findViewById(R.id.active_layout)

        model!!.onEventsFilter(pref!!.getToken()!!, "active")


//        second_card!!.setOnClickListener {
//            requireActivity().startActivity(
//                Intent(
//                    activity,
//                    StartRaceActivity::class.java
//                )
//            )
//        }
    }

    override fun onEventLimit(response: String) {
        if (activity != null) {
            val json = JSONObject(response)
            val data = json.getJSONObject("data")
            val eventArray = data.getJSONArray("events")
            if (eventArray.length() > 0) {
                for (x in 0 until eventArray.length()) {
                    val desSub: String
                    val inflator = LayoutInflater.from(activity)
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
                    if(height<=864){
                        title.setTextSize(height*0.014.toFloat())
                        details.setTextSize(height*0.008.toFloat())
                        description.setTextSize(height*0.011.toFloat())
                        participantsText.setTextSize(height*0.007.toFloat())
                        startTimeText.setTextSize(height*0.009.toFloat())
                        endTimeText.setTextSize(height*0.009.toFloat())
                    }
//                    newView.layoutParams =
//                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500)
                    title!!.text = dataJSON.getString("title")
                    details!!.text =
                        "${dataJSON.getString("category")} • ${dataJSON.getString("routeLength")} KM • ${
                            dataJSON.getString(
                                "startingAreaAddress"
                            )
                        }"
                    description!!.text = desSub
                    price!!.text = "$${dataJSON.getString("price")}"
                    participantsText!!.text = "${participatesList.length()}/$totalPartcipants"
                    startTimeText!!.text =
                        "${startTimeFrom.date} ${sdf.format(c.time)} ${startTimeFrom.hours}:${startTimeFrom.minutes} " +
                                "To ${startTimeTo.date} ${sdf.format(c1.time)} ${startTimeTo.hours}:${startTimeTo.minutes}"

                    endTimeText!!.text =
                        "${endTimeFrom.date} ${sdf.format(c2.time)} ${endTimeFrom.hours}:${endTimeFrom.minutes} " +
                                "To ${endTimeTo.date} ${sdf.format(c3.time)} ${endTimeTo.hours}:${endTimeTo.minutes}"

                    event_image!!.setImageDrawable(null)
                    Picasso.with(requireActivity()).load(imageURL).into(event_image)

                    newView.setOnClickListener {
                        startActivity(
                            Intent(
                                requireActivity(),
                                StartRaceActivity::class.java
                            ).putExtra("details", eventArray[x].toString())
                        )
                    }

                    active_layout!!.addView(newView)

                }
            }

        }
    }
}
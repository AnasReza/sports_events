package com.sportseventmanagement.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R
import com.sportseventmanagement.model.AllEventsMakersModel
import com.sportseventmanagement.model.FetchEventModel
import com.sportseventmanagement.model.NearByEventModel
import com.sportseventmanagement.ui.activity.events.*
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), View.OnClickListener, NearByEventModel.NearByEvent,
    FetchEventModel.Event, AllEventsMakersModel.AllEventMakers {

    private var all_event_maker: TextView? = null
    private var nearby_event: TextView? = null
    private var all_event: TextView? = null
    private var nearbyCard: LinearLayout? = null
    private var eventLimitEvent: LinearLayout? = null
    private var event_maker_layout: LinearLayout? = null
    private var nearbyModel: NearByEventModel? = null
    private var eventModel: FetchEventModel? = null
    private var eventMakerModel: AllEventsMakersModel? = null
    private var pref: Preferences? = null
    private var locationManager: LocationManager? = null
    private var check: Boolean = true
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var height: Int = 0
    private var width: Int = 0
    private var nearbyJson: String = ""
    private var allEventJson: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        init(root)
        return root
    }

    private fun init(root: View) {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        val display = requireActivity().windowManager.defaultDisplay
        val stageWidth = display.width
        val stageHeight = display.height
        Log.d("Anas", "$stageWidth x $stageHeight")
        nearbyModel = NearByEventModel(this, requireActivity())
        eventModel = FetchEventModel(this, requireActivity())
        eventMakerModel = AllEventsMakersModel(this, requireActivity())
        pref = Preferences(requireActivity())
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        all_event_maker = root.findViewById(R.id.all_event_maker)
        all_event = root.findViewById(R.id.all_event)
        nearby_event = root.findViewById(R.id.nearby_event)

        nearbyCard = root.findViewById(R.id.nearbyCard)
        eventLimitEvent = root.findViewById(R.id.eventLimitLayout)
        event_maker_layout = root.findViewById(R.id.event_maker_layout)

        Log.e("Anas","${pref!!.getToken()} token")
        eventMakerModel!!.onEventMaker(pref!!.getToken()!!)
        eventModel!!.onEventsLimit(pref!!.getToken()!!)


        all_event_maker!!.setOnClickListener(this)
        all_event!!.setOnClickListener(this)
        nearby_event!!.setOnClickListener(this)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            } else {

                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0F,
                    locationListener
                )


            }
        } else {

            var list = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(list, 100)

        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.nearby_event -> {
                if (nearbyJson != "") {
                    requireActivity().startActivity(
                        Intent(activity, NearByActivity::class.java)
                            .putExtra("NearByJson", nearbyJson)
                    )
                }
            }
            R.id.all_event_maker -> {
                requireActivity().startActivity(
                    Intent(
                        activity,
                        AllEventsMakerActivity::class.java
                    )
                )
            }
            R.id.all_event -> {
                if (allEventJson != "") {
                    requireActivity().startActivity(
                        Intent(
                            activity,
                            AllEventsActivity::class.java
                        ).putExtra("allEventJson", allEventJson)
                    )
                }

            }

        }
    }

    private fun getNearByEvent(location: Location) {
        try {
            var latitude = location.latitude
            var longitude = location.longitude
            var token = pref!!.getToken()
            var json = JSONObject()
            json.put("longitude", longitude)
            json.put("latitude", latitude)
            Log.d("Anas", "$json")
            nearbyModel!!.onGetNearByEvent(latitude, longitude, token!!)
        } catch (e: ArrayIndexOutOfBoundsException) {
            Toast.makeText(
                requireActivity(),
                "Please Switch on Your Location to get Nearby Events",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {

            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0F,
                    locationListener
                )
            }

        }
    }

    private val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            // Log.d("Anas","${location.longitude} longi")
            if (check) {
                Log.d("Anas", "${location.longitude} longi")
                check = false

                latitude = location.latitude
                longitude = location.longitude
                getNearByEvent(location)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onProviderDisabled(provider: String) {
            latitude = 0.0
            longitude = 0.0
        }
    }

    override fun onNearBy(response: String) {
        Log.i("Anas", "onNearBy")
        nearbyJson = response
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

                newView.layoutParams =
                    ViewGroup.LayoutParams((width * 0.84).toInt(), (height * 0.23).toInt())
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
                Picasso.with(requireActivity()).load(imageURL).into(event_image)

                newView.setOnClickListener {
                    startActivity(
                        Intent(
                            requireActivity(),
                            EventDetailActivity::class.java
                        ).putExtra("details", eventArray[x].toString())
                    )
                }

                nearbyCard!!.addView(newView)

            }
        }


    }

    override fun onEventLimit(response: String) {
        Log.i("Anas", "onEventLimit")
        allEventJson = response
        val json = JSONObject(response)
        val data = json.getJSONObject("data")
        val eventArray = data.getJSONArray("events")

        for (x in 0 until 2) {
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

            newView.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (height * 0.3).toInt())
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
            Picasso.with(requireActivity()).load(imageURL).into(event_image)

            newView.setOnClickListener {
                startActivity(
                    Intent(
                        requireActivity(),
                        EventDetailActivity::class.java
                    ).putExtra("details", eventArray[x].toString())
                )
            }

            eventLimitEvent!!.addView(newView)

        }

    }

    override fun onAllEventMaker(response: String) {
        Log.i("Anas", "onAllEventMaker")
        val json = JSONObject(response)
        val data = json.getJSONObject("data")
        val eventArray = data.getJSONArray("eventMakers")
        if (eventArray.length() > 0) {
            for (x in 0 until eventArray.length()) {
                val jsonObject = eventArray.getJSONObject(x)
                val inflator = LayoutInflater.from(activity)
                var newView = inflator!!.inflate(R.layout.event_maker_card, null)
                val logoURL = jsonObject.getString("logo")

                newView.layoutParams =
                    ViewGroup.LayoutParams(
                        (width * 0.185).toInt(),
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )


                val imageView: CircleImageView = newView.findViewById(R.id.image_view)
                val maker_name: TextView = newView.findViewById(R.id.maker_name)
                maker_name.text = jsonObject.getString("businessName")
                Picasso.with(requireActivity()).load(logoURL).into(imageView)
                newView.setOnClickListener {
                    startActivity(
                        Intent(
                            requireActivity(),
                            EventMakerDetailsActivity::class.java
                        ).putExtra("json", jsonObject.toString())
                    )
                }

                event_maker_layout!!.addView(newView)

            }
        }
    }
}
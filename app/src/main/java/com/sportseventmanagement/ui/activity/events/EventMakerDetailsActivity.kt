package com.sportseventmanagement.ui.activity.events

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.sportseventmanagement.R
import com.sportseventmanagement.model.FetchEventModel
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class EventMakerDetailsActivity : AppCompatActivity(), View.OnClickListener, FetchEventModel.Event {
    private var back_button: ImageView? = null
    private var headline: TextView? = null
    private var maker_image: ImageView? = null
    private var maker_name: TextView? = null
    private var description: TextView? = null
    private var bottomLayout: RelativeLayout? = null
    private var eventsLayout:LinearLayout?=null

    private var mainJson: JSONObject? = null
    private var model: FetchEventModel? = null
    private var pref:Preferences?=null
    private var website: String = ""
    private var makerID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_event_maker_details)
        init()
    }

    private fun init() {
        mainJson = JSONObject(intent.getStringExtra("json"))
        model= FetchEventModel(this,this)
        pref= Preferences(this)

        website = mainJson!!.getString("websiteUrl")
        makerID = mainJson!!.getString("_id")
        model!!.onEventsByMakerID(pref!!.getToken()!!,makerID)

        back_button = findViewById(R.id.back_button)
        headline = findViewById(R.id.headline)
        maker_image = findViewById(R.id.maker_image)
        maker_name = findViewById(R.id.maker_name)
        description = findViewById(R.id.description)
        bottomLayout = findViewById(R.id.bottomLayout)
        eventsLayout=findViewById(R.id.eventsLayout)

        headline!!.text = mainJson!!.getString("businessName")
        maker_name!!.text = mainJson!!.getString("businessName")
        //  description!!.text=mainJson!!.getString("")

        Picasso.with(this).load(mainJson!!.getString("logo")).into(maker_image)



        back_button!!.setOnClickListener(this)
        bottomLayout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
                finish()
            }
            R.id.bottomLayout -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(website))
                val resolveInfo: ResolveInfo = this.packageManager
                    .resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY)!!
                val packageName = resolveInfo.activityInfo.packageName

                // Use the explicit browser package name

                // Use the explicit browser package name
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(website)
                i.setPackage(packageName)
                startActivity(i)
            }
        }
    }

    override fun onEventLimit(response: String) {
        val json = JSONObject(response)
        val data = json.getJSONObject("data")
        val eventArray = data.getJSONArray("events")

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

            eventsLayout!!.addView(newView)

        }
    }
}
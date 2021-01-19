package com.sportseventmanagement.ui.activity.settings

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.sportseventmanagement.R
import com.sportseventmanagement.model.AwardsModel
import com.sportseventmanagement.model.FetchEventModel
import com.sportseventmanagement.ui.activity.race.LeaderBoardActivity
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MyAwardsActivity : AppCompatActivity(), View.OnClickListener, AwardsModel.onAwardsResult,
    FetchEventModel.Event {
    private var back_button: ImageView? = null
    private var awardsLayout: LinearLayout? = null
    private var maker_name: TextView? = null
    private var dialog: Dialog? = null

    private var model: AwardsModel? = null
    private var eventModel: FetchEventModel? = null
    private var pref: Preferences? = null
    private var width: Int? = null
    private var height: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_my_awards)

        init()
    }

    private fun init() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        model = AwardsModel(this, this)
        eventModel = FetchEventModel(this, this)
        pref = Preferences(this)
        model!!.onAwards(pref!!.getToken()!!)


        back_button = findViewById(R.id.back_button)
        awardsLayout = findViewById(R.id.awards_layout)

        back_button!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
                finish()
            }
        }
    }

    private fun showCustomDialog(
        rank: Int,
        eventID: String,
        completedTime: String,
        title: String,
        length: Int
    ) {
        dialog = Dialog(this@MyAwardsActivity)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)

        dialog!!.setContentView(R.layout.dialog_award)
        var window = dialog!!.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val close_layout: RelativeLayout = dialog!!.findViewById(R.id.delete_layout)
        val leadership: CardView = dialog!!.findViewById(R.id.leaderboard_card)
        val descriptionText: TextView = dialog!!.findViewById(R.id.descriptionText)
        val medal_icon: ImageView = dialog!!.findViewById(R.id.medal_icon)
        val profile_image: ImageView = dialog!!.findViewById(R.id.profile_image)
        maker_name = dialog!!.findViewById(R.id.maker_name)

        when (rank) {
            1 -> {
                medal_icon.setImageResource(R.drawable.medal_gold)
            }
            2 -> {
                medal_icon.setImageResource(R.drawable.medal_silver)
            }
            3 -> {
                medal_icon.setImageResource(R.drawable.medal_bronze)
            }
            else -> {
                medal_icon.setImageResource(R.drawable.medal_runnerup)
            }
        }

        Picasso.with(dialog!!.context).load(pref!!.getPhotoURL()).into(profile_image)
        descriptionText!!.text =
            "Hi ${pref!!.getFullName()}. Congratulations on completing $length km of $title in $completedTime."

        close_layout!!.setOnClickListener { dialog!!.dismiss() }

        leadership!!.setOnClickListener {
            startActivity(
                Intent(
                    this@MyAwardsActivity,
                    LeaderBoardActivity::class.java
                ).putExtra("eventId", eventID)
            )
        }
        dialog!!.show()

    }

    override fun onAwardsResult(response: String) {
        val json = JSONObject(response)
        val message = json.getString("message")
        val success = json.getBoolean("success")

        if (success) {
            val data = json.getJSONObject("data")
            val awardsArray = data.getJSONArray("awards")
            val inflator = LayoutInflater.from(this@MyAwardsActivity)
            for (x in 0 until awardsArray.length()) {
                val awardsData = awardsArray.getJSONObject(x)
                val participant = awardsData.getJSONObject("participant")
                val dataJSON = awardsData.getJSONObject("event")
                val status = participant.getString("status")
                val rank = participant.getInt("rank")
                val eventID = dataJSON.getString("_id")
                val completionTime = participant.getInt("time")
                val lengthRoute = dataJSON.getInt("routeLength")

                var newView = inflator!!.inflate(R.layout.awards_item, null)
                val desSub: String

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

                val title: TextView = newView.findViewById(R.id.event_title)
                val details: TextView = newView.findViewById(R.id.event_details)
                val description: TextView = newView.findViewById(R.id.event_description)
                val event_image: ImageView = newView.findViewById(R.id.event_image)
                val price: TextView = newView.findViewById(R.id.price)
                val participantsText: TextView = newView.findViewById(R.id.participant_number)
                val startTimeText: TextView = newView.findViewById(R.id.start_time)
                val endTimeText: TextView = newView.findViewById(R.id.end_time)
                val medal_icon: ImageView = newView.findViewById(R.id.medal_icon)

                when (rank) {
                    1 -> {
                        medal_icon.setImageResource(R.drawable.medal_gold)
                    }
                    2 -> {
                        medal_icon.setImageResource(R.drawable.medal_silver)
                    }
                    3 -> {
                        medal_icon.setImageResource(R.drawable.medal_bronze)
                    }
                    else -> {
                        medal_icon.setImageResource(R.drawable.medal_runnerup)
                    }
                }

                newView.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (height!! * 0.3).toInt()
                    )
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
                Picasso.with(this@MyAwardsActivity).load(imageURL).into(event_image)
                var completedString: String = ""
                completedString = if (completionTime >= 60) {
                    var hours = completionTime / 60
                    var mins = completionTime % 60
                    "$hours hour and $mins mins"
                } else {
                    "$completionTime mins"
                }
                newView.setOnClickListener {
                    eventModel!!.onEventsByID(pref!!.getToken()!!, eventID)
                    showCustomDialog(
                        rank,
                        eventID,
                        completedString,
                        dataJSON.getString("title"),
                        lengthRoute
                    )
                }

                awardsLayout!!.addView(newView)

            }
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onEventLimit(response: String) {
        val mainJson = JSONObject(response)
        val success = mainJson.getBoolean("success")
        if (success) {
            val data = mainJson.getJSONObject("data")
            val eventsArray = data.getJSONArray("events")
            for (i in 0 until eventsArray.length()) {
                val eventsObject = eventsArray.getJSONObject(i)
                val makerObject = eventsObject.getJSONObject("eventMaker")

                val makerName = makerObject.getString("businessName")
                if (dialog!!.isShowing) {
                    maker_name!!.text = makerName
                    maker_name!!.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }

            }
        }
    }
}
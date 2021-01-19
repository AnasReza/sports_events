package com.sportseventmanagement.ui.activity.race

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.model.LeaderboardModel
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONException
import org.json.JSONObject

class LeaderBoardActivity : AppCompatActivity(), View.OnClickListener,
    LeaderboardModel.onLeaderboardResult {
    private var backbutton: ImageView? = null
    private var top_three_layout: LinearLayout? = null
    private var runnerup_layout: LinearLayout? = null

    private var model: LeaderboardModel? = null
    private var pref: Preferences? = null
    private var topThree: ArrayList<JSONObject>? = null
    private var runnerUpList: ArrayList<JSONObject>? = null
    private var eventID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_leaderboard)
        init()
    }

    private fun init() {
        eventID = intent.getStringExtra("eventId")!!
        pref = Preferences(this)
        model = LeaderboardModel(this, this)
        topThree = ArrayList()
        runnerUpList = ArrayList()

        model!!.onGetLeaderBoard(pref!!.getToken()!!, eventID)

        backbutton = findViewById(R.id.back_button)
        top_three_layout = findViewById(R.id.topThreeLayout)
        runnerup_layout = findViewById(R.id.runnerup_layout)

        backbutton!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
                finish()
            }
        }
    }

    override fun onleaderboardResult(response: String) {
        Log.e("Anas", "inside the onleaderboard result")
        val json = JSONObject(response)
        val success = json.getBoolean("success")
        val message = json.getString("message")
        if (success) {
            val data = json.getJSONObject("data")
            val leaderboard = data.getJSONObject("leaderboard")
            val participants = leaderboard.getJSONArray("participants")
            for (x in 0 until participants.length()) {
                val json1 = participants.getJSONObject(x)
                val rank = json1.getInt("rank")
                if (rank == 1 || rank == 2 || rank == 3) {
                    topThree!!.add(json1)
                } else {
                    runnerUpList!!.add(json1)
                }
            }
            val inflator = LayoutInflater.from(this)
            for (x in 0 until topThree!!.size) {
                val json = topThree!![x]
                val rank = json.get("rank")
                val userObject = json.getJSONObject("user")
                val nameString = userObject.getString("fullname")


                var newView = inflator!!.inflate(R.layout.top_three_item, null)
                var number: TextView = newView.findViewById(R.id.number)
                var name: TextView = newView.findViewById(R.id.user_name)
                var profile_image: CircleImageView = newView.findViewById(R.id.profile_image)
                var medal_icon: ImageView = newView.findViewById(R.id.medal_icon)

                number!!.text = "${x + 1}."
                name!!.text = nameString
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
                top_three_layout!!.addView(newView)
                try {
                    val profileUrl = userObject.getString("image")
                    Picasso.with(this@LeaderBoardActivity).load(profileUrl).into(profile_image)
                } catch (e: KotlinNullPointerException) {
                    profile_image!!.setImageResource(R.drawable.ic_user_placeholder)
                } catch (e: JSONException) {
                    profile_image!!.setImageResource(R.drawable.ic_user_placeholder)
                }
            }

            for (x in 0 until runnerUpList!!.size) {
                val json = topThree!![x]
                val rank = json.get("rank")
                val userObject = json.getJSONObject("user")
                val nameString = userObject.getString("fullname")
                val timeValue = json.getInt("time")

                var newView = inflator!!.inflate(R.layout.runnerup_item, null)
                var time: TextView = newView.findViewById(R.id.time)
                var name: TextView = newView.findViewById(R.id.name)
                var profile_image: CircleImageView = newView.findViewById(R.id.profile_image)
                var medal_icon: ImageView = newView.findViewById(R.id.medal_icon)

                try {
                    val profileUrl = userObject.getString("image")
                    Picasso.with(this@LeaderBoardActivity).load(profileUrl).into(profile_image)
                } catch (e: KotlinNullPointerException) {
                    profile_image!!.setImageResource(R.drawable.ic_user_placeholder)
                } catch (e: JSONException) {
                    profile_image!!.setImageResource(R.drawable.ic_user_placeholder)
                }
                var completedTime=""
                completedTime = if (timeValue >= 60) {
                    var hours=timeValue/60
                    var mins=timeValue% 60
                    "$hours hours and $mins"
                }else{
                    "$timeValue mins"
                }

                time!!.text = completedTime
                name!!.text = nameString
                medal_icon.setImageResource(R.drawable.medal_runnerup)
                runnerup_layout!!.addView(newView)
            }

        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
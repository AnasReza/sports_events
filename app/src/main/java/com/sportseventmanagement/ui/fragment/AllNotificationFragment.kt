package com.sportseventmanagement.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportseventmanagement.R
import com.sportseventmanagement.`interface`.BaseItem
import com.sportseventmanagement.adapter.BaseListAdapter
import com.sportseventmanagement.data.NotificationData
import com.sportseventmanagement.model.FetchEventModel
import com.sportseventmanagement.model.NotificationModel
import com.sportseventmanagement.ui.activity.events.EventDetailActivity
import com.sportseventmanagement.ui.activity.race.LeaderBoardActivity
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AllNotificationFragment : Fragment(), NotificationModel.Notification, FetchEventModel.Event {
    private var recycler_view: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null

    private var notificationModel: NotificationModel? = null
    private var eventModel: FetchEventModel? = null

    private var pref: Preferences? = null
    private var token: String = ""

    private var today: ArrayList<NotificationData>? = null
    private var lastWeek: ArrayList<NotificationData>? = null
    private var lastMonth: ArrayList<NotificationData>? = null
    private var lastYear: ArrayList<NotificationData>? = null

    private var adapter: BaseListAdapter? = null
    private var mainList: MutableList<BaseItem>? = null
    private var notificationObject: JSONArray? = null
    private var eventObject: JSONArray? = null
    private var isClickable: Boolean = false
    private var id2: String = ""
    private var eventTitle: String = ""
    private var routeLength: Int = 0
    private var maker_name: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_all_notifications, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        mainList = mutableListOf()
        notificationModel = NotificationModel(this, requireActivity())
        eventModel = FetchEventModel(this, requireActivity())
        pref = Preferences(requireActivity())
        today = ArrayList()
        lastWeek = ArrayList()
        lastMonth = ArrayList()
        lastYear = ArrayList()
        eventObject = JSONArray()

        recycler_view = view.findViewById(R.id.recycler_view)
        layoutManager = LinearLayoutManager(requireActivity())

        token = pref!!.getToken()!!
        notificationModel!!.onGetNotification(token)
    }

    private fun showCustomDialog(
        rank: Int,
        eventID: String,
        completedTime: String,
        title: String,
        length: Int,
        maker_name: String
    ) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        dialog.setContentView(R.layout.dialog_award)
        var window = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val close_layout: RelativeLayout = dialog.findViewById(R.id.delete_layout)
        val leadership: CardView = dialog.findViewById(R.id.leaderboard_card)
        val descriptionText: TextView = dialog.findViewById(R.id.descriptionText)
        val medal_icon: ImageView = dialog.findViewById(R.id.medal_icon)
        val profile_image: ImageView = dialog.findViewById(R.id.profile_image)
        val makerName: TextView = dialog.findViewById(R.id.maker_name)

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

        Picasso.with(dialog.context).load(pref!!.getPhotoURL()).into(profile_image)
        descriptionText!!.text =
            "Hi ${pref!!.getFullName()}. Congratulations on completing $length km of $title in $completedTime."
        makerName.text = maker_name
        makerName.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        close_layout!!.setOnClickListener { dialog.dismiss() }

        leadership!!.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    LeaderBoardActivity::class.java
                ).putExtra("eventId", eventID)
            )
        }
        dialog.show()

    }

    override fun onNotification(response: String) {
        Log.e("Anas", "$response from notification")
        val json = JSONObject(response)
        val data = json.getJSONObject("data")
        notificationObject = data.getJSONArray("notifications")

        Log.d("Anas", "${notificationObject!!.length()}   notification length")
        for (x in 0 until notificationObject!!.length()) {
            val jsonObject = notificationObject!!.getJSONObject(x)
            val notiID = jsonObject.getString("_id")
            val userID = jsonObject.getString("user")
            val title = jsonObject.getString("title")
            val body = jsonObject.getString("body")
            val from = jsonObject.getString("from")
            val type = jsonObject.getString("type")
            val createdAt = jsonObject.getString("createdAt")
            val updatedAt = jsonObject.getString("updatedAt")
            val eventData = jsonObject.getJSONObject("data")
            val eventID = eventData.getString("event")
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val c = Calendar.getInstance()
            val current = Calendar.getInstance()
            val startTimeFrom = inputFormat.parse(createdAt)
            c.time = startTimeFrom
            val diff = current.time.time - c.time.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days: Int = (hours / 24).toInt()

            eventModel!!.onEventsByID(pref!!.getToken()!!, eventID)


            if (x == notificationObject!!.length() - 1) {
                isClickable = true
            }

            when {
                days == 0 -> {
                    Log.d("Anas", "Today")
                    today!!.add(
                        NotificationData(
                            notiID,
                            userID,
                            title,
                            body,
                            from,
                            type,
                            createdAt,
                            updatedAt,
                            data
                        )
                    )
                }
                days in 1..30 -> {
                    Log.d("Anas", "Last Week")
                    lastWeek!!.add(
                        NotificationData(
                            notiID,
                            userID,
                            title,
                            body,
                            from,
                            type,
                            createdAt,
                            updatedAt,
                            data
                        )
                    )
                }
                days in 30..365 -> {
                    Log.d("Anas", "Last Month")
                    lastMonth!!.add(
                        NotificationData(
                            notiID,
                            userID,
                            title,
                            body,
                            from,
                            type,
                            createdAt,
                            updatedAt,
                            data
                        )
                    )
                }
                days > 365 -> {
                    Log.d("Anas", "Last Year")
                    lastYear!!.add(
                        NotificationData(
                            notiID,
                            userID,
                            title,
                            body,
                            from,
                            type,
                            createdAt,
                            updatedAt,
                            data
                        )
                    )
                }
            }

            Log.d("Anas", "days=$days - hours=$hours - minutes=$minutes - seconds=$seconds")
        }
        if (today!!.size != 0) {

            var bodyList: MutableList<String> = mutableListOf()
            var notiList: MutableList<String> = mutableListOf()
            var sideList: MutableList<Int> = mutableListOf()
            for (x in 0 until today!!.size) {
                val body = lastWeek!![x].getBody()
                bodyList.add(body)
                notiList.add(lastWeek!![x].getNotificationID())
                if (body.contains("CHECK NOW!", true)) {
                    sideList.add(R.drawable.ic_red_cycle)
                } else {
                    sideList.add(R.drawable.ic_blue_medal)
                }
            }
            var map: HashMap<String, MutableList<String>> =
                hashMapOf("notificationID" to notiList, "body" to bodyList)
            createAlphabetizedFruit(map, "Today", sideList)
        }

        if (lastWeek!!.size != 0) {

            var bodyList: MutableList<String> = mutableListOf()
            var notiList: MutableList<String> = mutableListOf()
            var sideList: MutableList<Int> = mutableListOf()

            for (x in 0 until lastWeek!!.size) {
                val body = lastWeek!![x].getBody()
                bodyList.add(body)
                notiList.add(lastWeek!![x].getNotificationID())
                if (body.contains("CHECK NOW!", true)) {
                    sideList.add(R.drawable.ic_yellow_cycle)
                } else {
                    sideList.add(R.drawable.ic_purple_medal)
                }

            }
            var map: HashMap<String, MutableList<String>> =
                hashMapOf("notificationID" to notiList, "body" to bodyList)
            createAlphabetizedFruit(map, "Last Week", sideList)
        }
        if (lastMonth!!.size != 0) {

            var bodyList: MutableList<String> = mutableListOf()
            var notiList: MutableList<String> = mutableListOf()
            var sideList: MutableList<Int> = mutableListOf()
            for (x in 0 until lastMonth!!.size) {
                try{
                    val body = lastWeek!![x].getBody()
                    bodyList.add(body)
                    notiList.add(lastWeek!![x].getNotificationID())
                    if (body.contains("CHECK NOW!", true)) {
                        sideList.add(R.drawable.ic_yellow_cycle)
                    } else {
                        sideList.add(R.drawable.ic_purple_medal)
                    }
                }catch(e:IndexOutOfBoundsException){
                    e.printStackTrace()
                }

            }
            var map: HashMap<String, MutableList<String>> =
                hashMapOf("notificationID" to notiList, "body" to bodyList)
            createAlphabetizedFruit(map, "Last Month", sideList)
        }

        if (lastYear!!.size != 0) {

            var bodyList: MutableList<String> = mutableListOf()
            var notiList: MutableList<String> = mutableListOf()
            var sideList: MutableList<Int> = mutableListOf()
            for (x in 0 until lastYear!!.size) {
                val body = lastWeek!![x].getBody()
                bodyList.add(body)
                notiList.add(lastWeek!![x].getNotificationID())
                if (body.contains("CHECK NOW!", true)) {
                    sideList.add(R.drawable.ic_yellow_cycle)
                } else {
                    sideList.add(R.drawable.ic_purple_medal)
                }
            }
            var map: HashMap<String, MutableList<String>> =
                hashMapOf("notificationID" to notiList, "body" to bodyList)
            createAlphabetizedFruit(map, "Last Year", sideList)
        }

        adapter = BaseListAdapter { fruitClicked ->
            if (isClickable) {
                for (x in 0 until notificationObject!!.length()) {
                    val data = notificationObject!!.getJSONObject(x)
                    val dataObject=data.getJSONObject("data")
                    val id = data.getString("_id")
                    val title = data.getString("title")
                    Log.w("Anas", "$title title")

                    if (fruitClicked.uniqueId.toString() == id) {
                        val eventObject1 = data.getJSONObject("data")
                        val eventID = eventObject1.getString("event")
                        for (z in 0 until eventObject!!.length()) {
                            val json = eventObject!!.getJSONObject(z)
                            id2 = json.getString("_id")

                            if (id2 == eventID) {

                                eventTitle = json.getString("title")
                                routeLength = json.getInt("routeLength")
                                val makerObject = json.getJSONObject("eventMaker")
                                maker_name = makerObject.getString("businessName")

                                if (title == "Congratulations! you won") {
                                    val rank = dataObject.getInt("rank")
                                    val completedTime=dataObject.getInt("completionTime")
                                    var completedString: String = ""
                                    completedString = if (completedTime >= 60) {
                                        var hours = completedTime / 60
                                        var mins = completedTime % 60
                                        "$hours hour and $mins mins"
                                    } else {
                                        "$completedTime mins"
                                    }
                                    showCustomDialog(
                                        rank, id2, completedString, eventTitle,
                                        routeLength, maker_name
                                    )
                                } else {
                                    requireActivity().startActivity(
                                        Intent(
                                            requireActivity(),
                                            EventDetailActivity::class.java
                                        ).putExtra("details", json.toString())
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
        recycler_view!!.layoutManager = layoutManager
        recycler_view!!.adapter = adapter

        adapter!!.submitList(mainList)
    }

    private fun createAlphabetizedFruit(
        list: HashMap<String, MutableList<String>>,
        headText: String,
        sideImage: MutableList<Int>
    ) {
        var notiList: MutableList<String> = list["notificationID"]!!
        var bodyList: MutableList<String> = list["body"]!!
        var currentHeader = true

        for (x in 0 until notiList.size) {
            if (currentHeader) {
                mainList!!.add(BaseListAdapter.HeaderItem(headText))
                currentHeader = false
            }
            mainList!!.add(
                BaseListAdapter.FruitItem(
                    requireActivity(),
                    bodyList!![x],
                    notiList!![x],
                    sideImage!![x],
                    ""
                )
            )
        }
    }

    override fun onEventLimit(response: String) {
        val json = JSONObject(response)
        val data = json.getJSONObject("data")
        val eventsData = data.getJSONArray("events")

        val eventsDetails = eventsData.getJSONObject(0)
        eventObject!!.put(eventsDetails)
    }
}
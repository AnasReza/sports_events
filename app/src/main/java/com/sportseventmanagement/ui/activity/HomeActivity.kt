package com.sportseventmanagement.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.sportseventmanagement.R
import com.sportseventmanagement.model.LogoutModel
import com.sportseventmanagement.ui.activity.login.LoginActivity
import com.sportseventmanagement.ui.fragment.AllNotificationFragment
import com.sportseventmanagement.ui.fragment.HomeFragment
import com.sportseventmanagement.ui.fragment.MyEventsFragment
import com.sportseventmanagement.ui.fragment.SettingsFragment
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import kotlin.math.roundToInt


class HomeActivity : AppCompatActivity(), LogoutModel.Logout {

    private var menuIcon: ImageView? = null
    private var info: ImageView? = null
    private var headlineText: TextView? = null
    private var emailText: TextView? = null
    private var nameText: TextView? = null
    private var profileImage: CircleImageView? = null
    private var eventText: TextView? = null
    private var homeText: TextView? = null
    private var notiText: TextView? = null
    private var logout: TextView? = null
    private var settingsText: TextView? = null
    private var drawerLayout: DrawerLayout? = null
    private var navView: NavigationView? = null

    private var homeFragement: HomeFragment? = null
    private var eventsFragement: MyEventsFragment? = null
    private var notiFragement: AllNotificationFragment? = null
    private var settingsFragement: SettingsFragment? = null
    private var fragmentManager: FragmentManager? = null
    private var fragmentTransaction: FragmentTransaction? = null
    private var logoutModel: LogoutModel? = null
    private var check: Boolean = true
    private var pref: Preferences? = null


    @SuppressLint("WrongConstant", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_home)

        init()
    }

    private fun init() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        homeFragement = HomeFragment()
        eventsFragement = MyEventsFragment()
        notiFragement = AllNotificationFragment()
        settingsFragement = SettingsFragment()
        switchFragment(homeFragement!!)
        pref = Preferences(this)
        logoutModel = LogoutModel(this, this)


        menuIcon = findViewById(R.id.ic_menu_button)
        menuIcon!!.setImageResource(R.drawable.ic_menu)
        info = findViewById(R.id.info_button)
        headlineText = findViewById(R.id.headline_text)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        var view = navView!!.getHeaderView(0)
        emailText = view.findViewById(R.id.emailText)
        nameText = view.findViewById(R.id.nameText)
        profileImage = view.findViewById(R.id.profile_image)
        homeText = view.findViewById(R.id.home)
        eventText = view.findViewById(R.id.my_events)
        notiText = view.findViewById(R.id.notifications)
        settingsText = view.findViewById(R.id.settings)
        logout = view.findViewById(R.id.logout)

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params.topMargin = (height * 0.6).roundToInt()

        emailText!!.text = pref!!.getEmail()
        nameText!!.text = pref!!.getFullName()
        if (pref!!.getPhotoURL() != "") {
            profileImage!!.setImageDrawable(null)
            Picasso.with(this).load(pref!!.getPhotoURL()).into(profileImage)
        }


        logout!!.layoutParams = params

        menuIcon!!.setOnClickListener {
            if (check) {
                drawerLayout!!.open()
            } else {
                switchFragment(homeFragement!!)
                check = true
                menuIcon!!.setImageResource(R.drawable.ic_menu)
                headlineText!!.text = "Explore Events"
                info!!.visibility = View.VISIBLE
            }
        }
        homeText!!.setOnClickListener {
            switchFragment(homeFragement!!)
            drawerLayout!!.close()
            info!!.visibility = View.VISIBLE
            menuIcon!!.setImageResource(R.drawable.ic_menu)
            headlineText!!.text = "Explore Events"
            check = true
        }
        eventText!!.setOnClickListener {
            switchFragment(eventsFragement!!)
            drawerLayout!!.close()
            info!!.visibility = View.GONE
            headlineText!!.text = "My Events"
            menuIcon!!.setImageResource(R.drawable.ic_back_arrow)
            check = false
        }
        notiText!!.setOnClickListener {
            switchFragment(notiFragement!!)
            drawerLayout!!.close()
            info!!.visibility = View.GONE
            headlineText!!.text = "All Notifications"
            menuIcon!!.setImageResource(R.drawable.ic_back_arrow)
            check = false
        }
        settingsText!!.setOnClickListener {
            switchFragment(settingsFragement!!)
            drawerLayout!!.close()
            info!!.visibility = View.GONE
            headlineText!!.text = "Settings"
            menuIcon!!.setImageResource(R.drawable.ic_back_arrow)
            check = false
        }

        logout!!.setOnClickListener {
            logoutModel!!.onLogin(pref!!.getToken()!!)
        }


    }

    private fun switchFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction!!.replace(R.id.nav_host_fragment, fragment)
        fragmentTransaction!!.commit()

    }

    override fun onLogout(response: String) {
        val json = JSONObject(response)
        val success = json.getBoolean("success")
        val message = json.getString("message")

        if (success) {
            pref!!.setToken("access_token")
            pref!!.setEmail("")
            pref!!.setType("")
            pref!!.setFullName("")
            pref!!.setID("")
            pref!!.setUserName("")
            pref!!.setGender("")
            pref!!.setPhotoURL("")
            pref!!.setLogin(false)

            startActivity(Intent(this, LoginActivity::class.java))
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
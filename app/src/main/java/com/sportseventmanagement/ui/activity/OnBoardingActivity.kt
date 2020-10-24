package com.sportseventmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.fragment.FollowRouteFragment
import com.sportseventmanagement.ui.fragment.JointsEventFragment
import com.sportseventmanagement.ui.fragment.ShareExpFragment
import com.sportseventmanagement.ui.fragment.TimeRaceFragment

class OnBoardingActivity : AppCompatActivity(), View.OnClickListener {

    private var fragment_layout: FrameLayout? = null
    private var fragmentManager: FragmentManager? = null
    private var fragmentTransaction: FragmentTransaction? = null
    private var join_events: JointsEventFragment? = null
    private var follow_route: FollowRouteFragment? = null
    private var time_race: TimeRaceFragment? = null
    private var share_exp: ShareExpFragment? = null

    private var right_arrow_layout: RelativeLayout? = null
    private var skip_text: TextView? = null
    private var icon_group: ImageView? = null
    private var index:Int=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_onboarding)
        init()
    }

    private fun init() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        join_events = JointsEventFragment()
        follow_route = FollowRouteFragment()
        time_race = TimeRaceFragment()
        share_exp = ShareExpFragment()

        fragment_layout = findViewById(R.id.fragment_layout)
        right_arrow_layout = findViewById(R.id.right_arrow_layout)
        skip_text=findViewById(R.id.skip_text)
        icon_group=findViewById(R.id.icon_group)

        switchFragment(join_events!!)

        right_arrow_layout!!.setOnClickListener(this)
        skip_text!!.setOnClickListener(this)


    }

    private fun switchFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction!!.replace(R.id.fragment_layout, fragment)
        fragmentTransaction!!.commit()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.right_arrow_layout->{
                if(index==1){
                    icon_group!!.setBackgroundResource(R.drawable.ic_group_9)
                    switchFragment(follow_route!!)
                    index++
                }else if(index ==2){
                    icon_group!!.setBackgroundResource(R.drawable.ic_group_10)
                    switchFragment(time_race!!)
                    index++
                }else if (index==3){
                    icon_group!!.setBackgroundResource(R.drawable.ic_group_11)
                    switchFragment(share_exp!!)
                    skip_text!!.visibility=View.GONE
                    index++
                }else if(index>3){
                   startActivity(Intent(this@OnBoardingActivity,LoginActivity::class.java))
                }
            }
            R.id.skip_text->{
                startActivity(Intent(this@OnBoardingActivity,LoginActivity::class.java))
            }
        }
    }
}
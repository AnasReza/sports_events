package com.sportseventmanagement.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.sportseventmanagement.R
import com.sportseventmanagement.adapter.EventsTabAdapter


class MyEventsFragment : Fragment() {

    private var tabs_main: TabLayout?=null
    private var viewPager_main: ViewPager?=null
    private var fragmentAdapter: EventsTabAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_my_events, container, false)
init(view)
        return view
    }

    private fun init(view: View) {

        fragmentAdapter=EventsTabAdapter(childFragmentManager)
        viewPager_main= view.findViewById(R.id.viewpager_main)
        tabs_main= view.findViewById(R.id.tabs_main)

        viewPager_main!!.adapter=fragmentAdapter
        tabs_main!!.setupWithViewPager(viewPager_main)
       // tabs_main!!.setSelectedTabIndicatorColor(Color.parseColor("#0C65AF"))
    }
}
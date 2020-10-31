package com.sportseventmanagement.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.sportseventmanagement.ui.fragment.ActiveEventsFragment
import com.sportseventmanagement.ui.fragment.PastEventsFragment

class EventsTabAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ActiveEventsFragment()

            else -> {
                return PastEventsFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "ACTIVE"
            else -> {
                return "PAST"
            }
        }
    }

}
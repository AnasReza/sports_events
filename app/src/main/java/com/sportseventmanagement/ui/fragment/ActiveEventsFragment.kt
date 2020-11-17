package com.sportseventmanagement.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.race.StartRaceActivity
import kotlinx.android.synthetic.main.fragment_all_notifications.*

class ActiveEventsFragment() : Fragment() {

    private var first_card:CardView?=null
    private var second_card:CardView?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view= inflater.inflate(R.layout.fragment_active_events,null)
init(view)
        return view
    }

    private fun init(view: View) {
        first_card=view.findViewById(R.id.first_card)
        second_card=view.findViewById(R.id.second_card)


        first_card!!.setOnClickListener { requireActivity().startActivity(Intent(activity,StartRaceActivity::class.java)) }
        second_card!!.setOnClickListener { requireActivity().startActivity(Intent(activity,StartRaceActivity::class.java)) }
    }
}
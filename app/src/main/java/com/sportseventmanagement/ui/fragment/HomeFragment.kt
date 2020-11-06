package com.sportseventmanagement.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.*


class HomeFragment : Fragment(), View.OnClickListener {
    private var first_card: CardView? = null
    private var all_event_maker: TextView? = null
    private var nearby_event: TextView? = null
    private var all_event: TextView? = null
    private var foxLayout:LinearLayout?=null

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
        first_card = root.findViewById(R.id.first_card)
        all_event_maker = root.findViewById(R.id.all_event_maker)
        all_event = root.findViewById(R.id.all_event)
        nearby_event = root.findViewById(R.id.nearby_event)
        foxLayout=root.findViewById(R.id.foxLayout)


        first_card!!.setOnClickListener(this)
        all_event_maker!!.setOnClickListener(this)
        all_event!!.setOnClickListener(this)
        nearby_event!!.setOnClickListener(this)
        foxLayout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.first_card -> {
                requireActivity().startActivity(Intent(activity, EventDetailActivity::class.java))
            }
            R.id.nearby_event -> {
                requireActivity().startActivity(Intent(activity, NearByActivity::class.java))
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
                requireActivity().startActivity(Intent(activity, AllEventsActivity::class.java))
            }
            R.id.foxLayout->{
requireActivity().startActivity(Intent(activity,EventMakerDetailsActivity::class.java))
            }
        }
    }
}
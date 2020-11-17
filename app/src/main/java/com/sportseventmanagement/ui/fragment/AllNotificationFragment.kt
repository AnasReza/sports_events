package com.sportseventmanagement.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.events.NearByActivity
import com.sportseventmanagement.ui.activity.race.LeaderBoardActivity


class AllNotificationFragment : Fragment() {
private var first:LinearLayout?=null
private var second:LinearLayout?=null
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
        first=view.findViewById(R.id.first)
        second=view.findViewById(R.id.second)

        first!!.setOnClickListener { requireActivity().startActivity(Intent(activity,NearByActivity::class.java)) }
        second!!.setOnClickListener { showFinishDialog() }
    }

    private fun showFinishDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_award)
        var window=dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        val close_layout: RelativeLayout = dialog.findViewById(R.id.delete_layout)
        val leaderboard: CardView = dialog.findViewById(R.id.leaderboard_card)

        close_layout.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(requireActivity(), LeaderBoardActivity::class.java))
        }
        leaderboard.setOnClickListener {
            startActivity(
                Intent(
                    requireActivity(),
                    LeaderBoardActivity::class.java
                )
            )
        }
        dialog.show()
    }
}
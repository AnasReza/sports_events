package com.sportseventmanagement.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.settings.MyAwardsActivity
import com.sportseventmanagement.ui.activity.settings.PersonalInfoActivity
import com.sportseventmanagement.ui.activity.settings.ReportProblemActivity

class SettingsFragment : Fragment(), View.OnClickListener {
private var info_text:TextView?=null
private var report:TextView?=null
private var myAwards:TextView?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)
init(view)
        return view
    }

    private fun init(view: View) {
        info_text=view.findViewById(R.id.info_text)
        report=view.findViewById(R.id.report)
        myAwards=view.findViewById(R.id.my_awards)

        info_text!!.setOnClickListener(this)
        report!!.setOnClickListener(this)
        myAwards!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.info_text->{
                startActivity(Intent(activity, PersonalInfoActivity::class.java))
            }
            R.id.report->{
                startActivity(Intent(activity,ReportProblemActivity::class.java))
            }
            R.id.my_awards->{
                startActivity(Intent(activity,MyAwardsActivity::class.java))
            }
        }
    }
}
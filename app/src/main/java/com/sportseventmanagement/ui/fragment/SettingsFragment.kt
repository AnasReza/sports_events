package com.sportseventmanagement.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.PersonalInfoActivity

class SettingsFragment : Fragment(), View.OnClickListener {
private var info_text:TextView?=null
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

        info_text!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.info_text->{
                startActivity(Intent(activity,PersonalInfoActivity::class.java))
            }
        }
    }
}
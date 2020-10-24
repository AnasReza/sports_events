package com.sportseventmanagement.ui.fragment

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R

class JointsEventFragment: Fragment() {
    private var center_image:ImageView?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_join_event,container,false)
init(view)
        return view
    }

    private fun init(view: View?) {
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        center_image=view!!.findViewById(R.id.center_image)
        center_image!!.layoutParams.width= (width*0.5).toInt()
        center_image!!.layoutParams.height= (height*0.3).toInt()
    }
}
package com.sportseventmanagement.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportseventmanagement.R
import com.sportseventmanagement.adapter.TextAdapter


class TextActivity : AppCompatActivity() {
    private var headline: TextView? = null
    private var back_button: ImageView? = null
    private var list:RecyclerView?=null
    private var adapter: TextAdapter?=null
    private var mLayoutManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
        init()
    }

    private fun init() {
        headline = findViewById(R.id.headLine)
        back_button = findViewById(R.id.back_button)
        list=findViewById(R.id.textList)
        mLayoutManager = LinearLayoutManager(this)


        var str = intent.getStringExtra("headline")
        headline!!.text = str
        if (str == "TERMS OF USAGE") {
            val textList = resources.getStringArray(R.array.termsArray)
            adapter= TextAdapter(textList)
            list!!.layoutManager = mLayoutManager
            list!!.adapter=adapter

        }else if(str=="PRIVACY POLICY"){
            val textList = resources.getStringArray(R.array.policyArray)
            adapter= TextAdapter(textList)
            list!!.layoutManager = mLayoutManager
            list!!.adapter=adapter
        }

        back_button!!.setOnClickListener { onBackPressed() }
    }
}
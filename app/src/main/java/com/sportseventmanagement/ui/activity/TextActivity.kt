package com.sportseventmanagement.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_text)
        init()
    }

    private fun init() {
        headline = findViewById(R.id.headLine)
        back_button = findViewById(R.id.back_button)
        list=findViewById(R.id.textList)
        mLayoutManager = LinearLayoutManager(this)


        var str = intent.getStringExtra("headline")
        Log.e("Anas", "$str    string")
        headline!!.text = str!!
        if (str == "TERMS OF USAGE") {Log.i("Anas","inside terms of usage")
            val textList = resources.getStringArray(R.array.termsArray)
            Log.w("Anas","${textList[1]}")
            adapter= TextAdapter(textList)
            list!!.layoutManager = mLayoutManager
            list!!.adapter=adapter

        }else if(str=="PRIVACY POLICY"){
            Log.i("Anas","inside privacy policy")
            val textList = resources.getStringArray(R.array.policyArray)
            Log.w("Anas","${textList[1]}")
            adapter= TextAdapter(textList)
            list!!.layoutManager = mLayoutManager
            list!!.adapter=adapter
        }

        back_button!!.setOnClickListener { onBackPressed() }
    }
}
package com.sportseventmanagement.ui.activity.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.model.ReportProblemModel
import com.sportseventmanagement.utility.Preferences
import org.json.JSONObject


class ReportProblemActivity : AppCompatActivity(), View.OnClickListener, ReportProblemModel.Report {

    private var back_button: ImageView? = null
    private var descriptionText: EditText? = null
    private var limit: TextView? = null
    private var submit: RelativeLayout? = null
    private var model:ReportProblemModel?=null
private var pref:Preferences?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_report_problem)
        init()
    }

    private fun init() {
        model= ReportProblemModel(this,this)
        pref= Preferences(this)

        back_button = findViewById(R.id.back_button)
        descriptionText = findViewById(R.id.descriptionText)
        submit = findViewById(R.id.submit)
        limit = findViewById(R.id.limit)

        descriptionText!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                limit!!.text = "${s.length}/120 Chars"
            }
        })
        back_button!!.setOnClickListener(this)
        submit!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
                finish()
            }
            R.id.submit->{
                var description=descriptionText!!.text.toString()
                var token=pref!!.getToken()

                var json = JSONObject()
                json.put("description", description)
                Log.d("Anas", "${json.toString()}")
                model!!.reportCall(json,token!!)
            }
        }
    }

    override fun onReport(response: String) {
        val data=JSONObject(response)
        val success=data.getBoolean("success")
        val message=data.getString("message")

        if(success){
            Toast.makeText(this,"Your Problem has been Reported Successfully.",Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }

    }
}
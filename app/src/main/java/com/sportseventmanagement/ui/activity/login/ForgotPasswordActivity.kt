package com.sportseventmanagement.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.model.RecoveryEmailModel
import com.sportseventmanagement.ui.activity.OTPActivity
import org.json.JSONObject

class ForgotPasswordActivity :AppCompatActivity(), View.OnClickListener, RecoveryEmailModel.Result {
    private var send_email:RelativeLayout?=null
    private var emailText:EditText?=null
    private var model:RecoveryEmailModel?=null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_forgot)
        
        init()
    }

    private fun init() {
        model= RecoveryEmailModel(this,this)

        send_email=findViewById(R.id.send_email)
        emailText=findViewById(R.id.emailText)
        
        send_email!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.send_email->{
                var email=emailText!!.text.toString()
                if(email.isNotEmpty()){
                    var json=JSONObject()
                    json.put("email",email)

                    model!!.onRecoveryEmail(json)
                }
//
            }
        }
    }

    override fun onResult(response: String) {
        var json=JSONObject(response)
        var success=json.getBoolean("success")
        var message=json.getString("message")

        if(success){
            startActivity(Intent(this, OTPActivity::class.java))
        }else {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }

    }
}
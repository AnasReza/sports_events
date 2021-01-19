package com.sportseventmanagement.ui.activity

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
import com.sportseventmanagement.ui.activity.login.NewPasswordActivity
import org.json.JSONObject

class OTPActivity : AppCompatActivity(), View.OnClickListener, RecoveryEmailModel.Result {

    private var verify_layout: RelativeLayout? = null
    private var edit1: EditText? = null
    private var edit2: EditText? = null
    private var edit3: EditText? = null
    private var edit4: EditText? = null
    private var edit5: EditText? = null
    private var edit6: EditText? = null

    private var model: RecoveryEmailModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_otp)

        init()
    }

    private fun init() {
        model = RecoveryEmailModel(this, this)

        verify_layout = findViewById(R.id.verify_layout)
        edit1 = findViewById(R.id.edit1)
        edit2 = findViewById(R.id.edit2)
        edit3 = findViewById(R.id.edit3)
        edit4 = findViewById(R.id.edit4)
        edit5 = findViewById(R.id.edit5)
        edit6 = findViewById(R.id.edit6)

        verify_layout!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.verify_layout -> {
                var code =
                    "${edit1!!.text.toString()}${edit2!!.text.toString()}${edit3!!.text.toString()}${edit4!!.text.toString()}${edit5!!.text.toString()}${edit6!!.text.toString()}"
                val json = JSONObject()
                json.put("code", code)

                model!!.onRecoveryCode(json)
                // startActivity(Intent(this, NewPasswordActivity::class.java))
            }
        }
    }

    override fun onResult(response: String) {
        val json = JSONObject(response)
        val data = json.getJSONObject("data")
        val id = data.getString("id")
        val success = json.getBoolean("success")
        val message = json.getString("message")

        if (success) {
            startActivity(Intent(this, NewPasswordActivity::class.java).putExtra("id",id))
        }else{
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
    }
}
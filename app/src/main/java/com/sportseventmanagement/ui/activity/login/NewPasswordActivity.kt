package com.sportseventmanagement.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.model.RecoveryEmailModel
import org.json.JSONObject

class NewPasswordActivity : AppCompatActivity(), View.OnClickListener, RecoveryEmailModel.Result {
    private var reset_password: RelativeLayout? = null
    private var new_pass: EditText? = null
    private var eye_image: ImageView? = null

    private var repeat_pass: EditText? = null
    private var eye_image2: ImageView? = null
    private var check: Boolean = true
    private var id: String = ""
    private var model: RecoveryEmailModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_new_password)

        init()
    }

    private fun init() {
        id = intent.getStringExtra("id")!!
        model = RecoveryEmailModel(this, this)

        reset_password = findViewById(R.id.reset_password)
        new_pass = findViewById(R.id.new_password)
        eye_image = findViewById(R.id.eye_image)
        repeat_pass = findViewById(R.id.repeat_pass)
        eye_image2 = findViewById(R.id.eye_image2)

        eye_image!!.setImageResource(R.drawable.ic_remove_red_eye)
        eye_image2!!.setImageResource(R.drawable.ic_remove_red_eye)
        reset_password!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.reset_password -> {
                val newPass = new_pass!!.text.toString()
                val reapetPass = repeat_pass!!.text.toString()
                if (newPass.isNotEmpty() && reapetPass.isNotEmpty()) {
                    if (newPass == reapetPass) {
                        val json = JSONObject()
                        json.put("id", id)
                        json.put("newPassword", newPass)

                        model!!.onResetPass(json)
                    }else {
                        Toast.makeText(
                            this,
                            "Your New Password and Repeat is not same",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Your New Password or Repeat Password is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            R.id.eye_image -> {
                if (!check) {
                    new_pass!!.transformationMethod = PasswordTransformationMethod.getInstance()
                    eye_image!!.setImageResource(R.drawable.ic_remove_red_eye)
                    check = true
                } else {
                    new_pass!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    eye_image!!.setImageResource(R.drawable.ic_invisible)
                    check = false
                }
            }
        }
    }

    override fun onResult(response: String) {
        val json=JSONObject(response)
        val success=json.getBoolean("success")
        val message=json.getString("message")

        if(success){
            startActivity(Intent(this, ResetConfirmationActivity::class.java))
        }else{
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
    }
}
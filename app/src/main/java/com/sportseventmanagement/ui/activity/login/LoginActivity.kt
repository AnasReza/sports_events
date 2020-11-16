package com.sportseventmanagement.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.HomeActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var create_one: TextView? = null
    private var forgot_text: TextView? = null
    private var login: RelativeLayout? =null
    private var pass_edit_text: EditText? =null
    private var eye_image: ImageView? =null

    private var check:Boolean=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        create_one = findViewById(R.id.create_one)
        forgot_text = findViewById(R.id.forgot_text)
        login = findViewById(R.id.login)
        pass_edit_text = findViewById(R.id.pass_edit_text)
        eye_image = findViewById(R.id.eye_image)
        eye_image!!.setImageResource(R.drawable.ic_remove_red_eye)


        create_one!!.setOnClickListener(this)
        forgot_text!!.setOnClickListener(this)
        login!!.setOnClickListener(this)
        eye_image!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.create_one -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            R.id.forgot_text -> {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }

            R.id.login -> {
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            }

            R.id.eye_image->{
            if(!check) {
                pass_edit_text!!.transformationMethod = PasswordTransformationMethod.getInstance()
                eye_image!!.setImageResource(R.drawable.ic_remove_red_eye)
                check=true
            }else{
                pass_edit_text!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
                eye_image!!.setImageResource(R.drawable.ic_invisible)
                check=false
            }
        }
        }
    }
}
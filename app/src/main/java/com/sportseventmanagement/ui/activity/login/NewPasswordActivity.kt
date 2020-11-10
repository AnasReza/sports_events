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
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class NewPasswordActivity : AppCompatActivity(), View.OnClickListener {
    private var reset_password:LinearLayout?=null
    private var pass_edit_text: EditText? =null
    private var eye_image: ImageView? =null

    private var pass_edit_text2: EditText? =null
    private var eye_image2: ImageView? =null
    private var check:Boolean=true


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
        reset_password=findViewById(R.id.reset_password)
        pass_edit_text = findViewById(R.id.pass_edit_text)
        eye_image = findViewById(R.id.eye_image)
        pass_edit_text2 = findViewById(R.id.pass_edit_text2)
        eye_image2 = findViewById(R.id.eye_image2)

        eye_image!!.setImageResource(R.drawable.ic_remove_red_eye)
        eye_image2!!.setImageResource(R.drawable.ic_remove_red_eye)
        reset_password!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.reset_password->{
                startActivity(Intent(this, ResetConfirmationActivity::class.java))
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
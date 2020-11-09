package com.sportseventmanagement.ui.activity.settings

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {

    private var edit_password: EditText? =null
    private var imagePass: ImageView?=null
    private var check: Boolean?= true
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_change_password)
init()
    }



    private fun init(){


        edit_password=findViewById<EditText>(R.id.edit_password)
        imagePass=findViewById<ImageView>(R.id.show_pass_btn)
        imagePass!!.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        if (v!!.id==R.id.show_pass_btn){
            if(check!!) {
                edit_password!!.inputType = InputType.TYPE_CLASS_TEXT
                imagePass!!.setImageResource(R.drawable.ic_eye_hidden)
                check=false
            }
            else{
                edit_password!!.transformationMethod = PasswordTransformationMethod.getInstance()

                imagePass!!.setImageResource(R.drawable.ic_remove_red_eye)
                check=true
            }
            }
        }
    }



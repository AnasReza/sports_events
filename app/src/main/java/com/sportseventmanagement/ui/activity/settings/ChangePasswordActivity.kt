package com.sportseventmanagement.ui.activity.settings

import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener {

    private var editpassword: EditText? =null
    private var imagePass: ImageView?=null
    private var editpassword1: EditText? =null
    private var imagePass1: ImageView?=null
    private var check:Boolean=true
    private var check1:Boolean=true
    private var back_button:ImageView?=null
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


        editpassword=findViewById<EditText>(R.id.edit_password)
        imagePass=findViewById<ImageView>(R.id.imagePass)
        back_button=findViewById(R.id.back_button)
        editpassword1=findViewById<EditText>(R.id.edit_password1)
        imagePass1=findViewById<ImageView>(R.id.imagePass1)

        imagePass!!.setImageResource(R.drawable.ic_remove_red_eye)
        imagePass1!!.setImageResource(R.drawable.ic_remove_red_eye)

        imagePass!!.setOnClickListener(this)
        imagePass1!!.setOnClickListener(this)
        back_button!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v!!.id){

            R.id.back_button -> {
                finish()
            }

            R.id.imagePass->{
                if(!check) {
                    editpassword!!.transformationMethod = PasswordTransformationMethod.getInstance()
                    imagePass!!.setImageResource(R.drawable.ic_remove_red_eye)
                    check=true
                }
                else{
                    editpassword!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    imagePass!!.setImageResource(R.drawable.ic_invisible)
                    check=false


                }
            }
            R.id.imagePass1->{
                if(!check1) {
                    editpassword1!!.transformationMethod = PasswordTransformationMethod.getInstance()
                    imagePass1!!.setImageResource(R.drawable.ic_remove_red_eye)
                    check1=true
                }

                else{
                    editpassword1!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    imagePass1!!.setImageResource(R.drawable.ic_invisible)
                    check1=false
                }
            }
        }
    }
}



package com.sportseventmanagement.ui.activity.settings

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PersonalInfoActivity :AppCompatActivity(), View.OnClickListener{
    private var changeEmail: TextView? =null
    private var changePassword: TextView?=null
    private var changeUsername: TextView?=null
    private var nameText: TextView?=null
    private var emailText: TextView?=null
    private var back_button: ImageView?=null
    private var profile_image: CircleImageView?=null
    private var pref:Preferences?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_info)
        init()
    }
    private fun init(){
        pref= Preferences(this)

        changeEmail =findViewById(R.id.changeEmail)
        changePassword=findViewById(R.id.changePassword)
        changeUsername=findViewById(R.id.changeUsername)
        nameText=findViewById(R.id.nameText)
        emailText=findViewById(R.id.emailText)
        back_button=findViewById(R.id.back_button)
        profile_image=findViewById(R.id.profile_image)

        nameText!!.text=pref!!.getFullName()
        emailText!!.text=pref!!.getEmail()
        if(pref!!.getPhotoURL()!=""){
            profile_image!!.setImageDrawable(null)
            Picasso.with(this).load(pref!!.getPhotoURL()).into(profile_image)
        }

        changeEmail!!.setOnClickListener(this)
        changePassword!!.setOnClickListener(this)
        changeUsername!!.setOnClickListener(this)
        back_button!!.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        when (v!!.id) {
            R.id.changeEmail -> {
                startActivity(Intent(this@PersonalInfoActivity, ChangeEmailActivity::class.java))
            }
            R.id.changePassword -> {
                startActivity(Intent(this@PersonalInfoActivity, ChangePasswordActivity::class.java))
            }
            R.id.changeUsername -> {
                startActivity(Intent(this@PersonalInfoActivity, ChangeUserNameActivity::class.java))
            }
            R.id.back_button -> {
                finish()
            }

        }

    }
}
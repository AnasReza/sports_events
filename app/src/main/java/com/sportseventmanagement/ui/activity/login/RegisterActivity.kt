package com.sportseventmanagement.ui.activity.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.TextActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private var create_account:LinearLayout?=null
    private var terms_policy:TextView?=null
    private var back_button: ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        create_account=findViewById(R.id.create_account)
        terms_policy=findViewById(R.id.terms_policy)
        back_button=findViewById(R.id.back_button)

        back_button!!.setOnClickListener(this)


        val termsSpan=SpannableString("By registering you consent to our Terms & Condition And Privacy Policy")
        var termClickable:ClickableSpan=object: ClickableSpan() {
            override fun onClick(p0: View) {
                startActivity(Intent(this@RegisterActivity, TextActivity::class.java).putExtra("headline", "TERMS OF USAGE"))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText=true
            }
        }
        var policyClickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(
                    Intent(
                        this@RegisterActivity,
                        TextActivity::class.java
                    ).putExtra("headline", "PRIVACY POLICY")
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)

                ds.isUnderlineText = true
            }
        }
        termsSpan.setSpan(termClickable,34,50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        termsSpan[56, 69]=policyClickable
        create_account!!.setOnClickListener(this)
        terms_policy!!.text=termsSpan
        terms_policy!!.movementMethod=LinkMovementMethod.getInstance()
        terms_policy!!.highlightColor= Color.TRANSPARENT
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.create_account->{
                startActivity(Intent(this, VerifyAccountActivity::class.java))
            }
            R.id.back_button -> {
                finish()
            }
        }
    }
}
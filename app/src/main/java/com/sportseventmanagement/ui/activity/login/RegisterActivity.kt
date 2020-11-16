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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.TextActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private var create_account: RelativeLayout? = null
    private var terms_policy: TextView? = null
    private var back_button: ImageView? = null
//    private var edit1:EditText?=null
//    private var edit2:EditText?=null
//    private var edit3:EditText?=null
//


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
        create_account = findViewById(R.id.create_account)
        terms_policy = findViewById(R.id.terms_policy)
        back_button = findViewById(R.id.back_button)
//        edit1 = findViewById(R.id.edit1)
//        edit2 = findViewById(R.id.edit2)
//        edit3 = findViewById(R.id.edit3)


        back_button!!.setOnClickListener(this)



            val termsSpan =
                SpannableString("By registering you consent to our Terms & Condition And Privacy Policy")
            var termClickable: ClickableSpan = object : ClickableSpan() {
                override fun onClick(p0: View) {
                    startActivity(
                        Intent(
                            this@RegisterActivity,
                            TextActivity::class.java
                        ).putExtra("headline", "TERMS OF USAGE")
                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isFakeBoldText = false
                    ds.isUnderlineText = true

                    ds.color = resources.getColor(R.color.termsColor)
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
                    ds.isFakeBoldText = false
                    ds.isUnderlineText = true

                    ds.color = resources.getColor(R.color.termsColor)
                }
            }
            termsSpan.setSpan(termClickable, 34, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            termsSpan[56, 70] = policyClickable
            create_account!!.setOnClickListener(this)
            terms_policy!!.text = termsSpan
            terms_policy!!.movementMethod = LinkMovementMethod.getInstance()
            terms_policy!!.highlightColor = Color.TRANSPARENT
        }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.create_account -> {
                startActivity(Intent(this, VerifyAccountActivity::class.java))
            }
            R.id.back_button -> {
                finish()
            }
        }
    }
}
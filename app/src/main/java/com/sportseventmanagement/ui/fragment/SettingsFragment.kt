package com.sportseventmanagement.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.set
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R
import com.sportseventmanagement.ui.activity.TextActivity
import com.sportseventmanagement.ui.activity.settings.MyAwardsActivity
import com.sportseventmanagement.ui.activity.settings.PersonalInfoActivity
import com.sportseventmanagement.ui.activity.settings.ReportProblemActivity

class SettingsFragment : Fragment(), View.OnClickListener {
    private var info_text: TextView? = null
    private var report: TextView? = null
    private var terms_policy: TextView? = null
    private var myAwards: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        info_text = view.findViewById(R.id.info_text)
        report = view.findViewById(R.id.report)
        myAwards = view.findViewById(R.id.my_awards)
        terms_policy = view.findViewById(R.id.terms_policy)

        info_text!!.setOnClickListener(this)
        report!!.setOnClickListener(this)
        myAwards!!.setOnClickListener(this)

        val termsSpan = SpannableString("Terms & Condition · Privacy Policy")
        var termClickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                activity!!.startActivity(
                    Intent(
                        activity,
                        TextActivity::class.java
                    ).putExtra("headline", "TERMS OF USAGE")
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isFakeBoldText = false
                ds.isUnderlineText = false

                ds.color = activity!!.resources.getColor(R.color.termsColor)
            }
        }
        var policyClickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                activity!!.startActivity(
                    Intent(
                        activity,
                        TextActivity::class.java
                    ).putExtra("headline", "PRIVACY POLICY")
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isFakeBoldText = false
                ds.isUnderlineText = false

                ds.color = activity!!.resources.getColor(R.color.termsColor)
            }
        }
        termsSpan.setSpan(termClickable, 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        termsSpan[20, 34] = policyClickable
        terms_policy!!.text = termsSpan
        terms_policy!!.movementMethod = LinkMovementMethod.getInstance()
        terms_policy!!.highlightColor = Color.TRANSPARENT

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.info_text -> {
                startActivity(Intent(activity, PersonalInfoActivity::class.java))
            }
            R.id.report -> {
                startActivity(Intent(activity, ReportProblemActivity::class.java))
            }
            R.id.my_awards -> {
                startActivity(Intent(activity, MyAwardsActivity::class.java))
            }
        }
    }
}
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
import android.widget.Toast
import androidx.core.text.set
import androidx.fragment.app.Fragment
import com.sportseventmanagement.R
import com.sportseventmanagement.model.LogoutModel
import com.sportseventmanagement.ui.activity.TextActivity
import com.sportseventmanagement.ui.activity.login.LoginActivity
import com.sportseventmanagement.ui.activity.settings.MyAwardsActivity
import com.sportseventmanagement.ui.activity.settings.PersonalInfoActivity
import com.sportseventmanagement.ui.activity.settings.ReportProblemActivity
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject

class SettingsFragment : Fragment(), View.OnClickListener, LogoutModel.Logout {
    private var info_text: TextView? = null
    private var report: TextView? = null
    private var nameText: TextView? = null
    private var emailText: TextView? = null
    private var profileImage: CircleImageView? = null
    private var terms_policy: TextView? = null
    private var myAwards: TextView? = null
    private var logout: TextView? = null

    private var pref:Preferences?=null
    private var logoutModel: LogoutModel? = null
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
        pref= Preferences(requireActivity())
        logoutModel= LogoutModel(this,requireActivity())

        info_text = view.findViewById(R.id.info_text)
        report = view.findViewById(R.id.report)
        myAwards = view.findViewById(R.id.my_awards)
        terms_policy = view.findViewById(R.id.terms_policy)
        nameText=view.findViewById(R.id.nameText)
        emailText=view.findViewById(R.id.emailText)
        profileImage=view.findViewById(R.id.profile_image)
        logout=view.findViewById(R.id.logout)

        nameText!!.text=pref!!.getFullName()
        emailText!!.text=pref!!.getEmail()
        if(pref!!.getPhotoURL()!=""){
            profileImage!!.setImageDrawable(null)
            Picasso.with(requireActivity()).load(pref!!.getPhotoURL()).into(profileImage)
        }

        info_text!!.setOnClickListener(this)
        report!!.setOnClickListener(this)
        myAwards!!.setOnClickListener(this)
        logout!!.setOnClickListener(this)

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
            R.id.logout->{
                logoutModel!!.onLogin(pref!!.getToken()!!)
            }
        }
    }

    override fun onLogout(response: String) {
        val json = JSONObject(response)
        val success = json.getBoolean("success")
        val message = json.getString("message")

        if (success) {
            pref!!.setToken("access_token")
            pref!!.setEmail("")
            pref!!.setType("")
            pref!!.setFullName("")
            pref!!.setID("")
            pref!!.setUserName("")
            pref!!.setGender("")
            pref!!.setPhotoURL("")
            pref!!.setLogin(false)

            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}
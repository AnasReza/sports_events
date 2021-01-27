package com.sportseventmanagement.ui.activity.settings

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
import com.sportseventmanagement.model.UpdatePasswordModel
import com.sportseventmanagement.utility.Preferences
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener,
    UpdatePasswordModel.UpdatePassword {

    private var current_password: EditText? = null
    private var imagePass: ImageView? = null
    private var new_password: EditText? = null
    private var repeat_password: EditText? = null
    private var imagePass1: ImageView? = null
    private var saveChanges: RelativeLayout? = null
    private var back_button: ImageView? = null

    private var check: Boolean = true
    private var check1: Boolean = true
    private var pref: Preferences? = null
    private var model: UpdatePasswordModel? = null

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


    private fun init() {
        pref = Preferences(this)
        model = UpdatePasswordModel(this, this)

        current_password = findViewById(R.id.current_password)
        imagePass = findViewById<ImageView>(R.id.imagePass)
        back_button = findViewById(R.id.back_button)
        new_password = findViewById(R.id.new_password)
        repeat_password = findViewById(R.id.repeat_password)
        imagePass1 = findViewById(R.id.imagePass1)
        saveChanges = findViewById(R.id.saveChanges)

        imagePass!!.setImageResource(R.drawable.ic_remove_red_eye)
        imagePass1!!.setImageResource(R.drawable.ic_remove_red_eye)

        imagePass!!.setOnClickListener(this)
        imagePass1!!.setOnClickListener(this)
        back_button!!.setOnClickListener(this)
        saveChanges!!.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.back_button -> {
                finish()
            }

            R.id.imagePass -> {
                if (!check) {
                    current_password!!.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    imagePass!!.setImageResource(R.drawable.ic_remove_red_eye)
                    check = true
                } else {
                    current_password!!.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    imagePass!!.setImageResource(R.drawable.ic_invisible)
                    check = false


                }
            }
            R.id.imagePass1 -> {
                if (!check1) {
                    new_password!!.transformationMethod = PasswordTransformationMethod.getInstance()
                    imagePass1!!.setImageResource(R.drawable.ic_remove_red_eye)
                    check1 = true
                } else {
                    new_password!!.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    imagePass1!!.setImageResource(R.drawable.ic_invisible)
                    check1 = false
                }
            }
            R.id.saveChanges -> {
                val current = current_password!!.text.toString()
                val newPass = new_password!!.text.toString()
                val repeat = repeat_password!!.text.toString()
                val token = pref!!.getToken()

                if (current.isNotEmpty() && newPass.isNotEmpty() && repeat.isNotEmpty()) {
                    if (newPass == repeat) {
                        val json = JSONObject()
                        json.put("currentPassword", current)
                        json.put("newPassword", newPass)

                        model!!.updateUsername(json, token!!)
                    } else {
                        Toast.makeText(
                            this,
                            "Your New Password and Repeat is not equal.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    when {
                        current.isEmpty() -> {
                            Toast.makeText(
                                this,
                                "Your Current Password is Empty.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        newPass.isEmpty() -> {
                            Toast.makeText(this, "Your New Password is Empty.", Toast.LENGTH_SHORT)
                                .show()
                        }
                        repeat.isEmpty() -> {
                            Toast.makeText(
                                this,
                                "Your repeat Password is Empty.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }

            }
        }
    }

    override fun onUpdate(response: String) {
        Toast.makeText(
            this@ChangePasswordActivity,
            "Your password has been updated.",
            Toast.LENGTH_SHORT
        ).show()
    }
}



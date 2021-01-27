package com.sportseventmanagement.ui.activity.settings

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sportseventmanagement.R
import com.sportseventmanagement.model.UpdateUsernameModel
import com.sportseventmanagement.utility.Preferences
import org.json.JSONObject

class ChangeUserNameActivity : AppCompatActivity(), View.OnClickListener,
    UpdateUsernameModel.UpdateUsername {
    private var usernameText: EditText? = null
    private var saveChanges: RelativeLayout? = null
    private var back_button: ImageView? = null

    private var model: UpdateUsernameModel? = null
    private var pref: Preferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_change_username)
        init()

    }

    private fun init() {
        pref = Preferences(this)
        model = UpdateUsernameModel(this, this)

        back_button = findViewById(R.id.back_button)
        usernameText = findViewById(R.id.usernameText)
        saveChanges = findViewById(R.id.saveChanges)

        usernameText!!.setText(pref!!.getUserName()!!)

        back_button!!.setOnClickListener(this)
        saveChanges!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back_button -> {
                finish()
            }
            R.id.saveChanges -> {
                val username = usernameText!!.text.toString()
                val token = pref!!.getToken()
                if (username.isNotEmpty()) {
                    var json = JSONObject()
                    json.put("username", username)
                    model!!.updateUsername(json, token!!)
                }
            }
        }
    }

    override fun onUpdate(response: String) {
        val data = JSONObject(response)
        val success = data.getBoolean("success")
        val message = data.getString("message")
        if (success) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

}
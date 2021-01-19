package com.sportseventmanagement.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.sportseventmanagement.R
import com.sportseventmanagement.model.FacebookLoginModel
import com.sportseventmanagement.model.GoogleLoginModel
import com.sportseventmanagement.model.LoginModel
import com.sportseventmanagement.ui.activity.HomeActivity
import com.sportseventmanagement.utility.Preferences
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class LoginActivity : AppCompatActivity(), View.OnClickListener, LoginModel.Result,
    GoogleLoginModel.onGoogleResult, FacebookLoginModel.onFacebookResult {
    private var create_one: TextView? = null
    private var forgot_text: TextView? = null
    private var login: RelativeLayout? = null
    private var pass_edit_text: EditText? = null
    private var email_text: EditText? = null
    private var eye_image: ImageView? = null
    private var google_button: Button? = null
    private var facebook_button: Button? = null

    private var check: Boolean = true
    private var loginModel: LoginModel? = null
    private var googleLogin: GoogleLoginModel? = null
    private var facebookLogin: FacebookLoginModel? = null

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var callbackManager: CallbackManager? = null
    private var pref: Preferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        loginModel = LoginModel(this, this)
        googleLogin = GoogleLoginModel(this, this)
        facebookLogin = FacebookLoginModel(this, this)
        pref = Preferences(this)

        create_one = findViewById(R.id.create_one)
        forgot_text = findViewById(R.id.forgot_text)
        login = findViewById(R.id.login)
        pass_edit_text = findViewById(R.id.pass_edit_text)
        email_text = findViewById(R.id.email_text)
        eye_image = findViewById(R.id.eye_image)
        google_button = findViewById(R.id.google_button)
        facebook_button = findViewById(R.id.facebook_button)

        callbackManager = CallbackManager.Factory.create()

        eye_image!!.setImageResource(R.drawable.ic_remove_red_eye)


        create_one!!.setOnClickListener(this)
        forgot_text!!.setOnClickListener(this)
        login!!.setOnClickListener(this)
        eye_image!!.setOnClickListener(this)
        google_button!!.setOnClickListener(this)
        facebook_button!!.setOnClickListener(this)

    }

    private fun getUserProfile(currentAccessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(currentAccessToken,
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(json: JSONObject?, response: GraphResponse?) {
                    Log.d("Anas", json.toString());
                    try {
                        val first_name = json!!.getString("first_name")
                        val last_name = json!!.getString("last_name")
                        val email = json!!.getString("email")
                        val id = json!!.getString("id")
                        val image_url = "https://graph.facebook.com/$id/picture?type=normal"
                        Log.w("Anas", "$first_name , $last_name, $email")

                    } catch (e: JSONException) {
                        e.printStackTrace();
                    }
                }
            })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.create_one -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            R.id.forgot_text -> {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }

            R.id.login -> {
                var email = email_text!!.text.toString()
                var password1: String = pass_edit_text!!.text.toString()

                var json = JSONObject()
                json.put("email", email)
                json.put("password", password1)
                Log.d("Anas", "${json.toString()}")

                loginModel!!.onLogin(json)

            }

            R.id.eye_image -> {
                if (!check) {
                    pass_edit_text!!.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    eye_image!!.setImageResource(R.drawable.ic_remove_red_eye)
                    check = true
                } else {
                    pass_edit_text!!.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                    eye_image!!.setImageResource(R.drawable.ic_invisible)
                    check = false
                }
            }
            R.id.google_button -> {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("790288349026-17ea6u64d3dn3bntkt16k7gb9f25u46o.apps.googleusercontent.com")
                    .requestEmail()
                    .build()

                mGoogleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)

                val signInIntent = mGoogleSignInClient.signInIntent

                startActivityForResult(signInIntent, 9001)

            }
            R.id.facebook_button -> {
                Log.e("Anas", "facebook button pressed")
                val accessToken = AccessToken.getCurrentAccessToken()
                val isLoggedIn = accessToken != null && !accessToken.isExpired
                Log.d("Anas", "$isLoggedIn  isLogged in")
                LoginManager.getInstance().logInWithReadPermissions(
                    this@LoginActivity,
                    listOf("email", "public_profile")
                )
                if (isLoggedIn) {
                    Log.e("Anas", "${accessToken.toString()} access token")
                } else {
                    LoginManager.getInstance().registerCallback(callbackManager,
                        object : FacebookCallback<LoginResult> {
                            override fun onSuccess(result: LoginResult?) {
                                val access_token = result!!.accessToken.token
                                val fullname=Profile.getCurrentProfile().name
                                val id=Profile.getCurrentProfile().id
                                val photoUrl=Profile.getCurrentProfile().getProfilePictureUri(250,250)
                                pref!!.setToken(access_token)
                                pref!!.setEmail("email")
                                pref!!.setType("facebook")
                                     pref!!.setFullName(fullname)
                                pref!!.setID(id)
                                pref!!.setUserName(fullname)
                                pref!!.setGender("")
                                pref!!.setPhotoURL("")
                                pref!!.setLogin(true)
                                FirebaseApp.initializeApp(this@LoginActivity)
                                FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        task.exception!!.printStackTrace()
                                        return@addOnCompleteListener
                                    }
                                    val token = task.result?.token

                                    var json = JSONObject()
                                    json.put("access_token", access_token)
                                    json.put("pushToken", token)
                                    facebookLogin!!.onFacebookLogin(json)
                                }

                            }

                            override fun onCancel() {
                                Log.d("Anas", "onCancel")
                            }

                            override fun onError(error: FacebookException?) {
                                Log.e("Anas", "${error!!.message}")
                            }

                        })
                }


            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            val account = task!!.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Anas", "$googleId  GOOGLE ID")

            val googleFirstName = account?.givenName ?: ""
            Log.i("Anas", "$googleFirstName GOOGLE FIRST NAME")

            val googleLastName = account?.familyName ?: ""
            Log.i("Anas", "$googleLastName  GOOGLE LAST NAME")

            val googleEmail = account?.email ?: ""
            Log.i("Anas", "$googleEmail  GOOGLE EMAIL")

            val googleDisplayName = account?.displayName
            Log.i("Anas", "$googleDisplayName  GOOGLE PROFILE PIC")
            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Anas", "$googleProfilePicURL  GOOGLE PROFILE PIC")

            val googleIdToken = account?.idToken ?: ""
            Log.i("Anas", "$googleIdToken  GOOGLE TOKEN")
            pref!!.setToken(googleIdToken)
            pref!!.setEmail(googleEmail)
            pref!!.setFullName("$googleFirstName $googleLastName")
            pref!!.setID(googleId)
            pref!!.setUserName(googleDisplayName!!)
            pref!!.setGender("")
            pref!!.setPhotoURL(googleProfilePicURL)
            pref!!.setLogin(true)
            FirebaseApp.initializeApp(this)
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    task.exception!!.printStackTrace()
                    return@addOnCompleteListener
                }
                val token = task.result?.token

                var json = JSONObject()
                json.put("idToken", googleIdToken)
                json.put("pushToken", token)
                googleLogin!!.onGoogleLogin(json)
            }

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "Anas", e.statusCode.toString()
            )
        }
    }

    override fun onResult(response: String) {
        Log.d("Anas", "$response")
        var json = JSONObject(response)
        var success: Boolean = json.getBoolean("success")
        var message: String = json.getString("message")
        Log.e("Anas", "$success  bool")
        when (success) {
            true -> {
                var dataObject = json.getJSONObject("data")
                var token = dataObject.getString("token")
                var userObject = dataObject.getJSONObject("user")
                var id = userObject.getString("_id")
                var email = userObject.getString("email")
                var username = userObject.getString("username")
                var fullname = userObject.getString("fullname")
                var gender = userObject.getString("gender")
                var image=userObject.getString("image")


                pref!!.setToken(token)
                pref!!.setEmail(email)
                pref!!.setType("email")
                pref!!.setFullName(fullname)
                pref!!.setID(id)
                pref!!.setUserName(username)
                pref!!.setGender(gender)
                pref!!.setPhotoURL(image)
                pref!!.setLogin(true)

                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
            }
            false -> {
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onGoogleResult(response: String) {
        Log.e("Anas","$response from google")
        val json = JSONObject(response)
        val success = json.getBoolean("success")
        val message = json.getString("message")

        if (success) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFacebookResult(response: String) {
        val json = JSONObject(response)
        val success = json.getBoolean("success")
        val message = json.getString("message")

        if (success) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        }
    }


}
package com.sportseventmanagement.ui.activity.login

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.text.set
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.sportseventmanagement.R
import com.sportseventmanagement.model.GoogleLoginModel
import com.sportseventmanagement.model.RegisterModel
import com.sportseventmanagement.ui.activity.TextActivity
import com.sportseventmanagement.utility.Preferences
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject

class RegisterActivity : AppCompatActivity(), View.OnClickListener, RegisterModel.Result,
    GoogleLoginModel.onGoogleResult {
    private var create_account: RelativeLayout? = null
    private var terms_policy: TextView? = null
    private var back_button: ImageView? = null
    private var email_text: EditText? = null
    private var password_text: EditText? = null
    private var name_text: EditText? = null
    private var username_text: EditText? = null
    private var gender_spin: Spinner? = null
    private var profile_image: CircleImageView? = null
    private var google_button:Button?=null
    private var genderSelected = "Male"

    private var uploadedURL: String = ""
    private var imageUpload: Boolean = true
    private var locationIsEnabled: Boolean = false
    private var locationManager: LocationManager? = null
    private var check: Boolean = true
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var model: RegisterModel? = null
    private var googleLogin: GoogleLoginModel? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private var pref:Preferences?=null


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
        model = RegisterModel(this, this)
        googleLogin = GoogleLoginModel(this, this)
        pref= Preferences(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        create_account = findViewById(R.id.create_account)
        terms_policy = findViewById(R.id.terms_policy)
        back_button = findViewById(R.id.back_button)
        email_text = findViewById(R.id.email_text)
        password_text = findViewById(R.id.password_text)
        name_text = findViewById(R.id.name_text)
        username_text = findViewById(R.id.username_text)
        gender_spin = findViewById(R.id.gender_spin)
        profile_image = findViewById(R.id.profile_image)
        google_button=findViewById(R.id.google_button)

        ArrayAdapter.createFromResource(
            this,
            R.array.gender_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            gender_spin!!.adapter = adapter
        }

        back_button!!.setOnClickListener(this)
        profile_image!!.setOnClickListener(this)
        create_account!!.setOnClickListener(this)
        google_button!!.setOnClickListener(this)

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
        terms_policy!!.text = termsSpan
        terms_policy!!.movementMethod = LinkMovementMethod.getInstance()
        terms_policy!!.highlightColor = Color.TRANSPARENT

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                Log.e("Anas", "inside the location")
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0F,
                    locationListener
                )
            }
        } else {

            var list = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(list, 100)

        }

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.create_account -> {
                Log.e("Anas", "Create Account is pressed")
                var email = email_text!!.text.toString()
                var username = username_text!!.text.toString()
                var name = name_text!!.text.toString()
                var password = password_text!!.toString()
                genderSelected = gender_spin!!.selectedItem.toString()
                if (imageUpload) {
                    Log.d("Anas", "$imageUpload imageUploaded")
                    if (locationIsEnabled) {
                        Log.d("Anas", "$locationIsEnabled imageUploaded")
                        FirebaseApp.initializeApp(this)
                        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                task.exception!!.printStackTrace()
                                return@addOnCompleteListener
                            }
                            val token = task.result?.token

                            val locationJson = JSONObject()
                            locationJson.put("latitude", latitude)
                            locationJson.put("longitude", longitude)
                            val mainJson = JSONObject()
                            mainJson.put("email", email)
                            mainJson.put("password", password)
                            mainJson.put("fullname", name)
                            mainJson.put("username", username)
                            mainJson.put("gender", genderSelected)
                            mainJson.put("image", uploadedURL)
                            mainJson.put("location", locationJson)
                            mainJson.put("pushToken", token)

                            model!!.onRegister(mainJson)
                        }

                    }else{
                        Toast.makeText(this,"Please enable your Location",Toast.LENGTH_SHORT).show()
                    }

                }

                // startActivity(Intent(this, VerifyAccountActivity::class.java))
            }
            R.id.back_button -> {
                finish()
            }
            R.id.profile_image -> {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 100)
            }
            R.id.google_button -> {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("790288349026-17ea6u64d3dn3bntkt16k7gb9f25u46o.apps.googleusercontent.com")
                    .requestEmail()
                    .build()

                mGoogleSignInClient = GoogleSignIn.getClient(this@RegisterActivity, gso)

                val signInIntent = mGoogleSignInClient.signInIntent

                startActivityForResult(signInIntent, 9001)

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0F,
                    locationListener
                )
            }

        }
    }

    private val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            if (check) {
                Log.d("Anas", "${location.latitude}   ${location.longitude}")
                locationIsEnabled = true
                check = false

                latitude = location!!.latitude
                longitude = location!!.longitude
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String) {
            locationIsEnabled = true
            Log.i("Anas", "location is enabled")
        }

        override fun onProviderDisabled(provider: String) {
            Log.e("Anas", "location is disabled")
            locationIsEnabled = false
            latitude = 0.0
            longitude = 0.0
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            var config: HashMap<String, String> = HashMap()
            config["cloud_name"] = "deot6gwx4"
            config["api_key"] = "347487796288245"
            config["api_secret"] = "H51-o1h0UVu09XmlVp_T4XlYc6I"
            profile_image!!.setImageDrawable(null)
            profile_image!!.setImageURI(data?.data)
            MediaManager.init(this@RegisterActivity, config)
            MediaManager.get().upload(data?.data).callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Log.d("Anas", "$requestId  on Start")
                    imageUpload = false
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    Log.d("Anas", "$requestId  on Progress")
                }

                override fun onSuccess(
                    requestId: String?,
                    resultData: MutableMap<Any?, Any?>?
                ) {
                    imageUpload = true
                    uploadedURL = resultData!!["url"].toString()
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Log.d("Anas", "${error!!.description}  on Error")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    Log.d("Anas", "$requestId  on Reschedule")
                }
            }).dispatch()

        }else if (resultCode == Activity.RESULT_OK &&requestCode == 9001) {
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
        Log.e("Anas", "$response  response on register")
        val data = JSONObject(response)
        val message = data.getString("message")
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onGoogleResult(response: String) {
        TODO("Not yet implemented")
    }
}
package com.sportseventmanagement.ui.activity.race

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.sportseventmanagement.R
import com.sportseventmanagement.model.DistanceModel
import com.sportseventmanagement.model.GPXModel
import com.sportseventmanagement.model.StartRaceModel
import com.sportseventmanagement.ui.activity.HomeActivity
import com.sportseventmanagement.utility.Preferences
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import org.w3c.dom.*
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import kotlin.collections.ArrayList
import kotlin.random.Random


class FinishRaceActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener,
    GPXModel.GpxResult, StartRaceModel.StartRaceResult, DistanceModel.onDirectionResult {
    private var mapFragment: SupportMapFragment? = null
    private var transparent_image: ImageView? = null
    private var finish_race: RelativeLayout? = null
    private var info_layout2: RelativeLayout? = null
    private var info_layout: RelativeLayout? = null
    private var details_layout: LinearLayout? = null
    private var timeLayout: LinearLayout? = null
    private var distanceCoveredLayout: RelativeLayout? = null
    private var percentageLayout: LinearLayout? = null
    private var up_down_layout: RelativeLayout? = null
    private var up_down: ImageView? = null
    private var quitText: TextView? = null
    private var checkpoints: TextView? = null
    private var route_length: TextView? = null
    private var timer: TextView? = null

    private var selectedOption: Int = 0
    private var check: Boolean = true
    private var json = ""
    private var startRacemodel: StartRaceModel? = null
    private var gpxModel: GPXModel? = null
    private var distmodel: DistanceModel? = null
    private var pref: Preferences? = null
    private var mMap: GoogleMap? = null
    private var gpxUrl: String = ""
    private var gpxList: ArrayList<LatLng>? = null
    private var MillisecondTime: Long = 0
    private var startTime: Long = 0
    private var Seconds: Int = 0
    private var Minutes: Int = 0
    private var handler: Handler? = null
    private var TimeBuff: Long = 0
    private var UpdateTime = 0L
    private var MilliSeconds: Int = 0
    private var locationManager: LocationManager? = null
    private var wptList: ArrayList<LatLng>? = null
    var durationArr: ArrayList<String>? = null
    var distanceArr: ArrayList<String>? = null
    private var avg: Double? = null
    private var index: Int? = -1
    var destlat: Double = 0.0
    var destlong: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_race)

        init()
    }

    private fun init() {
        json = intent.getStringExtra("details")!!
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        gpxModel = GPXModel(this, this)
        distmodel= DistanceModel(this,this)
        startRacemodel = StartRaceModel(this, this)
        pref = Preferences(this)
        gpxList = ArrayList()
        wptList = ArrayList()
        pref = Preferences(this)
        durationArr = ArrayList()
        distanceArr = ArrayList()
        startTime = SystemClock.uptimeMillis()
        Log.d("Anas", "$startTime start time")
        handler = Handler()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000,
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

        transparent_image = findViewById(R.id.transparent_image)
        details_layout = findViewById(R.id.details_layout)
        up_down_layout = findViewById(R.id.up_down_layout)
        up_down = findViewById(R.id.up_down)
        quitText = findViewById(R.id.quitText)
        checkpoints = findViewById(R.id.checkpoints)
        route_length = findViewById(R.id.route_length)
        finish_race = findViewById(R.id.finish_race)
        info_layout2 = findViewById(R.id.info_layout2)
        info_layout = findViewById(R.id.info_layout)
        finish_race = findViewById(R.id.finish_race)
        timeLayout = findViewById(R.id.timeLayout)
        distanceCoveredLayout = findViewById(R.id.distanceCoveredLayout)
        percentageLayout = findViewById(R.id.percentageLayout)
        timer = findViewById(R.id.timer)
        handler?.postDelayed(runnable, 0)

        readingJSON(json)

        up_down!!.setImageResource(R.drawable.ic_down)

        mapFragment!!.getMapAsync(this)

        up_down_layout!!.setOnClickListener(this)
        quitText!!.setOnClickListener(this)
        finish_race!!.setOnClickListener(this)

    }

    var runnable: Runnable = object : Runnable {
        override fun run() {

            MillisecondTime = SystemClock.uptimeMillis() - startTime

            UpdateTime = TimeBuff + MillisecondTime

            Seconds = (UpdateTime / 1000).toInt()
            Minutes = Seconds / 60
            Seconds %= 60

            MilliSeconds = (UpdateTime % 1000).toInt()


            val f:NumberFormat=DecimalFormat("00")

            timer!!.text = "${f.format(Minutes)}:${f.format(Seconds)}"
            handler?.postDelayed(this, 0)
        }
    }

    fun calculationByDistance(StartP: LatLng, EndP: LatLng): Double {
        val Radius = 6371 // radius of earth in Km
        val lat1 = StartP.latitude
        val lat2 = EndP.latitude
        val lon1 = StartP.longitude
        val lon2 = EndP.longitude
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec = Integer.valueOf(newFormat.format(meter))
        Log.i(
            "Radius Value", "" + valueResult + "   KM  " + kmInDec
                    + " Meter   " + meterInDec
        )
        return Radius * c
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
                    LocationManager.GPS_PROVIDER,
                    2000,
                    0F,
                    locationListener
                )
            }

        }

    }
    var longi1: Double = 0.00
    var lat1: Double = 0.00
    var marker: Marker? = null
    private val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {

            val a = Random.nextDouble(from = 2.00, until = 3.00)
            val b = Random.nextDouble(from = 2.00, until = 2.5)
            lat1 = location.latitude + a
            longi1 = location.longitude + b
            Log.d("q1", "$lat1  and   $longi1")

            val latLng = LatLng(lat1, longi1)
            Log.d("marker1", "inside map ready   $lat1  and $longi1")
            if (marker == null) {
                if (pref!!.getPhotoURL() != "") {
                    Glide.with(this@FinishRaceActivity).asBitmap().load(pref!!.getPhotoURL())
                        .override(70, 70)
                        .apply(RequestOptions().fitCenter())
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                e!!.printStackTrace()
                                return true
                            }

                            override fun onResourceReady(
                                resource: Bitmap?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {


                                var icon: BitmapDescriptor =
                                    BitmapDescriptorFactory.fromBitmap(getCircular(resource!!))

                                var markerOptions: MarkerOptions =
                                    MarkerOptions().position(LatLng(lat1,longi1)).icon(icon)


                                marker = mMap!!.addMarker(markerOptions)

                                return true
                            }

                        })
                        .placeholder(R.drawable.ic_user_placeholder)
                        .preload()
                }


               // textView!!.setText(lat1.toString() + "," + longi1.toString())
                Log.d("if1", "inside if   $lat1  and $longi1")
            } else {

                //textView!!.setText(lat1.toString() + " , " + longi1.toString())
                marker!!.setPosition(latLng)
                Log.d("if", "inside else   $lat1  and $longi1")
            }
            //val options = MarkerOptions().position(latLng).icon(bitmapDescriptorFromVector(R.drawable.ic_garbage_truck)).title("i am there")
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onProviderDisabled(provider: String) {
            //check=true
            lat1 = 0.0
            longi1 = 0.0
            Log.d("locatiion", "location is disabled")
        }
    }


    private fun readingJSON(str: String) {
        val dataJSON = JSONObject(str)
        val eventID = dataJSON.getString("_id")
        var json = JSONObject()
        json.put("event", eventID)
        startRacemodel!!.onStartRace(pref!!.getToken()!!, json)
        Log.e("Anas", "$eventID")
        val startingLocationObject = dataJSON.getJSONObject("startingAreaLocation")
        val coordinates = startingLocationObject.getJSONArray("coordinates")
        gpxList!!.add(LatLng(coordinates.getDouble(1), coordinates.getDouble(0)))
        gpxUrl = dataJSON.getString("gpxUrl")
        val routeLength = dataJSON.getInt("routeLength")
        val startTimeData = dataJSON.getJSONObject("startTime")
        val endTimeData = dataJSON.getJSONObject("endTime")
        route_length!!.text = "$routeLength KM"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val c = Calendar.getInstance()
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        val c3 = Calendar.getInstance()
        val sdf = SimpleDateFormat("MMM")
        val startTimeFrom = inputFormat.parse(startTimeData.getString("from"))
        val startTimeTo = inputFormat.parse(startTimeData.getString("to"))
        val endTimeFrom = inputFormat.parse(endTimeData.getString("from"))
        val endTimeTo = inputFormat.parse(endTimeData.getString("to"))
        c.time = startTimeFrom
        c1.time = startTimeTo
        c2.time = endTimeFrom
        c3.time = endTimeTo

        val diff = startTimeTo.compareTo(startTimeFrom)
        Log.i("Anas", "$diff  difference")

    }

//    override fun onMapReady(map: GoogleMap?) {
//        Log.i("Anas", "MAP IS READY")
//        mMap = map!!
//        gpxModel!!.onGetGPX(gpxUrl)
//
//    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.up_down_layout -> {
                if (check) {
                    var params = info_layout!!.layoutParams
                    params.height = 100
                    info_layout!!.layoutParams = params
                    var params1 = details_layout!!.layoutParams
                    params1.height = 0
                    details_layout!!.layoutParams = params1

                    // info_layout!!.setBackgroundColor(Color.WHITE)
                    up_down!!.setImageResource(R.drawable.ic_up)
                    timeLayout!!.visibility = View.GONE
                    distanceCoveredLayout!!.visibility = View.GONE
                    percentageLayout!!.visibility = View.GONE
                    info_layout2!!.visibility = View.GONE
                    // details_layout!!.visibility=View.GONE
                    check = false
                } else {
                    var params = info_layout!!.layoutParams
                    params.height = 280
                    info_layout!!.layoutParams = params

                    var params1 = details_layout!!.layoutParams
                    params1.height = 250
                    details_layout!!.layoutParams = params1
                    // info_layout!!.setBackgroundColor(Color.TRANSPARENT)
                    up_down!!.setImageResource(R.drawable.ic_down)
                    timeLayout!!.visibility = View.VISIBLE
                    distanceCoveredLayout!!.visibility = View.VISIBLE
                    percentageLayout!!.visibility = View.VISIBLE
                    info_layout2!!.visibility = View.VISIBLE
                    //   details_layout!!.visibility=View.VISIBLE
                    check = true
                }

            }

            R.id.quitText -> {
                showAlertDialog()
            }
            R.id.finish_race -> {
                showFinishDialog()
            }
        }
    }

    private fun showFinishDialog() {
        val dialog = Dialog(this@FinishRaceActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_award)

        val close_layout: RelativeLayout = dialog.findViewById(R.id.delete_layout)
        val leaderboard: CardView = dialog.findViewById(R.id.leaderboard_card)

        close_layout.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(this@FinishRaceActivity, LeaderBoardActivity::class.java))
        }
        leaderboard.setOnClickListener {
            startActivity(
                Intent(
                    this@FinishRaceActivity,
                    LeaderBoardActivity::class.java
                )
            )
        }
        dialog.show()
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this@FinishRaceActivity)
        alertDialog.setTitle("")
        alertDialog.setCancelable(false)
        alertDialog.setMessage("Are you sure you want to finish the race, you won't be able to undo it?")
        alertDialog.setPositiveButton(
            "Yes"
        ) { p0, p1 -> showCustomDialog() }
        alertDialog.setNegativeButton("No") { p0, p1 -> }
        alertDialog.show()

    }


    private fun showCustomDialog() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val dialog = Dialog(this@FinishRaceActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_reason)
        var window = dialog.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        var radioGroup: RadioGroup = dialog.findViewById(R.id.radioGroup)
        val finishText: TextView = dialog.findViewById(R.id.finish_race)
        var radioButton: RadioButton

        radioGroup!!.setOnCheckedChangeListener { p0, selectedButtonId ->
            try {
                Log.e("Anas", "$selectedButtonId  selectedID")
                selectedOption = selectedButtonId

            } catch (e: NullPointerException) {
            }

        }

        finishText.setOnClickListener {

            radioButton = dialog.findViewById(selectedOption)
            val text = radioButton!!.text.toString()
            if (text != "Other") {

                startActivity(Intent(this@FinishRaceActivity, HomeActivity::class.java))
                finishAffinity()
            } else {
                startActivity(Intent(this@FinishRaceActivity, OtherReasonActivity::class.java))
                finishAffinity()
            }
        }
        dialog.show()
    }

    private fun decode(response: String) {
        val documentBuilderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        try {

            val documentBuilder: DocumentBuilder = documentBuilderFactory.newDocumentBuilder()
            val inputSource = InputSource()
            inputSource.characterStream = StringReader(response)
            val document: Document = documentBuilder.parse(inputSource)
            val elementRoot: Element = document.documentElement
            val nodelist_trkpt: NodeList = elementRoot.getElementsByTagName("trkpt")
            for (i in 0 until nodelist_trkpt.length) {
                val node: Node = nodelist_trkpt.item(i)
                val attributes: NamedNodeMap = node.attributes
                val newLatitude: String = attributes.getNamedItem("lat").textContent
                val newLatitude_double = newLatitude.toDouble()
                val newLongitude: String = attributes.getNamedItem("lon").textContent
                val newLongitude_double = newLongitude.toDouble()
                val newLocationName = "$newLatitude:$newLongitude"
                val newLocation = Location(newLocationName)
                newLocation.latitude = newLatitude_double
                newLocation.longitude = newLongitude_double
                gpxList!!.add(LatLng(newLatitude_double, newLongitude_double))
            }
        } catch (e: ParserConfigurationException) {
            // TODO Auto-generated catch block
            Log.d("Anas", "ParserConfigurationException")
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            Log.d("Anas", "FileNotFoundException")
            e.printStackTrace()
        } catch (e: SAXException) {
            // TODO Auto-generated catch block
            Log.d("Anas", "SAXException")
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            Log.d("Anas", "IOException")
            e.printStackTrace()
        }
    }

    fun getCircular(bm: Bitmap): Bitmap? {
        var w = bm.width
        var h = bm.height
        val radius = if (w < h) w else h
        w = radius
        h = radius
        val bmOut = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmOut)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = -0xbdbdbe
        val rect = Rect(0, 0, w, h)
        val rectF = RectF(rect)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(
            rectF.left + rectF.width() / 2,
            rectF.top + rectF.height() / 2,
            (radius / 2).toFloat(),
            paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bm, rect, rect, paint)
        return bmOut
    }

    override fun onGpxResult(response: String) {
        decode(response)

        var polyLineOptions = PolylineOptions()
        polyLineOptions.addAll(gpxList)
        polyLineOptions.width(15F)
        polyLineOptions.color(Color.RED)

        mMap!!.addPolyline(polyLineOptions)

        val middle: Int = (gpxList!!.size) / 2
        checkpoints!!.text = gpxList!!.size.toString()
        for (i in gpxList!!.indices) {

            var latlng = LatLng(gpxList!![i].latitude, gpxList!![i].longitude)

            when (i) {
                0 -> {
                    if (pref!!.getPhotoURL() != "") {
                        Glide.with(this).asBitmap().load(pref!!.getPhotoURL())
                            .override(70, 70)
                            .apply(RequestOptions().fitCenter())
                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    e!!.printStackTrace()
                                    return true
                                }

                                override fun onResourceReady(
                                    resource: Bitmap?,
                                    model: Any?,
                                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {


                                    var icon: BitmapDescriptor =
                                        BitmapDescriptorFactory.fromBitmap(getCircular(resource!!))

                                    var markerOptions: MarkerOptions =
                                        MarkerOptions().position(latlng).icon(icon)


                                    mMap!!.addMarker(markerOptions)

                                    return true
                                }

                            })
                            .placeholder(R.drawable.ic_user_placeholder)
                            .preload()
                    }
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(gpxList!![middle], 16.5f))
                }
                gpxList!!.size - 1 -> {

                    Glide.with(this).asBitmap().load(R.drawable.finish_circle)
                        .override(70, 70)
                        .apply(RequestOptions().fitCenter())
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                e!!.printStackTrace()
                                return true
                            }

                            override fun onResourceReady(
                                resource: Bitmap?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {


                                var icon: BitmapDescriptor =
                                    BitmapDescriptorFactory.fromBitmap(getCircular(resource!!))

                                var markerOptions: MarkerOptions =
                                    MarkerOptions().position(latlng).icon(icon)


                                mMap!!.addMarker(markerOptions)

                                return true
                            }

                        })
                        .preload()
                }
                else -> {
                    mMap!!.addMarker(
                        MarkerOptions().position(latlng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    )
                }
            }

//                            info +=(
//                        gpxList!![i].latitude
//                            .toString() + " : "
//                                + gpxList!![i].longitude
//                    )
//                            Log . d ("Anas", "$info    lat lng"
//                )
        }
    }

    override fun onStartRaceResult(response: String) {
        Log.i("Anas", "$response  from startRace")
    }
    override fun onMapReady(map: GoogleMap?) {
        Log.i("Anas", "MAP IS READY")
        mMap = map
        // model!!.onGetGPX(gpxUrl)
        var assetInStream: InputStream? = null

        try {
            assetInStream = assets.open("test1.gpx")
            val size: Int = assetInStream.available()
            val buffer = ByteArray(size)
            var text: String = String(buffer)
            //  Log.d("Anas","$text   text")

            var file: File = File.createTempFile("test", ".gpx")


            FileOutputStream(file).use { out -> IOUtils.copy(assetInStream, out) }
            Log.d("Anas", "${file.exists()}  input stream file")
            decodeGPX(file)
            checkpoints!!.text = wptList!!.size.toString()
            for (x in 0 until wptList!!.size) {
                if (x < wptList!!.size - 1) {
                    distmodel!!.onGetDistance(wptList!![x], wptList!![x + 1])
                }
                when (x) {
                    0 -> {
                        if (pref!!.getPhotoURL() != "") {
                            Glide.with(this).asBitmap().load(pref!!.getPhotoURL())
                                .override(70, 70)
                                .apply(RequestOptions().fitCenter())
                                .listener(object : RequestListener<Bitmap> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        e!!.printStackTrace()
                                        return true
                                    }

                                    override fun onResourceReady(
                                        resource: Bitmap?,
                                        model: Any?,
                                        target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {


                                        var icon: BitmapDescriptor =
                                            BitmapDescriptorFactory.fromBitmap(getCircular(resource!!))

                                        var markerOptions: MarkerOptions =
                                            MarkerOptions().position(wptList!![0]).icon(icon)


                                        map!!.addMarker(markerOptions)

                                        return true
                                    }

                                })
                                .placeholder(R.drawable.ic_user_placeholder)
                                .preload()
                        }
                        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(wptList!![0], 10f))
                    }
                    wptList!!.size - 1 -> {
                        Glide.with(this).asBitmap().load(R.drawable.finish_circle)
                            .override(70, 70)
                            .apply(RequestOptions().fitCenter())
                            .listener(object : RequestListener<Bitmap> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    e!!.printStackTrace()
                                    return true
                                }

                                override fun onResourceReady(
                                    resource: Bitmap?,
                                    model: Any?,
                                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {


                                    var icon: BitmapDescriptor =
                                        BitmapDescriptorFactory.fromBitmap(getCircular(resource!!))

                                    var markerOptions: MarkerOptions =
                                        MarkerOptions().position(wptList!![x]).icon(icon)


                                    map!!.addMarker(markerOptions)

                                    return true
                                }

                            })
                            .preload()
                    }
                    else -> {
                        map!!.addMarker(
                            MarkerOptions().position(wptList!![x])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                        )
                    }

                }

            }

            Log.d("Anas", "${wptList!!.size} wptList ki size ")
            var polyLineOptions = PolylineOptions()
            polyLineOptions.addAll(wptList)
            polyLineOptions.width(13F)
            polyLineOptions.color(Color.MAGENTA)
            polyLineOptions.geodesic(true)


        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            assetInStream?.close()
        }
    }

    private fun decodePolyline(encoded: String, int: Int): List<LatLng> {
        Log.d("inside", "inside decode $int")
        val poly = ArrayList<LatLng>()
        //latlngList!!.clear()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))

            poly!!.add(latLng)
        }
        // mainList!!.add(latlngList1)
        //Log.d("sizeOF","ye time to  ${poly[1]}")
        return poly
    }

    private fun jsonParsing(response: String?) {
        val json = JSONObject(response)
        val route = json.getJSONArray("routes")
        var mainList = ArrayList<ArrayList<LatLng>>()
        Log.d("array of", "${route.length()}")

        for (item in 0 until route.length()) {
            Log.d("index", "$item  item")
            //ye sare route k ander jitne itme ha unka andar jitne objet ha wo ha ye
            val routeItems = route.getJSONObject(item)
            //Log.d("item", "${routeItems.length()}")

            // ye ek array tha jo legs me tha api me
            val legs = routeItems.getJSONArray("legs")
            // Log.d("legsData","${legs.length()}")
            for (X in 0 until legs.length()) {
                val legItem = legs.getJSONObject(X)
                val duration = legItem.getJSONObject("duration").getString("text")
                val distance = legItem.getJSONObject("distance").getString("text")
                durationArr!!.add(duration)
                distanceArr!!.add(distance)

                val durTime = duration.split(' ')[0]
                val disTime = distance.split(' ')[0]

                var average = (durTime.toDouble() + disTime.toDouble()).toDouble() / 2

                if (avg != null && avg!! < average) {
                    Log.d("small", "inside if  ${avg}")
                } else {
                    index = item
                    avg = average
                    Log.d("small", "inside else  ${avg}  ${index}")
                }
                Log.d(
                    "size1",
                    "}   vvvvv  ${durTime} ${disTime} ${average}  ${durationArr}  ${distanceArr} "
                )


                val endLocation = legItem.getJSONObject("end_location")
                destlat = endLocation.getDouble("lat")
                destlong = endLocation.getDouble("lng")
//                mMap!!.addMarker(
//                    MarkerOptions().position(LatLng(destlat, destlong))
//                        .title("this is my destination")
//                )
                val steps = legItem.getJSONArray("steps")
                Log.d("list", "step length   ${steps.length()}")
                //Log.d("legItems","${legItem.length()}")
                val latlngList1 = ArrayList<LatLng>()
                for (Y in 0 until steps.length()) {
                    val stepsItem = steps.getJSONObject(Y)
                    val polyline = stepsItem.getJSONObject("polyline")
                    val points = polyline.getString("points")
                    //Log.d("points","$points")
                    latlngList1!!.addAll(decodePolyline(points, item))
                    // var lineOptions = PolylineOptions()


                }

                mainList!!.add(latlngList1)
//                val c=mainList!!.size
//                Log.d("sizeOF","}   vvvvv  $mainList[c]")
            }
        }
        for (x in 0 until mainList.size) {
            var lineOptions = PolylineOptions()
            lineOptions.addAll(mainList!![x])
            lineOptions.width(10f)
            lineOptions.color(Color.MAGENTA)
            lineOptions.geodesic(true)

            mMap!!.addPolyline(lineOptions)


        }


    }
    private fun decodeGPX(file: File) {

        val documentBuilderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        try {
            val documentBuilder: DocumentBuilder = documentBuilderFactory.newDocumentBuilder()
            val fileInputStream = FileInputStream(file)
            val document: Document = documentBuilder.parse(fileInputStream)
            val elementRoot: Element = document.documentElement
            val nodelist_trkpt: NodeList = elementRoot.getElementsByTagName("trkpt")
            val nodeList_wpt: NodeList = elementRoot.getElementsByTagName("wpt")
            Log.d("Anas", "${nodeList_wpt.length} length of nodelist wpt")
            val metadata = elementRoot.getElementsByTagName("metadata")
            var minLat: Double = 0.0
            var minLong: Double = 0.0
            for (i in 0 until wptList!!.size) {
                val metaNode = metadata.item(i)
                val attr = metaNode.attributes
                val maxLat = attr.getNamedItem("maxlat").textContent.toDouble()
                val maxLong = attr.getNamedItem("maxlon").textContent.toDouble()
                minLat = attr.getNamedItem("minlat").textContent.toDouble()
                minLong = attr.getNamedItem("minlon").textContent.toDouble()
                wptList!!.add(LatLng(maxLat, maxLong))
            }


            for (x in 0 until nodeList_wpt.length) {
                val node: Node = nodeList_wpt.item(x)
                val attributes: NamedNodeMap = node.attributes
                val newLatitude: String = attributes.getNamedItem("lat").textContent
                val newLatitude_double = newLatitude.toDouble()
                val newLongitude: String = attributes.getNamedItem("lon").textContent
                val newLongitude_double = newLongitude.toDouble()
                val newLocationName = "$newLatitude:$newLongitude"
                val newLocation = Location(newLocationName)
                newLocation.latitude = newLatitude_double
                newLocation.longitude = newLongitude_double
                Log.d(
                    "Anas",
                    "Latitude=${newLocation.latitude}   Longitude=${newLocation.longitude}"
                )
                wptList!!.add(LatLng(newLatitude_double, newLongitude_double))
                //gpxList!!.add(LatLng(newLatitude_double, newLongitude_double))
            }
            checkpoints!!.text = wptList!!.size.toString()
            //  wptList!!.add(LatLng(minLat, minLong))
//            for (i in 0 until nodelist_trkpt.length) {
//                val node: Node = nodelist_trkpt.item(i)
//                val attributes: NamedNodeMap = node.attributes
//                val newLatitude: String = attributes.getNamedItem("lat").textContent
//                val newLatitude_double = newLatitude.toDouble()
//                val newLongitude: String = attributes.getNamedItem("lon").textContent
//                val newLongitude_double = newLongitude.toDouble()
//                val newLocationName = "$newLatitude:$newLongitude"
//                val newLocation = Location(newLocationName)
//                newLocation.latitude = newLatitude_double
//                newLocation.longitude = newLongitude_double
//                gpxList!!.add(LatLng(newLatitude_double, newLongitude_double))
//            }
            fileInputStream.close()
        } catch (e: ParserConfigurationException) {
            // TODO Auto-generated catch block
            Log.d("Anas", "ParserConfigurationException")
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            Log.d("Anas", "FileNotFoundException")
            e.printStackTrace()
        } catch (e: SAXException) {
            // TODO Auto-generated catch block
            Log.d("Anas", "SAXException")
            e.printStackTrace()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            Log.d("Anas", "IOException")
            e.printStackTrace()
        }
    }
    override fun onDirectionResult(response: String) {
        jsonParsing(response)
    }

}
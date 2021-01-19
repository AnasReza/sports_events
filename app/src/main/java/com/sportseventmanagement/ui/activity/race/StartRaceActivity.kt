package com.sportseventmanagement.ui.activity.race

import android.content.Intent
import android.graphics.*
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
import com.sportseventmanagement.model.GPXModel
import com.sportseventmanagement.utility.Preferences
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_all_events.*
import org.json.JSONObject
import org.w3c.dom.*
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import kotlin.collections.ArrayList

class StartRaceActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener,
    GPXModel.GpxResult {
    private var mainScroll: ScrollView? = null
    private var mapFragment: SupportMapFragment? = null
    private var transparent_image: ImageView? = null
    private var startRace: RelativeLayout? = null
    private var backbutton: ImageView? = null
    private var event_maker: TextView? = null
    private var headline: TextView? = null
    private var details: TextView? = null
    private var title: TextView? = null
    private var maker_name: TextView? = null
    private var participant: TextView? = null
    private var startFrom: TextView? = null
    private var startTo: TextView? = null
    private var endFrom: TextView? = null
    private var endTo: TextView? = null
    private var description: TextView? = null
    private var eventImage: ImageView? = null
    private var rulesLayout: LinearLayout? = null

    private var jsonStr: String = ""
    private var model: GPXModel? = null
    private var pref:Preferences?=null
    private var mMap: GoogleMap? = null
    private var gpxUrl: String = ""
    private var gpxList: ArrayList<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_race)

        init()
    }

    private fun init() {
        jsonStr = intent.getStringExtra("details")!!
        model = GPXModel(this, this)
        pref=Preferences(this)
        gpxList= ArrayList()

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mainScroll = findViewById(R.id.mainScroll)
        transparent_image = findViewById(R.id.transparent_image)
        startRace = findViewById(R.id.start_race)
        backbutton = findViewById(R.id.back_button)
        event_maker = findViewById(R.id.event_maker)
        headline = findViewById(R.id.headline)
        title = findViewById(R.id.title)
        details = findViewById(R.id.allDetails)
        startFrom = findViewById(R.id.startFrom)
        startTo = findViewById(R.id.startTo)
        endFrom = findViewById(R.id.endFrom)
        endTo = findViewById(R.id.endTo)
        description = findViewById(R.id.descriptionText)
        participant = findViewById(R.id.participantsText)
        eventImage = findViewById(R.id.eventImage)
        maker_name = findViewById(R.id.event_maker)
        rulesLayout = findViewById(R.id.rulesLayout)



        mapFragment!!.getMapAsync(this)
        startRace!!.setOnClickListener(this)
        back_button.setOnClickListener(this)

        readingJSON(jsonStr)

        transparent_image!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                var action: Int = event!!.action
                return when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Disallow ScrollView to intercept touch events.
                        mainScroll!!.requestDisallowInterceptTouchEvent(true)
                        // Disable touch on transparent view
                        false
                    }
                    MotionEvent.ACTION_UP -> {
                        // Allow ScrollView to intercept touch events.
                        mainScroll!!.requestDisallowInterceptTouchEvent(false)
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        mainScroll!!.requestDisallowInterceptTouchEvent(true)
                        false
                    }
                    else -> true
                }
                return false
            }

        })


    }

    private fun readingJSON(str: String) {
        val dataJSON = JSONObject(str)

        val rulesList = dataJSON.getJSONArray("rules")
        val imageURL = dataJSON.getString("picture")
        val participatesList = dataJSON.getJSONArray("participates")
        val totalPartcipants = dataJSON.getInt("totalParticipants")
        val descriptionString = dataJSON.getString("description")
        val startTimeData = dataJSON.getJSONObject("startTime")
        val startingLocationObject = dataJSON.getJSONObject("startingAreaLocation")
        val coordinates = startingLocationObject.getJSONArray("coordinates")
        gpxList!!.add(LatLng(coordinates.getDouble(1), coordinates.getDouble(0)))
        gpxUrl = dataJSON.getString("gpxUrl")
        val endTimeData = dataJSON.getJSONObject("endTime")
        val makerJson = dataJSON.getJSONObject("eventMaker")
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

        maker_name!!.text = makerJson.getString("businessName")
        maker_name!!.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        title!!.text = dataJSON.getString("title")
        headline!!.text = dataJSON.getString("title")

        details!!.text =
            "${dataJSON.getString("category")} • " +
                    "${dataJSON.getString("routeLength")} KM • " +
                    "${dataJSON.getString("startingAreaAddress")}"
        description!!.text = descriptionString

        participant!!.text = "${participatesList.length()}/$totalPartcipants"
        startFrom!!.text =
            "${startTimeFrom.date} ${sdf.format(c.time)} ${startTimeFrom.hours}:${startTimeFrom.minutes} "
        startTo!!.text =
            "${startTimeTo.date} ${sdf.format(c1.time)} ${startTimeTo.hours}:${startTimeTo.minutes}"

        endFrom!!.text =
            "${endTimeFrom.date} ${sdf.format(c2.time)} ${endTimeFrom.hours}:${endTimeFrom.minutes} "
        endTo!!.text =
            "${endTimeTo.date} ${sdf.format(c3.time)} ${endTimeTo.hours}:${endTimeTo.minutes}"

        eventImage!!.setImageDrawable(null)
        Picasso.with(this).load(imageURL).into(eventImage)

        Log.d("Anas", "${rulesList.length()} rulesLenght")
        for (x in 0 until rulesList.length()) {
            val inflator = LayoutInflater.from(this)
            var newView = inflator!!.inflate(R.layout.rules_view, null)

            val number: TextView = newView.findViewById(R.id.number)
            val rulesDes: TextView = newView.findViewById(R.id.rulesDes)

            rulesDes.text = rulesList.getString(x)
            number!!.text = (x + 1).toString()

            rulesLayout!!.addView(newView)
        }


    }

    override fun onMapReady(map: GoogleMap?) {
        Log.i("Anas", "MAP IS READY")
        mMap = map!!
        model!!.onGetGPX(gpxUrl)

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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.start_race -> {
                startActivity(
                    Intent(
                        this@StartRaceActivity,
                        ReadyRaceActivity::class.java
                    ).putExtra("details", jsonStr)
                )
            }
            R.id.back_button -> {
                finish()
            }
        }
    }

    override fun onGpxResult(response: String) {
        decode(response)

        var polyLineOptions = PolylineOptions()
        polyLineOptions.addAll(gpxList)
        polyLineOptions.width(15F)
        polyLineOptions.color(Color.RED)

        mMap!!.addPolyline(polyLineOptions)
        val middle: Int = (gpxList!!.size) / 2
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

}
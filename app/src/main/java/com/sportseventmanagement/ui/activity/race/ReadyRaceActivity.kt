package com.sportseventmanagement.ui.activity.race

import android.content.Intent
import android.graphics.*
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
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

class ReadyRaceActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener,
    GPXModel.GpxResult {
    private var mapFragment: SupportMapFragment? = null
    private var start_race: RelativeLayout? = null
    private var route_length: TextView? = null
    private var checkpoints: TextView? = null

    private var model: GPXModel? = null
    private var pref: Preferences? = null
    private var mMap: GoogleMap? = null
    private var gpxUrl: String = ""
    private var gpxList: ArrayList<LatLng>? = null
    private var jsonStr: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready_race)

        init()
    }

    private fun init() {
        jsonStr = intent.getStringExtra("details")!!
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        model = GPXModel(this, this)
        pref = Preferences(this)
        gpxList = ArrayList()

        start_race = findViewById(R.id.start_race_button)
        route_length = findViewById(R.id.route_length)
        checkpoints = findViewById(R.id.checkpoints)

        readingJSON(jsonStr)

        mapFragment!!.getMapAsync(this)
        start_race!!.setOnClickListener(this)

    }

    private fun readingJSON(str: String) {
        val dataJSON = JSONObject(str)
        val eventID = dataJSON.getString("_id")
        Log.e("Anas", "$eventID")
        val startingLocationObject = dataJSON.getJSONObject("startingAreaLocation")
        val coordinates = startingLocationObject.getJSONArray("coordinates")
        gpxList!!.add(LatLng(coordinates.getDouble(1), coordinates.getDouble(0)))
        gpxUrl = dataJSON.getString("gpxUrl")
        val routeLength = dataJSON.getInt("routeLength")
        val startTimeData = dataJSON.getJSONObject("startTime")
        val endTimeData = dataJSON.getJSONObject("endTime")

        route_length!!.text = routeLength.toString()

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

    override fun onMapReady(map: GoogleMap?) {
        Log.i("Anas", "MAP IS READY")
        mMap = map!!
        model!!.onGetGPX(gpxUrl)

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.start_race_button -> {
                startActivity(
                    Intent(
                        this@ReadyRaceActivity,
                        FinishRaceActivity::class.java
                    ).putExtra("details",jsonStr)
                )
            }
        }
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

        checkpoints!!.text = gpxList!!.size.toString()

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
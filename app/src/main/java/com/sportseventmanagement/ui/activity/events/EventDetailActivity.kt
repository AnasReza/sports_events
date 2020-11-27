package com.sportseventmanagement.ui.activity.events

import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.sportseventmanagement.R
import org.apache.commons.io.IOUtils
import org.w3c.dom.*
import org.xml.sax.SAXException
import java.io.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException


class EventDetailActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {
    private var mainScroll: ScrollView? = null
    private var mapFragment: SupportMapFragment? = null
    private var transparent_image: ImageView? = null
    private var To_Pay: RelativeLayout? = null
    private var back_button: ImageView? = null
    private var event_maker: TextView? = null
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        init()


    }

    private fun init() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mainScroll = findViewById(R.id.mainScroll)
        transparent_image = findViewById(R.id.transparent_image)
        To_Pay = findViewById(R.id.ToPay)
        back_button = findViewById(R.id.back_button)
        event_maker = findViewById(R.id.event_maker)


        mapFragment!!.getMapAsync(this)
        To_Pay!!.setOnClickListener(this)
        back_button!!.setOnClickListener(this)
        event_maker!!.setOnClickListener(this)
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

    override fun onMapReady(map: GoogleMap?) {
        Log.i("Anas", "MAP IS READY")
        var assetInStream: InputStream? = null

        try {
            assetInStream = assets.open("test.gpx")
            val size: Int = assetInStream.available()
            val buffer = ByteArray(size)
            var text: String = String(buffer)
            //  Log.d("Anas","$text   text")

            var file: File = File.createTempFile("test", ".gpx")


            FileOutputStream(file).use { out -> IOUtils.copy(assetInStream, out) }
            Log.d("Anas", "${file.exists()}  input stream file")
            val gpxList = decodeGPX(file)

            var polyLineOptions: PolylineOptions = PolylineOptions()
            polyLineOptions.addAll(gpxList)
            polyLineOptions.width(15F)
            polyLineOptions.color(Color.RED)

            map!!.addPolyline(polyLineOptions)

            for (i in gpxList!!.indices) {

                var latlng = LatLng(gpxList!![i].latitude, gpxList!![i].longitude)
                map!!.addMarker(
                    MarkerOptions().position(latlng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                )
                when (i) {
                    0 -> {
                        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,16f))
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

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            assetInStream?.close()
        }
    }

    private fun decodeGPX(file: File): List<LatLng>? {
        val list: ArrayList<LatLng> = ArrayList<LatLng>()
        val documentBuilderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        try {
            val documentBuilder: DocumentBuilder = documentBuilderFactory.newDocumentBuilder()
            val fileInputStream = FileInputStream(file)
            val document: Document = documentBuilder.parse(fileInputStream)
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
                list.add(LatLng(newLatitude_double, newLongitude_double))
            }
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
        return list
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ToPay -> {
                // startActivity(Intent(this@EventDetailActivity, PaymentActivity::class.java))
            }
            R.id.back_button -> {
                finish()
            }
            R.id.event_maker -> {
                startActivity(
                    Intent(
                        this@EventDetailActivity,
                        EventMakerDetailsActivity::class.java
                    )
                )
            }
        }
    }

}
package com.sportseventmanagement.model

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class DistanceModel(private val result: onDirectionResult, private val context: Context) {

    //MODEL BOILERPLATE
    interface onDirectionResult {
        fun onDirectionResult(response: String)
    }

    private var requestQueue: RequestQueue? = null


    fun onGetDistance(latlng: LatLng, nextLatlng: LatLng) {
        requestQueue = Volley.newRequestQueue(context)
        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=${latlng.latitude},${latlng.longitude}&destination=${nextLatlng.latitude},${nextLatlng.longitude}&mode=bicycling&key=AIzaSyD3h20CTKpHUM_zB1FcTK8irTowxPZaghI"
        Log.d("Anas", url)
        val postReq = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->

                //  mainResponse = response
                result.onDirectionResult(response.toString())
            }, Response.ErrorListener { error ->
                error.printStackTrace()
                val response = error.networkResponse
                if (error is ServerError && response != null) {
                    try {
                        val res = String(
                            response.data,
                        )
                        // Now you can use any deserializer to make sense of data
                        val obj = JSONObject(res)
                        result.onDirectionResult(obj.toString())
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }
            }) {

        }

        requestQueue!!.add(postReq)
    }

}
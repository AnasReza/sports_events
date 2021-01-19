package com.sportseventmanagement.model

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class GPXModel (private val result: GpxResult, private val context: Context) {

    //MODEL BOILERPLATE
    interface GpxResult {
        fun onGpxResult(response: String)
    }

    private var requestQueue: RequestQueue? = null

    fun onGetGPX(url:String) {
        requestQueue = Volley.newRequestQueue(context)
        val url = url
        Log.d("Anas", url)
        val postReq = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->

                //  mainResponse = response
                result.onGpxResult(response.toString())
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
                        result.onGpxResult(obj.toString())
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
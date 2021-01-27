package com.sportseventmanagement.model

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.sportseventmanagement.api.API
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class FetchEventModel(private val result: Event?, private val context: Context) {

    private var requestQueue: RequestQueue? = null

    fun onEventsLimit(token: String) {
        requestQueue = Volley.newRequestQueue(context)
        val url = "${API.userEvents}?populateEventMaker=true"
        Log.d("Anas", url)
        val postReq = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->

                result!!.onEventLimit(response.toString())
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
                        result!!.onEventLimit(obj.toString())
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $token"

                return params
            }
        }

        requestQueue!!.add(postReq)
    }
    fun onEventsFilter(token: String,filter:String) {
        requestQueue = Volley.newRequestQueue(context)
        val url = "${API.userEvents}?filter=$filter&populateEventMaker=true"
        Log.d("Anas", url)
        val postReq = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->

                result!!.onEventLimit(response.toString())
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
                        result!!.onEventLimit(obj.toString())
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $token"

                return params
            }
        }

        requestQueue!!.add(postReq)
    }
    fun onEventsByID(token: String,eventID:String) {
        requestQueue = Volley.newRequestQueue(context)
        val url = "${API.userEvents}?event=$eventID&populateEventMaker=true"
        Log.d("Anas", url)
        val postReq = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->

                result!!.onEventLimit(response.toString())
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
                        result!!.onEventLimit(obj.toString())
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $token"

                return params
            }
        }

        requestQueue!!.add(postReq)
    }

    fun onEventsByMakerID(token: String,eventID:String) {
        requestQueue = Volley.newRequestQueue(context)
        val url = "${API.userEvents}?eventMaker=$eventID&populateEventMaker=true"
        Log.d("Anas", url)
        val postReq = object : StringRequest(Request.Method.GET, url,
            Response.Listener { response ->

                result!!.onEventLimit(response.toString())
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
                        result!!.onEventLimit(obj.toString())
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer $token"

                return params
            }
        }

        requestQueue!!.add(postReq)
    }

    //MODEL BOILERPLATE
    interface Event {
        fun onEventLimit(response: String)
    }
}
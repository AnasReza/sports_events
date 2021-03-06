package com.sportseventmanagement.model

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sportseventmanagement.api.API
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class FacebookLoginModel (private val result: onFacebookResult, private val context: Context) {

    //MODEL BOILERPLATE
    interface onFacebookResult {
        fun onFacebookResult(response: String)
    }

    private var requestQueue: RequestQueue? = null


    fun onFacebookLogin(json: JSONObject) {
        requestQueue = Volley.newRequestQueue(context)
        val url = API.userFacebookLogin
        Log.d("Anas", url)
        val postReq = object : JsonObjectRequest(Request.Method.POST, url, json,
            Response.Listener<JSONObject> { response ->

                //  mainResponse = response
                result.onFacebookResult(response.toString())
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
                        result.onFacebookResult(obj.toString())
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
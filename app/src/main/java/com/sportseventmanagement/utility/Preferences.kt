package com.sportseventmanagement.utility

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log

class Preferences(context: Context) {

    private val LOGIN = "login"
    private val EMAIL = "email"
    private val TYPE="type"
    private val ID = "id"
    private val GENDER = "gender"
    private val USERNAME = "username"
    private val FULLNAME = "fullname"
    private val TOKEN = "token"
    private val PHOTOURL = "photourl"



    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getLogin(): Boolean? {
        var deviceToken = preferences.getBoolean(LOGIN, false)
        return deviceToken
    }

    fun setLogin(value: Boolean) {
        preferences.edit().putBoolean(LOGIN, value).apply()
    }
    fun setType(value: String) {
        preferences.edit().putString(TYPE, value).apply()
    }
    fun getType(): String? {
        var deviceToken = preferences.getString(TYPE, "")
        return deviceToken
    }

    fun getEmail(): String? {
        var deviceToken = preferences.getString(EMAIL, "")
        return deviceToken
    }


    fun setEmail(value: String) {
        preferences.edit().putString(EMAIL, value).apply()
    }

    fun getID(): String? {
        var deviceToken = preferences.getString(ID, "")
        return deviceToken
    }

    fun setID(value: String) {
        preferences.edit().putString(ID, value).apply()
    }

    fun getGender(): String? {
        var deviceToken = preferences.getString(GENDER, "")
        return deviceToken
    }

    fun setGender(value: String) {
        preferences.edit().putString(GENDER, value).apply()
    }
    fun getUserName(): String? {
        var deviceToken = preferences.getString(USERNAME, "")
        return deviceToken
    }

    fun setUserName(value: String) {
        preferences.edit().putString(USERNAME, value).apply()
    }

    fun getFullName(): String? {
        var deviceToken = preferences.getString(FULLNAME, "")
        return deviceToken
    }

    fun setFullName(value: String) {
        preferences.edit().putString(FULLNAME, value).apply()
    }

    fun getToken(): String? {
        var deviceToken = preferences.getString(TOKEN, "")
        return deviceToken
    }

    fun setToken(value: String) {
        preferences.edit().putString(TOKEN, value).apply()
    }
    fun getPhotoURL(): String? {
        var deviceToken = preferences.getString(PHOTOURL, "")
        return deviceToken
    }

    fun setPhotoURL(value: String) {
        preferences.edit().putString(PHOTOURL, value).apply()
    }

}
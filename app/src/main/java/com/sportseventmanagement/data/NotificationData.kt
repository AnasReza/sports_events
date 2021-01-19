package com.sportseventmanagement.data

import org.json.JSONObject

class NotificationData(
    noti: String,
    user: String,
    head: String,
    des: String,
    str: String,
    type: String,
    createdTime: String,
    updatedTime: String,
    event: JSONObject
) {
    private var notificationID: String = noti
    private var userID: String = user
    private var title: String = head
    private var body: String = des
    private var from: String = str
    private var type: String = type
    private var createdAt: String = createdTime
    private var updatedAt: String = updatedTime
    private var data: JSONObject = event

    fun getNotificationID():String{
        return notificationID
    }
    fun getUserID():String{
        return userID
    }
    fun getTitle():String{
        return title
    }
    fun getFrom():String{
        return from
    }
    fun getType():String{
        return type
    }
    fun getCreatedAt():String{
        return createdAt
    }
    fun getUpdateAt():String{
        return updatedAt
    }
    fun getData():JSONObject{
        return data
    }
    fun getBody():String{
        return body
    }

}
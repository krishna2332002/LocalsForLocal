package com.example.dailycare.Models

import java.sql.Timestamp
data class Message (
    var msg: String? = null,
    var senderId: String? = null,
    var msgId:String?=null
)
package com.example.dailycare.Models

data class ServiceProvider(
    val serviceProviderName: String? = null,
    val serviceName: String? = null,
    val serviceProviderUid:String?=null,
    val completedWoks:Int=0,
    val customersFavourite:Int=0,
    val servicerPic:String?=null,
    val servicerPhone:String?=null,
    val alternativeServicerPhone:String?=null,
    val description:String?=null,
    var experience:String?=null
)

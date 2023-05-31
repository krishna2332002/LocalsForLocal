package com.localsforlocal.dailycare.Models

data class Post(
    val jobVacancies: String? = null,
    val jobAddress: String? = null,
    val expectedSalary: String? = null,
    val description: String? = null,
    val jobProviderUid: String? = null,
    val jobProviderName: String? = null,
    val jobProviderImage: String? = null,
    val district:String?=null,
    val state:String?=null,
    val jobType:String?=null,
    val postsUid:String?=null,
    val applies:Int?=null,
    val date:String?=null,
    val jobTime:String?=null
)
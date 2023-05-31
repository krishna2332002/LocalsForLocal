package com.localsforlocal.dailycare.Models

data class Notification(
   var notificationType:Int?=null,
   var notificationBy: String?=null,
   var date: Long?=null,
   var checkOpen: Boolean = false,
)
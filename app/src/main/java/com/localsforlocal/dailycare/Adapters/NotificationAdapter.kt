package com.localsforlocal.dailycare.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.localsforlocal.dailycare.Models.Notification
import com.localsforlocal.dailycare.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.notification.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class NotificationAdapter(private var context:Context, private var notificationList:ArrayList<Notification>, private var usersUid:String)
    :RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){
    class NotificationViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
       var view=LayoutInflater.from(context).inflate(R.layout.notification,parent,false)
        return NotificationViewHolder(view)
    }
    private var date: DateFormat = SimpleDateFormat("hh:mm a")
    private var date2: DateFormat = SimpleDateFormat("dd MMM")
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    var userImage:String?=null
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
         var notification=notificationList[position]
        holder.itemView.apply {
            notificationSenderName.setText(notification.notificationBy)
            if(notification.notificationType==1){
                notificationTitle.setText("Requested for service")
            }
            else if(notification.notificationType==2)
                notificationTitle.setText("Accepted your request")
            else
            {
                notificationTitle.setText("Accepted your request")
            }
            }
            var time1 = System.currentTimeMillis() / 86400000
            var time2 = notification.date.toString().toLong() / 86400000
            var const: Long = 1
            if (time1 != time2) {
                if (time1 - time2 == const)
                    holder.itemView.notificationTime.setText("Yesterday")
                else {
                    holder.itemView.notificationTime.setText(
                        date2.format(
                            notification.date.toString().toLong()
                        ).toString()
                    )
                }
            } else {
                holder.itemView.notificationTime.setText(
                    date.format(
                        notification.date.toString().toLong()
                    ).toString()
                )
            }
        }


    override fun getItemCount(): Int {
        return notificationList.size
    }
}
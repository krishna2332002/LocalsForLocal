package com.localsforlocal.dailycare.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.localsforlocal.dailycare.Activities.ChatActivity
import com.localsforlocal.dailycare.Models.User
import com.localsforlocal.dailycare.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.service_provider_view_holder.view.*

class FriendAdapter (
        private var context: Context,
        private var serviceProviderList: ArrayList<String>,
) : RecyclerView.Adapter<FriendAdapter.ServiceViewHolder>() {
        class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
                var view = LayoutInflater.from(context).inflate(R.layout.service_provider_view_holder, parent, false)
                return ServiceViewHolder(view)
        }
        var database= FirebaseDatabase.getInstance().reference
        override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
                var serviceProviderUid = serviceProviderList[position]
                holder.itemView.apply {
                        database.child("Profile").child(serviceProviderUid!!).addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                        val user=snapshot.getValue(User::class.java)
                                        serviceProviderName.setText(user!!.name)
                                        serviceName.setText(user.phoneNo)
                                        Glide.with(context)
                                                .load(user.image)
                                                .override(1000, 1000)
                                                .placeholder(R.drawable.profile)
                                                .into(serviceProviderPic)
                                        holder.itemView.setOnClickListener {
                                                var intent=Intent(context,ChatActivity::class.java)
                                                intent.putExtra("receiverUid",serviceProviderUid)
                                                intent.putExtra("receiverName",user.name)
                                                intent.putExtra("receiverPic",user.image)
                                                context.startActivity(intent)
                                        }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                }
                        })
                }

        }

        override fun getItemCount(): Int {
                return serviceProviderList.size
        }

}
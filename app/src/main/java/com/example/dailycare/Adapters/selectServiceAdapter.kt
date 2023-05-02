package com.example.dailycare.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dailycare.Activities.CreateServiceProviderAccount
import com.example.dailycare.Models.Service
import com.example.dailycare.R
import kotlinx.android.synthetic.main.select_service_view_holder.view.*
import kotlinx.android.synthetic.main.service_view_holder.view.*

class selectServiceAdapter (
    private var context: Context,
    private var serviceList: ArrayList<Service>,
    ) : RecyclerView.Adapter<selectServiceAdapter.ServiceViewHolder>() {
        class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
            var view = LayoutInflater.from(context).inflate(R.layout.select_service_view_holder, parent, false)
            return ServiceViewHolder(view)
        }

        override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
            var service = serviceList[position]
            holder.itemView.apply {
                selectServiceTitle.setText(service.title)
                serviceProvidersNumber.setText("0")
                Glide.with(this)
                    .load(service.pic)
                    .override(1000, 1000)
                    .placeholder(R.drawable.burger)
                    .into(selectServicePic)
            }
            holder.itemView.setOnClickListener{
                var intent= Intent(context,CreateServiceProviderAccount::class.java)
                intent.putExtra("ServiceName",service.title)
                context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return serviceList.size
        }

    }
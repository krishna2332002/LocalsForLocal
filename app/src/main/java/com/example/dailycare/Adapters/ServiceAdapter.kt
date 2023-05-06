package com.example.dailycare.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dailycare.Activities.ShowServiceProviders
import com.example.dailycare.Models.Service
import com.example.dailycare.R
import kotlinx.android.synthetic.main.service_view_holder.view.*

class ServiceAdapter (
    private var context: Context,
    private var serviceList: ArrayList<Service>,
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {
    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.service_view_holder, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        var service = serviceList[position]
        holder.itemView.apply {
            serviceName.setText(service.title)
            Glide.with(this)
                .load(service.pic)
                .override(1000, 1000)
                .placeholder(R.drawable.burger)
                .into(servicePic)
        }
        holder.itemView.setOnClickListener {
            var intent= Intent(context,ShowServiceProviders::class.java)
            intent.putExtra("serviceName",service.title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

}
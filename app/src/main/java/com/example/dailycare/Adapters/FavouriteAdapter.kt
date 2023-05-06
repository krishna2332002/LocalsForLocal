package com.example.dailycare.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dailycare.Models.ServiceProvider
import com.example.dailycare.R
import kotlinx.android.synthetic.main.service_provider_view_holder.view.*

class FavouriteAdapter (
    private var context: Context,
    private var serviceProviderList: ArrayList<ServiceProvider>
) : RecyclerView.Adapter<FavouriteAdapter.ServiceViewHolder>() {
    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.service_provider_view_holder, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        var serviceProvider = serviceProviderList[position]
        holder.itemView.apply {
            serviceProviderName.setText(serviceProvider.serviceProviderName)
            serviceName.setText(serviceProvider.serviceName)
            Glide.with(this)
                .load(serviceProvider.servicerPic)
                .override(1000, 1000)
                .placeholder(R.drawable.profile)
                .into(serviceProviderPic)
        }
    }

    override fun getItemCount(): Int {
        return serviceProviderList.size
    }

}
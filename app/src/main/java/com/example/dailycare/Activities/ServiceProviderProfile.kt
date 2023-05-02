package com.example.dailycare.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.dailycare.Models.ServiceProvider
import com.example.dailycare.R
import com.example.dailycare.databinding.ActivityServiceProviderProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class ServiceProviderProfile : AppCompatActivity() {
    private lateinit var binding:ActivityServiceProviderProfileBinding
    private lateinit var fauth:FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var serviceProvider: ServiceProvider
    private lateinit var providerUid:String
    private val requestCall = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityServiceProviderProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fauth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference
        providerUid= intent.getStringExtra("ServiceProviderUid").toString()
        database.child("ServiceProvidersList").child(providerUid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    serviceProvider= snapshot.getValue(ServiceProvider::class.java)!!
                    binding.serviceProviderName.setText(serviceProvider.serviceProviderName)
                    binding.serviceName.setText(serviceProvider.serviceName)
                    binding.serviceProviderId.setText(serviceProvider.serviceProviderUid)
                    Glide.with(this@ServiceProviderProfile)
                        .load(serviceProvider.servicerPic)
                        .override(1000, 1000)
                        .placeholder(R.drawable.profile)
                        .into(binding.serviceProviderPic)
                    binding.completedWorkNo.setText(serviceProvider.completedWoks.toString())
                    binding.customersFavouriteNo.setText(serviceProvider.customersFavourite.toString())
                    binding.descriptionOfServiceProvider.setText(serviceProvider.description.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun makePhoneCall() {
        val number: String = fauth.currentUser!!.phoneNumber.toString()

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val dial = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
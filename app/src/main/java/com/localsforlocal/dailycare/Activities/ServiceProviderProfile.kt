package com.localsforlocal.dailycare.Activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.localsforlocal.dailycare.Models.ServiceProvider
import com.localsforlocal.dailycare.Models.User
import com.localsforlocal.dailycare.databinding.ActivityServiceProviderProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_service_provider_profile.view.*

class ServiceProviderProfile : AppCompatActivity() {
    private lateinit var binding:ActivityServiceProviderProfileBinding
    private lateinit var fauth:FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var serviceProvider: ServiceProvider
    private lateinit var user: User
    private lateinit var providerUid:String
    private val requestCall = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityServiceProviderProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fauth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference
        database2=FirebaseDatabase.getInstance().reference
        providerUid= intent.getStringExtra("ServiceProviderUid").toString()
        database.child("ServiceProvidersList").child(providerUid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    serviceProvider= snapshot.getValue(ServiceProvider::class.java)!!
                    binding.serviceProviderName.setText(serviceProvider.serviceProviderName)
                    binding.serviceName.setText(serviceProvider.serviceName)
                    binding.serviceProviderId.setText("Experience: ${serviceProvider.experience} Years")
                    Glide.with(this@ServiceProviderProfile)
                        .load(serviceProvider.servicerPic)
                        .override(1000, 1000)
                        .placeholder(com.localsforlocal.dailycare.R.drawable.profile)
                        .into(binding.serviceProviderPic)

                    binding.completedWorkNo.setText(serviceProvider.completedWoks.toString())
                    binding.customersFavouriteNo.setText(serviceProvider.customersFavourite.toString())
                    binding.descriptionOfServiceProvider.setText(serviceProvider.description.toString())
                    if(fauth.currentUser!!.uid==serviceProvider.serviceProviderUid) {
                        binding.messageServiceProvider.visibility= View.GONE
                        binding.callServiceProvider.visibility=View.GONE
                        binding.addToFavouriteServiceProvider.visibility=View.GONE
                        binding.removeFavouriteServiceProvider.visibility=View.GONE
                        binding.agreementServiceProvider.visibility=View.GONE
                        binding.workDoneServiceProvider.visibility=View.GONE
                    }
                    else{
                        binding.messageServiceProvider.visibility= View.VISIBLE
                        binding.callServiceProvider.visibility=View.VISIBLE
                        binding.agreementServiceProvider.visibility=View.VISIBLE
                        binding.workDoneServiceProvider.visibility=View.VISIBLE
                        binding.messageServiceProvider.setOnClickListener {
                            var intent=Intent(this@ServiceProviderProfile,ChatActivity::class.java)
                            intent.putExtra("receiverUid",serviceProvider.serviceProviderUid)
                            intent.putExtra("receiverName",serviceProvider.serviceProviderName)
                            intent.putExtra("receiverPic",serviceProvider.servicerPic)
                            startActivity(intent)
                        }
                        binding.callServiceProvider.setOnClickListener {
                            Toast.makeText(this@ServiceProviderProfile, serviceProvider.servicerPhone.toString(), Toast.LENGTH_SHORT).show()
                            makePhoneCall(serviceProvider.servicerPhone.toString())
                        }
                        binding.agreementServiceProvider.setOnClickListener {
                            Toast.makeText(this@ServiceProviderProfile, "Working on agreement", Toast.LENGTH_SHORT).show()
                        }
                        binding.workDoneServiceProvider.setOnClickListener {
                            database.child("ServiceProvidersList").child(providerUid).child("completedWoks").setValue((serviceProvider.completedWoks+1)).addOnSuccessListener {
                                Toast.makeText(this@ServiceProviderProfile,"Work Done Succesfully added",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        if(providerUid==fauth.currentUser!!.uid){
            binding.addToFavouriteServiceProvider.visibility=View.GONE
            binding.removeFavouriteServiceProvider.visibility=View.GONE
            binding.agreementServiceProvider.visibility=View.GONE
            binding.workDoneServiceProvider.visibility=View.GONE
        }else{
            binding.agreementServiceProvider.visibility=View.VISIBLE
            binding.workDoneServiceProvider.visibility=View.VISIBLE
            database2.child("Profile").child(fauth.currentUser!!.uid).child("Favourites").child(providerUid).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        binding.addToFavouriteServiceProvider.visibility=View.GONE
                        binding.removeFavouriteServiceProvider.visibility=View.VISIBLE
                        binding.removeFavouriteServiceProvider.setOnClickListener {
                            database2.child("Profile").child(fauth.currentUser!!.uid).child("Favourites").child(providerUid).setValue(null).addOnSuccessListener {
                                    Toast.makeText(this@ServiceProviderProfile,"Removed From Favourites",Toast.LENGTH_SHORT).show()
                                    binding.addToFavouriteServiceProvider.visibility=View.VISIBLE
                                    binding.removeFavouriteServiceProvider.visibility=View.GONE
                            }
                        }
                    }else{
                        binding.addToFavouriteServiceProvider.visibility=View.VISIBLE
                        binding.removeFavouriteServiceProvider.visibility=View.GONE
                        binding.addToFavouriteServiceProvider.setOnClickListener {
                            database2.child("Profile").child(fauth.currentUser!!.uid).child("Favourites").child(providerUid).setValue(serviceProvider).addOnSuccessListener {
                                database2.child("ServiceProvidersList").child(providerUid).child("customersFavourite").setValue((serviceProvider.customersFavourite+1)).addOnSuccessListener {
                                    Toast.makeText(this@ServiceProviderProfile,"Added To Favourites",Toast.LENGTH_SHORT).show()
                                    binding.addToFavouriteServiceProvider.visibility=View.GONE
                                    binding.removeFavouriteServiceProvider.visibility=View.VISIBLE
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


    }
    private fun makePhoneCall(number:String) {
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
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package com.example.dailycare.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailycare.Adapters.ServiceProvidersAdapter
import com.example.dailycare.Models.ServiceProvider
import com.example.dailycare.Models.User
import com.example.dailycare.R
import com.example.dailycare.databinding.ActivityServiceProviderProfileBinding
import com.example.dailycare.databinding.ActivityShowServiceProvidersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ShowServiceProviders : AppCompatActivity() {
    private lateinit var binding:ActivityShowServiceProvidersBinding
    private lateinit var fauth:FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var serviceProviderList:ArrayList<ServiceProvider>
    private lateinit var serviceProviderAdapter: ServiceProvidersAdapter
    private lateinit var linearLayoutManager2: LinearLayoutManager
    private lateinit var pinCode:String
    private lateinit var user: User
    private lateinit var serviceName:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityShowServiceProvidersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        serviceName=intent.getStringExtra("serviceName").toString()
        binding.textView2.setText("Best $serviceName in your area")
        fauth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference
        database.child("Profile").child(fauth.currentUser!!.uid).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    user= snapshot.getValue(User::class.java)!!
                    pinCode=user.pinCode.toString()
                    serviceProviderView()
                }
                else
                {
                    var intent= Intent(this@ShowServiceProviders, SetUpProfile::class.java)
                    startActivity(intent)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun serviceProviderView() {
        serviceProviderList= arrayListOf()
        linearLayoutManager2 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.servicesView.layoutManager = linearLayoutManager2
        serviceProviderAdapter=ServiceProvidersAdapter(this,serviceProviderList)
        binding.servicesView.adapter = serviceProviderAdapter
        Toast.makeText(this, pinCode, Toast.LENGTH_SHORT).show()
        database.child("Services").child(serviceName).child(pinCode).addValueEventListener(object :
            ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                serviceProviderList.clear()
                for (dataSnapshot in snapshot.children) {
                    val rest = dataSnapshot.getValue(ServiceProvider::class.java)
                    if (rest != null) {
                        serviceProviderList.add(rest)
                    }
                }
                if(serviceProviderList.size==0){
                    binding.noDataFoundImg.visibility=View.VISIBLE
                    binding.noDataFoundText.visibility=View.VISIBLE
                    binding.servicesView.visibility=View.GONE
                }
                else
                {
                    binding.noDataFoundImg.visibility=View.GONE
                    binding.noDataFoundText.visibility=View.GONE
                    binding.servicesView.visibility=View.VISIBLE
                }
                serviceProviderAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}
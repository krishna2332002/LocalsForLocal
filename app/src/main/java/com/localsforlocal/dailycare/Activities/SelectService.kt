package com.localsforlocal.dailycare.Activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.localsforlocal.dailycare.Adapters.selectServiceAdapter
import com.localsforlocal.dailycare.Models.Service
import com.localsforlocal.dailycare.databinding.ActivitySelectServiceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SelectService : AppCompatActivity() {
    private lateinit var binding:ActivitySelectServiceBinding
    private lateinit var fauth:FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var serviceList:ArrayList<Service>
    private lateinit var linearLayoutManager:LinearLayoutManager
    private lateinit var selectServiceAdapter: selectServiceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySelectServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fauth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference
        serviceList= arrayListOf()
        linearLayoutManager = GridLayoutManager(this,2)
        binding.selectServicesView.layoutManager = linearLayoutManager
        selectServiceAdapter= selectServiceAdapter(this,serviceList)
        binding.selectServicesView.adapter = selectServiceAdapter
        database.child("Services").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                serviceList.clear()
                for (dataSnapshot in snapshot.children) {
                    val rest = dataSnapshot.getValue(Service::class.java)
                    if (rest != null) {
                        serviceList.add(rest)
                    }
                }
                selectServiceAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
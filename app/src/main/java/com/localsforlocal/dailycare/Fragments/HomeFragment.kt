package com.localsforlocal.dailycare.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.localsforlocal.dailycare.Activities.SetUpProfile
import com.localsforlocal.dailycare.Adapters.ServiceAdapter
import com.localsforlocal.dailycare.Adapters.ServiceProvidersAdapter
import com.localsforlocal.dailycare.Models.Service
import com.localsforlocal.dailycare.Models.ServiceProvider
import com.localsforlocal.dailycare.Models.User
import com.localsforlocal.dailycare.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.localsforlocal.dailycare.R

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var serviceList: ArrayList<Service>
    private lateinit var serviceAdapter: ServiceAdapter
    private lateinit var serviceProviderList: ArrayList<ServiceProvider>
    private lateinit var serviceProviderAdapter: ServiceProvidersAdapter
    private lateinit var linearLayoutManager2: LinearLayoutManager
    private lateinit var fauth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var user: User
    private lateinit var pinCode:String
    private lateinit var dialog: ProgressDialog
    private lateinit var timer: CountDownTimer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        fauth=FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().reference
        dialog= ProgressDialog(context)
        dialog.setMessage("Loading Services")
        dialog.show()
        database.child("Profile").child(fauth.currentUser!!.uid).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    user= snapshot.getValue(User::class.java)!!
                    pinCode=user.pinCode.toString()
                    binding.userName.setText("Hi "+user.name)
                    serviceProviderView()
                    Glide.with(requireContext())
                        .load(user.image)
                        .override(1000, 1000)
                        .placeholder(R.drawable.profile)
                        .into(binding.userProfile)
                }
                else
                {
                    dialog.cancel()
                    val intent= Intent(context, SetUpProfile::class.java)
                    startActivity(intent)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        servicesView()
        return binding.root
    }
    private fun servicesView() {
        serviceList= arrayListOf()
        linearLayoutManager = GridLayoutManager(context,3)
        binding.servicesView.layoutManager = linearLayoutManager
        serviceAdapter=ServiceAdapter(requireContext(),serviceList)
        binding.servicesView.adapter = serviceAdapter
        database.child("Services").addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                serviceList.clear()
                for (dataSnapshot in snapshot.children) {
                    val rest = dataSnapshot.getValue(Service::class.java)
                    if (rest != null) {
                        serviceList.add(rest)
                    }
                }
                serviceAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    private fun serviceProviderView() {
        serviceProviderList= arrayListOf()
        linearLayoutManager2 = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.servieProviderView.layoutManager = linearLayoutManager2
        serviceProviderAdapter=ServiceProvidersAdapter(requireContext(),serviceProviderList)
        binding.servieProviderView.adapter = serviceProviderAdapter
        Toast.makeText(context, pinCode, Toast.LENGTH_SHORT).show()
        database.child("ServicesProvider").child(pinCode.toString()).addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                serviceProviderList.clear()
                for (dataSnapshot in snapshot.children) {
                    val rest = dataSnapshot.getValue(ServiceProvider::class.java)
                    if (rest != null) {
                        serviceProviderList.add(rest)
                    }
                }
                dialog.cancel()
                serviceProviderAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
        private fun dialogShow(){
        dialog= ProgressDialog(context)
        dialog.setMessage("Loading Ranks...")
        dialog.setCancelable(false)
        dialog.show()
        if(dialog.isShowing){
            timer = object: CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }
                override fun onFinish() {
//                    dialog.dismiss()
//                    Toast.makeText(context,"Network slow down ",Toast.LENGTH_SHORT).show()
//                    binding.tryAgain.setVisibility(View.VISIBLE)
//                    binding.tryAgain.setOnClickListener {
//                        dialog.show()
//                        fetchData()
//                        binding.tryAgain.setVisibility(View.INVISIBLE)
//                    }
                }
            }
            timer.start()
        }
    }

}
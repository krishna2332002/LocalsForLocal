package com.example.dailycare.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dailycare.Activities.SelectService
import com.example.dailycare.Activities.SetUpProfile
import com.example.dailycare.Activities.SplashActivity
import com.example.dailycare.Models.User
import com.example.dailycare.R
import com.example.dailycare.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.service_provider_view_holder.view.*

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding:FragmentProfileBinding
    private lateinit var fauth:FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        fauth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference

        database.child("Profile").child(fauth.currentUser!!.uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    user= snapshot.getValue(User::class.java)!!
                    binding.userName.setText(user.name)
                    binding.userPhoneNo.setText(user.phoneNo)
                    binding.userAddress.setText(user.address)
                    binding.userId.setText(user.myId)
                    Glide.with(requireContext())
                        .load(user.pic)
                        .override(1000, 1000)
                        .placeholder(R.drawable.profile)
                        .into(binding.userPic)
                    binding.logoutAccount.setOnClickListener {
                        fauth.signOut()
                        var intent=Intent(requireContext(),SplashActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent)
                        activity!!.finish()
                    }
                }
                else
                {
                    var intent= Intent(context, SetUpProfile::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        database.child("ServiceProvidersList").child(fauth.currentUser!!.uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    binding.openMyServiceAccount.visibility=View.VISIBLE
                    binding.createMyServiceAccount.visibility=View.GONE
                }else{
                    binding.openMyServiceAccount.visibility=View.GONE
                    binding.createMyServiceAccount.visibility=View.VISIBLE
                    binding.createMyServiceAccount.setOnClickListener {
                        var intent=Intent(requireContext(),SelectService::class.java)
                        startActivity(intent)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        return binding.root
    }
}
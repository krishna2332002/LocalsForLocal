package com.localsforlocal.dailycare.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.localsforlocal.dailycare.Activities.*
import com.localsforlocal.dailycare.Models.User
import com.localsforlocal.dailycare.databinding.FragmentProfileBinding
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
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    user= snapshot.getValue(User::class.java)!!
                    binding.userName.setText(user.name)
                    binding.userPhoneNo.setText(user.phoneNo)
                    binding.userAddress.setText("${user.address}, ${user.district}, ${user.state}")
                    binding.userAge.setText("${user.age} years old")
                    Glide.with(requireContext())
                        .load(user.image)
                        .override(1000, 1000)
                        .placeholder(com.localsforlocal.dailycare.R.drawable.profile)
                        .into(binding.userPic)
                    binding.logoutAccount.setOnClickListener {
                        fauth.signOut()
                        var intent=Intent(requireContext(),SplashActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent)
                        activity!!.finish()
                    }
                    binding.faqs.setOnClickListener {
                        var intent=Intent(context,FaqsActivity::class.java)
                        startActivity(intent)
                    }
                    binding.shareApp.setOnClickListener {
                        val appLink = "https://play.google.com/store/apps/details?id=com.example.dailycare"
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, appLink)
                        startActivity(Intent.createChooser(intent, "Share link via"))
                    }
                    binding.editMyAccount.setOnClickListener {
                        var intent=Intent(context,SetUpProfile::class.java)
                        intent.putExtra("Value","1")
                        startActivity(intent)
                    }
                    binding.openMyServiceAccount.setOnClickListener {
                        var intent=Intent(context,ServiceProviderProfile::class.java)
                        intent.putExtra("ServiceProviderUid",fauth.currentUser!!.uid)
                        startActivity(intent)
                    }
                    binding.termsAndCondition.setOnClickListener {
                        var intent=Intent(context,TermsActivity::class.java)
                        startActivity(intent)
                    }
                    binding.completeKyc.setOnClickListener {
                        Toast.makeText(context, "Kyc is under development", Toast.LENGTH_SHORT).show()
                    }
                    binding.uploadJobs.setOnClickListener {
                        var intent=Intent(context,PostSelect::class.java)
                        startActivity(intent)
                    }
                }
                else
                {
                    var intent= Intent(context, SetUpProfile::class.java)
                    intent.putExtra("Value","1")
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
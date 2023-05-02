package com.example.dailycare.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dailycare.Models.Service
import com.example.dailycare.Models.ServiceProvider
import com.example.dailycare.Models.User
import com.example.dailycare.R
import com.example.dailycare.databinding.ActivityCreateServiceProviderAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CreateServiceProviderAccount : AppCompatActivity() {
    private lateinit var binding:ActivityCreateServiceProviderAccountBinding
    private lateinit var fauth:FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var serviceName:String
    private  var user:User?=null
    private lateinit var service: Service
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCreateServiceProviderAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        serviceName=intent.getStringExtra("ServiceName").toString()
        fauth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference
        database.child("Services").child(serviceName).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    service=snapshot.getValue(Service::class.java)!!
                    binding.selectServiceTitle.setText(service.title)
                    Glide.with(this@CreateServiceProviderAccount)
                        .load(service.pic)
                        .override(1000, 1000)
                        .placeholder(R.drawable.profile)
                        .into(binding.selectServicePic)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        database.child("Profile").child(fauth.currentUser!!.uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    user=snapshot.getValue(User::class.java)!!
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        binding.updateBtn.setOnClickListener {
            if(binding.userAlternativePhone.toString()=="")
            {
                Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_LONG).show()
                binding.userAlternativePhone.setError("Phone Number is needed!") //To set error on TextView
                binding.userAlternativePhone.requestFocus()
            }
            else if(binding.userDescription.toString()=="")
            {
                Toast.makeText(this, "Enter Description", Toast.LENGTH_LONG).show()
                binding.userDescription.setError("Description is needed!") //To set error on TextView
                binding.userDescription.requestFocus()
            }
            else if(binding.userExperience.toString()=="")
            {
                Toast.makeText(this, "Enter Experience", Toast.LENGTH_LONG).show()
                binding.userExperience.setError("Experience is required!") //To set error on TextView
                binding.userExperience.requestFocus()
            }
            else if(user==null){
                Toast.makeText(this, "Fetching User Info", Toast.LENGTH_LONG).show()
            }
            else{
                val provider:ServiceProvider= ServiceProvider(user!!.name,serviceName,fauth.currentUser!!.uid,0,0,user!!
                    .pic,user!!.phoneNo,
                    binding.userAlternativePhone.text.toString(),
                    binding.userDescription.text.toString(),
                    binding.userExperience.text.toString())

                database.child("ServiceProvidersList").child(fauth.currentUser!!.uid).setValue(provider).addOnSuccessListener {
                    database.child("ServicesProvider").child(user!!.pinCode.toString()).child(fauth.currentUser!!.uid).setValue(provider)
                       .addOnSuccessListener {
                          Toast.makeText(this,"Congratulations you have been become a service Provider ", Toast.LENGTH_SHORT).show()
                           var intent= Intent(this,MainActivity::class.java)
                           startActivity(intent)
                           finish()
                    }
                }
            }

        }

    }
}
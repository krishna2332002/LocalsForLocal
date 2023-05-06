package com.example.dailycare.Fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.dailycare.Activities.SetUpProfile
import com.example.dailycare.Activities.SplashActivity
import com.example.dailycare.Adapters.FriendAdapter
import com.example.dailycare.Adapters.NotificationAdapter
import com.example.dailycare.Models.Notification
import com.example.dailycare.Models.User
import com.example.dailycare.R
import com.example.dailycare.databinding.FragmentChatBinding
import com.example.dailycare.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding: FragmentChatBinding
    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var usersUid:String
    private lateinit var fauth: FirebaseAuth
    private lateinit var friendList:ArrayList<String>
    private lateinit var friendAdapter: FriendAdapter
    private var x:Long=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChatBinding.inflate(inflater,container,false)
        database= FirebaseDatabase.getInstance().reference
        database2= FirebaseDatabase.getInstance().reference
        fauth= FirebaseAuth.getInstance()
        usersUid=fauth.currentUser!!.uid
        friendList= arrayListOf()
        friendAdapter= FriendAdapter(requireContext(),friendList)
        binding.friendListView.layoutManager= LinearLayoutManager(context)
        binding.friendListView.adapter=friendAdapter
        database.child("Profile").child(fauth.currentUser!!.uid).child("friend")
            .orderByChild("lastMsgTime").addValueEventListener(object : ValueEventListener {
                @SuppressLint("ResourceAsColor")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.childrenCount.equals(null)) {
                        binding.noDataFoundImg.visibility=View.VISIBLE
                        binding.noDataFoundText.visibility=View.VISIBLE
                        binding.friendListView.visibility=View.GONE
                    } else {
                        friendList.clear()
                        for (frSnapshot in snapshot.children) {
                            frSnapshot.key?.let { friendList.add(it) }
                        }
                        binding.noDataFoundImg.visibility=View.GONE
                        binding.noDataFoundText.visibility=View.GONE
                        binding.friendListView.visibility=View.VISIBLE
                        friendList.reverse()
                        friendAdapter.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        return binding.root
    }
}
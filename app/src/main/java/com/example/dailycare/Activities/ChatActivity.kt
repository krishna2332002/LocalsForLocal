package com.example.dailycare.Activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dailycare.Adapters.messageAdapter
import com.example.dailycare.Models.Message
import com.example.dailycare.R
import com.example.dailycare.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var database: DatabaseReference
    private lateinit var database2: DatabaseReference
    private lateinit var fauth: FirebaseAuth
    private lateinit var senderId: String
    private lateinit var receiverId: String
    private lateinit var receiverName: String
    private lateinit var senderRoom: String
    private lateinit var receiverRoom: String
    private lateinit var messages: ArrayList<Message>
    private lateinit var phone: String
    private var value: Int = 0
    private val requestCall = 1
    private lateinit var message: Message
    private lateinit var receiverPic:String
    private lateinit var msgAdapter:messageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fauth=FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance().reference
        database2= FirebaseDatabase.getInstance().reference
        senderId=fauth.currentUser!!.uid
        receiverId=intent.getStringExtra("receiverUid").toString()
        receiverName=intent.getStringExtra("receiverName").toString()
        receiverPic=intent.getStringExtra("receiverPic").toString()
        senderRoom = senderId + receiverId
        receiverRoom = receiverId + senderId
        messages = arrayListOf()
        var linearLayoutmanager = LinearLayoutManager(this)
        linearLayoutmanager.stackFromEnd = true
        binding.recyclerView2.layoutManager = linearLayoutmanager
        msgAdapter = messageAdapter(this, messages, senderRoom, receiverRoom)
        binding.recyclerView2.adapter = msgAdapter

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = null
            Glide.with(this@ChatActivity)
                .load(receiverPic)
                .override(120, 120)
                .circleCrop()
                .placeholder(R.drawable.profile)
                .into(binding.userChatImage)
            binding.title.text = receiverName
        }
        database2.child("Profile").child(senderId)
            .child("friend").child(receiverId).child("messages").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (dataSnapshot in snapshot.children) {
                    var message = dataSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }
                msgAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "No Message sent", Toast.LENGTH_SHORT).show()
            }
        })

        binding.msgSentBtn.setOnClickListener {
            var newMsg = binding.msgBox.text
            if (newMsg == null) {
                Toast.makeText(this, "Type something", Toast.LENGTH_SHORT).show()
            } else {
                binding.msgBox.setText("")
                var rand = database.push().key.toString()
                message = Message(
                    newMsg.toString().trim(),
                    senderId,
                    rand
                )
                if (rand != null) {
                    database.child("Profile").child(senderId).child("friend").child(receiverId).child("messages").child(rand).setValue(message).addOnSuccessListener {
                        database.child("Profile").child(receiverId).child("friend").child(senderId).child("messages").child(rand).setValue(message).addOnSuccessListener {
                            database.child("Profile").child(senderId).child("friend").child(receiverId).child("lastMsgTime").setValue(System.currentTimeMillis().toString()).addOnSuccessListener {
                                database.child("Profile").child(receiverId).child("friend").child(senderId).child("lastMsgTime").setValue(System.currentTimeMillis().toString()).addOnSuccessListener {
                                    Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
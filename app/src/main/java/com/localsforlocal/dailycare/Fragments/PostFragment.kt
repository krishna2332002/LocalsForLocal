package com.localsforlocal.dailycare.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.localsforlocal.dailycare.Activities.SetUpProfile
import com.localsforlocal.dailycare.Adapters.FriendAdapter
import com.localsforlocal.dailycare.Adapters.NotificationAdapter
import com.localsforlocal.dailycare.Adapters.PostAdapter
import com.localsforlocal.dailycare.Models.Notification
import com.localsforlocal.dailycare.Models.Post
import com.localsforlocal.dailycare.Models.User
import com.localsforlocal.dailycare.R
import com.localsforlocal.dailycare.databinding.FragmentNotificationsBinding
import com.localsforlocal.dailycare.databinding.FragmentPostBinding

class PostFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding:FragmentPostBinding
    private lateinit var database: DatabaseReference
    private lateinit var usersUid:String
    private lateinit var fauth: FirebaseAuth
    private lateinit var postList:ArrayList<Post>
    private lateinit var postAdapter: PostAdapter
    private lateinit var user: User
    private lateinit var pinCode:String
    private lateinit var dialog: ProgressDialog
    private lateinit var timer: CountDownTimer
    private var x:Long=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentPostBinding.inflate(inflater,container,false)
        database= FirebaseDatabase.getInstance().reference
        fauth= FirebaseAuth.getInstance()
        usersUid=fauth.currentUser!!.uid
        dialog= ProgressDialog(context)
        dialog.setMessage("Loading Post")
        dialog.show()
        database.child("Profile").child(fauth.currentUser!!.uid).addListenerForSingleValueEvent(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    user= snapshot.getValue(User::class.java)!!
                    pinCode=user.pinCode.toString()
                    postView()
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


        return binding.root
    }

    private fun postView() {
        postList= arrayListOf()
        postAdapter=PostAdapter(requireContext(),postList)
        binding.postsView.layoutManager= LinearLayoutManager(context)
        binding.postsView.adapter=postAdapter
        database.child("Post").child(pinCode)
            .limitToLast(20).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    dialog.cancel()
                    if(snapshot.childrenCount==x){
                        binding.noDataFoundImg.visibility=View.VISIBLE
                        binding.noDataFoundText.visibility=View.VISIBLE
                        binding.postsView.visibility=View.GONE
                    }
                    else{
                        postList.clear()
                        for(postSnapshot in snapshot.children){
                            var post=postSnapshot.getValue(Post::class.java)
                            if(post!=null){
                                postList.add(post)
                            }
                        }
                        binding.noDataFoundImg.visibility=View.GONE
                        binding.noDataFoundText.visibility=View.GONE
                        binding.postsView.visibility=View.VISIBLE
                        postList.reverse()
                        postAdapter.notifyDataSetChanged()

                    }
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
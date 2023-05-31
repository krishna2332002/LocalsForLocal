package com.localsforlocal.dailycare.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.localsforlocal.dailycare.Adapters.NotificationAdapter
import com.localsforlocal.dailycare.Models.Notification
import com.localsforlocal.dailycare.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NotificationsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var database: DatabaseReference
    private lateinit var usersUid:String
    private lateinit var fauth:FirebaseAuth
    private lateinit var notificationsList:ArrayList<Notification>
    private lateinit var notificationAdapter: NotificationAdapter
    private var x:Long=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNotificationsBinding.inflate(inflater,container,false)
        database= FirebaseDatabase.getInstance().reference
        fauth= FirebaseAuth.getInstance()
        usersUid=fauth.currentUser!!.uid
        notificationsList= arrayListOf()
        notificationAdapter= NotificationAdapter(requireContext(),notificationsList,usersUid)
        binding.notificationListView.layoutManager= LinearLayoutManager(context)
        binding.notificationListView.adapter=notificationAdapter
        database.child("Profile").child(usersUid).child("Notifications")
            .limitToLast(20).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.childrenCount==x){
                        binding.noDataFoundImg.visibility=View.VISIBLE
                        binding.noDataFoundText.visibility=View.VISIBLE
                        binding.notificationListView.visibility=View.GONE
                    }
                    else{
                        notificationsList.clear()
                        for(notificationSnapshot in snapshot.children){
                            var notification=notificationSnapshot.getValue(Notification::class.java)
                            if(notification!=null){
                                notificationsList.add(notification)
                            }
                        }
                        binding.noDataFoundImg.visibility=View.GONE
                        binding.noDataFoundText.visibility=View.GONE
                        binding.notificationListView.visibility=View.VISIBLE
                        notificationsList.reverse()
                        notificationAdapter.notifyDataSetChanged()

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        return binding.root
    }


}
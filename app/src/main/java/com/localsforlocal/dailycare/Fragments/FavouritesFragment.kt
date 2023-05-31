package com.localsforlocal.dailycare.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.localsforlocal.dailycare.Adapters.FavouriteAdapter
import com.localsforlocal.dailycare.Models.ServiceProvider
import com.localsforlocal.dailycare.databinding.FragmentFavouritesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavouritesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var database: DatabaseReference
    private lateinit var usersUid:String
    private lateinit var fauth: FirebaseAuth
    private lateinit var favouritesList:ArrayList<ServiceProvider>
    private lateinit var favouriteAdapter: FavouriteAdapter
    private var x:Long=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFavouritesBinding.inflate(inflater,container,false)
        database= FirebaseDatabase.getInstance().reference
        fauth= FirebaseAuth.getInstance()
        usersUid=fauth.currentUser!!.uid
        favouritesList= arrayListOf()
        favouriteAdapter= FavouriteAdapter(requireContext(),favouritesList)
        binding.favouritesListView.layoutManager= LinearLayoutManager(context)
        binding.favouritesListView.adapter=favouriteAdapter
        database.child("Profile").child(usersUid).child("Favourites").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.childrenCount==x){
                        binding.noDataFoundImg.visibility=View.VISIBLE
                        binding.noDataFoundText.visibility=View.VISIBLE
                        binding.favouritesListView.visibility=View.GONE
                    }
                    else{
                        favouritesList.clear()
                        for(notificationSnapshot in snapshot.children){
                            var favourite=notificationSnapshot.getValue(ServiceProvider::class.java)
                            if(favourite!=null){
                                favouritesList.add(favourite)
                            }
                        }
                        binding.noDataFoundImg.visibility=View.GONE
                        binding.noDataFoundText.visibility=View.GONE
                        binding.favouritesListView.visibility=View.VISIBLE
                        favouriteAdapter.notifyDataSetChanged()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        return binding.root
    }

}
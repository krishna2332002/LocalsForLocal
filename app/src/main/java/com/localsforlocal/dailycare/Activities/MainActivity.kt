package com.localsforlocal.dailycare.Activities

import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.localsforlocal.dailycare.Fragments.*
import com.localsforlocal.dailycare.R
import com.localsforlocal.dailycare.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var fauth: FirebaseAuth
    private var currentFragment: Int = 1
    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var favouriteFragment: FavouritesFragment
    private lateinit var notificationFragment: NotificationsFragment
    private lateinit var postFragment: PostFragment
    private lateinit var chatFragment: ChatFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference()
        fauth = FirebaseAuth.getInstance()
        val sharedPref = getSharedPreferences("User Info",
            FirebaseMessagingService.MODE_PRIVATE
        )
        homeFragment = HomeFragment()
        profileFragment = ProfileFragment()
        notificationFragment=NotificationsFragment()
        favouriteFragment=FavouritesFragment()
        chatFragment=ChatFragment()
        postFragment= PostFragment()
        setCurrentFragment(homeFragment)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    setCurrentFragment(homeFragment)
                    currentFragment = 1
                }
                R.id.profile -> {
                    setCurrentFragment(profileFragment)
                    currentFragment = 3
                }
                R.id.favorite->{
                    setCurrentFragment(favouriteFragment)
                    currentFragment=3
                }
                R.id.chat->{
                    setCurrentFragment(chatFragment)
                    currentFragment=4
                }
                R.id.post->{
                    setCurrentFragment(postFragment)
                    currentFragment=5
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.firstFragment, fragment)
            commit()
        }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.first, menu)
        return true
    }
}
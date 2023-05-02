package com.example.dailycare.Activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dailycare.Fragments.FavouritesFragment
import com.example.dailycare.Fragments.HomeFragment
import com.example.dailycare.Fragments.NotificationsFragment
import com.example.dailycare.Fragments.ProfileFragment
import com.example.dailycare.R
import com.example.dailycare.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var fauth: FirebaseAuth
    private var currentFragment: Int = 1
    private lateinit var homeFragment: HomeFragment
    private lateinit var profileFragment: ProfileFragment
    private lateinit var favouriteFragment: FavouritesFragment
    private lateinit var notificationFragment: NotificationsFragment
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
                R.id.notifications->{
                    setCurrentFragment(notificationFragment)
                    currentFragment=4
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
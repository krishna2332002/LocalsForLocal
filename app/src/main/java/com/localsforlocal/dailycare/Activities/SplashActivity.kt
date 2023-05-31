package com.localsforlocal.dailycare.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.localsforlocal.dailycare.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var fauth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fauth= FirebaseAuth.getInstance()
        binding.startBtn.setOnClickListener {
            if(fauth.currentUser==null)
            {
                var intent= Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                var intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
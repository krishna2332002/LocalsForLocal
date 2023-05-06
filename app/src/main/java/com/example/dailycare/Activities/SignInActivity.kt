package com.example.dailycare.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.dailycare.databinding.ActivitySignInBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var fauth: FirebaseAuth
    private var verificationId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fauth = FirebaseAuth.getInstance()
        binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sendOtpBtn.setOnClickListener {
            if (binding.userPhone.text.equals(null)) {
                Toast.makeText(this, "Enter Valid Phone number!!", Toast.LENGTH_SHORT).show()
            } else {
                var phone = "+91" + binding.userPhone.text.toString()
                sendVerificationCode(phone)
            }
        }
        binding.signupBtn.setOnClickListener {
            if (binding.phoneOtp.text.equals(null)) {
                Toast.makeText(this, "Enter Valid Otp!!", Toast.LENGTH_SHORT).show()
            } else {
                verifyCode(binding.phoneOtp.text.toString())
            }
        }
    }
    private fun sendVerificationCode(phone: String) {
        var options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(fauth).setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallBack).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    binding.phoneOtp.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@SignInActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        fauth.signInWithCredential(credential)
            .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    val i = Intent(this, SetUpProfile::class.java)
                    i.putExtra("Value","0");
                    startActivity(i)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        task.exception!!.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}
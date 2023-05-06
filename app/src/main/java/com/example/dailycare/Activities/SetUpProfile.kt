package com.example.dailycare.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dailycare.R
import com.example.dailycare.databinding.ActivitySetUpProfileBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.ref.Cleaner.create
import java.util.*

class SetUpProfile : AppCompatActivity() {
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
    private lateinit var binding: ActivitySetUpProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var fAuth: FirebaseAuth
    private var selectedImage: Uri? = null
    private lateinit var reference: StorageReference
    private lateinit var user: Map<String, String>
    private var selectedDistrict: String? = null
    private  var selectedState:kotlin.String? = null
    private lateinit var tvStateSpinner: TextView
    private lateinit var tvDistrictSpinner : TextView
    private lateinit var stateSpinner: Spinner
    private  lateinit var districtSpinner: Spinner
    private lateinit var stateAdapter: ArrayAdapter<CharSequence>
    private lateinit var districtAdapter: ArrayAdapter<CharSequence?>
    private var pincode:String=""
    private lateinit var value:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetUpProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fAuth = FirebaseAuth.getInstance()
        value=intent.getStringExtra("Value").toString()
        database = FirebaseDatabase.getInstance().getReference()
        var userUid=fAuth.currentUser!!.uid
        requestLocationPermission()
        getLocation()
        database.child("Profile").child(userUid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists() && value=="0")
                {
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    stateSpinner = findViewById(R.id.spinner_indian_states)
                    stateAdapter = ArrayAdapter.createFromResource(this@SetUpProfile, R.array.array_indian_states,R.layout.spinner_layout)
                    stateAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    stateSpinner.setAdapter(stateAdapter)
                    stateSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            //Define City Spinner but we will populate the options through the selected state
                            districtSpinner = findViewById<Spinner>(R.id.spinner_indian_districts)
                            selectedState =
                                stateSpinner.getSelectedItem().toString() //Obtain the selected State
                            val parentID = parent.id
                            if (parentID == R.id.spinner_indian_states) {
                                when (selectedState) {
                                    "Select Your State" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_default_districts, R.layout.spinner_layout
                                    )
                                    "Andhra Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_andhra_pradesh_districts, R.layout.spinner_layout
                                    )
                                    "Arunachal Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_arunachal_pradesh_districts, R.layout.spinner_layout
                                    )
                                    "Assam" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_assam_districts, R.layout.spinner_layout
                                    )
                                    "Bihar" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_bihar_districts, R.layout.spinner_layout
                                    )
                                    "Chhattisgarh" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_chhattisgarh_districts, R.layout.spinner_layout
                                    )
                                    "Goa" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_goa_districts, R.layout.spinner_layout
                                    )
                                    "Gujarat" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_gujarat_districts, R.layout.spinner_layout
                                    )
                                    "Haryana" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_haryana_districts, R.layout.spinner_layout
                                    )
                                    "Himachal Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_himachal_pradesh_districts, R.layout.spinner_layout
                                    )
                                    "Jharkhand" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_jharkhand_districts, R.layout.spinner_layout
                                    )
                                    "Karnataka" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_karnataka_districts, R.layout.spinner_layout
                                    )
                                    "Kerala" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_kerala_districts, R.layout.spinner_layout
                                    )
                                    "Madhya Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_madhya_pradesh_districts, R.layout.spinner_layout
                                    )
                                    "Maharashtra" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_maharashtra_districts, R.layout.spinner_layout
                                    )
                                    "Manipur" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_manipur_districts, R.layout.spinner_layout
                                    )
                                    "Meghalaya" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_meghalaya_districts, R.layout.spinner_layout
                                    )
                                    "Mizoram" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_mizoram_districts, R.layout.spinner_layout
                                    )
                                    "Nagaland" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_nagaland_districts, R.layout.spinner_layout
                                    )
                                    "Odisha" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_odisha_districts, R.layout.spinner_layout
                                    )
                                    "Punjab" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_punjab_districts, R.layout.spinner_layout
                                    )
                                    "Rajasthan" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_rajasthan_districts, R.layout.spinner_layout
                                    )
                                    "Sikkim" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_sikkim_districts, R.layout.spinner_layout
                                    )
                                    "Tamil Nadu" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                       R.array.array_tamil_nadu_districts, R.layout.spinner_layout
                                    )
                                    "Telangana" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_telangana_districts, R.layout.spinner_layout
                                    )
                                    "Tripura" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_tripura_districts, R.layout.spinner_layout
                                    )
                                    "Uttar Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_uttar_pradesh_districts, R.layout.spinner_layout
                                    )
                                    "Uttarakhand" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_uttarakhand_districts,R.layout.spinner_layout
                                    )
                                    "West Bengal" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_west_bengal_districts,R.layout.spinner_layout
                                    )
                                    "Andaman and Nicobar Islands" -> districtAdapter =
                                        ArrayAdapter.createFromResource(
                                            parent.context,
                                            R.array.array_andaman_nicobar_districts, R.layout.spinner_layout
                                        )
                                    "Chandigarh" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_chandigarh_districts, R.layout.spinner_layout
                                    )
                                    "Dadra and Nagar Haveli" -> districtAdapter =
                                        ArrayAdapter.createFromResource(
                                            parent.context,
                                            R.array.array_dadra_nagar_haveli_districts, R.layout.spinner_layout
                                        )
                                    "Daman and Diu" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_daman_diu_districts, R.layout.spinner_layout
                                    )
                                    "Delhi" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_delhi_districts, R.layout.spinner_layout
                                    )
                                    "Jammu and Kashmir" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_jammu_kashmir_districts, R.layout.spinner_layout
                                    )
                                    "Lakshadweep" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_lakshadweep_districts, R.layout.spinner_layout
                                    )
                                    "Ladakh" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_ladakh_districts,R.layout.spinner_layout
                                    )
                                    "Puducherry" -> districtAdapter = ArrayAdapter.createFromResource(
                                        parent.context,
                                        R.array.array_puducherry_districts, R.layout.spinner_layout
                                    )
                                    else -> {
                                    }
                                }
                                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Specify the layout to use when the list of choices appears
                                districtSpinner.setAdapter(districtAdapter) //Populate the list of Districts in respect of the State selected

                                //To obtain the selected District from the spinner
                                districtSpinner.setOnItemSelectedListener(object :
                                    AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        selectedDistrict = districtSpinner.getSelectedItem().toString()
                                    }

                                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                                })
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    })
                    tvStateSpinner = findViewById(R.id.textView_indian_states)
                    tvDistrictSpinner = findViewById<TextView>(R.id.textView_indian_districts)
                    binding.userImage.setOnClickListener {
                        launchGallery()
                    }
                    binding.updateBtn.setOnClickListener {
                        Toast.makeText(this@SetUpProfile, pincode, Toast.LENGTH_SHORT).show()
                        if(!LiveLocationPermissionCheck()){
                            Toast.makeText(this@SetUpProfile, "Enable your Live location Permission", Toast.LENGTH_SHORT).show()
                            requestLocationPermission()
                        }else if(binding.userAddress.text.toString().isEmpty())
                        {
                            getLocation()
                            Toast.makeText(this@SetUpProfile,"Enter Your Pincode",Toast.LENGTH_SHORT).show()
                            binding.userAddress.setError("Pincode is required")
                            return@setOnClickListener
                        }
                        else if (selectedState == "Select Your State") {
                            Toast.makeText(this@SetUpProfile, "Please select your state from the list", Toast.LENGTH_LONG).show()
                            tvStateSpinner.setError("State is required!") //To set error on TextView
                            tvStateSpinner.requestFocus()
                            return@setOnClickListener
                        } else if (selectedDistrict == "Select Your District") { Toast.makeText(this@SetUpProfile, "Please select your district from the list", Toast.LENGTH_LONG).show()
                            tvDistrictSpinner.setError("District is required!")
                            tvDistrictSpinner.requestFocus()
                            tvStateSpinner.setError(null)
                            return@setOnClickListener
                        }
                        else if (selectedImage != null) {
                        tvStateSpinner.setError(null)
                        tvDistrictSpinner.setError(null)
                            pincode=binding.userAddress.text.toString().trim()
                            reference = FirebaseStorage.getInstance().getReference().child("Profile").child(fAuth.currentUser!!.uid)
                            reference.putFile(selectedImage!!).addOnCompleteListener(
                                OnCompleteListener {
                                if (it.isSuccessful) {
                                    reference.downloadUrl.addOnSuccessListener {
                                        var imageUpload=it.toString()
                                        user = mapOf<String, String>(
                                            "usersUid" to userUid.toString().trim(),
                                            "address" to binding.userAddress.text.toString().trim(),
                                            "age" to binding.userAge.text.toString().trim(),
                                            "name" to binding.userName.text.toString().trim(),
                                            "image" to it.toString(),
                                            "state" to selectedState.toString(),
                                            "district" to selectedDistrict.toString(),
                                            "pinCode" to "a$pincode",
                                            "phoneNo" to fAuth.currentUser!!.phoneNumber.toString()
                                        )
                                        database.child("Profile").child(userUid).updateChildren(user).addOnSuccessListener {
                                            var sharedPref = getSharedPreferences("User Info", MODE_PRIVATE)
                                            var editor = sharedPref.edit()
                                            editor.putString("Name",binding.userName.text.toString().trim() )
                                            editor.putString("UserUid",userUid )
                                            editor.putString("State",selectedState.toString() )
                                            editor.putString("District",selectedDistrict.toString() )
                                            editor.putString("Image", imageUpload)
                                            editor.apply()
                                            var intent = Intent(this@SetUpProfile, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }.addOnFailureListener {
                                            Toast.makeText(this@SetUpProfile, "Error In Updating ", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            })
                        } else {
                            tvStateSpinner.setError(null)
                            tvDistrictSpinner.setError(null)
                            pincode=binding.userAddress.text.toString().trim()
                            user = mapOf<String, String>(
                                "usersUid" to userUid.toString().trim(),
                                "address" to binding.userAddress.text.toString().trim(),
                                "age" to binding.userAge.text.toString().trim(),
                                "name" to binding.userName.text.toString().trim(),
                                "state" to selectedState.toString(),
                                "district" to selectedDistrict.toString(),
                                "pinCode" to "a$pincode",
                                "phoneNo" to fAuth.currentUser!!.phoneNumber.toString()
                            )
                            database.child("Profile").child(userUid.toString().trim()).updateChildren(user).addOnSuccessListener {
                                Toast.makeText(this@SetUpProfile, "Status Uploaded", Toast.LENGTH_SHORT).show()
                                var sharedPref = getSharedPreferences("User Info", Context.MODE_PRIVATE)
                                var editor = sharedPref.edit()
                                editor.putString("Name",binding.userName.text.toString().trim() )
                                editor.putString("UserUid",userUid)
                                editor.putString("State",selectedState.toString())
                                editor.putString("District",selectedDistrict.toString())
                                editor.apply()
                                var intent = Intent(this@SetUpProfile, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (data.data != null) {
                binding.userImage.setImageURI(data.data)
                selectedImage = data.data
            }
        }
    }

    private fun launchGallery() {
        var intent = Intent()
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, 100)
    }
    private fun requestLocationPermission() {
        if (!LiveLocationPermissionCheck()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLocation()
        }
    }
    private fun LiveLocationPermissionCheck():Boolean{
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun getLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude
                val longitude = location.longitude
                val address = getAddress(latitude, longitude)
                pincode = address?.postalCode.toString()
            }
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }
    private fun getAddress(latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses!!.isNotEmpty()) {
            return addresses[0]
        }
        return null
    }
}
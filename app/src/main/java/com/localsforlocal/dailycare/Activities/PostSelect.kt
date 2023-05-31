package com.localsforlocal.dailycare.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.localsforlocal.dailycare.Models.Post
import com.localsforlocal.dailycare.Models.Profile
import com.localsforlocal.dailycare.Models.User
import com.localsforlocal.dailycare.R
import com.localsforlocal.dailycare.databinding.ActivityPostSelectBinding

class PostSelect : AppCompatActivity() {
    private lateinit var binding: ActivityPostSelectBinding
    private lateinit var database: DatabaseReference
    private lateinit var fAuth: FirebaseAuth
    private var selectedDistrict: String? = null
    private  var selectedState:String? = null
    private  var selectedJob:String? = null
    private  lateinit var districtSpinner: Spinner
    private lateinit var jobSpinner: Spinner
    private lateinit var stateAdapter: ArrayAdapter<CharSequence>
    private lateinit var districtAdapter: ArrayAdapter<CharSequence>
    private lateinit var jobAdapter: ArrayAdapter<CharSequence>
    private var user: User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPostSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database= FirebaseDatabase.getInstance().reference
        fAuth= FirebaseAuth.getInstance()
        database.child("Profile").child(fAuth.currentUser!!.uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists())
                {
                    Toast.makeText(this@PostSelect, "Your Profile cant be loaded", Toast.LENGTH_SHORT).show()
                }else{
                    user=snapshot.getValue(User::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        jobSpinner = findViewById(R.id.spinnerJobType)
        jobAdapter = ArrayAdapter.createFromResource(
            this, R.array.array_type_of_job, R.layout.spinner_layout
        )
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jobSpinner.setAdapter(jobAdapter)
        jobSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {

                selectedJob = jobSpinner.getSelectedItem().toString() //Obtain the selected State

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        stateAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.array_indian_states,R.layout.spinner_layout
        )

        // Specify the layout to use when the list of choices appear
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerIndianState.setAdapter(stateAdapter) //Set the adapter to the spinner to populate the State Spinner

        //When any item of the stateSpinner uis selected
        binding.spinnerIndianState.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Define City Spinner but we will populate the options through the selected state
                selectedState =
                    binding.spinnerIndianState.getSelectedItem().toString() //Obtain the selected State
                districtSpinner = findViewById(R.id.spinnerIndianDistrict)
                val parentID = parent.id
                if (parentID == R.id.spinnerIndianState) {
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
                            R.array.array_haryana_districts,R.layout.spinner_layout
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
                            R.array.array_kerala_districts,R.layout.spinner_layout
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
                            R.array.array_uttarakhand_districts, R.layout.spinner_layout
                        )
                        "West Bengal" -> districtAdapter = ArrayAdapter.createFromResource(
                            parent.context,
                            R.array.array_west_bengal_districts, R.layout.spinner_layout
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
                            R.array.array_daman_diu_districts,R.layout.spinner_layout
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
                            R.array.array_ladakh_districts, R.layout.spinner_layout
                        )
                        "Puducherry" -> districtAdapter = ArrayAdapter.createFromResource(
                            parent.context,
                            R.array.array_puducherry_districts,R.layout.spinner_layout
                        )
                        else -> {
                            Toast.makeText(this@PostSelect, "Problem", Toast.LENGTH_SHORT).show()
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
        binding.finalPostSendBtn.setOnClickListener{
            if (selectedState == "Select Your State") {
                Toast.makeText(
                    this,
                    "Please select your state from the list",
                    Toast.LENGTH_LONG
                ).show()
                binding.textViewIndianState.setError("State is required!") //To set error on TextView
                binding.textViewIndianState.requestFocus()
                return@setOnClickListener
            } else if (selectedDistrict == "Select Your District") {
                Toast.makeText(
                    this,
                    "Please select your district from the list",
                    Toast.LENGTH_LONG
                ).show()
                binding.textViewIndianDistrict.setError("District is required!")
                binding.textViewIndianDistrict.requestFocus()
                binding.textViewIndianDistrict.setError(null)
                return@setOnClickListener
            } else if(selectedJob=="Select Job type"){
                Toast.makeText(
                    this,
                    "Please select Job Type",
                    Toast.LENGTH_LONG
                ).show()
                binding.textViewSelectJobType.setError("Job type is required!") //To set error on TextView
                binding.textViewSelectJobType.requestFocus()
                return@setOnClickListener
            }
            else if(binding.expectedSalary.text.equals(null))
            {
                Toast.makeText(
                    this,
                    "Please Enter Expected Salary",
                    Toast.LENGTH_LONG
                ).show()
                binding.expectedSalary.setError("Enter Expected Salary") //To set error on TextView
                binding.expectedSalary.requestFocus()
                return@setOnClickListener
            }
            else if(binding.jobAddress.text.equals(null))
            {
                Toast.makeText(
                    this,
                    "Please Enter address",
                    Toast.LENGTH_LONG
                ).show()
                binding.jobAddress.setError("Enter Job address") //To set error on TextView
                binding.jobAddress.requestFocus()
                return@setOnClickListener
            }
            else if(binding.jobTimings.text.equals(null))
            {
                Toast.makeText(
                    this,
                    "Please Enter Timings of job",
                    Toast.LENGTH_LONG
                ).show()
                binding.jobTimings.setError("Enter Timings of Job") //To set error on TextView
                binding.jobTimings.requestFocus()
                return@setOnClickListener
            }
            else if(binding.noOfVacancies.text.equals(null))
            {
                Toast.makeText(
                    this,
                    "Please Enter Expected Salary",
                    Toast.LENGTH_LONG
                ).show()
                binding.noOfVacancies.setError("Enter Number of vacancies") //To set error on TextView
                binding.noOfVacancies.requestFocus()
                return@setOnClickListener
            }
            if(user!=null){
                var rand = database.push().key.toString()
                var post= Post(binding.noOfVacancies.text.toString(),binding.jobAddress.text.toString(),binding.expectedSalary.text.toString(),binding.postCaption.text.toString(),
                    fAuth.currentUser!!.uid,user!!.name,user!!.image,selectedDistrict,selectedState,selectedJob,rand,0,"",binding.jobTimings.toString())
                database.child("Post").child(user!!.pinCode.toString()).child(rand).setValue(post).addOnSuccessListener {
                    Toast.makeText(this, "Service Vacancies Uploaded", Toast.LENGTH_SHORT).show()
                    var intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
            }

        }
    }
}
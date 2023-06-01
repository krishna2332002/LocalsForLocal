package com.localsforlocal.dailycare.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.localsforlocal.dailycare.Models.Notification
import com.localsforlocal.dailycare.Models.Post
import com.localsforlocal.dailycare.R
import kotlinx.android.synthetic.main.activity_post_select.view.*
import kotlinx.android.synthetic.main.notification.view.*
import kotlinx.android.synthetic.main.post.view.*
import kotlinx.android.synthetic.main.service_provider_view_holder.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat

class PostAdapter (private var context: Context, private var postList:ArrayList<Post>)
    : RecyclerView.Adapter<PostAdapter.PostViewHolder>(){
    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        var view= LayoutInflater.from(context).inflate(R.layout.post,parent,false)
        return PostViewHolder(view)
    }
    private var date: DateFormat = SimpleDateFormat("hh:mm a")
    private var date2: DateFormat = SimpleDateFormat("dd MMM")
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    var userImage:String?=null
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        var post=postList[position]
        holder.itemView.apply {
            postUploaderName.setText(post.jobProviderName)
            postVacancies.setText("Vacancies: "+post.jobVacancies)
            postDescription.setText("Description: "+post.description)
            postJobLocation.setText("Address: "+post.jobAddress)
            postExpectedSalary.setText("Quotation : "+post.expectedSalary)
            if(post.expectedSalary!=null)
                postExpectedSalary.setText("Quotation : "+post.expectedSalary)
            else
                postExpectedSalary.setText("Quotation : "+"Not Provided")
            if(post.jobTime!=null)
                postJobTimings.setText("Timing: "+post.jobTime)
            else
                postJobTimings.setText("Timing: "+"Not Provided")

            jobAppliedBtn.setText(post.applies.toString())
            Glide.with(this)
                .load(post.jobProviderImage)
                .override(1000, 1000)
                .placeholder(R.drawable.profile)
                .into(postUploaderImage)
        }
    }


    override fun getItemCount(): Int {
        return postList.size
    }
}
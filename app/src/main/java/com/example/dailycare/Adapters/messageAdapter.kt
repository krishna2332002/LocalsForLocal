package com.example.dailycare.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.dailycare.Models.Message
import com.example.dailycare.R
import com.example.dailycare.databinding.ItemRecieveBinding
import com.example.dailycare.databinding.ItemSentBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_recieve.view.*
import kotlinx.android.synthetic.main.item_sent.view.*

class messageAdapter(
    private var context: Context,
    var msgList: ArrayList<Message>,
    private var senderRoom: String,
    private var receiverRoom: String
) : Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            var view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false)
            return sentViewHolder(view)
        } else {
            var view = LayoutInflater.from(context).inflate(R.layout.item_recieve, parent, false)
            return recieveViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = msgList[position]
        if (holder::class.java == sentViewHolder::class.java) {
            val viewHolder = holder as sentViewHolder
            holder.sentMessage.text=currentMessage.msg
        } else {
            val viewHolder = holder as recieveViewHolder
            holder.recieveMessage.text = currentMessage.msg

        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        var message = msgList[position]
        if (FirebaseAuth.getInstance().currentUser!!.uid.equals(message.senderId)) {
            return 1
        } else {
            return 2
        }
    }

    class sentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.msgSentText
        var binding = ItemSentBinding.bind(itemView)
    }

    class recieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recieveMessage = itemView.msgRecieveBox
        var binding = ItemRecieveBinding.bind(itemView)
    }
}
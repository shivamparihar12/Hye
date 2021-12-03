package com.example.hye.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hye.R
import com.example.hye.model.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    lateinit var messageList: ArrayList<Message>
    val ITEM_SENT = 1;
    val ITEM_RECIEVED = 2;

    constructor(context: Context, messageList: ArrayList<Message>) : this() {
        this.context = context
        this.messageList = messageList
    }

    class sentViewHolder : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView)
        val message=itemView.findViewById<TextView>(R.id.send_message)

    }

    class recieveViewHolder : RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView)
        val message=itemView.findViewById<TextView>(R.id.recieve_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==ITEM_SENT){
            val view :View=LayoutInflater.from(context).inflate(R.layout.item_send,parent,false)
            return sentViewHolder(view)
        }else{
            val view:View=LayoutInflater.from(context).inflate(R.layout.item_recieve,parent,false)
            return recieveViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message :Message=messageList.get(position)
        if (holder.javaClass==sentViewHolder::class){
            val viewHolder:sentViewHolder = holder as sentViewHolder
            viewHolder.message.text=message.toString()
        }
        else{
            val viewHolder:recieveViewHolder= holder as recieveViewHolder
            viewHolder.message.text=message.toString()
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val message: Message = messageList.get(position)
        if (FirebaseAuth.getInstance().uid.equals(message.senderID)) {
            return ITEM_SENT
        } else return ITEM_RECIEVED
        return super.getItemViewType(position)
    }
}
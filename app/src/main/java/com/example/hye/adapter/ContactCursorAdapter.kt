package com.example.hye.adapter

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hye.R
import com.example.hye.activity.ChatActivity

class ContactCursorAdapter(context: Context) : BaseCursorAdapter<ContactCursorAdapter.ContactViewHolder>(),
    View.OnClickListener {
    private val context: Context=context

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.user_name)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, cursor: Cursor) {
        val mColumnIndexName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        val contactName = cursor.getString(mColumnIndexName)
        holder.userName.text = contactName
        holder.userName.setOnClickListener {
            val i = Intent(context, ChatActivity::class.java)
            i.putExtra("username",contactName)
            context.startActivity(i)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.contacts_list_item, parent, false)
        return ContactViewHolder(view as ViewGroup)
    }

    override fun onClick(p0: View?) {

    }

}
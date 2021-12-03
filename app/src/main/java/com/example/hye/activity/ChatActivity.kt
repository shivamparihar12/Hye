package com.example.hye.activity

import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.hye.R
import com.google.firebase.auth.FirebaseAuth

class ChatActivity : AppCompatActivity() {
    lateinit var username: String
    lateinit var recieveID: String
    lateinit var senderRoom: String
    lateinit var recieverRoom: String
    lateinit var senderID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        username = intent.getStringExtra("username").toString()
        recieveID = intent.getStringExtra("uid").toString()
        senderID = FirebaseAuth.getInstance().uid.toString()

        senderRoom = senderID + recieveID
        recieverRoom = recieveID + senderID

        supportActionBar?.title = username

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
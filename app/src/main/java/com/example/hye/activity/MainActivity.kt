package com.example.hye.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hye.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    private lateinit var fab:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        fab=findViewById(R.id.floatingActionButton)

        if (mAuth.currentUser==null) run {
            val intent = Intent(this, PhoneVerification::class.java)
            startActivity(intent)
        }

        fab.setOnClickListener {
            val intent=Intent(this, ContactList::class.java)
            startActivity(intent)
        }


    }
}
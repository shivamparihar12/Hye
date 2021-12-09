package com.example.hye.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.hye.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {


    private lateinit var fab:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab=findViewById(R.id.floatingActionButton)



        fab.setOnClickListener {
            val intent=Intent(this, ContactList::class.java)
            startActivity(intent)
        }
        val textView=findViewById<TextView>(R.id.hello)
        textView.setOnClickListener {
            startActivity(Intent(this,BuildProfileActivity::class.java))
        }


    }
}
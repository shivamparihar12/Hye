package com.example.hye.activity

import android.Manifest
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

import android.content.Context

import androidx.core.app.ActivityCompat

import android.os.Build
import android.content.pm.PackageManager
import com.example.hye.adapter.ContactCursorAdapter
import com.example.hye.R


class ContactList : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private val REQUEST = 112
    lateinit var recyclerView: RecyclerView
    lateinit var contactCursorAdapter: ContactCursorAdapter
    lateinit var layoutManager: LinearLayoutManager
    private val CONTACTS_LOADER_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        checkContactPermission()
        recyclerView = findViewById(R.id.contact_RecyclerView)
        layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        contactCursorAdapter = ContactCursorAdapter(this)
        recyclerView.adapter = contactCursorAdapter

        //contactCursorAdapter.onc

    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val uri: Uri = ContactsContract.Contacts.CONTENT_URI //contacts uri
        val projection = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        val selection = null
        val selectionArgs = arrayOf<String>()
        val sortOder = null

        return CursorLoader(
            applicationContext,
            uri,
            projection,
            selection,
            selectionArgs,
            sortOder
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        contactCursorAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        contactCursorAdapter.swapCursor(null)
    }

    fun checkContactPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            val PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS)
            if (!hasPermissions(applicationContext, *PERMISSIONS)) {
                ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    REQUEST
                )
            } else {
                LoaderManager.getInstance(this).initLoader(CONTACTS_LOADER_ID, null, this)
            }
        } else {
            LoaderManager.getInstance(this).initLoader(CONTACTS_LOADER_ID, null, this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoaderManager.getInstance(this).initLoader(CONTACTS_LOADER_ID, null, this)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "The app was not allowed to read your contact",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

}
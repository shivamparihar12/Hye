package com.example.hye.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Path
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.hye.R
import com.example.hye.databinding.ActivityBuildProfileBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.math.roundToInt
import kotlin.math.sqrt


class BuildProfileActivity : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    private lateinit var redBitmap: Bitmap
    private lateinit var binding: ActivityBuildProfileBinding

    companion object {
        private const val REQUEST_CODE_IMAGE = 2314
        private const val STORAGE_PERMISSION_CODE = 211
        private const val TAG = "BuildProfileActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBuildProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.profileImageView.setOnClickListener {
            requestStoragePermission()
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_CODE_IMAGE)
        }


        binding.saveProfile.setOnClickListener {
            binding.progressBar.visibility=View.VISIBLE
            uploadUserDetails()
        }
    }

    private fun uploadUserDetails() {
        val userName: String = binding.username.text.toString()
        val userID = FirebaseAuth.getInstance().uid.toString()
        val filePathName: String = "Users/" + userID + "ProfilePicture"
        val baos = ByteArrayOutputStream()
        redBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val downsizedImageBytes: ByteArray = baos.toByteArray()

//        val lnth = bitmap.byteCount
//        val dst: ByteBuffer = ByteBuffer.allocate(lnth)
//        bitmap.copyPixelsToBuffer(dst)
//        val data: ByteArray = dst.array()

        val storageReference = FirebaseStorage.getInstance().reference.child(filePathName)
        storageReference.putBytes(downsizedImageBytes)
            .addOnCompleteListener {
                Log.d(TAG, "Entering onSuccessLinstner of data putting bytes method")
                val uriTask: Task<Uri> = storageReference.downloadUrl
                while (!uriTask.isSuccessful) {
                    val downloadUri = uriTask.result.toString()
                    if (uriTask.isSuccessful) {
                        val hashMap: HashMap<Any, String> = HashMap()
                        hashMap["userName"] = userName
                        hashMap["UserID"] = userID
                        hashMap["downloadUri"] = downloadUri

                        val documentRef = FirebaseFirestore.getInstance().collection("Users")
                            .document(userID + "userDetail")
                        documentRef.set(hashMap).addOnSuccessListener {

                            binding.progressBar.visibility=View.GONE
                            Toast.makeText(this, "Profile Details Saved", Toast.LENGTH_SHORT).show()
                            val i = Intent(this, MainActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(i)
                            finish()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            }
                    }
                    else Toast.makeText(this,"Unsuccessful, PleaseTryAgain",Toast.LENGTH_SHORT).show()

                }

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            val uri = data.data!!
            binding.profileImageView.setImageURI(uri)
            imageToBitmap(uri)

        }
    }

    private fun imageToBitmap(imageUri: Uri) {
        //Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        redBitmap = reduceBitmapSize(bitmap, 240000)
    }

    private fun reduceBitmapSize(bitmap: Bitmap, MAX_SIZE: Int): Bitmap {
        var ratioSq: Double
        val bitmapHeight: Int = bitmap.height
        val bitmapWidth: Int = bitmap.width
        ratioSq = (bitmapHeight * bitmapWidth).toDouble()
        ratioSq /= MAX_SIZE
        if (ratioSq <= 1) {
            return bitmap
        }
        val ratio = sqrt(ratioSq)
        Log.d("imageResizer", "ratio: $ratio")
        val reqHeight = (bitmapHeight / ratio).roundToInt()
        val reqWidth = (bitmapWidth / ratio).roundToInt()
        return Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true)
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 211) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
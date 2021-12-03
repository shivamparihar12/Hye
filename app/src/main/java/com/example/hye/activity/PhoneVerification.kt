package com.example.hye.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.hye.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneVerification : AppCompatActivity() {

    companion object {
        private const val TAG = "PhoneVerification"
    }

    private lateinit var mAuth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var getOtpLayout: LinearLayout
    private lateinit var verifyOtpLayout: LinearLayout
    private lateinit var PhoneNoView: EditText
    private lateinit var getOtp: Button
    private lateinit var verify: Button
    private lateinit var phoneNo:String
    private lateinit var otp:EditText
    private val otpFlag = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_verification)

        mAuth = FirebaseAuth.getInstance()


        getOtpLayout = findViewById(R.id.getOtp_layout)
        verifyOtpLayout = findViewById(R.id.verifyOtp_layout)
        PhoneNoView = findViewById(R.id.phoneNo)
        getOtp = findViewById(R.id.get_otp)
        verify=findViewById(R.id.verify_otp)
        otp=findViewById(R.id.otp)



        if (mAuth.currentUser==null) run {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        if (otpFlag == 0) {
            verifyOtpLayout.isEnabled = false
            verifyOtpLayout.visibility = View.INVISIBLE
        }


        getOtp.setOnClickListener {
            phoneNo = "+91"+PhoneNoView.text.toString()
            if (phoneNo.isEmpty()) {
                Toast.makeText(this, "Please Enter Mobile no", Toast.LENGTH_SHORT).show()
            } else {
                getOtpLayout.isEnabled = false
                getOtpLayout.visibility = View.INVISIBLE
                verifyOtpLayout.isEnabled = true
                verifyOtpLayout.visibility = View.VISIBLE

                startPhoneNumberVerification(phoneNo)
            }
        }

        verify.setOnClickListener {
            val otp=otp.text.toString()
            if (otp.isNotEmpty()){
                verifyPhoneNumberWithCode(storedVerificationId,otp)
            }
            else Toast.makeText(this,"Please Enter OTP",Toast.LENGTH_SHORT).show()
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(this@PhoneVerification,"check Your Internet Connection",Toast.LENGTH_SHORT).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI'
                Toast.makeText(this@PhoneVerification,"Verification Failed",Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }

        }


    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }


    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
        // [END verify_with_code]
    }

    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
        if (token != null) {
            optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
// [END resend_verification]

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user

                    val intent=Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser? = mAuth.currentUser) {
    }

}
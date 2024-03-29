package com.example.appchat_kotlin.loginHandle

import android.content.Intent
import android.content.SharedPreferences

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat_kotlin.MainActivity
import com.example.appchat_kotlin.databinding.ActivityLoginBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

import com.google.firebase.messaging.FirebaseMessagingService
import java.util.regex.Pattern


class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val FILE_NAME = "account"

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth


        val sharedPreferences: SharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        val emailStore = sharedPreferences.getString("email", "")
        val passwordStore = sharedPreferences.getString("password", "")

        binding.emailEditText.setText(emailStore)
        binding.passwordEditText.setText(passwordStore)

        setListener()
    }



    private fun setListener() {

        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val email: String = binding.emailEditText.text.toString().trim()
            val password: String = binding.passwordEditText.text.toString().trim()


            if(binding.rememberPassword.isChecked) {
                storageInfoLogin(email, password)
            } else {
                deleteInfoLogin()
            }
            isLoading(true)

            if(isValidSignInDetails(email, password)) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        isLoading(false)
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            binding.emailLayout.error = task.exception?.localizedMessage ?: "Something went wrong"
                            binding.passwordLayout.error = ""
                        }
                    }
            }
        }
    }

    private fun isValidSignInDetails (email:String, password:String) : Boolean {
        return if(email.isEmpty()) {
            binding.emailLayout.error = "Please enter email"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailLayout.error = "Please enter valid email"
            false
        } else if(password.isEmpty()) {
            binding.passwordLayout.error = "Please enter password"
            false
        } else true
    }

    private fun isLoading(isLoading:Boolean) {
        if(isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.textLoginBtn.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.textLoginBtn.visibility = View.VISIBLE
        }
    }


    private fun storageInfoLogin(email: String?, password: String?) {
        val editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

     private fun deleteInfoLogin() {
        val setting = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        setting.edit().clear().apply()
    }
}
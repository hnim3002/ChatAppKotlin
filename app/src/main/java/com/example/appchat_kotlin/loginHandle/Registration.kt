package com.example.appchat_kotlin.loginHandle

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.appchat_kotlin.MainActivity
import com.example.appchat_kotlin.R
import com.example.appchat_kotlin.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setListener()
    }

    private fun setListener() {

        binding.registerBtn.setOnClickListener() {
            val email: String = binding.emailEditText.text.toString().trim()
            val password: String = binding.passwordEditText.text.toString().trim()
            val password2: String = binding.repeatPasswordEditText.text.toString().trim()
            isLoading(true)
            if (isValidSignInDetails(email, password, password2)) {
                auth.createUserWithEmailAndPassword(email, password)
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

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
    }

    private fun isValidSignInDetails (email:String, password:String, rePassword:String) : Boolean {
        return if(email.isEmpty()) {
            binding.emailLayout.error = "Please enter email"
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailLayout.error = "Please enter valid email"
            false
        } else if(password.isEmpty()) {
            binding.passwordLayout.error = "Please enter password"
            false
        } else if(rePassword.isEmpty()) {
            binding.repeatPasswordLayout.error = "Please enter password"
            false
        } else if(password != rePassword) {
            binding.repeatPasswordLayout.error = "Password & confirm password must be the same"
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


}


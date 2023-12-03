package com.example.appchat_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.example.appchat_kotlin.databinding.ActivityLoginBinding
import com.example.appchat_kotlin.databinding.ActivityRegistrationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = Firebase.auth

        binding.registerBtn.setOnClickListener() {
            val email: String =  binding.emailEditText.text.toString().trim()
            val password: String =  binding.passwordEditText.text.toString().trim()
            val password2: String =  binding.repeatPasswordEditText.text.toString().trim()
            binding.progressBar.visibility = View.VISIBLE
            binding.textLoginBtn.visibility = View.GONE


            if(email.isEmpty()) {
                if(password.isEmpty()) {
                    binding.passwordLayout.error = "Please enter password"
                    return@setOnClickListener
                }

                if(password2.isEmpty()) {
                    binding.emailLayout.error = "Please enter email"
                return@setOnClickListener
            }

                binding.repeatPasswordLayout.error = "Please enter password"
                return@setOnClickListener
            }

            if(password == password2) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        binding.progressBar.visibility = View.GONE
                        binding.textLoginBtn.visibility = View.VISIBLE
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
}
package com.samuel.vocaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.samuel.vocaapp.databinding.ActivityLpBinding

class LPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        binding.btnLp.setOnClickListener {
            val email = binding.edtLp.text.toString()
            val edtEmail = binding.edtLp

            if(email.isEmpty()) {
                edtEmail.error = "Email tidak bisa kosong."
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Email tidak valid."
                edtEmail.requestFocus()
                return@setOnClickListener
            }
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "Email reset password telah dikirim.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
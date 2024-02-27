package com.samuel.vocaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.samuel.vocaapp.databinding.ActivityPengumumanBinding

class PengumumanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPengumumanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengumumanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnPengumanBack.setOnClickListener {
            val intent = Intent(this@PengumumanActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
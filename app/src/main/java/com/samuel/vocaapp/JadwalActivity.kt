package com.samuel.vocaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.samuel.vocaapp.databinding.ActivityJadwalBinding

class JadwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this@JadwalActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
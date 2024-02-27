package com.samuel.vocaapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.samuel.vocaapp.databinding.ActivityBkkBinding

class BKKActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBkkBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBkkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnRegristrasiBkk.setOnClickListener {
            openUrl("https://bkk.smk-ananda.sch.id")
        }

        binding.btnBkkBack.setOnClickListener {
            val intent = Intent(this@BKKActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun openUrl(link: String) {

        val uri = Uri.parse(link)
        val inte = Intent(Intent.ACTION_VIEW, uri)
        startActivity(inte)
    }
}
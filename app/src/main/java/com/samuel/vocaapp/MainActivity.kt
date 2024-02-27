package com.samuel.vocaapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.samuel.vocaapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navBar: ChipNavigationBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val textView = findViewById<TextView>(R.id.txt_vocaapp)

        val text = "VOCA APP"
        val spannableString = SpannableString(text)

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#3E8BF3")),
            0, 4,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#203864")),
            5, text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableString

        navBar = findViewById(R.id.navbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navBar.setOnItemSelectedListener { itemId ->
            when (itemId) {
                R.id.beranda -> navController.navigate(R.id.berandaFragment2)
                R.id.tentang -> navController.navigate(R.id.tentangFragment2)
                R.id.kontak -> navController.navigate(R.id.kontakFragment)
                R.id.profil -> navController.navigate(R.id.profilFragment)
            }
        }
    }
}
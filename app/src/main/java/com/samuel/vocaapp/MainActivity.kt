package com.samuel.vocaapp

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.ViewPager
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.samuel.vocaapp.databinding.ActivityMainBinding
import com.samuel.vocaapp.fragment.BerandaFragment
import com.samuel.vocaapp.fragment.KontakFragment
import com.samuel.vocaapp.fragment.ProfilFragment
import com.samuel.vocaapp.fragment.TentangFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager
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

        viewPager = findViewById(R.id.viewPager)
        navBar = findViewById(R.id.navbar)

        val adapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter

        navBar.setOnItemSelectedListener { itemId ->
            when (itemId) {
                R.id.beranda -> viewPager.currentItem = 0
                R.id.tentang -> viewPager.currentItem = 1
                R.id.kontak -> viewPager.currentItem = 2
                R.id.profil -> viewPager.currentItem = 3
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> navBar.setItemSelected(R.id.beranda)
                    1 -> navBar.setItemSelected(R.id.tentang)
                    2 -> navBar.setItemSelected(R.id.kontak)
                    3 -> navBar.setItemSelected(R.id.profil)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> BerandaFragment()
                1 -> TentangFragment()
                2 -> KontakFragment()
                3 -> ProfilFragment()
                else -> throw IllegalArgumentException("Invalid position $position")
            }
        }

        override fun getCount(): Int {
            return 4
        }
    }
}
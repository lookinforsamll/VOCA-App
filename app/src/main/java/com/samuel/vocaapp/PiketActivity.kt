package com.samuel.vocaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.samuel.vocaapp.databinding.ActivityPiketBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PiketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPiketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPiketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnPiketBack.setOnClickListener {
            val intent = Intent(this@PiketActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val textViewHari: TextView = findViewById(R.id.txt_hari)
        val textViewTanggal: TextView = findViewById(R.id.txt_tanggal)

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val dayName = getDayName(dayOfWeek)
        textViewHari.text = dayName

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        textViewTanggal.text = formattedDate

        val textViewGuruPiket1: TextView = findViewById(R.id.txt_gp1)
        val guruPiket1Text = getGuruPiket1(dayOfWeek)
        textViewGuruPiket1.text = guruPiket1Text

        val textViewGuruPiket2: TextView = findViewById(R.id.txt_gp2)
        val guruPiket2Text = getGuruPiket2(dayOfWeek)
        textViewGuruPiket2.text = guruPiket2Text
    }

    private fun getDayName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Minggu"
            Calendar.MONDAY -> "Senin"
            Calendar.TUESDAY -> "Selasa"
            Calendar.WEDNESDAY -> "Rabu"
            Calendar.THURSDAY -> "Kamis"
            Calendar.FRIDAY -> "Jumat"
            Calendar.SATURDAY -> "Sabtu"
            else -> ""
        }
    }

    private fun getGuruPiket1(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "Rosmerlin T., S.Th"
            Calendar.TUESDAY -> "Dra. Dwi Arti Miwiti"
            Calendar.WEDNESDAY -> "Maria Fransisca, S.S"
            Calendar.THURSDAY -> "Dra. Suryati"
            Calendar.FRIDAY -> "Reni Purba, S.Pd"
            else -> "Hari Libur"
        }
    }

    private fun getGuruPiket2(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "Ridho Firdiansyah, A.Md"
            Calendar.TUESDAY -> "Nurhasanah, A.Md"
            Calendar.WEDNESDAY -> "Reza Handitama S."
            Calendar.THURSDAY -> "Vita Yuliyanti, S.Pd"
            Calendar.FRIDAY -> "Rosmerlin T., S.Th"
            else -> "Hari Libur"
        }
    }
}
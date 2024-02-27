package com.samuel.vocaapp

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.samuel.vocaapp.databinding.ActivityKdpBinding

class KDPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKdpBinding

    companion object {
        const val EXTRA_SOURCE_ACTIVITY = "source_activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKdpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val text = "Pengguna yang telah mendaftar atau bergabung dengan Voca App telah setuju dan sepakat mengenai kebijakan privasi yang berlaku dalam aplikasi Voca App.\n\n" +
                "Keamanan Penggunaan\n\n" +
                "Voca App menjamin dan bertanggung atas data dan privasi pengguna yang sudah terdaftar dan tersimpan dalam aplikasi. Data Pengguna akan tersimpan secara rahasia oleh pihak aplikasi.\n\n" +
                "Voca App akan bertanggung jawab apabila terdapat kesalahan ataupun kebocoran data akibat kelalaian dari pihak aplikasi yang merugikan Pengguna.\n\n" +
                "Penggunaan Data\n\n" +
                "Voca App dapat menggunakan data yang telah dimasukkan ke dalam aplikasi seperti :\n\n" +
                "- Nama Pengguna (Username)\n" +
                "- Kata Sandi (Password)\n" +
                "- Notifikasi (Notification)\n" +
                "- Akun Voca App (Account)\n\n" +
                "Informasi data Pengguna dapat digunakan atau dibaca oleh aplikasi Voca App untuk mengumpulkan suatu data informasi.\n\n" +
                "Voca App akan berhenti menyimpan data yang diberikan Pengguna apabila Pengguna telah menghapus akun Voca App. Data Pengguna akan dihilangkan ketika Pengguna telah menghapus atau memberhentikan layanan aplikasi Voca App.\n\n" +
                "Pembaruan Kebijakan\n\n" +
                "Voca App dapat sewaktu-waktu mengubah dan menetapkan ketentuan kebijakan privasi yang baru. Pengguna disarankan membaca dan memahami ketentuan yang berlaku nantinya ketika terdapat perubahan kebijakan."

        val spannableText = SpannableString(text)
        val boldWords = arrayOf("Voca App", "Keamanan Penggunaan", "Penggunaan Data", "Pembaruan Kebijakan")
        for (word in boldWords) {
            var startIndex = 0
            while (startIndex != -1) {
                startIndex = text.indexOf(word, startIndex)
                if (startIndex != -1) {
                    val endIndex = startIndex + word.length
                    spannableText.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableText.setSpan(RelativeSizeSpan(1.2f), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    startIndex = endIndex
                }
            }
        }
        binding.txtKdp.text = spannableText

        binding.btnKdpBack.setOnClickListener {
            handleBack()
            finish()
        }
    }
    private fun handleBack() {
        val sourceActivity = intent.getStringExtra(EXTRA_SOURCE_ACTIVITY)
        val destinationActivity = when (sourceActivity) {
            LoginActivity::class.java.simpleName -> LoginActivity::class.java
            RegisterActivity::class.java.simpleName -> RegisterActivity::class.java

            else -> return
        }
        startActivity(Intent(this@KDPActivity, destinationActivity))
        finish()
    }
}
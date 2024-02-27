package com.samuel.vocaapp

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.samuel.vocaapp.databinding.ActivitySdkBinding

class SDKActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySdkBinding

    companion object {
        const val EXTRA_SOURCE_ACTIVITY = "source_activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySdkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val text = "Selamat Datang di Voca App\n\n" +
                "Syarat dan ketentuan yang ditetapkan di bawah ini mengatur Pengguna layanan dalam menggunakan isi konten yang disediakan oleh Voca App dalam berupa bentuk apapun (Teks ataupun Grafik). Pengguna disarankan untuk memperhatikan ketentuan yang berlaku.\n\n" +
                "Dengan mendaftar atau menggunakan aplikasi Voca App, maka Pengguna dianggap telah membaca dan menyetujui semua isi Syarat dan Ketentuan yang berlaku. Jika Pengguna tidak menyetujui Syarat dan Ketentuan yang berlaku, maka Pengguna tidak diperkenankan untuk menggunakan ataupun mengakses aplikasi Voca App.\n\n" +
                "Peraturan dan Larangan\n\n" +
                "Pengguna dapat menggunakan aplikasi Voca App hanya untuk tujuan yang baik dan mendukung Kegiatan Belajar Mengajar (KBM) di lingkungan sekolah. Pengguna tidak dapat menggunakan aplikasi apabila :\n\n" +
                "1. Menyalahkangunakan informasi yang terdapat di dalam aplikasi Voca App dengan tujuan yang negatif\n" +
                "2. Merugikan pihak atau entitas lain dalam mempergunakan aplikasi Voca App.\n" +
                "3. Menggandakan, menjual, mengeksploitasi ataupun mengubah layanan maupun informasi yang terdapat di dalam aplikasi Voca App.\n\n" +
                "Ketentuan Penggunaan\n\n" +
                "Selama menggunakan aplikasi Voca App, Pengguna diharapkan menggunakan layanan atau informasi dengan menghindari hal :\n\n" +
                "1. Penghinaan atau Ujaran Kebencian\n" +
                "2. Unsur Provokasi\n" +
                "3. Mengandung SARA\n" +
                "4. Bersifat Pornografi\n\n" +
                "Penggunaan Layanan\n\n" +
                "Voca App akan menyediakan berbagai layanan informasi kepada Pengguna. Pengguna dapat memberhentikan layanan penggunaan kapan saja tanpa adanya batas tertentu.\n\n" +
                "Voca App dapat melakukan modifikasi terhadap layanan ataupun informasi seiring berjalannya waktu dan tidak bertanggung jawab kepada pihak ketiga yang merugikan pengguna dalam hal tertentu."

        val spannableText = SpannableString(text)
        val boldWords = arrayOf("Voca App", "Peraturan dan Larangan", "Ketentuan Penggunaan", "Penggunaan Layanan")
        for (word in boldWords) {
            var startIndex = 0
            while (startIndex != -1) {
                startIndex = text.indexOf(word, startIndex)
                if (startIndex != -1) {
                    val endIndex = startIndex + word.length
                    spannableText.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableText.setSpan(RelativeSizeSpan(1.2f), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) // Menjadikan teks lebih besar
                    startIndex = endIndex
                }
            }
        }
        binding.txtSdk.text = spannableText

        binding.btnSdkBack.setOnClickListener {
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
        startActivity(Intent(this@SDKActivity, destinationActivity))
        finish()
    }
}
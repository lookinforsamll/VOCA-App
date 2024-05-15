package com.samuel.vocaapp

import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

open class BaseActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0
    private var backToast: Toast? = null

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast?.cancel()
            super.onBackPressed()
        } else {
            backToast = Toast.makeText(baseContext, "Ketuk kembali lagi untuk keluar dari aplikasi", Toast.LENGTH_SHORT)
            backToast?.show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
package com.samuel.vocaapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.samuel.vocaapp.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        const val EXTRA_SOURCE_ACTIVITY = "source_activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()

        val textView = binding.txtRegister1

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

        val text2 =
            "Dengan masuk ke aplikasi Voca App. Kamu Menyetujui segala Syarat dan Ketentuan dan Kebijakan Privasi Voca App"
        val spannableString2 = SpannableString(text2)

        val termAndConditionStart = text2.indexOf("Syarat dan Ketentuan")
        val termAndConditionEnd = termAndConditionStart + "Syarat dan Ketentuan".length
        val privacyPolicyStart = text2.indexOf("Kebijakan Privasi Voca App")
        val privacyPolicyEnd = privacyPolicyStart + "Kebijakan Privasi Voca App".length

        spannableString2.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startSDKActivity(RegisterActivity::class.java.simpleName)
            }
        }, termAndConditionStart, termAndConditionEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString2.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startKDPActivity(RegisterActivity::class.java.simpleName)
            }
        }, privacyPolicyStart, privacyPolicyEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString2.setSpan(
            StyleSpan(Typeface.BOLD),
            termAndConditionStart, termAndConditionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(
            StyleSpan(Typeface.BOLD),
            privacyPolicyStart, privacyPolicyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString2.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorTermAndCondition)),
            termAndConditionStart, termAndConditionEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString2.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrivacyPolicy)),
            privacyPolicyStart, privacyPolicyEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txtRegister6.text = spannableString2
        binding.txtRegister6.movementMethod = LinkMovementMethod.getInstance()

        binding.txtRLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtRegisterEmail.text.toString()
            val password = binding.edtRegisterPassword.text.toString()
            val confirmPassword = binding.edtRegisterKonfirmasiPassword.text.toString()

            if (!isValidEmail(email)) {
                binding.edtRegisterEmail.error = "Email tidak valid."
                binding.edtRegisterEmail.requestFocus()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                binding.edtRegisterPassword.error = "Password harus terdiri dari minimal 6 karakter."
                binding.edtRegisterPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.edtRegisterKonfirmasiPassword.error = "Password tidak sesuai."
                binding.edtRegisterKonfirmasiPassword.requestFocus()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { registrationTask ->
                    if (registrationTask.isSuccessful) {
                        val currentUser = auth.currentUser

                        val userData = hashMapOf(
                            "Email" to email
                        )
                        val usersRef = database.getReference("users")
                        currentUser?.uid?.let { uid ->
                            usersRef.child(uid).setValue(userData)
                        }
                        val intent = Intent(this, RegisterActivity2::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        binding.edtRegisterEmail.error = "Email sudah terdaftar"
                        binding.edtRegisterEmail.requestFocus()
                    }
                }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun startSDKActivity(sourceActivity: String) {
        val sdkIntent = Intent(this@RegisterActivity, SDKActivity::class.java).apply {
            putExtra(EXTRA_SOURCE_ACTIVITY, sourceActivity)
        }
        startActivity(sdkIntent)
    }

    private fun startKDPActivity(sourceActivity: String) {
        val kdpIntent = Intent(this@RegisterActivity, KDPActivity::class.java).apply {
            putExtra(EXTRA_SOURCE_ACTIVITY, sourceActivity)
        }
        startActivity(kdpIntent)
    }
}
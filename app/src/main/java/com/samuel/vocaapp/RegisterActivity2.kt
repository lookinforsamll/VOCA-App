package com.samuel.vocaapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.samuel.vocaapp.databinding.ActivityRegister2Binding

class RegisterActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityRegister2Binding
    private lateinit var database: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference
    companion object {
        const val EXTRA_SOURCE_ACTIVITY = "source_activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val textView = binding.txtRegister21

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

        val text2 = "Dengan masuk ke aplikasi Voca App. Kamu Menyetujui segala Syarat dan Ketentuan dan Kebijakan Privasi Voca App"
        val spannableString2 = SpannableString(text2)

        val termAndConditionStart = text2.indexOf("Syarat dan Ketentuan")
        val termAndConditionEnd = termAndConditionStart + "Syarat dan Ketentuan".length
        val privacyPolicyStart = text2.indexOf("Kebijakan Privasi Voca App")
        val privacyPolicyEnd = privacyPolicyStart + "Kebijakan Privasi Voca App".length

        spannableString2.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startSDKActivity(RegisterActivity2::class.java.simpleName)
            }
        }, termAndConditionStart, termAndConditionEnd, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString2.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                startKDPActivity(RegisterActivity2::class.java.simpleName)
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

        binding.txtRegister27.text = spannableString2
        binding.txtRegister27.movementMethod = LinkMovementMethod.getInstance()

        val kelasArray = resources.getStringArray(R.array.kelas)

        val kelasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kelasArray)
        kelasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerkelas.adapter = kelasAdapter

        val jurusanArray = resources.getStringArray(R.array.jurusan)

        val jurusanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jurusanArray)
        jurusanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerjurusan.adapter = jurusanAdapter

        val jkArray = resources.getStringArray(R.array.jk)

        val jkAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jkArray)
        jurusanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerjk.adapter = jkAdapter


        var userId: String? = FirebaseAuth.getInstance().currentUser?.uid
        if (userId.isNullOrEmpty()) {
            finish()
            return
        }

        database = FirebaseDatabase.getInstance()
        usersRef = database.getReference("users")

        binding.btnRegister2.setOnClickListener {
            val name = binding.edtRegisterName.text.toString().trim()
            val jk = binding.spinnerjk.selectedItem.toString()
            val kelas = binding.spinnerkelas.selectedItem.toString()
            val jurusan = binding.spinnerjurusan.selectedItem.toString()

            if (name.isEmpty()) {
                binding.edtRegisterName.error = "Nama harus diisi."
                binding.edtRegisterName.requestFocus()
                return@setOnClickListener
            }

            val userData = hashMapOf<String, Any>(
                "Jurusan" to jurusan,
                "Jenis Kelamin" to jk,
                "Kelas" to kelas,
                "Nama Lengkap" to name
            )
            usersRef.child(userId).updateChildren(userData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil Registrasi, Silakhan Login.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun startSDKActivity(sourceActivity: String) {
        val sdkIntent = Intent(this@RegisterActivity2, SDKActivity::class.java).apply {
            putExtra(EXTRA_SOURCE_ACTIVITY, sourceActivity)
        }
        this@RegisterActivity2.startActivity(sdkIntent)
    }

    private fun startKDPActivity(sourceActivity: String) {
        val kdpIntent = Intent(this@RegisterActivity2, KDPActivity::class.java).apply {
            putExtra(EXTRA_SOURCE_ACTIVITY, sourceActivity)
        }
        this@RegisterActivity2.startActivity(kdpIntent)
    }
}
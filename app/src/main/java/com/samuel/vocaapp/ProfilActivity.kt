package com.samuel.vocaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samuel.vocaapp.databinding.ActivityProfilBinding
import com.squareup.picasso.Picasso

class ProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilBinding
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnEpBack.setOnClickListener {
            finish()
        }

        binding.editButton.setOnClickListener{
            val intent = Intent(this@ProfilActivity, EditProfilActivity::class.java)
            startActivity(intent)
            finish()
        }
        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        val usersRef = FirebaseDatabase.getInstance().getReference("users")

        uid?.let {
            usersRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("Nama Lengkap").getValue(String::class.java)
                    val email = snapshot.child("Email").getValue(String::class.java)
                    val jk = snapshot.child("Jenis Kelamin").getValue(String::class.java)
                    val kelas = snapshot.child("Kelas").getValue(String::class.java)
                    val jurusan = snapshot.child("Jurusan").getValue(String::class.java)
                    val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

                    binding.titleName.text = name
                    binding.txtKelas.text = kelas
                    binding.txtJurusan.text = jurusan
                    binding.userName.text = name
                    binding.userEmail.text = email
                    binding.userJeniskelamin.text = jk
                    binding.userKelas.text = kelas
                    binding.userJurusan.text = jurusan
                    profileImageUrl?.let {
                        Picasso.get().load(it).into(binding.profileImg)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}
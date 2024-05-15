package com.samuel.vocaapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.samuel.vocaapp.databinding.ActivityEditProfilBinding
import com.squareup.picasso.Picasso
import com.yalantis.ucrop.UCrop
import java.io.File

class EditProfilActivity : BaseActivity() {
    private lateinit var binding: ActivityEditProfilBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userId: String
    private lateinit var storageReference: StorageReference
    private lateinit var getContent: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = Firebase.auth
        database = Firebase.database.reference
        storageReference = FirebaseStorage.getInstance().reference
        userId = auth.currentUser!!.uid

        getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let { handleImageSelectionResult(it) }
            }
        }

        binding.btnEppBack.setOnClickListener {
            val intent = Intent(this@EditProfilActivity, ProfilActivity::class.java)
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
                    val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

                    binding.userName.text = name?.toEditable()
                    binding.userEmail.text = email?.toEditable()

                    profileImageUrl?.let {
                        Picasso.get().load(it).into(binding.profileImg)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        val kelasArray = resources.getStringArray(R.array.kelas)
        val kelasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kelasArray)
        kelasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.userKelas.adapter = kelasAdapter

        val jurusanArray = resources.getStringArray(R.array.jurusan)
        val jurusanAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jurusanArray)
        jurusanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.userJurusan.adapter = jurusanAdapter

        val jkArray = resources.getStringArray(R.array.jk)
        val jkAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jkArray)
        jkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.userJeniskelamin.adapter = jkAdapter

        binding.btnGantipp.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            getContent.launch(intent)
        }

        binding.editButton.setOnClickListener {
            val newName = binding.userName.text.toString().trim()
            val newEmail = binding.userEmail.text.toString().trim()
            val newKelas = binding.userKelas.selectedItem.toString()
            val newJurusan = binding.userJurusan.selectedItem.toString()
            val newJenisKelamin = binding.userJeniskelamin.selectedItem.toString()

            if (newName.isEmpty()) {
                binding.userName.error = "Nama harus diisi."
                binding.userName.requestFocus()
                return@setOnClickListener
            }

            if (newEmail.isEmpty()) {
                binding.userEmail.error = "Email harus diisi."
                binding.userEmail.requestFocus()
                return@setOnClickListener
            }

            val updatedData = hashMapOf(
                "Nama Lengkap" to newName,
                "Email" to newEmail,
                "Kelas" to newKelas,
                "Jurusan" to newJurusan,
                "Jenis Kelamin" to newJenisKelamin
            )

            database.child("users").child(userId).updateChildren(updatedData as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil Disimpan.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProfilActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK && data != null) {
            val resultUri: Uri? = UCrop.getOutput(data)
            resultUri?.let {
                val fileRef: StorageReference =
                    storageReference.child("foto_profil").child("$userId.jpg")
                fileRef.putFile(it).addOnSuccessListener { _ ->
                    Toast.makeText(
                        this@EditProfilActivity,
                        "Mohon tunggu, foto sedang diupload",
                        Toast.LENGTH_SHORT
                    ).show()

                    val profileImageRef = storageReference.child("foto_profil").child("$userId.jpg")
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        Picasso.get().load(uri).into(binding.profileImg)

                        database.child("users").child(userId).child("profileImageUrl")
                            .setValue(uri.toString())
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@EditProfilActivity,
                                    "Foto profil berhasil diupload",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this@EditProfilActivity,
                                    "Gagal menyimpan URL gambar: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@EditProfilActivity,
                        "Gagal mengunggah gambar: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun handleImageSelectionResult(data: Intent) {
        val imageUri: Uri? = data.data
        imageUri?.let { startCropActivity(it) }
    }

    private fun startCropActivity(imageUri: Uri) {
        val destinationFileName = "${userId}_cropped.jpg"
        val options = UCrop.Options()
        options.setCompressionQuality(70)
        options.setHideBottomControls(false)
        options.setToolbarColor(ContextCompat.getColor(this, R.color.cyan))

        UCrop.of(imageUri, Uri.fromFile(File(cacheDir, destinationFileName)))
            .withOptions(options)
            .start(this)
    }

    private fun String?.toEditable(): Editable {
        return Editable.Factory.getInstance().newEditable(this ?: "")
    }
}
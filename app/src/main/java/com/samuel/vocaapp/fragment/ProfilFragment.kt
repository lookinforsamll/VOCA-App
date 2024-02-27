package com.samuel.vocaapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.samuel.vocaapp.KDPActivity
import com.samuel.vocaapp.LoginActivity
import com.samuel.vocaapp.ProfilActivity
import com.samuel.vocaapp.R
import com.samuel.vocaapp.SDKActivity
import com.samuel.vocaapp.SessionManager
import com.samuel.vocaapp.databinding.FragmentProfilBinding
import com.squareup.picasso.Picasso

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        val view = binding.root

        val sessionManager = SessionManager(requireActivity())

        binding.btnEp.setOnClickListener {
            val intent = Intent(activity, ProfilActivity::class.java)
            startActivity(intent)
        }

        binding.btnSdk.setOnClickListener {
            val intent = Intent(activity, SDKActivity::class.java)
            startActivity(intent)
        }
        binding.btnKdp.setOnClickListener {
            val intent = Intent(activity, KDPActivity::class.java)
            startActivity(intent)
        }
        binding.btnKeluar.setOnClickListener {
            sessionManager.sessionLogout()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        val usersRef = FirebaseDatabase.getInstance().getReference("users")

        uid?.let {
            usersRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("Nama Lengkap").getValue(String::class.java)
                    val kelas = snapshot.child("Kelas").getValue(String::class.java)
                    val jurusan = snapshot.child("Jurusan").getValue(String::class.java)
                    val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

                    binding.txtUsername.text = name
                    binding.txtKelas.text = kelas
                    binding.txtJurusan.text = jurusan
                    profileImageUrl?.let {
                        Picasso.get().load(it).into(binding.profileImg)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        binding.btnTentang.setOnClickListener {
            findNavController().navigate(R.id.tentangFragment2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.samuel.vocaapp.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.samuel.vocaapp.databinding.FragmentKontakBinding

class KontakFragment : Fragment() {

    private var _binding: FragmentKontakBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKontakBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnCs.setOnClickListener {
            openUrl("https://linktr.ee/vocaapp")
        }
        binding.btnSdm.setOnClickListener {
            openUrl("https://forms.gle/crwFJJAxD8bEkCWx9")
        }

        return view
    }

    private fun openUrl(link: String) {
        val uri = Uri.parse(link)
        val inte = Intent(Intent.ACTION_VIEW, uri)
        startActivity(inte)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
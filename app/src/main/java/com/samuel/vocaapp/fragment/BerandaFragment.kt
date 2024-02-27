package com.samuel.vocaapp.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.samuel.vocaapp.BKKActivity
import com.samuel.vocaapp.JadwalActivity
import com.samuel.vocaapp.PengumumanActivity
import com.samuel.vocaapp.PiketActivity
import com.samuel.vocaapp.R
import com.samuel.vocaapp.SliderAdapter
import com.samuel.vocaapp.SliderItem
import com.samuel.vocaapp.databinding.FragmentBerandaBinding
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class BerandaFragment : Fragment() {

    private var _binding: FragmentBerandaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBerandaBinding.inflate(inflater, container, false)

        binding.btnJadwal.setOnClickListener{
            val intent = Intent(requireActivity(), JadwalActivity::class.java)
            startActivity(intent)
        }
        binding.btnPiket.setOnClickListener{
            val intent = Intent(requireActivity(), PiketActivity::class.java)
            startActivity(intent)
        }
        binding.btnPengumuman.setOnClickListener{
            val intent = Intent(requireActivity(), PengumumanActivity::class.java)
            startActivity(intent)
        }
        binding.imgBkk.setOnClickListener{
            val intent = Intent(requireActivity(), BKKActivity::class.java)
            startActivity(intent)
        }
        binding.imgTs.setOnClickListener {
            openUrl("https://smk-ananda.sch.id")
        }
        binding.btnVt.setOnClickListener {
            openUrl("https://vocatechno.smk-ananda.sch.id")
        }
        binding.btnVm.setOnClickListener{
            openUrl("https://vocamedia.smk-ananda.sch.id")
        }
        binding.btnVr.setOnClickListener{
            openUrl("https://vocafood.my.id")
        }
        binding.vf.setOnClickListener {
            openUrl("https://google.com/404")
        }
        val sliderView: SliderView = binding.imageSlider

        val sliderItems = mutableListOf<SliderItem>()
        sliderItems.add(SliderItem(R.drawable.img_header1))
        sliderItems.add(SliderItem(R.drawable.img_header2))
        sliderItems.add(SliderItem(R.drawable.img_header3))

        val adapter = SliderAdapter(requireContext(), sliderItems)
        sliderView.setSliderAdapter(adapter)

        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.WHITE
        sliderView.indicatorUnselectedColor = Color.GRAY
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        return binding.root
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
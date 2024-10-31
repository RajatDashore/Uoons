package com.uoons.india.ui.saas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uoons.india.R
import com.uoons.india.databinding.FragmentFourPhotoesBinding
import com.uoons.india.ui.saas.adapter.SaasAdapter

class four_photoes : Fragment() {

    private lateinit var binding: FragmentFourPhotoesBinding
    private lateinit var saasAdapter: SaasAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var imgList: ArrayList<Int> = arrayListOf(
            R.drawable.ic_error,
            R.drawable.audio,
            R.drawable.jwellary,
            R.drawable.ic_photo_camera
        )
        saasAdapter = SaasAdapter(requireContext(), imgList)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFourPhotoesBinding.inflate(inflater, container, false)
        return binding.root
    }

}

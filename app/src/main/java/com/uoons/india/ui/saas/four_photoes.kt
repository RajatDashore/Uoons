package com.uoons.india.ui.saas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uoons.india.databinding.FragmentFourPhotoesBinding

class four_photoes : Fragment() {

    private lateinit var binding: FragmentFourPhotoesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFourPhotoesBinding.inflate(inflater, container, false)
        return binding.root
    }

}

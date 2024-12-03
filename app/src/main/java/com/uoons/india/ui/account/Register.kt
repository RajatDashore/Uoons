package com.uoons.india.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uoons.india.databinding.FragmentRegisterBinding

class Register : Fragment() {
    private var binding: FragmentRegisterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding!!.btnSubmit.setOnClickListener {
            checkForNullsValues()
        }
        binding!!.backButton.setOnClickListener {

        }

        return binding?.root
    }

    private fun checkForNullsValues() {
        val companyName = binding!!.edtCompanyName.text.toString()
        val shortDesc = binding!!.edtShortDesc.text.toString()
        val longDesc = binding!!.edtLongDesc.text.toString()
        val price = binding!!.edtPrice.text.toString()
        val url = binding!!.edtUrl.text.toString()
        if (companyName.isEmpty()) {
            binding!!.edtCompanyName.error = "Enter company name"
            return
        }
        if (shortDesc.isEmpty()) {
            binding!!.edtShortDesc.error = "Enter short description"
            return
        }
        if (longDesc.isEmpty()) {
            binding!!.edtLongDesc.error = "Enter long description"

            return
        }
        if (price.isEmpty()) {
            binding!!.edtPrice.error = "Enter price"

            return
        }
        if (url.isEmpty()) {
            binding!!.edtUrl.error = "Enter url"

            return
        }
        if (companyName.isNotEmpty() && shortDesc.isNotEmpty() && longDesc.isNotEmpty() && price.isNotEmpty() && url.isNotEmpty()) {
            binding!!.edtCompanyName.error = "fieled can not be empty"
            binding!!.edtShortDesc.error = "fieled can not be empty"
            binding!!.edtLongDesc.error = "fieled can not be empty"
            binding!!.edtPrice.error = "fieled can not be empty"
            binding!!.edtUrl.error = "fieled can not be empty"

            return
        }


    }
}





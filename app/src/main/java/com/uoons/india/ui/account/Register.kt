package com.uoons.india.ui.account

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import coil3.Uri
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.uoons.india.R
import com.uoons.india.databinding.FragmentRegisterBinding
import java.io.File

@Suppress("DEPRECATION")
class Register : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    private var count: Boolean? = false
    private var navController: NavController? = null
    private val CAMERA_REQUEST_CODE = 1
    private val GALLER_REQUEST_CODE = 2

    private var uri: Uri? = null
    private val PickGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        try {
            val bitmap = it?.let { it1 ->
                requireContext().contentResolver.openInputStream(
                    it1
                )?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }
            }
            binding!!.ivlogo.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)


        // uri = createUri()
        binding!!.btnSubmit.setOnClickListener {
            checkForNullsValues()
        }
        binding!!.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding!!.ivlogo.setOnClickListener {
            selectImage()

        }
        binding?.chechBoxYes?.setOnClickListener {
            count = count?.not()
            binding?.edtDPIIT?.visibility = if (count == true) View.VISIBLE else View.GONE
        }


        return binding?.root
    }

    private fun checkForNullsValues() {
        val companyName = binding?.edtCompanyName?.text.toString()
        val shortDesc = binding?.edtShortDesc?.text.toString()
        val longDesc = binding?.edtLongDesc?.text.toString()
        val price = binding?.edtPrice?.text.toString()
        val url = binding?.edtUrl?.text.toString()

        when {
            companyName.isBlank() -> binding?.edtCompanyName?.error = "Enter company name"
            companyName.length < 2 -> binding?.edtCompanyName?.error =
                "Name must be at least 2 characters"

            shortDesc.isBlank() -> binding?.edtShortDesc?.error = "Enter short description"
            shortDesc.length < 10 -> binding?.edtShortDesc?.error =
                "Description must be at least 10 characters"

            longDesc.isBlank() -> binding?.edtLongDesc?.error = "Enter long description"
            price.isBlank() -> binding?.edtPrice?.error = "Enter price"
            url.isBlank() -> binding?.edtUrl?.error = "Enter URL"
            else -> showSubmissionDialog()
        }
    }

    private fun showSubmissionDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.submit_register)
        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val submit = dialog.findViewById<TextView>(R.id.submit)

        cancel.setOnClickListener { dialog.dismiss() }
        submit.setOnClickListener {
            Toast.makeText(requireContext(), "Submitted successfully", Toast.LENGTH_SHORT).show()
            navController?.navigate(R.id.action_registerFragment_to_accountFragment)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1) {
            val uri = data?.data
            binding!!.ivlogo.setImageURI(uri)
            binding!!.text.visibility = View.GONE
        }
    }

    private fun selectImage() {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.pick_image_layout)

        bottomSheet.findViewById<TextView>(R.id.txtOpenCamera)?.setOnClickListener {
            checkAndOpenCamera()
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
            // uri = createUri()
            bottomSheet.dismiss()
        }

        bottomSheet.findViewById<TextView>(R.id.txtOpenGallery)?.setOnClickListener {
            PickGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            bottomSheet.dismiss()
        }

        bottomSheet.show()
    }


    private fun createUri() {//: Uri? {
        val uri = File(requireContext().filesDir, "camera_photo.png")
        //return FileProvider.getUriForFile(requireContext(), "com.uoons.india", uri)
    }


    private fun checkAndOpenCamera() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            //    contracts.launch(uri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  contracts.launch(uri)
                PickGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}





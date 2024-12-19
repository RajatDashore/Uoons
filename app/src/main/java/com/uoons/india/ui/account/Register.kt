package com.uoons.india.ui.account

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.uoons.india.R
import com.uoons.india.databinding.FragmentRegisterBinding
import com.uoons.india.ui.base.BaseActivity
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils.getRealPathFromURI
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class Register : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    lateinit var baseActivity: BaseActivity<*, *>
    private var count: Boolean? = false
    private var navController: NavController? = null
    private var picturePath = ""
    private var imageFilePath: String? = null
    private var mImageUri: Uri? = null
    private var imageInputStream: InputStream? = null
    private val permissionRequestCode = 200
    private val CAMERA_REQUEST_CODE_BANNER = 201
    private val GALLERY_REQUEST_CODE_BANNER = 202

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
            binding!!.ivBanner.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        setupClickListeners()


        return binding?.root
    }


    private fun showSubmissionDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.submit_register)
        /*  dialog.window?.setLayout(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
          )

         */
        dialog.setCancelable(false)
        /* val w = WindowManager.LayoutParams()
         w.copyFrom(dialog.window?.attributes)
         // to show the dialog box in full screen but it will not remove the backGround
         w.width = WindowManager.LayoutParams.MATCH_PARENT
         w.height = WindowManager.LayoutParams.MATCH_PARENT
         dialog.window?.attributes = w
         */
        val cancel = dialog.findViewById<TextView>(R.id.txtCancel)
        val submit = dialog.findViewById<TextView>(R.id.txtSubmit)

        cancel.setOnClickListener { dialog.dismiss() }
        submit.setOnClickListener {
            binding?.edtCompanyName!!.setText("")
            binding?.edtPrice!!.setText("")
            binding?.edtLongDesc!!.setText("")
            binding?.edtShortDesc!!.setText("")
            binding?.edtDPIIT!!.setText("")
            binding?.edtUrl!!.setText("")
            binding?.ivlogo!!.setImageResource(R.drawable.ic_plus)
            binding?.ivBanner!!.setImageResource(R.drawable.ic_plus)
            Toast.makeText(requireContext(), "Submitted successfully", Toast.LENGTH_SHORT).show()
            navController?.navigate(R.id.action_registerFragment_to_accountFragment)
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireContext() as Activity,
            arrayOf(Manifest.permission.CAMERA),
            permissionRequestCode
        )
    }

    @Deprecated("hello in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data

            if (selectedImageUri != null) {
                try {
                    when (requestCode) {
                        AppConstants.GALLERY_IMAGE_REQUEST -> {
                            val inputStream =
                                requireContext().contentResolver.openInputStream(selectedImageUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            inputStream?.close()
                            binding?.ivlogo?.setImageBitmap(bitmap)
                        }

                        AppConstants.CAMERA_REQUEST -> {
                            val inputStream =
                                requireContext().contentResolver.openInputStream(selectedImageUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            binding?.ivlogo?.setImageBitmap(bitmap)
                        }

                        CAMERA_REQUEST_CODE_BANNER -> {
                            val inputStream =
                                requireContext().contentResolver.openInputStream(selectedImageUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            binding?.ivBanner?.setImageBitmap(bitmap)
                        }

                        GALLERY_REQUEST_CODE_BANNER -> {
                            val inputStream =
                                requireContext().contentResolver.openInputStream(selectedImageUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            inputStream?.close()
                            binding?.ivBanner?.setImageBitmap(bitmap)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Error processing the image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Operation canceled or failed", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private val SAMPLE_CROPPED_IMAGE_NAME = "UoonsCropImage"
    /* private fun startCrop(uri: Uri) {
         var destinationFileName: String = SAMPLE_CROPPED_IMAGE_NAME
         val tsLong = System.currentTimeMillis() / 1000
         val ts = tsLong.toString()
         destinationFileName += ts
         destinationFileName += ".jpg"
         val uCrop: UCrop = UCrop.of(uri, Uri.fromFile(File(context?.cacheDir, destinationFileName)))
         uCrop.withAspectRatio(3F, 2F)
     }

     */


    @SuppressLint("IntentReset")
    private fun pickImageFromGallery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        i.type = AppConstants.IMAGE
        activity?.startActivityForResult(i, AppConstants.GALLERY_IMAGE_REQUEST)
    }

    private fun selectImageDialog() {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.pick_image_layout)
        bottomSheet.setCancelable(true)
        bottomSheet.findViewById<TextView>(R.id.txtOpenCamera)?.setOnClickListener {
            pickImageFromCamera()
            bottomSheet.dismiss()
        }

        bottomSheet.findViewById<TextView>(R.id.txtOpenGallery)?.setOnClickListener {
            pickImageFromGallery()
            bottomSheet.dismiss()
        }
        bottomSheet.show()
    }

    /*  private fun handleCropResult(result: Intent) {
          val resultUri = UCrop.getOutput(result)
          if (resultUri != null) {
              picturePath = context?.let { getRealPathFromURI(it, resultUri) }!!

              val file = File(picturePath)
              // Get length of file in bytes
              val fileSizeInBytes = file.length()
              // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
              val fileSizeInKB = fileSizeInBytes / 1024
              // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
              val fileSizeInMB = fileSizeInKB / 1024
              if (fileSizeInMB > 5) {
                  picturePath = ""
                  Toast.makeText(
                      requireContext(),
                      "File size should not be greater then 5 mb",
                      Toast.LENGTH_LONG
                  ).show()
              } else {
                  //binding.saveUserProfilePic(picturePath)
                  binding?.ivlogo?.setImageURI(resultUri) //
                  // Glide.with(this).load(picturePath).into(binding!!.ivlogo)
              }
          } else {
              Toast.makeText(
                  context,
                  R.string.toast_cannot_retrieve_cropped_image,
                  Toast.LENGTH_SHORT
              ).show()
          }
      }
     */


    @SuppressLint("ObsoleteSdkInt")
    private fun pickImageFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
            startActivityForResult(pictureIntent, AppConstants.CAMERA_REQUEST)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //  intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
            startActivityForResult(intent, AppConstants.CAMERA_REQUEST)
        }
    }


    /*Helper Methods
    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        val timeStamp =
            SimpleDateFormat(
                AppConstants.createImageFileDateFile,
                Locale.getDefault()
            ).format(
                Date()
            )
        val imageFileName = AppConstants.IMG_ + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        imageFilePath = image.absolutePath
        return image
    }

     */


    /* private fun checkAndOpenCamera() {
         if (ActivityCompat.checkSelfPermission(
                 requireContext(),
                 Manifest.permission.CAMERA
             ) == PackageManager.PERMISSION_GRANTED
         ) {


         } else {
             ActivityCompat.requestPermissions(
                 requireActivity(),
                 arrayOf(Manifest.permission.CAMERA),
                 CAMERA_REQUEST_CODE_BANNER
             )
         }
     }

     */


    private fun setupClickListeners() {
        binding!!.btnSubmit.setOnClickListener {
            checkForNullsValues()
        }
        binding!!.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding!!.ivBanner.setOnClickListener {
            if (checkPermission()) {
                checkAndOpenCameraBanner()
            } else {
                requestPermission()
            }
        }
        binding!!.ivlogo.setOnClickListener {
            if (checkPermission()) {
                selectImageDialog()
            } else {
                requestPermission()
            }
        }
        binding?.chechBoxYes?.setOnClickListener {
            count = count?.not()
            binding?.edtDPIIT?.visibility = if (count == true) View.VISIBLE else View.GONE
        }

    }

    private fun checkAndOpenCameraBanner() {
        val bottomSheet = BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.pick_image_layout)
        bottomSheet.setCancelable(true)
        bottomSheet.findViewById<TextView>(R.id.txtOpenCamera)?.setOnClickListener {
            pickImageFromCameraBanner()
            bottomSheet.dismiss()
        }

        bottomSheet.findViewById<TextView>(R.id.txtOpenGallery)?.setOnClickListener {
            pickImageFromGalleryBanner()
            bottomSheet.dismiss()
        }
        bottomSheet.show()

    }

    @SuppressLint("IntentReset", "ObsoleteSdkInt")
    private fun pickImageFromCameraBanner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            /*   var photoFile: File? = null
               try {
                   photoFile = context?.let { createImageFile(it) }
               } catch (ex: IOException) {
                   // Error occurred while creating the File
               }
               if (photoFile != null) {
                   mImageUri = context?.let {
                       FileProvider.getUriForFile(
                           requireContext(),
                           "com.uoons.india",
                           photoFile
                       )
                   }

             */
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
            startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE_BANNER)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // mImageUri = getOutputMediaFileUri(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
            startActivityForResult(intent, CAMERA_REQUEST_CODE_BANNER)
        }
    }

    @SuppressLint("IntentReset")
    private fun pickImageFromGalleryBanner() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        i.type = AppConstants.IMAGE
        activity?.startActivityForResult(i, GALLERY_REQUEST_CODE_BANNER)
    }

    private fun checkForNullsValues() {
        val companyName = binding?.edtCompanyName?.text.toString()
        val shortDesc = binding?.edtShortDesc?.text.toString()
        val longDesc = binding?.edtLongDesc?.text.toString()
        val price = binding?.edtPrice?.text.toString()
        val url = binding?.edtUrl?.text.toString()

        when {
            companyName.isBlank() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter brand name",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }



            shortDesc.isBlank() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter short description about the brand",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            longDesc.isBlank() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter long description about the brand",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            price.isBlank() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter price",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            url.isBlank() -> {
                Toast.makeText(
                    requireContext(),
                    "Please enter URL",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            else -> showSubmissionDialog()
        }
    }

}






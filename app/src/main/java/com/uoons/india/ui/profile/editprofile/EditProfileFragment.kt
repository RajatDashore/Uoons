package com.uoons.india.ui.profile.editprofile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentEditProfilesBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.profile.editprofile.model.ProfileImageModel
import com.uoons.india.ui.profile.model.UserDetailsModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import com.uoons.india.utils.CommonUtils.getRealPathFromURI
import com.uoons.india.utils.applyClickShrink
import com.yalantis.ucrop.UCrop
import org.lsposed.lsparanoid.Obfuscate
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


@Obfuscate
class EditProfileFragment: BaseFragment<FragmentEditProfilesBinding, EditProfileFragmentVM>(), EditProfileFrgamentNavigator {
    override val bindingVariable: Int = BR.editProfileFragmentVM
    override val layoutId: Int = R.layout.fragment_edit_profiles
    override val viewModelClass: Class<EditProfileFragmentVM> = EditProfileFragmentVM::class.java
    private var navController: NavController? = null
    private val lstGender = arrayOf("Please select gender","Male","Female","Other")
    private val lstOccupation = arrayOf("Please select occupation","Job/Service","Housewife","Teacher","Business","Student","Others")
    private val lstStates = arrayOf("Please select state","Andaman and Nicobar (UT)","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chandigarh (UT)","Chhattisgarh",
        "Dadra and Nagar Haveli (UT)","Daman and Diu (UT)","Delhi","Goa","Gujarat","Haryana","Himachal Pradesh","Jammu and Kashmir","Jharkhand","Karnataka","Kerala","Lakshadweep (UT)",
       "Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Orissa","Puducherry (UT)","Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura",
   "Uttar Pradesh","Uttarakhand","West Bengal")
    var PID :String = ""
    private var imageFilePath: String? = null
    private var mImageUri: Uri? = null
    private var picturePath = ""
    private var imageInputStream: InputStream? = null
    private var arrayGenderAdapter : ArrayAdapter<String>? =  null
    private var arrayOccupationAdapter : ArrayAdapter<String>? =  null
    private var arrayStateAdapter : ArrayAdapter<String>? =  null
    private val permissionRequestCode = 200

    override  fun init() {
        mViewModel.navigator = this
        // Start shimmer animation
        viewDataBinding.shimmerEditProfileLayout.startShimmer()
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getUserDetailsApiCall()
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {})
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
//        PID= getActivity()?.let { getEncryptedSharedprefs(it).getString("PROFILE_ID", "") }.toString()

        PID =    AppPreference.getValue(PreferenceKeys.PROFILE_ID)
        viewDataBinding.edtPhoneNumber.setText(AppPreference.getValue(PreferenceKeys.MOBILE_NO))
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.edit_profile)
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE
        viewDataBinding.toolbar.ivBackBtn.setOnClickListener {
            super.onBackClick()
        }
        //visibility hide about
        viewDataBinding. edtAboutMe.visibility= View.GONE
        viewDataBinding. txvAboutMe.visibility= View.GONE
        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                naviGateToMyCartFragment()
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return@OnClickListener
            }
        })

        // Sppiner For Gender
        arrayGenderAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_dropdown_item_1line, lstGender)
        viewDataBinding.sppinerGender.adapter = arrayGenderAdapter

        // Sppiner For Occupation
        arrayOccupationAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_dropdown_item_1line, lstOccupation)
        viewDataBinding.sppinerOccupation.adapter = arrayOccupationAdapter

        // Sppiner For State
        arrayStateAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_dropdown_item_1line, lstStates)
        viewDataBinding.sppinerState.adapter = arrayStateAdapter


        viewDataBinding.edtAddress.visibility= View.GONE
        viewDataBinding.txvAddressText.visibility= View.GONE
    }

    fun naviGateToMyCartFragment(){
        navController?.navigate(R.id.action_editProfileFragment_to_myCartFragment)
    }

    override fun getUserDetailsData() {
        if(view !=null){
            mViewModel.getUserDetailsResponse.observe(viewLifecycleOwner, Observer<UserDetailsModel> {
                if (it != null){
                    setUserDetails(it)
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    private fun setUserDetails(userDetails: UserDetailsModel) {
        if (userDetails.Data[0].name.toString().isEmpty()){
            viewDataBinding.ivProfile.setImageResource(R.drawable.ic_profile)
        }else{
            if (userDetails.Data[0].profile.equals(AppConstants.assets_user)){
                viewDataBinding.ivProfile.setImageResource(R.drawable.ic_profile)
            }else{
                Glide.with(this).load(userDetails.Data[0].profile).into(viewDataBinding.ivProfile)
            }

            viewDataBinding.edtFullName.setText(userDetails.Data[0].name)
            viewDataBinding.edtPhoneNumber.setText(userDetails.Data[0].mobileNo)
            viewDataBinding.edtEmail.setText(userDetails.Data[0].email)
            viewDataBinding.edtAddress.setText(userDetails.Data[0].address)
            viewDataBinding.edtLanguageSpoken.setText(userDetails.Data[0].languageSpoken)
            viewDataBinding.edtAboutMe.setText(userDetails.Data[0].aboutMe)
            viewDataBinding.edtPincode.setText(userDetails.Data[0].pinCode)
            viewDataBinding.edtCity.setText(userDetails.Data[0].city)

            val spinnerPosition: Int? = arrayGenderAdapter?.getPosition(userDetails.Data[0].gender)
            // on below line we are setting selection for our spinner to spinner position.
            if (spinnerPosition != null) {
                viewDataBinding.sppinerGender.setSelection(spinnerPosition)
            }

            val spinnerPositionState : Int? = arrayStateAdapter?.getPosition(userDetails.Data[0].state)
            if (spinnerPositionState != null) {
                viewDataBinding.sppinerState.setSelection(spinnerPositionState)
            }

            val spinnerPositionOccupation : Int? = arrayOccupationAdapter?.getPosition(userDetails.Data[0].occupation)
            if (spinnerPositionOccupation != null) {
                viewDataBinding.sppinerOccupation.setSelection(spinnerPositionOccupation)
            }

        }

        viewDataBinding.cstLayoutProfileDetails.visibility = View.VISIBLE
        viewDataBinding.shimmerEditProfileLayout.visibility = View.GONE
    }

    override fun saveUserData() {
        if (mViewModel.isValidField(strFullName = viewDataBinding.edtFullName.text.toString(), strEmail = viewDataBinding.edtEmail.text.toString(), strMobileNumber = viewDataBinding.edtPhoneNumber.text.toString()
                ,strGender = viewDataBinding.sppinerGender.selectedItem.toString(), strLanguageSpoken = viewDataBinding.edtLanguageSpoken.text.toString(),
                strAccoupation = viewDataBinding.sppinerOccupation.selectedItem.toString(), strAboutMe = viewDataBinding.edtAboutMe.text.toString(),
                strAddress = viewDataBinding.edtAddress.text.toString(), strPinCode = viewDataBinding.edtPincode.text.toString(),
                strCity = viewDataBinding.edtCity.text.toString(), strSate = viewDataBinding.sppinerState.selectedItem.toString()))
        {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mViewModel.saveUserApiCall(viewDataBinding.edtFullName.text.toString(),viewDataBinding.edtEmail.text.toString(),viewDataBinding.edtPhoneNumber.text.toString(),
                    viewDataBinding.sppinerGender.selectedItem.toString(),viewDataBinding.edtLanguageSpoken.text.toString(),viewDataBinding.sppinerOccupation.selectedItem.toString(),
                    viewDataBinding.edtAboutMe.text.toString(),viewDataBinding.edtAddress.text.toString(), viewDataBinding.edtPincode.text.toString(),viewDataBinding.edtCity.text.toString(),
                    viewDataBinding.sppinerState.selectedItem.toString(),PID,activity)
            }else{
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {})
            }
        }
    }

    override fun saveUserDetailResponse() {
        navController?.navigate(R.id.action_editProfileFragment_to_homeFragment)
    }

    override fun opneGalleryCamera() {
        if (checkPermission()) {
            showPickImageDialog()
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireContext() as Activity, arrayOf(Manifest.permission.CAMERA), permissionRequestCode)
    }

    override fun saveUserProfileImageResponse() {
        if(view !=null){
            mViewModel.saveUserProfileImageResposne.observe(viewLifecycleOwner,Observer<ProfileImageModel>{
                if (it != null){
                    context?.let { it2 ->setProfileImage(it,it2) }
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun setProfileImage(profileImageModel: ProfileImageModel, context: Context) {
        AppPreference.addValue(PreferenceKeys.PROFILE_IMAGE,profileImageModel.Data?.fullPath.toString())
        Glide.with(this).load(picturePath).into(viewDataBinding.ivProfile)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////// START CODE FOR PIC IMAGE FROM CAMERA AND GALLERY //////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    private fun showPickImageDialog() {
        val selectImageDialog = context?.let { Dialog(it, R.style.Theme_Dialog) }
        selectImageDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        selectImageDialog?.setCancelable(true)
        selectImageDialog?.setCanceledOnTouchOutside(true)
        selectImageDialog?.setContentView(R.layout.image_picker_dialog)
        val window: Window =  requireActivity().window
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        selectImageDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val takePhoto = selectImageDialog.findViewById<RelativeLayout>(R.id.takePhoto)
        val chooseFromGallery =
            selectImageDialog.findViewById<RelativeLayout>(R.id.chooseFromGallery)
        val cancelTextView = selectImageDialog.findViewById<TextView>(R.id.cancelTextView)

        cancelTextView.applyClickShrink()
        cancelTextView.setOnClickListener { selectImageDialog.dismiss() }
        takePhoto.setOnClickListener {
            pickImageFromCamera()
            selectImageDialog.dismiss()
        }

        chooseFromGallery.setOnClickListener {
            pickImageFromGallery()
            selectImageDialog.dismiss()
        }

        if (!selectImageDialog.isShowing) {
            selectImageDialog.show()
        }
    }

    private fun pickImageFromGallery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        i.type = AppConstants.IMAGE
        activity?.startActivityForResult(i, AppConstants.GALLERY_IMAGE_REQUEST)
    }



    private val SAMPLE_CROPPED_IMAGE_NAME = "UoonsCropImage"

    private fun startCrop(uri: Uri) {
        var destinationFileName: String = SAMPLE_CROPPED_IMAGE_NAME
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        destinationFileName += ts
        destinationFileName += ".jpg"
        val uCrop: UCrop = UCrop.of(uri, Uri.fromFile(File(context?.cacheDir, destinationFileName)))
        uCrop.withAspectRatio(3F, 2F)
        uCrop.start(baseActivity, UCrop.REQUEST_CROP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstants.GALLERY_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = selectedImage?.let { context?.contentResolver?.query(it, filePathColumn, null, null, null) }
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            picturePath = columnIndex?.let { cursor.getString(it) }.toString()
            cursor?.close()
            mViewModel.saveUserProfilePic(picturePath)

            try {
                imageInputStream = requireContext().contentResolver.openInputStream(selectedImage!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == AppConstants.CAMERA_REQUEST) {
            Log.e("","EditFRagment_IS:======")
            when (resultCode) {
                Activity.RESULT_OK -> {
                    startCrop(mImageUri!!)
                }
                Activity.RESULT_CANCELED -> // user cancelled Image capture
                    Toast.makeText(context, getStringResource(R.string.cancel_image_capture), Toast.LENGTH_SHORT).show()
                else -> // failed to capture image
                    Toast.makeText(context, getStringResource(R.string.fail_image_capture), Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (data != null) {
                handleCropResult(data)
            }
        }
    }


    private fun pickImageFromCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //Create a file to store the image
            var photoFile: File? = null
            try {
                photoFile = context?.let { createImageFile(it) }
            } catch (ex: IOException) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                mImageUri = context?.let { FileProvider.getUriForFile(requireContext(), "com.uoons.india", photoFile) }
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
                activity?.startActivityForResult(pictureIntent, AppConstants.CAMERA_REQUEST)
            }
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            mImageUri = getOutputMediaFileUri(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
            activity?.startActivityForResult(intent, AppConstants.CAMERA_REQUEST)
        }
    }

    /*Helper Methods*/
    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat(AppConstants.createImageFileDateFile, Locale.getDefault()).format(Date())
        val imageFileName = AppConstants.IMG_ + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, /* prefix */".jpg", /* suffix */storageDir      /* directory */)
        imageFilePath = image.absolutePath
        return image
    }


    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    private fun getOutputMediaFile(type: Int): File? {
        // External sdcard location
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            AppConstants.IMAGE_DIRECTORY_NAME
        )

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat(
            AppConstants.createImageFileDateFile,
            Locale.getDefault()
        ).format(Date())
        val mediaFile: File
        if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            mediaFile = File(
                mediaStorageDir.path + File.separator
                        + AppConstants.IMG_ + timeStamp + ".jpg"
            )
        } else {
            return null
        }

        return mediaFile
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            //val resultUri = result.getUri()
            picturePath = context?.let { getRealPathFromURI(it, resultUri) }!!

            val file=File(picturePath)
            // Get length of file in bytes
            val fileSizeInBytes = file.length()
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            val fileSizeInKB = fileSizeInBytes / 1024
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            val fileSizeInMB = fileSizeInKB / 1024
            if (fileSizeInMB > 5) {
                picturePath=""
                mViewModel.navigator!!.showValidationError(getString(R.string.image_length))
            }
            else{
                mViewModel.saveUserProfilePic(picturePath)
                viewDataBinding.ivProfile.setImageURI(resultUri)
                Glide.with(this).load(picturePath).into(viewDataBinding.ivProfile)
            }
        } else {
            Toast.makeText(
                context,
                R.string.toast_cannot_retrieve_cropped_image,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////// END CODE FOR PIC IMAGE FROM CAMERA AND GALLERY //////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
/*
    fun getEncryptedSharedprefs(context: Context): SharedPreferences {

        val masterkeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "secured_prefs", masterkeyAlias, context ,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }
*/

}
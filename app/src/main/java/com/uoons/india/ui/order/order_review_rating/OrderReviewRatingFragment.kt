package com.uoons.india.ui.order.order_review_rating

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentOrderReviewRatingBinding
import com.uoons.india.ui.base.BaseFragment
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

import androidx.recyclerview.widget.LinearLayoutManager

import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.order.order_review_rating.adaper.ProductReviewPhotosAdapter
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.google.gson.GsonBuilder
import com.uoons.india.utils.*
import com.uoons.india.utils.CommonUtils.getRealPathFromURI
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OrderReviewRatingFragment : BaseFragment<FragmentOrderReviewRatingBinding, OrderReviewRatingFragmentVM>(),
    OrderReviewRatingFragmentNavigator {

    override val bindingVariable: Int = BR.orderReviewRatingFragmentVM
    override val layoutId: Int = R.layout.fragment_order_review_rating
    override val viewModelClass: Class<OrderReviewRatingFragmentVM> = OrderReviewRatingFragmentVM::class.java
    private var navController: NavController? = null

    private var pId : String = ""
    private var ratingProduct : Float = 0.0f
    private var pName : String = ""
    private var pImage : String = ""
    var PID :String = ""
    private var imageFilePath: String? = null
    private var mImageUri: Uri? = null
    private var picturePath = ""
    private var imageInputStream: InputStream? = null
    private val SAMPLE_CROPPED_IMAGE_NAME = "UoonsCropImage"
    private lateinit var productReviewPhotosAdapter : ProductReviewPhotosAdapter
    private var productReviewList: ArrayList<Uri> = ArrayList<Uri>()

    private var multipartArralist: ArrayList<MultipartBody.Part> = ArrayList<MultipartBody.Part>()
    var imageUrl : String =""

    override    fun init() {
        mViewModel.navigator = this
        pId = arguments?.getString(AppConstants.PId).toString()
//        Log.e("OrderReviewRatingFragment","ratingProduct:- "+arguments?.getFloat(AppConstants.ratingProduct).toString())

        ratingProduct = arguments?.getFloat(AppConstants.ratingProduct)!!

        pName = arguments?.getString(AppConstants.pName).toString()
        pImage = arguments?.getString(AppConstants.pImage).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.e("OrderReviewRatingFragment","")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
//        PID= getActivity()?.let { getEncryptedSharedprefs(it).getString("PROFILE_ID", "") }.toString()
        PID =    AppPreference.getValue(PreferenceKeys.PROFILE_ID)
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.order_and_review)
        if (PID.isNotEmpty()){
            mViewModel.getMyCartItemsApiCall()
            mViewModel.getWishListProductApiCall()
        }
        viewDataBinding.txvProductName.text = pName
        viewDataBinding.ratingBar.rating = ratingProduct

        if (pImage.isEmpty()){
            viewDataBinding.ivProductImage.setImageResource(R.drawable.image_gray_color)
        }else{
            CommonUtils.loadImage(viewDataBinding.ivProductImage, pImage, viewDataBinding.ivProductImage.id)
        }

        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        viewDataBinding.rcvProductImages.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener {
            super.onBackClick()
        })

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener {
            navController?.navigate(R.id.action_orderReviewRatingFragment_to_myCartFragment)
        })

        viewDataBinding.toolbar.ivHeartVector.setOnClickListener(View.OnClickListener {
            navController?.navigate(R.id.action_orderReviewRatingFragment_to_wishListFragment)
        })


    }

    override fun opneGalleryCamera() {
        /*if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.GRANTED_PERMISSION))) {
            context?.let {
                checkPermission(it, *CommonUtils.READ_WRITE_EXTERNAL_STORAGE_AND_CAMERA) }
        }else{
            showPickImageDialog()
        }*/
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
        ActivityCompat.requestPermissions(requireContext() as Activity, arrayOf(Manifest.permission.CAMERA), 200)
    }

    override fun submitReviewAndRating() {
        val review : String = viewDataBinding.edtEnterYourReview.text.toString()
        if (mViewModel.navigator!!.checkIfInternetOn()) {
          /*  val gson = GsonBuilder().setPrettyPrinting().create()
            val ABC = gson.toJson(multipartArralist)
            Log.e("OrderReviewRatingFragmentVM", "multipartArralist $ABC")*/
           // mViewModel.uploadReviewOnProduct(pId, ratingProduct,review, multipartArralist)
            mViewModel.uploadReviewOnProduct(pId, ratingProduct,review, multipartArralist)
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {
                onInternet()
            })
        }

    }

    private fun onInternet(){
        val review : String = viewDataBinding.edtEnterYourReview.text.toString()
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.uploadReviewOnProduct(pId, ratingProduct,review, multipartArralist)
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {
                onInternet()
            })
        }
    }

    override fun ratingReviewResponse() {
        if(view !=null){
            mViewModel.ratingReviewResponse.observe(viewLifecycleOwner, Observer<RatingAndReviewModel> {
                if (it != null){
                   showReviewReponse(it)
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun showReviewReponse(ratingAndReviewModel: RatingAndReviewModel) {
        navController?.navigate(R.id.action_orderReviewRatingFragment_to_reviewAndRatingThankyouScreenFragment)
//        Log.e("OrderReviewRatingFragmentVM", "ratingAndReviewModel $ratingAndReviewModel")
        val gson = GsonBuilder().setPrettyPrinting().create()
        val ABC = gson.toJson(ratingAndReviewModel)
//        Log.e("OrderReviewRatingFragmentVM", "Response123 $ABC")
    }

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
                mImageUri = context?.let {
                    FileProvider.getUriForFile(requireContext(), "com.uoons.india", photoFile)
                }
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
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, /* prefix */".jpg", /* suffix */storageDir      /* directory */)
        imageFilePath = image.absolutePath
        return image
    }

    /**
     * Creating file uri to store image/video
     */
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
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val mediaFile: File
        if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            mediaFile = File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        } else {
            return null
        }

        return mediaFile
    }

    private fun pickImageFromGallery() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        i.type = "image/*"
        activity?.startActivityForResult(i, AppConstants.GALLERY_IMAGE_REQUEST)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun saveUserProfilePic(picturePath: String) {

        val file = File(picturePath)
        val requestFile: RequestBody = file.asRequestBody(("multipart/form-data").toMediaType())
        val parts: MultipartBody.Part = MultipartBody.Part.createFormData("product_image[]", file.name, requestFile)

        multipartArralist.add(parts)

        Log.e("", "OrderReviewRatingFragment_saveUserProfilePic:- $picturePath")
        Log.e("", "OrderReviewRatingFragment_Uri_saveUserProfilePic:- "+Uri.fromFile(file))
        productReviewList.add(Uri.fromFile(file))
        productReviewPhotosAdapter = ProductReviewPhotosAdapter(productReviewList, onclick = {
            productReviewList.removeAt(it)
            multipartArralist.removeAt(it)
            productReviewPhotosAdapter.notifyDataSetChanged()
        })
        viewDataBinding.rcvProductImages.adapter = productReviewPhotosAdapter
        productReviewPhotosAdapter.notifyItemRangeInserted(productReviewList.size, 1)
       // Glide.with(this).load(Uri.fromFile(file)).into(viewDataBinding.ivProfile)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun captureReviewImages(file: File) {

        Log.d("", "OrderReviewRatingFragment_captureReviewImages:- $picturePath")

        Log.d("", "OrderReviewRatingFragment_Uri_captureReviewImages:- "+Uri.fromFile(file))

        val requestFile: RequestBody = file.asRequestBody(("multipart/form-data").toMediaType())
        val parts: MultipartBody.Part = MultipartBody.Part.createFormData("product_image[]", file.name, requestFile)

        multipartArralist.add(parts)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == AppConstants.GALLERY_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = data?.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = selectedImage?.let { context?.contentResolver?.query(it, filePathColumn, null, null, null) }
            cursor?.moveToFirst()
            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
            picturePath = columnIndex?.let { cursor.getString(it) }.toString()

            Log.e("saveUserProfilePic","imageUrl:- "+picturePath)

            cursor?.close()
            saveUserProfilePic(picturePath)


            try {
                imageInputStream = requireContext().contentResolver.openInputStream(selectedImage!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        else if (requestCode == AppConstants.CAMERA_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    startCrop(mImageUri!!)

                   /* Log.e("=========uri",mImageUri.toString())
                    var imagePath= FileUtils.getRealPathFromURI(requireContext(),mImageUri!!);
                    if (imagePath != null) {
                        Log.e("=========path",imagePath)
                    }

                    mImageUri?.let { captureReviewImages(it.toString()) }

                    productReviewList.add(mImageUri!!)
                    Log.e("OrderReviewRatingFragment", "mImageUri_productReviewList:- $mImageUri")
                    productReviewPhotosAdapter = ProductReviewPhotosAdapter(productReviewList, onclick = {
                        productReviewList.removeAt(it)
                        multipartArralist.removeAt(it)
                        productReviewPhotosAdapter.notifyDataSetChanged()
                    })
                    viewDataBinding.rcvProductImages.adapter = productReviewPhotosAdapter
                    productReviewPhotosAdapter.notifyItemRangeInserted(productReviewList.size, 1)*/

                   // productReviewList.add(mImageUri!!)
                 /*   Log.e("OrderReviewRatingFragment", "mImageUri_productReviewList:- $mImageUri")
                    productReviewPhotosAdapter = ProductReviewPhotosAdapter(productReviewList, onclick = {
                        productReviewList.removeAt(it)
                        multipartArralist.removeAt(it)
                        productReviewPhotosAdapter.notifyDataSetChanged()
                    })
                    viewDataBinding.rcvProductImages.adapter = productReviewPhotosAdapter
                    productReviewPhotosAdapter.notifyItemRangeInserted(productReviewList.size, 1)*/

                  /*  val newImageUri = mImageUri.toString().replace("content:","")

                    Log.e("OrderReviewRatingFragment", "newImageUri:- $newImageUri")
                    val file = File(newImageUri)
                    val requestFile: RequestBody =
                        file.asRequestBody(("multipart/form-data").toMediaType())
                    val parts: MultipartBody.Part = MultipartBody.Part.createFormData("product_image", file.name, requestFile)
                    multipartArralist.add(parts)

                    Log.e("OrderReviewRatingFragment", "parts:- $parts")*/

                }
                Activity.RESULT_CANCELED -> // user cancelled Image capture
                    Toast.makeText(context, getStringResource(R.string.cancel_image_capture), Toast.LENGTH_SHORT).show()
                else -> // failed to capture image
                    Toast.makeText(context, getStringResource(R.string.fail_image_capture), Toast.LENGTH_SHORT).show()
            }
        }

        else if (requestCode==69)
        {
            if (data != null) {
                handleCropResult(data)
            }
        }

    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            //val resultUri = result.getUri()
            picturePath = getRealPathFromURI(requireContext(), resultUri)!!
            val file=File(picturePath)

            mImageUri?.let { captureReviewImages(file) }

            productReviewList.add(resultUri)
            Log.d("", "OrderReviewRatingFragment_mImageUri_productReviewList:- $resultUri")
            productReviewPhotosAdapter = ProductReviewPhotosAdapter(productReviewList, onclick = {
                productReviewList.removeAt(it)
                multipartArralist.removeAt(it)
                productReviewPhotosAdapter.notifyDataSetChanged()
            })
            viewDataBinding.rcvProductImages.adapter = productReviewPhotosAdapter
            productReviewPhotosAdapter.notifyItemRangeInserted(productReviewList.size, 1)
        } else {
           /* Toast.makeText(
                this@EditProfileActivity,
                R.string.toast_cannot_retrieve_cropped_image,
                Toast.LENGTH_SHORT
            ).show()*/
        }
    }



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

    override fun getMyCartItemsResponse() {
        if(view !=null){
            mViewModel.getMyCartItemsDataResponse.observe(viewLifecycleOwner,Observer<GetMyCartDataModel>{
                mViewModel.getMyCartItemsDataResponse.observe(viewLifecycleOwner, Observer<GetMyCartDataModel> {
                    if (it != null){
                        setMyCartItemsAdapterData(it)
                    }
                })
            })
        }

    }

    private fun setMyCartItemsAdapterData(data: GetMyCartDataModel) {
        if (data.status.equals(AppConstants.SUCCESS)){
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountMyCartItems.text = data.Data?.itemCount.toString()
        }else{
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE
        }
    }

    override fun getWishListResponse() {
        if(view !=null){
            mViewModel.getWishListDataResponse.observe(viewLifecycleOwner, Observer<GetWishListDataModel> {
                if (it != null){
                    setWishListData(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }
    }

    private fun setWishListData(getWishListDataModel: GetWishListDataModel) {
        if (getWishListDataModel.status.equals(AppConstants.SUCCESS)){
            viewDataBinding.toolbar.crdCountWishList.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountWishList.text = getWishListDataModel.Data.size.toString()
        }else{
            viewDataBinding.toolbar.crdCountWishList.visibility = View.GONE
        }
    }



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
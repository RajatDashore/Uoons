package com.uoons.india.ui.category.category_items.category_items_details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentCategoryItemDetailsBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.category.category_items.category_items_details.adapter.MoreLikeSimilerProductAdapter
import com.uoons.india.ui.category.category_items.category_items_details.model.SimilarProducts
import com.uoons.india.ui.category.category_items.category_items_details.model.SingleProductDetailModel
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.lang.Integer.parseInt
import java.text.DecimalFormat


@Obfuscate
class CategoryItemDetailsFragment : BaseFragment<FragmentCategoryItemDetailsBinding, CategoryItemDetailsFragmentVM>(), CategoryItemDetailsFragmentNavigator {

    override val bindingVariable: Int = BR.categoryItemDetailsFragmentVM
    override val layoutId: Int = R.layout.fragment_category_item_details
    override val viewModelClass: Class<CategoryItemDetailsFragmentVM> = CategoryItemDetailsFragmentVM::class.java
    private var navController: NavController? = null
    private var counter = 1
    private lateinit var stringVal: String
    private var pId : Int = 0
    private lateinit var moreLikeSimilerProductAdapter : MoreLikeSimilerProductAdapter
    var lstSimilerProduct : ArrayList<SimilarProducts> = ArrayList<SimilarProducts>()
    var inWishList : String = ""
    var inCart : String = ""
    var PID :String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.e("CategoryItemsFragment","")
    }


    override   fun init() {
        mViewModel.navigator = this
        viewDataBinding.executePendingBindings()
        moreLikeSimilerProductAdapter = MoreLikeSimilerProductAdapter()
        setUpBinding()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
        val pId = parseInt(arguments?.getString("pId").toString())
        getSingleProductBinding(pId)
//        PID= getActivity()?.let { getEncryptedSharedprefs(it).getString("PROFILE_ID", "") }.toString()
        PID =    AppPreference.getValue(PreferenceKeys.PROFILE_ID)

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener { view ->
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                }else{
                    navController?.navigate(R.id.action_categoryItemDetailsFragment_to_myCartFragment)
                }
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return@OnClickListener
            }
        })

        viewDataBinding.toolbar.ivHeartVector.setOnClickListener(View.OnClickListener { view ->
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                }else{
                    navController?.navigate(R.id.action_categoryItemDetailsFragment_to_wishListFragment)
                }
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return@OnClickListener
            }
        })

        viewDataBinding.crdCheckPinCode.setOnClickListener(View.OnClickListener {
            if (mViewModel.isValidField(strPinCode = viewDataBinding.edtPinCode.text.toString())){
                mViewModel.checkPinCodeAvailability(viewDataBinding.edtPinCode.text.toString().toInt(),pId)
            }
        })

        moreLikeSimilerProductAdapter.setOnItemClickListener(object :
            MoreLikeSimilerProductAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(pId: String) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    val siPid = parseInt(pId)
                    getSingleProductBinding(siPid)
                    (viewDataBinding.nstScrollView).post {
                        (viewDataBinding.nstScrollView).fullScroll(View.FOCUS_UP)
                    }
                }else if (mViewModel.navigator!!.isConnectedToInternet()){
                    return
                }
            }
        })

    }

    fun getSingleProductBinding(pId: Int){
        mViewModel.getSingleProductApiCall(pId)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun getSingleProductData() {
        if(view !=null){
            mViewModel.getSingleProductDataResponse.observe(viewLifecycleOwner, Observer<SingleProductDetailModel> {
                if (it != null){
                    context?.let {
                            it2 ->setDataSingleProduct(it,it2)
                    }
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDataSingleProduct(data: SingleProductDetailModel, context: Context){

        pId = parseInt(data.Data?.pid.toString())

        viewDataBinding.toolbar.txvTitleName.text = data.Data?.productName
        viewDataBinding.txvProductName.text = data.Data?.productName
        viewDataBinding.txvProductPrice.text = resources.getString(R.string.rupees)+data.Data?.productSalePrice
        viewDataBinding.txvMRPPrice.text = resources.getString(R.string.rupees)+data.Data?.productPrice

        inWishList = data.Data?.inWishlist.toString()
        inCart = data.Data?.inCart.toString()

        if (inWishList == AppConstants.TRUE){
            viewDataBinding.ivRomveWishList.visibility = View.VISIBLE
            viewDataBinding.ivAddWishList.visibility = View.GONE
        }else{
            viewDataBinding.ivRomveWishList.visibility = View.GONE
            viewDataBinding.ivAddWishList.visibility = View.VISIBLE
        }

        val productPrice = parseInt(data.Data?.productPrice.toString())
        val productSellPrice = parseInt(data.Data?.productSalePrice.toString())
        val youSavePrice = productPrice-productSellPrice

        val str : String = data.Data?.discount.toString()
        val float1 : Float = str.toFloat()
        val discount = DecimalFormat("#").format(float1)


        if(discount.equals("0")){
            viewDataBinding.txtDiscount.visibility= View.GONE
        }else{
            viewDataBinding.txtDiscount.text = resources.getString(R.string.rupees)+discount+"%"+"off"
        }


        data.Data?.productDescription?.let {
          //  viewDataBinding.txvDescriptionDetails.setHtml(it, HtmlHttpImageGetter( viewDataBinding.txvDescriptionDetails))
            viewDataBinding.txvDescriptionDetails.text = Html.toHtml(HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY))
        }

        data.Data?.addInfo?.let {
          //  viewDataBinding.txvAdditonDescription.setHtml(it, HtmlHttpImageGetter( viewDataBinding.txvAdditonDescription))
        }

        data.Data?.returnPolicy?.let {
            viewDataBinding.txvReturnProlicyDesicription.text = Html.toHtml(HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY))
           // viewDataBinding.txvReturnProlicyDesicription.setHtml(it, HtmlHttpImageGetter( viewDataBinding.txvReturnProlicyDesicription))
        }

        CommonUtils.loadImage(viewDataBinding.ivProductImage, data.Data?.productImages, viewDataBinding.ivProductImage.id)
        for (i in data.Data?.similarProducts?.indices!!) {
            lstSimilerProduct.addAll(data.Data!!.similarProducts.get(i))
        }

        similerProductsData(lstSimilerProduct,context)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun similerProductsData(lstSimilerProduct: ArrayList<SimilarProducts>, context: Context) {
        moreLikeSimilerProductAdapter.setSimilerProductList(lstSimilerProduct,context)
        viewDataBinding.rcvMoreLikeThisProducts.adapter = moreLikeSimilerProductAdapter
        moreLikeSimilerProductAdapter.notifyDataSetChanged()
    }


    override fun naviGateToMyCartFragment() {
        if (view != null) {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                viewDataBinding.llCheckPinCode.visibility = View.VISIBLE
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return
            }
        }

    }

    override fun addResponseCartItem() {
        navController?.navigate(R.id.action_categoryItemDetailsFragment_to_myCartFragment)
    }

    override fun naviGateToCheckOutAddressFragment() {
        if(view !=null){
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                }else{
                    navController?.navigate(R.id.action_categoryItemDetailsFragment_to_checkOutAddressFragment)
                }
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return
            }
        }

    }

    override fun itemIncrease() {
        counter++
        stringVal = counter.toString()
        viewDataBinding.txvQuantityNo.text = stringVal
    }

    override fun itemDecrease() {
        if(1== counter){
            stringVal = Integer.toString(counter)
            viewDataBinding.txvQuantityNo.text = stringVal
        }else{
            counter--
            stringVal = Integer.toString(counter)
            viewDataBinding.txvQuantityNo.text = stringVal
        }
    }

    override fun addWishList() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(PID)){
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
            }else{
                mViewModel.addWishListApiCall(pId)
            }
        }else if (mViewModel.navigator!!.isConnectedToInternet()){
            return
        }

    }

    override fun removeWishList() {
        mViewModel.removeWishListItemApiCall(pId)
    }

    override fun shearLink() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareBody = "Download this Application now:-https://play.google.com/store/search?q=whatsapp&h1=en"
        val shareSub = "Ecommerce App"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub)
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody)
        startActivity(Intent.createChooser(shareIntent,"Share Using"))
    }


    override fun addWishListResponse() {
        viewDataBinding.ivAddWishList.visibility = View.GONE
        viewDataBinding.ivRomveWishList.visibility = View.VISIBLE
    }


    override fun removeWishListItemResponse() {
        viewDataBinding.ivAddWishList.visibility = View.VISIBLE
        viewDataBinding.ivRomveWishList.visibility = View.GONE
        mViewModel.getSingleProductApiCall(pId)
    }

    override fun availabilityResponse() {
        viewDataBinding.llCheckPinCode.visibility = View.GONE
        if (CommonUtils.isStringNullOrBlank(PID)){
            val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
            loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
        }else{
            if (inCart == AppConstants.TRUE){
                navController?.navigate(R.id.action_categoryItemDetailsFragment_to_myCartFragment)
            }else{
                mViewModel.addCartItem(pId, counter)
            }
        }

    }

    private fun setUpBinding(){
        viewDataBinding.rcvMoreLikeThisProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
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
package com.uoons.india.ui.product_detail


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentProductDetailBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.category.category_items.category_items_details.model.AddItemToCartDataResponse
import com.uoons.india.ui.category.category_items.category_items_details.model.ProductAvailabilityResponseModel
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.my_cart.model.RemoveCartItemResponse
import com.uoons.india.ui.product_detail.adapter.*
import com.uoons.india.ui.product_detail.model.*
import com.uoons.india.ui.profile.model.UserDetailsModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


@Obfuscate
class ProductDetailFragment : BaseFragment<FragmentProductDetailBinding, ProductDetailFragmentVM>(),
    ProductDetailFragmentNavigator {
    private var logTag = "ProductDetailFragment"
    override val bindingVariable: Int = BR.productDetailFragmentVM
    override val layoutId: Int = R.layout.fragment_product_detail
    override val viewModelClass: Class<ProductDetailFragmentVM> =
        ProductDetailFragmentVM::class.java
    private var navController: NavController? = null
    private var pId: String = ""
    private var pImage: String = ""
    private var pName: String = ""
    private var outOfStock: Boolean = false
    private var questionsLikeUnlike: Boolean = false
    private var reviewLikeUnlike: Boolean = false
    var userCoins: String = ""
    var inWishList: String = ""
    var inCart: String = ""
    private lateinit var myImagesViewPagerAdapter: MyImagesViewPagerAdapter

    private lateinit var productDetailsAdditionalInfoAdapter: ProductDetailsAdditionalInfoAdapter

    private lateinit var productDetailsSalientFeaturesAdapter: ProductDetailsSalientFeaturesAdapter

    private lateinit var productDetailsDescriptionImageAdapter: ProductDetailsDescriptionImageAdapter

    private lateinit var moreProductDetailsMoreProductAdapter: MoreProductDetailsMoreProductAdapter

    private lateinit var productDetailsQuastionAnswerAdapter: ProductDetailsQuastionAnswerAdapter

    private lateinit var productDeatilsFrequentlyBouoghtAdapter: ProductDeatilsFrequentlyBouoghtAdapter

    private lateinit var productDeatilsRatingReviewAdapter: ProductDeatilsRatingReviewAdapter

    private var productAddInfo1: ArrayList<ProductAddInfoModel> = ArrayList()
    private var productAddInfo2: ArrayList<ProductAddInfoModel> = ArrayList()

    private var salientFeatureList1: ArrayList<String> = ArrayList()
    private var salientFeatureList2: ArrayList<String> = ArrayList()

    private var questionAnswerList: ArrayList<QuestionanswerModel> = ArrayList()
    private var reviewsModelList: ArrayList<ReviewsModel> = ArrayList()
    private var reviewsModelList1: ArrayList<ReviewsModel> = ArrayList()

    private var similerProductList: ArrayList<SimilarProductsModel> = ArrayList()
    private var similerMoreProductList1: ArrayList<SimilarProductsModel> = ArrayList()
    private var checkCartPIdList: ArrayList<String> = ArrayList()

    private var productMultipleImagesList: ArrayList<ImagesArrayListModel> = ArrayList()
    private var isExpanded = false
    private val MAX_LINES_COLLAPSED = 3

    override  fun init() {
        mViewModel.navigator = this
        viewDataBinding.txvPinCodeText.text = AppConstants.EMPTY
        pId = arguments?.getString(AppConstants.PId).toString()

        Log.d(logTag, "PRODUCT_ID:: $pId")

        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getSingleProductApiCall(pId)
            mViewModel.addRecentlyProductApiCall(pId)
            mViewModel.getUserDetailsApiCall()
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerProductDetailsLayout.stopShimmer()
        viewDataBinding.shimmerProductDetailsLayout.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        // Start shimmer animation
        viewDataBinding.shimmerProductDetailsLayout.startShimmer()

        if (AppPreference.getValue(PreferenceKeys.PROFILE_ID).isNotEmpty()) {
            mViewModel.getMyCartItemsApiCall()
            mViewModel.getWishListProductApiCall()
        }

        viewDataBinding.txvRecentlyViewed.visibility = View.GONE
        viewDataBinding.rcvRecentlyViewedProducts.visibility = View.GONE

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener {
            super.onBackClick()
        }

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                } else {
                    navController?.navigate(R.id.action_productDetailFragment_to_myCartFragment)
                }
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return@OnClickListener
            }
        })

        viewDataBinding.toolbar.ivHeartVector.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                } else {
                    navController?.navigate(R.id.action_productDetailFragment_to_wishListFragment)
                }
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return@OnClickListener
            }
        })

        viewDataBinding.rcvSalientFeatures.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewDataBinding.rcvDescriptionImages.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewDataBinding.rcvAdditionalInfo.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewDataBinding.rcvQuestionsAnswer.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewDataBinding.rcvFrequentlyProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewDataBinding.rcvProductsReview.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewDataBinding.ivMoreQuestionsAnswer.setOnClickListener {
            val bundle = bundleOf(AppConstants.PId to pId)
            navController?.navigate(R.id.action_productDetailFragment_to_productDetailsQuestionAndAnswerFragment, bundle)
        }

        viewDataBinding.cstLayoutSearchQuestions.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                val bundle = bundleOf(AppConstants.PId to pId)
                navController?.navigate(R.id.action_productDetailFragment_to_searchQuestionAnswerFragment, bundle)
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return@OnClickListener
            }
        })

        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PIN_CODE))) {
            viewDataBinding.crdCheckPinCode.visibility = View.VISIBLE
            viewDataBinding.edtEnterPincode.visibility = View.VISIBLE
        } else {
            viewDataBinding.txvPinCodeText.text = AppPreference.getValue(PreferenceKeys.PIN_CODE_EXPECTED_DELIVERY)
            viewDataBinding.edtEnterPincode.setText(AppPreference.getValue(PreferenceKeys.PIN_CODE))
            viewDataBinding.txvPinCodeText.visibility = View.VISIBLE
        }
    }

    override fun getSingleProductData() {
        if (view != null) {
            mViewModel.getSingleProductDataResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        setDataSingleProduct(it)

                    } else if (it.status.equals(AppConstants.FAILURE, ignoreCase = true)) {
//                        CommonUtils.showToastMessage(requireContext(), it.message.toString())
                    }
                } else {
                    CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data))
                }
            }
        }
    }

    override fun addWishList() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(childFragmentManager, logTag)

            } else {
                mViewModel.addWishListApiCall(pId.toInt())
                val logger = activity?.let { AppEventsLogger.newLogger(it) }
                val params = Bundle()
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, pId)
                params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, AppConstants.ECOMMERCE)
                params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, AppConstants.INR)
                logger?.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_WISHLIST, 10.0, params)

            }
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun shearLink() {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLongLink(Uri.parse(AppConstants.ShareDynamicLinkProduct + pId + AppConstants.NewShearLinkWebside + pId))
            .buildShortDynamicLink()
            .addOnCompleteListener(requireActivity()) { task ->
                try {
                    if (task.isSuccessful) {
                        // Short link created
                        val shortLink: Uri? = task.result.shortLink
                        val flowchartLink: Uri? = task.result.previewLink
                        Log.d(logTag, "shortLink:- $shortLink and flowchartLink:- $flowchartLink")

                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = AppConstants.TextPlain
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shortLink.toString())
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(Intent.createChooser(shareIntent, AppConstants.ShareUsing))
                    } else {
                        Log.d(logTag, "Else_Error:")
                    }
                } catch (e: Exception) {
                    Log.e(logTag, "catch===Error:-$e")
                }
            }
    }

    override fun writeAReview() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                loginMobileNoBottomSheet.show(childFragmentManager, logTag)
            } else {
                navController?.navigate(R.id.action_productDetailFragment_to_orderBundleFragment)
            }
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun removeWishList() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.removeWishListItemApiCall(pId.toInt())
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun addWishListResponse() {
        if (view != null) {
            mViewModel.addWishListResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        mViewModel.getWishListProductApiCall()
                        viewDataBinding.ivAddWishList.visibility = View.GONE
                        viewDataBinding.ivRomveWishList.visibility = View.VISIBLE
                    } else if (it.status.equals(AppConstants.FAILURE, ignoreCase = true)) {
//                        CommonUtils.showToastMessage(requireContext(), it.message.toString())
                    }
                } else {
                    CommonUtils.showToastMessage(
                        requireContext(),
                        resources.getString(R.string.error_in_fetching_data)
                    )
                }
            }
        }
    }

    override fun removeWishListItemResponse() {
        if (view != null) {
            mViewModel.removeWishListItemDataResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    removeWishlistResponseOnProduct(it)
                } else {
                    CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data))
                }
            }
        }
    }

    private fun removeWishlistResponseOnProduct(removeCartItemResponse: RemoveCartItemResponse) {
        if (removeCartItemResponse.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
            mViewModel.getWishListProductApiCall()
            viewDataBinding.ivAddWishList.visibility = View.VISIBLE
            viewDataBinding.ivRomveWishList.visibility = View.GONE
        } else {
            CommonUtils.showToastMessage(requireContext(), removeCartItemResponse.message.toString())
        }
    }


    override fun naviGateToCheckOutAddressFragment() {
        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PIN_CODE))) {
            context?.let { CommonUtils.showToastMessage(it, AppConstants.CheckLoacationAvailibilty) }

        } else {
            if (view != null) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    if (!outOfStock) {
                        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                            val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                            loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                        } else {
                            if (inCart == AppConstants.TRUE) {
                                context.let { CommonUtils.showToastMessage(requireContext(), AppConstants.AllReadyAddCartProduct) }

                            } else {
                                mViewModel.addPlaceOrder(pId.toInt(), AppConstants.one.toInt(), requireContext())
                            }
                        }
                    } else {
                        context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.out_of_stock_product)) }

                    }

                } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                    return
                }
            }
        }
    }

    override fun naviGateToMyCartFragment() {
        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PIN_CODE))) {
            context?.let { CommonUtils.showToastMessage(it, AppConstants.CheckLoacationAvailibilty) }

        } else {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (!outOfStock) {
                    if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                        val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                        loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                    } else {
                        if (inCart.equals(AppConstants.TRUE, ignoreCase = true)) {
                            CommonUtils.showToastMessage(requireContext(), AppConstants.AllReadyAddCartProduct)

                        } else {
                            mViewModel.addCartItem(pId.toInt(), AppConstants.one.toInt(), requireContext())
                        }
                    }
                } else {
                    CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.out_of_stock_product))

                }
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return
            }
        }
    }

    override fun placeOrderResponse() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            navController?.navigate(R.id.action_productDetailFragment_to_myCartFragment)
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun addResponseCartItem() {
        context.let { CommonUtils.showToastMessage(requireContext(), AppConstants.ProductAddInCart) }

        if (AppPreference.getValue(PreferenceKeys.PROFILE_ID).isNotEmpty()) {
            mViewModel.getMyCartItemsApiCall()
            mViewModel.getWishListProductApiCall()
        }
    }

    override fun addResponseFrequentCartItem() {
        if (view != null) {
            mViewModel.addToCartItemsFrequentResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    addToCartFrequentBought(it)
                } else { CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data)) }
            }
        }
    }

    private fun addToCartFrequentBought(addItemToCartDataResponse: AddItemToCartDataResponse) {
        if (addItemToCartDataResponse.Data == true) {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                context.let { CommonUtils.showToastMessage(requireContext(), AppConstants.AllReadyAddCartProduct) }
                if (AppPreference.getValue(PreferenceKeys.PROFILE_ID).isNotEmpty()) {
                    mViewModel.getMyCartItemsApiCall()
                    mViewModel.getWishListProductApiCall()
                }
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return
            }
        } else if (addItemToCartDataResponse.Data == false) {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                context.let { CommonUtils.showToastMessage(requireContext(), AppConstants.ProductAddInCart) }
                if (AppPreference.getValue(PreferenceKeys.PROFILE_ID).isNotEmpty()) {
                    mViewModel.getMyCartItemsApiCall()
                    mViewModel.getWishListProductApiCall()
                }
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return
            }
        }
    }

    override fun availabilityResponse() {
        if (view != null) {
            mViewModel.getProductLocationAvailabilityDataResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    setAvailibilityProduct(it)
                } else {
                    CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data))
                }
            }
        }
    }

    override fun suggestionToEnhanceClick() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            val bundle = bundleOf(AppConstants.PId to pId)
            navController?.navigate(R.id.action_productDetailFragment_to_suggestionForEnhanceFragment, bundle)
        } else if (mViewModel.navigator!!.isConnectedToInternet()) {
            return
        }
    }

    override fun checkPincodeAvailibility() {
        if (mViewModel.isValidField(strPinCode = viewDataBinding.edtEnterPincode.text.toString())) {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mViewModel.checkPinCodeAvailability(viewDataBinding.edtEnterPincode.text.toString().toInt(), pId.toInt())

            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return
            }
        }
    }

    private fun setAvailibilityProduct(productAvailabilityResponseModel: ProductAvailabilityResponseModel) {
        if (productAvailabilityResponseModel.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
            viewDataBinding.txvPinCodeText.text = productAvailabilityResponseModel.Data!!.expDeliveryTime
            AppPreference.addValue(PreferenceKeys.PIN_CODE_EXPECTED_DELIVERY, productAvailabilityResponseModel.Data!!.expDeliveryTime.toString())

        } else {
            CommonUtils.showToastMessage(requireContext(), productAvailabilityResponseModel.message.toString())
        }
    }

    private fun calculatePercentageChange(oldValue: Double, newValue: Double): Double {
        val percentageChange = ((newValue - oldValue) / oldValue) * 100
        return Math.abs(percentageChange)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setDataSingleProduct(data: ProductDetailsModel) {
        viewDataBinding.nstScrollView.visibility = View.VISIBLE
        viewDataBinding.nstScrollView.visibility = View.VISIBLE
        similerProductList.clear()
        similerMoreProductList1.clear()

        salientFeatureList1.clear()
        salientFeatureList2.clear()

        productAddInfo1.clear()
        productAddInfo2.clear()

        reviewsModelList.clear()
        reviewsModelList1.clear()

        pId = data.Data?.pid.toString()
        pImage = data.Data?.productImages.toString()
        pName = data.Data?.productName.toString()
        val price = data.Data?.productPrice.toString()
        val logger = activity?.let { AppEventsLogger.newLogger(it) }
        val params = Bundle()
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, pId)
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, pName)
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, price)
        logger?.logEvent(AppEventsConstants.EVENT_NAME_VIEWED_CONTENT, price.toDouble(), params)


        if (data.Data?.productStock?.toInt() == AppConstants.zero.toInt()) {
            viewDataBinding.txvOutOfStock.visibility = View.VISIBLE
            outOfStock = true
        } else {
            viewDataBinding.txvOutOfStock.visibility = View.GONE
        }

        viewDataBinding.toolbar.txvTitleName.text = data.Data?.productName
        viewDataBinding.txvProductName.text = data.Data?.productName

        viewDataBinding.txvAVGRating.text = data.Data?.rating?.rating.toString()

        viewDataBinding.txvRatingPoint.text = data.Data?.rating?.rating.toString()

        viewDataBinding.txvTotalRatings.text =
            data.Data?.rating?.total.toString() + AppConstants.Ratings

        viewDataBinding.txvTotalRatingOnProducts.text =
            data.Data?.rating?.total.toString() + AppConstants.Total

        viewDataBinding.txvFrequentlyBought.text = data.Data?.productName
        if (data.Data!!.images.size <= 0) {
            if (data.Data!!.productImages.isNullOrEmpty()) {
                viewDataBinding.ivFrequentlyBought.setImageResource(R.drawable.image_gray_color)
            } else {
                CommonUtils.catIoadImage(viewDataBinding.ivFrequentlyBought, data.Data!!.productImages, viewDataBinding.ivFrequentlyBought.id)
            }
        } else {
            if (data.Data!!.images[0].productImage.isNullOrEmpty()) {
                viewDataBinding.ivFrequentlyBought.setImageResource(R.drawable.image_gray_color)
            } else {
                CommonUtils.catIoadImage(viewDataBinding.ivFrequentlyBought, data.Data!!.images[0].productImage, viewDataBinding.ivFrequentlyBought.id)
            }
        }


        if (data.Data?.bestSelling.equals(AppConstants.zero)){
            viewDataBinding.txvBestSelling.setBackgroundColor(resources.getColor(R.color.transparent))
        } else if(data.Data?.bestSelling.equals(AppConstants.one)) {
            viewDataBinding.txvBestSelling.visibility = View.VISIBLE
            viewDataBinding.txvBestSelling.text = "BEST SELLING"
            viewDataBinding.txvBestSelling.setBackgroundColor(Color.parseColor(data.Data?.bestSellingColorCode.toString()))
        }

        viewDataBinding.txvOfferPrice.text =
            resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(data.Data?.productSalePrice?.toInt())
        viewDataBinding.txvMRPPrice.text =
            resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(data.Data?.productPrice?.toInt())
        if (data.Data?.description?.description.isNullOrEmpty()){
            viewDataBinding.txvDescriptionText.visibility = View.GONE
            viewDataBinding.ivMoreDecription.visibility = View.GONE
            viewDataBinding.viewDescription.visibility = View.GONE
        } else {
            viewDataBinding.txvDescriptionText.visibility = View.VISIBLE
            viewDataBinding.ivMoreDecription.visibility = View.VISIBLE
            viewDataBinding.viewDescription.visibility = View.VISIBLE
            viewDataBinding.txvDescription.text = data.Data?.description?.description
        }

        if (data.Data?.returnPolicy?.trim().isNullOrEmpty()){
            viewDataBinding.txvReturnPolicyText.visibility = View.GONE
        } else {
            viewDataBinding.txvReturnPolicyText.visibility = View.VISIBLE
            viewDataBinding.txvReturnPolicy.text = data.Data?.returnPolicy?.trim()
        }

        viewDataBinding.ratingBar.rating = data.Data?.rating?.rating?.toFloat()!!

        val productPrice = Integer.parseInt(data.Data?.productPrice.toString())
        val productSellPrice = Integer.parseInt(data.Data?.productSalePrice.toString())
        val youSavePrice = productPrice - productSellPrice

        val percentage = calculatePercentageChange(productPrice.toDouble(), productSellPrice.toDouble())

        val str: String = percentage.toString()
        val float1: Float = str.toFloat()
        val discount = DecimalFormat(AppConstants.hash).format(float1)

        if (youSavePrice.toString() == AppConstants.zero || youSavePrice.toString() == AppConstants.EMPTY) {
            viewDataBinding.txvYouSaveText.visibility = View.GONE
            viewDataBinding.txvYouSave.visibility = View.GONE

        } else {
            viewDataBinding.txvYouSave.text =
                resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                    .format(youSavePrice).toString() + " (" + discount + AppConstants.PERCENTAGE + ")"
        }

        inWishList = data.Data?.inWishlist.toString()
        inCart = data.Data?.inCart.toString()

        if (inWishList == AppConstants.TRUE) {
            viewDataBinding.ivRomveWishList.visibility = View.VISIBLE
            viewDataBinding.ivAddWishList.visibility = View.GONE
        } else {
            viewDataBinding.ivRomveWishList.visibility = View.GONE
            viewDataBinding.ivAddWishList.visibility = View.VISIBLE
        }

        if (data.Data!!.freqProd.isEmpty() || data.Data!!.freqProd.size == AppConstants.zero.toInt()) {
            viewDataBinding.crdFrequently.visibility = View.GONE
        }

        if (data.Data!!.reviews.isEmpty() || data.Data!!.reviews.size == AppConstants.zero.toInt()) {
            viewDataBinding.viewWriteAReview.visibility = View.GONE
            viewDataBinding.ivMoreReviews.visibility = View.GONE
        }

        if (data.Data!!.questionanswer.isEmpty() || data.Data!!.questionanswer.size == AppConstants.zero.toInt()) {
            viewDataBinding.txvQuestionAndAnswersText.visibility = View.GONE
            viewDataBinding.txvPostYourQuestion.visibility = View.VISIBLE
            viewDataBinding.ivMoreQuestionsAnswer.visibility = View.GONE
        }

        if (data.Data!!.advertisment?.image.isNullOrEmpty()) {
            viewDataBinding.txvSponserdName.visibility = View.GONE
            viewDataBinding.ivAdvertisement.visibility = View.GONE
        } else {
            viewDataBinding.txvSponserdName.text = data.Data?.advertisment!!.sponsoredBy
            CommonUtils.catIoadImage(
                viewDataBinding.ivAdvertisement,
                data.Data?.advertisment!!.image,
                viewDataBinding.ivAdvertisement.id
            )
        }

        viewDataBinding.ivAdvertisement.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                val bundle = bundleOf(
                    AppConstants.ParentId to AppConstants.BrandId,
                    AppConstants.SubId to data.Data?.advertisment!!.brandId,
                    AppConstants.CName to data.Data?.advertisment!!.sponsoredBy
                )
                navController?.navigate(R.id.action_productDetailFragment_to_productListFragment, bundle)

            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return@OnClickListener
            }
        })

        viewDataBinding.txtVariantText.visibility = View.GONE
        viewDataBinding.txvUseCode.visibility = View.GONE
        viewDataBinding.txvColorText.visibility = View.GONE

        // ---------Description images -----------------------
        if (data.Data?.description?.images.isNullOrEmpty()) {
            viewDataBinding.rcvDescriptionImages.visibility = View.GONE

        } else {

            productDetailsDescriptionImageAdapter = ProductDetailsDescriptionImageAdapter(data.Data?.description?.images, requireContext())
            viewDataBinding.rcvDescriptionImages.adapter = productDetailsDescriptionImageAdapter
            productDetailsDescriptionImageAdapter.notifyDataSetChanged()
        }

        // ---------Rating and Review --------------------
        if (data.Data!!.reviews.isEmpty() || data.Data!!.reviews.size == AppConstants.zero.toInt()) {
//            Log.e(LOG_TAG, "" + data.Data!!.reviews)
        } else {
            if (data.Data!!.reviews.size > 2) {
                reviewsModelList.clear()
                for (i in 0..2) {
                    reviewsModelList.add(data.Data!!.reviews[i])
                }
                productDeatilsRatingReviewAdapter =
                    ProductDeatilsRatingReviewAdapter(reviewsModelList, requireContext(),
                        onclickThumbUpReview = {
                            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                                loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                            } else {
                                reviewLikeUnlike = true
                                questionsLikeUnlike = true
                                mViewModel.likeUnlikeReviewAPICall(it, AppConstants.one)
                            }
                        },
                        onclickThumbDownReview = {
                            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                                loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                            } else {
                                reviewLikeUnlike = true
                                questionsLikeUnlike = true
                                mViewModel.likeUnlikeReviewAPICall(it, AppConstants.zero)
                            }
                        })
                viewDataBinding.rcvProductsReview.adapter = productDeatilsRatingReviewAdapter
                productDeatilsRatingReviewAdapter.notifyDataSetChanged()
                viewDataBinding.ivMoreReviews.visibility = View.VISIBLE
            } else {
                productDeatilsRatingReviewAdapter =
                    ProductDeatilsRatingReviewAdapter(data.Data?.reviews, requireContext(),
                        onclickThumbUpReview = {
                            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                                loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                            } else {
                                reviewLikeUnlike = true
                                questionsLikeUnlike = true
                                mViewModel.likeUnlikeReviewAPICall(it, AppConstants.one)
                            }
                        },
                        onclickThumbDownReview = {
                            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                                loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                            } else {
                                reviewLikeUnlike = true
                                questionsLikeUnlike = true
                                mViewModel.likeUnlikeReviewAPICall(it, AppConstants.zero)
                            }
                        })
                viewDataBinding.rcvProductsReview.adapter = productDeatilsRatingReviewAdapter
                productDeatilsRatingReviewAdapter.notifyDataSetChanged()
                viewDataBinding.ivMoreReviews.visibility = View.GONE
            }
        }

        // ---------Review more click
        viewDataBinding.ivMoreReviews.setOnClickListener {
            if (resources.getString(R.string.more) == viewDataBinding.txvRatingReview.text.toString()) {
                reviewsModelList1.clear()
                for (i in data.Data!!.reviews.indices) {
                    reviewsModelList1.add(data.Data!!.reviews[i])
                }
                productDeatilsRatingReviewAdapter = ProductDeatilsRatingReviewAdapter(
                    reviewsModelList1,
                    requireContext(),
                    onclickThumbUpReview = {
                        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                            val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                            loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                        } else {
                            reviewLikeUnlike = true
                            questionsLikeUnlike = true
                            mViewModel.likeUnlikeReviewAPICall(it, AppConstants.one)
                        }
                    },
                    onclickThumbDownReview = {
                        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                            val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                            loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                        } else {
                            reviewLikeUnlike = true
                            questionsLikeUnlike = true
                            mViewModel.likeUnlikeReviewAPICall(it, AppConstants.zero)
                        }
                    })
                viewDataBinding.rcvProductsReview.adapter = productDeatilsRatingReviewAdapter
                productDeatilsRatingReviewAdapter.notifyDataSetChanged()
                viewDataBinding.txvRatingReview.text = resources.getString(R.string.less)
            } else {
                productDeatilsRatingReviewAdapter = ProductDeatilsRatingReviewAdapter(
                    reviewsModelList,
                    requireContext(),
                    onclickThumbUpReview = {
                        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                            val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                            loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                        } else {
                            reviewLikeUnlike = true
                            questionsLikeUnlike = true
                            mViewModel.likeUnlikeReviewAPICall(it, AppConstants.one)
                        }
                    },
                    onclickThumbDownReview = {
                        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                            val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                            loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                        } else {
                            reviewLikeUnlike = true
                            questionsLikeUnlike = true
                            mViewModel.likeUnlikeReviewAPICall(it, AppConstants.zero)
                        }
                    })
                viewDataBinding.rcvProductsReview.adapter = productDeatilsRatingReviewAdapter
                productDeatilsRatingReviewAdapter.notifyDataSetChanged()
                viewDataBinding.txvRatingReview.text = resources.getString(R.string.more)
            }
        }

        // ---------Description more click
        viewDataBinding.ivMoreDecription.setOnClickListener {
            if (resources.getString(R.string.more) == viewDataBinding.txvMoreDecription.text.toString()) {
                viewDataBinding.txvDescription.maxLines = Integer.MAX_VALUE //your TextView
                viewDataBinding.txvMoreDecription.text = resources.getString(R.string.less)
            } else {
                viewDataBinding.txvDescription.maxLines = 4 //your TextView
                viewDataBinding.txvMoreDecription.text = resources.getString(R.string.more)
            }
        }

        productMultipleImagesList.clear()
        for (i in data.Data!!.images.indices) {
            productMultipleImagesList.add(data.Data!!.images[i])
        }

        myImagesViewPagerAdapter =
            MyImagesViewPagerAdapter(data.Data!!.images, requireContext(), onClick = {
                val value = Bundle()
                value.putSerializable("imagesList", productMultipleImagesList)
                navController?.navigate(R.id.action_productDetailFragment_to_fullScreenImageShowFragment, value)
            })

        viewDataBinding.productImageSlider.adapter = myImagesViewPagerAdapter
        if (data.Data!!.images.size > 1){
            TabLayoutMediator(viewDataBinding.dotsLayout1,viewDataBinding.productImageSlider, TabLayoutMediator.TabConfigurationStrategy { tab, position -> }).attach()
        } else {
            viewDataBinding.dotsLayout1.visibility = View.GONE
        }


        viewDataBinding.productImageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        //---- Quations And Answers--------------------
        if (data.Data!!.questionanswer.isEmpty() || data.Data!!.questionanswer.size == AppConstants.zero.toInt()) {
//            Log.e(LOG_TAG, "" + data.Data!!.questionanswer)
        } else {
            if (data.Data!!.questionanswer.size > 4) {
                questionAnswerList.clear()
                for (i in 0..3) {
                    questionAnswerList.add(data.Data!!.questionanswer[i])
                }
                productDetailsQuastionAnswerAdapter = ProductDetailsQuastionAnswerAdapter(
                    questionAnswerList,
                    requireContext(),
                    onclickThumbUp = {
                        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                            val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                            loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                        } else {
                            questionsLikeUnlike = true
                            reviewLikeUnlike = true
                            mViewModel.likeUnlikeQuestionAPICall(it, AppConstants.one)
                        }
                    },
                    onclickThumbDown = {
                        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                            val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                            loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                        } else {
                            questionsLikeUnlike = true
                            reviewLikeUnlike = true
                            mViewModel.likeUnlikeQuestionAPICall(it, AppConstants.zero)
                        }
                    })
                viewDataBinding.rcvQuestionsAnswer.adapter = productDetailsQuastionAnswerAdapter
                productDetailsQuastionAnswerAdapter.notifyDataSetChanged()
            } else {
                productDetailsQuastionAnswerAdapter =
                    ProductDetailsQuastionAnswerAdapter(data.Data!!.questionanswer,
                        requireContext(),
                        onclickThumbUp = {
                            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                                loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                            } else {
                                questionsLikeUnlike = true
                                reviewLikeUnlike = true
                                mViewModel.likeUnlikeQuestionAPICall(it, AppConstants.one)
                            }
                        },
                        onclickThumbDown = {
                            if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                                val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                                loginMobileNoBottomSheet.show(childFragmentManager, logTag)
                            } else {
                                questionsLikeUnlike = true
                                reviewLikeUnlike = true
                                mViewModel.likeUnlikeQuestionAPICall(it, AppConstants.zero)
                            }
                        })
                viewDataBinding.rcvQuestionsAnswer.adapter = productDetailsQuastionAnswerAdapter
                productDetailsQuastionAnswerAdapter.notifyDataSetChanged()
            }
        }

        //-------- Frequent Boughtly ----------
        if (data.Data!!.freqProd.isEmpty()) {
            viewDataBinding.crdFrequently.visibility = View.GONE
        } else {
            if (reviewLikeUnlike || questionsLikeUnlike) {
                Log.d(logTag, "reviewLikeUnlike || questionsLikeUnlike :: $reviewLikeUnlike :: $questionsLikeUnlike")

            } else {
                reviewLikeUnlike = false
                questionsLikeUnlike = false
                productDeatilsFrequentlyBouoghtAdapter =
                    ProductDeatilsFrequentlyBouoghtAdapter(
                        data.Data!!.freqProd,
                        requireContext(),
                        onclick = {
                            if (mViewModel.navigator!!.checkIfInternetOn()) {
                                if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PIN_CODE))) {
                                    context.let { CommonUtils.showToastMessage(requireContext(), AppConstants.CheckLoacationAvailibilty) }

                                } else {
                                    if (!outOfStock) {
                                        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                                            val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                                            loginMobileNoBottomSheet.show(childFragmentManager, logTag)

                                        } else {
                                            mViewModel.addFrequentBoughtCartItem(it.toInt(), AppConstants.one.toInt(), requireContext())

                                        }
                                    } else {
                                        CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.out_of_stock_product))
                                    }
                                }
                            } else {
                                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})

                            }
                        },
                        onclick1 = {
                            if (mViewModel.navigator!!.checkIfInternetOn()) {
                                val bundle = bundleOf(AppConstants.PId to it)
                                navController?.navigate(R.id.action_productDetailFragment_self, bundle)

                            } else {
                                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})

                            }
                        },
                        userCoins
                    )
                viewDataBinding.rcvFrequentlyProducts.adapter =
                    productDeatilsFrequentlyBouoghtAdapter
                productDeatilsFrequentlyBouoghtAdapter.notifyDataSetChanged()
            }
        }

        // -------- More product Show Adapter------------
        if (data.Data!!.similarProducts.isEmpty() || data.Data!!.similarProducts.size == 0) {
            Log.d(logTag, "Similar Products Empty" + data.Data!!.similarProducts)
        } else {
            if (questionsLikeUnlike || reviewLikeUnlike) {
                Log.d(logTag, "Similar Products:: " + data.Data!!.similarProducts)
            } else {
                questionsLikeUnlike = false
                reviewLikeUnlike = false
                if (data.Data!!.similarProducts.size > 4) {
                    viewDataBinding.txvClickMoreProducts.visibility = View.VISIBLE
                    salientFeatureList1.clear()
                    for (i in 0..3) {
                        similerProductList.add(data.Data!!.similarProducts[i])
                    }

                    moreProductDetailsMoreProductAdapter =
                        MoreProductDetailsMoreProductAdapter(
                            similerProductList,
                            requireContext(),
                            onclick = {
                                if (mViewModel.navigator!!.checkIfInternetOn()) {
                                    val bundle = bundleOf(AppConstants.PId to it)
                                    navController?.navigate(R.id.action_productDetailFragment_self, bundle)

                                } else {
                                    mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
                                }
                            })
                    viewDataBinding.rcvMoreLikeThisProducts.adapter = moreProductDetailsMoreProductAdapter
                    moreProductDetailsMoreProductAdapter.notifyDataSetChanged()
                } else {
                    moreProductDetailsMoreProductAdapter = MoreProductDetailsMoreProductAdapter(
                        data.Data!!.similarProducts,
                        requireContext(),
                        onclick = {
                            if (mViewModel.navigator!!.checkIfInternetOn()) {
                                val bundle = bundleOf(AppConstants.PId to it)
                                navController?.navigate(R.id.action_productDetailFragment_self, bundle)
                            } else {
                                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,
                                    resources.getString(R.string.please_check_internet_connection),
                                    onClick = {})
                            }
                        })
                    viewDataBinding.rcvMoreLikeThisProducts.adapter = moreProductDetailsMoreProductAdapter
                    moreProductDetailsMoreProductAdapter.notifyDataSetChanged()
                    viewDataBinding.txvClickMoreProducts.visibility = View.GONE
                }
            }
        }


        viewDataBinding.txvClickMoreProducts.setOnClickListener {
            similerMoreProductList1.clear()
            viewDataBinding.txvClickMoreProducts.visibility = View.GONE
            val insertIndex = 4

            for (i in 4 until data.Data!!.similarProducts.size) {
                similerProductList.add(insertIndex, data.Data!!.similarProducts[i])
            }

            moreProductDetailsMoreProductAdapter.notifyItemRangeInserted(
                insertIndex,
                similerProductList.size
            )
        }

        data.Data!!.salientFeatures.removeAll(listOf(null,""))
        if (data.Data!!.salientFeatures.isEmpty() || data.Data!!.salientFeatures.size == AppConstants.zero.toInt()) {
            viewDataBinding.txvSaliantFeatures.visibility = View.GONE
            viewDataBinding.crdSalientFeatures.visibility = View.GONE
            viewDataBinding.viewsalientFeature.visibility = View.GONE
        } else {
            data.Data!!.salientFeatures.removeAll(listOf(null,""))
            if (data.Data!!.salientFeatures.size > 4) {
                salientFeatureList1.clear()

                for (i in 0..3) {
                    salientFeatureList1.add(data.Data!!.salientFeatures[i])
                }

                if (salientFeatureList1[0].isEmpty()){
                    viewDataBinding.txvSaliantFeatures.visibility = View.GONE
                    viewDataBinding.crdSalientFeatures.visibility = View.GONE
                    viewDataBinding.viewsalientFeature.visibility = View.GONE
                } else {
                    productDetailsSalientFeaturesAdapter = ProductDetailsSalientFeaturesAdapter(salientFeatureList1, requireContext())
                    viewDataBinding.rcvSalientFeatures.adapter = productDetailsSalientFeaturesAdapter
                    productDetailsSalientFeaturesAdapter.notifyDataSetChanged()
                }

            } else {
                productDetailsSalientFeaturesAdapter = ProductDetailsSalientFeaturesAdapter(data.Data!!.salientFeatures, requireContext())
                viewDataBinding.rcvSalientFeatures.adapter = productDetailsSalientFeaturesAdapter
                productDetailsSalientFeaturesAdapter.notifyDataSetChanged()
                viewDataBinding.crdSalientFeatures.visibility = View.GONE

            }
        }

        //----------- Salient Features more click-----------------
        viewDataBinding.crdSalientFeatures.setOnClickListener {
            salientFeatureList2.clear()
            if (resources.getString(R.string.more) == viewDataBinding.txvSalientFeatures.text.toString()) {
                for (i in data.Data!!.salientFeatures.indices) {
                    salientFeatureList2.add(data.Data!!.salientFeatures[i])
                }
                productDetailsSalientFeaturesAdapter =
                    ProductDetailsSalientFeaturesAdapter(salientFeatureList2, requireContext())
                viewDataBinding.rcvSalientFeatures.adapter = productDetailsSalientFeaturesAdapter
                productDetailsSalientFeaturesAdapter.notifyDataSetChanged()
                viewDataBinding.txvSalientFeatures.text = resources.getString(R.string.less)
            } else {
                viewDataBinding.txvSalientFeatures.text = resources.getString(R.string.more)
                productDetailsSalientFeaturesAdapter =
                    ProductDetailsSalientFeaturesAdapter(salientFeatureList1, requireContext())
                viewDataBinding.rcvSalientFeatures.adapter = productDetailsSalientFeaturesAdapter
                productDetailsSalientFeaturesAdapter.notifyDataSetChanged()
            }
        }

        if (data.Data!!.addInformation.isEmpty() || data.Data!!.addInformation.size == AppConstants.zero.toInt()) {
            viewDataBinding.txvAdditionalInfoText.visibility = View.GONE
            viewDataBinding.ivAdditionalInfo.visibility = View.GONE
            viewDataBinding.viewAdditionalIndo.visibility = View.GONE
        } else {
            if (data.Data!!.addInformation.size > 5) {
                productAddInfo1.clear()
                for (i in 0..5) {
                    productAddInfo1.add(data.Data!!.addInformation[i])
                }

                if (productAddInfo1[0].title.isNullOrEmpty()){
                    viewDataBinding.txvAdditionalInfoText.visibility = View.GONE
                    viewDataBinding.ivAdditionalInfo.visibility = View.GONE
                    viewDataBinding.viewAdditionalIndo.visibility = View.GONE
                } else {
                    productDetailsAdditionalInfoAdapter = ProductDetailsAdditionalInfoAdapter(productAddInfo1, requireContext())
                    viewDataBinding.rcvAdditionalInfo.adapter = productDetailsAdditionalInfoAdapter
                    productDetailsAdditionalInfoAdapter.notifyDataSetChanged()
                }
            } else {
                productDetailsAdditionalInfoAdapter = ProductDetailsAdditionalInfoAdapter(data.Data!!.addInformation, requireContext())
                viewDataBinding.rcvAdditionalInfo.adapter = productDetailsAdditionalInfoAdapter
                productDetailsAdditionalInfoAdapter.notifyDataSetChanged()
                viewDataBinding.ivAdditionalInfo.visibility = View.GONE
            }
        }

        //----------- Addititonal more click--------------------
        viewDataBinding.ivAdditionalInfo.setOnClickListener {
            if (resources.getString(R.string.more) == viewDataBinding.txvAdditionalInfo.text.toString()) {
                productAddInfo2.clear()
                for (i in data.Data!!.addInformation.indices) {
                    productAddInfo2.add(data.Data!!.addInformation[i])
                }

                productDetailsAdditionalInfoAdapter = ProductDetailsAdditionalInfoAdapter(productAddInfo2, requireContext())
                viewDataBinding.rcvAdditionalInfo.adapter = productDetailsAdditionalInfoAdapter
                productDetailsAdditionalInfoAdapter.notifyDataSetChanged()
                viewDataBinding.txvAdditionalInfo.text = resources.getString(R.string.less)

            } else {
                viewDataBinding.txvAdditionalInfo.text = resources.getString(R.string.more)
                productDetailsAdditionalInfoAdapter = ProductDetailsAdditionalInfoAdapter(productAddInfo1, requireContext())
                viewDataBinding.rcvAdditionalInfo.adapter = productDetailsAdditionalInfoAdapter
                productDetailsAdditionalInfoAdapter.notifyDataSetChanged()

            }
        }

        viewDataBinding.shimmerProductDetailsLayout.visibility = View.GONE
    }

    override fun getMyCartItemsResponse() {
        if (view != null) {
            mViewModel.getMyCartItemsDataResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    setMyCartItemsAdapterData(it)
                }
            }
        }
    }

    override fun likeUnlikeQuestionResponse() {
        if (view != null) {
            mViewModel.getLikeUnlikeQuestionsResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        questionLikeUnlike()
                    } else {
                        CommonUtils.showToastMessage(requireContext(), it.message.toString())
                    }
                }
            }
        }
    }

    override fun getUserCoins() {
        if (view != null) {
            mViewModel.getUserDetailsResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        getUsersCoins(it)
                    }
                } else {
                    CommonUtils.showToastMessage(
                        requireContext(),
                        resources.getString(R.string.error_in_fetching_data)
                    )
                }
            }
        }
    }

    private fun getUsersCoins(userDetailsModel: UserDetailsModel) {
        userCoins = userDetailsModel.Data[0].coins.toString()
    }


    private fun questionLikeUnlike() {
        mViewModel.getRefreshSingleProductApiCall(pId)
        CommonUtils.showToastMessage(requireContext(), "Thank you")
    }


    private fun setMyCartItemsAdapterData(data: GetMyCartDataModel) {
        if (data.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
            checkCartPIdList.clear()
            for (i in data.Data?.items?.indices!!) {
                data.Data?.items!![i].pid?.let { checkCartPIdList.add(it) }
            }
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountMyCartItems.text = data.Data?.itemCount.toString()
        } else {
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE
        }
    }

    override fun getWishListResponse() {
        if (view != null) {
            mViewModel.getWishListDataResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    setWishListData(it)
                } else {
                    CommonUtils.showToastMessage(
                        requireContext(),
                        resources.getString(R.string.error_in_fetching_data)
                    )
                }
            }
        }
    }

    override fun getWishListFailureResponse() {
        mViewModel.getWishListDataFailureResponse.observe(viewLifecycleOwner) {
            if (it != null) {
//                Log.e(LOG_TAG, "getWishListFailureResponse====" + it.message.toString())
            } else {
                CommonUtils.showToastMessage(
                    requireContext(),
                    resources.getString(R.string.error_in_fetching_data)
                )
            }
        }
    }

    private fun setWishListData(getWishListDataModel: GetWishListDataModel) {
        if (getWishListDataModel.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
            viewDataBinding.toolbar.crdCountWishList.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountWishList.text =
                getWishListDataModel.Data.size.toString()
        } else {
            viewDataBinding.toolbar.crdCountWishList.visibility = View.GONE
        }
    }


/*
    fun getEncryptedSharedprefs(context: Context): SharedPreferences {

        val masterkeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "secured_prefs", masterkeyAlias, context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }
*/

}
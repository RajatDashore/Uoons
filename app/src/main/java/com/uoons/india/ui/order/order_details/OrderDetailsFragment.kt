package com.uoons.india.ui.order.order_details

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentOrderDetailsBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.order.order_details.adapter.SimilersProductOrderAdapter
import com.uoons.india.ui.order.order_details.model.OrderCancelModel
import com.uoons.india.ui.order.order_details.model.OrderDetailModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

@Obfuscate
class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding, OrderDetailsFragmentVM>(),
    OrderDetailsFragmentNavigator {
    private var LOG_TAG = "OrderDetailsFragment"
    override val bindingVariable: Int = BR.orderDetailsFragmentVM
    override val layoutId: Int = R.layout.fragment_order_details
    override val viewModelClass: Class<OrderDetailsFragmentVM> = OrderDetailsFragmentVM::class.java
    private var navController: NavController? = null
    private var orderId: String = ""
    private var ratingProduct: Float = 0.0f
    private var pId: String = ""
    private var pName: String = ""
    private var pImage: String = ""
    private var mesg: String = ""
    private var isSelected: Boolean = false
    private lateinit var similersProductOrderAdapter: SimilersProductOrderAdapter
    var PID :String = ""

    override fun init() {
        mViewModel.navigator = this
        orderId = arguments?.getString(AppConstants.PId).toString()

        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getAllOrdersList(orderId)
            // Start shimmer animation
            viewDataBinding.shimmerOrderDetailsLayout.startShimmer()
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerOrderDetailsLayout.stopShimmer()
        viewDataBinding.shimmerOrderDetailsLayout.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.order_details)
        PID =    AppPreference.getValue(PreferenceKeys.PROFILE_ID)
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        viewDataBinding.llCancelProduct.setOnClickListener(View.OnClickListener {
            showCheckUpdateDialog()
        })

        if (PID.isNotEmpty()) {
            mViewModel.getMyCartItemsApiCall()
            mViewModel.getWishListProductApiCall()
        }

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener {
            super.onBackClick()
        })

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener {
            navController?.navigate(R.id.action_orderDetailsFragment_to_myCartFragment)
        })

        viewDataBinding.toolbar.ivHeartVector.setOnClickListener(View.OnClickListener {
            navController?.navigate(R.id.action_orderDetailsFragment_to_wishListFragment)
        })

        viewDataBinding.rcvIntrestedItems.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }


        viewDataBinding.txvWriteAReview.setOnClickListener(View.OnClickListener {
//            Log.e(LOG_TAG, "Rating_on_this_product:- $ratingProduct")
            val bundle = bundleOf(
                AppConstants.PId to pId,
                AppConstants.ratingProduct to ratingProduct,
                AppConstants.pName to pName,
                AppConstants.pImage to pImage
            )
            navController?.navigate(
                R.id.action_orderDetailsFragment_to_orderReviewRatingFragment,
                bundle
            )
        })
    }

    override fun orderResponse() {
        if (view != null) {
            mViewModel.getOrderResponse.observe(viewLifecycleOwner, Observer<OrderDetailModel> {
                if (it != null) {
                    setOrderDetailsData(it)
                } else {
                    context?.let {
                        CommonUtils.showToastMessage(
                            it,
                            AppConstants.ErrorInFetchingData
                        )
                    }
                }
            })
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat", "NotifyDataSetChanged")
    private fun setOrderDetailsData(orderDetailModel: OrderDetailModel) {
        pId = orderDetailModel.Data?.pid.toString()
        pName = orderDetailModel.Data?.productName.toString()

        if (orderDetailModel.Data?.rating.isNullOrEmpty()) {
            viewDataBinding.productRating.onRatingBarChangeListener =
                OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    ratingProduct = rating
                    viewDataBinding.txvWriteAReview.visibility = View.VISIBLE
                }
        } else {
            viewDataBinding.productRating.setIsIndicator(true)
            viewDataBinding.productRating.rating = orderDetailModel.Data?.rating?.toFloat()!!
        }

        viewDataBinding.crdProductOrderDetails.setOnClickListener(View.OnClickListener {
            val bundle = bundleOf(AppConstants.PId to pId)
            navController?.navigate(
                R.id.action_orderDetailsFragment_to_productDetailFragment,
                bundle
            )
        })

        viewDataBinding.txvShowMoreDetailsOfProducts.setOnClickListener {
            val bundle = bundleOf(AppConstants.trackerId to pId)
            navController?.navigate(
                R.id.action_orderDetailsFragment_to_orderTrackerFragment,
                bundle
            )
        }

        viewDataBinding.orderId.text = orderDetailModel.Data?.bundleid
        viewDataBinding.txvProductName.text = orderDetailModel.Data?.productName
        val productSalePrice = orderDetailModel.Data?.productSalePrice?.toDouble()?.roundToInt()
        viewDataBinding.txtProductPrice.text =
            requireContext().getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(productSalePrice)

        if (orderDetailModel.Data?.productImages.isNullOrEmpty()) {
            viewDataBinding.ivProductImage.setImageResource(R.drawable.image_gray_color)
        } else {
            pImage = orderDetailModel.Data?.productImages.toString()
            CommonUtils.loadImage(
                viewDataBinding.ivProductImage,
                orderDetailModel.Data?.productImages,
                viewDataBinding.ivProductImage.id
            )
        }

        when {
            orderDetailModel.Data?.paymentStatus.equals(
                AppConstants.TXN_FAILURE,
                ignoreCase = true
            ) -> {
                viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_close)
                viewDataBinding.txtOrderedText.text = AppConstants.TXN_FAILURE
                viewDataBinding.llCancelProduct.visibility = View.GONE
            }
            orderDetailModel.Data?.paymentStatus.equals(
                AppConstants.TXN_SUCCESS,
                ignoreCase = true
            ) -> {
                when (orderDetailModel.Data?.status) {
                    AppConstants.zero -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Pending
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_baseline_history_24)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.one -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Pending
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_baseline_history_24)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.two -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Dispatch
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.dispatch)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.three -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Hold
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.hold)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.four -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Delivered
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.delivered)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.five -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Cancel
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_close)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.six -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Refund
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.refunds)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.seven -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Failed
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_close)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.eight -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.On_the_way
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.dispatch)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.nine -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Delivered_soon
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_settings)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                }
            }
            orderDetailModel.Data?.paymentStatus.equals(
                AppConstants.Success,
                ignoreCase = true
            ) -> {
                when (orderDetailModel.Data?.status) {
                    AppConstants.zero -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Pending
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_baseline_history_24)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.one -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Pending
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_baseline_history_24)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.two -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Dispatch
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.dispatch)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.three -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Hold
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.hold)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.four -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Delivered
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.delivered)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.five -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Cancel
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_close)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.six -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Refund
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.refunds)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.seven -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Failed
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_close)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.eight -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.On_the_way
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.dispatch)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.nine -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Delivered_soon
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_settings)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                }
            }
            orderDetailModel.Data?.paymentStatus.equals(
                AppConstants.Pending,
                ignoreCase = true
            ) -> {
                when (orderDetailModel.Data?.status) {
                    AppConstants.zero -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Pending
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_baseline_history_24)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.one -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Pending
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_baseline_history_24)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.two -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Dispatch
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.dispatch)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.three -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Hold
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.hold)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.four -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Delivered
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.delivered)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.five -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Cancel
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_close)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.six -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Refund
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.refunds)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.seven -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Failed
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_close)
                        viewDataBinding.llCancelProduct.visibility = View.GONE
                    }
                    AppConstants.eight -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.On_the_way
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.dispatch)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                    AppConstants.nine -> {
                        viewDataBinding.txtOrderedText.text = AppConstants.Delivered_soon
                        viewDataBinding.ivOrderSuccess.setImageResource(R.drawable.ic_settings)
                        viewDataBinding.llCancelProduct.visibility = View.VISIBLE
                    }
                }
            }
        }

        val formatInput = SimpleDateFormat(AppConstants.dateFormateIn)
        val formatOutput = SimpleDateFormat(AppConstants.dateFormateOut)

        val date: Date = formatInput.parse(orderDetailModel.Data?.createdAt)
        val dateString: String = formatOutput.format(date)
        viewDataBinding.txvOrderDate.text = dateString

        viewDataBinding.txvPaymentMode.text = orderDetailModel.Data?.paymentMethod
        val productSellingPrice = orderDetailModel.Data?.productPrice?.toDouble()?.roundToInt()
        viewDataBinding.txvSellingPrice.text =
            resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(productSellingPrice)
        val productOfferPrice = orderDetailModel.Data?.productSalePrice?.toDouble()?.roundToInt()
        viewDataBinding.txvOfferPrice.text =
            resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(productOfferPrice)
        val productPrice = Integer.parseInt(orderDetailModel.Data?.productPrice.toString())
        val productSellPrice = Integer.parseInt(orderDetailModel.Data?.productSalePrice.toString())
        val savePrice = productPrice - productSellPrice
        val youSavePrice = abs(savePrice)

        viewDataBinding.txvDiscount.text =
            "- " + resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(youSavePrice)

        if (orderDetailModel.Data?.shippingCharges.equals("0") || orderDetailModel.Data?.shippingCharges.isNullOrEmpty()) {
            viewDataBinding.txvShippingFee.text =
                resources.getString(R.string.rupees) + orderDetailModel.Data?.shippingCharges
        } else {
            viewDataBinding.txvShippingFee.text =
                "+ " + resources.getString(R.string.rupees) + orderDetailModel.Data?.shippingCharges
        }

        val productTotalPrice = orderDetailModel.Data?.TotalAmount?.toDouble()?.roundToInt()
        viewDataBinding.txvTotalPrice.text =
            resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(productTotalPrice)

        viewDataBinding.txvPaymentMdName.text = orderDetailModel.Data?.paymentMethod
        val productModePrice = orderDetailModel.Data?.TotalAmount?.toDouble()?.roundToInt()
        viewDataBinding.txvModePrice.text =
            resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(productModePrice)

        viewDataBinding.txvName.text = orderDetailModel.Data?.bname
        viewDataBinding.txvAddress1.text = orderDetailModel.Data?.baddress1
        viewDataBinding.txvAddress2.text = orderDetailModel.Data?.baddress2
        viewDataBinding.txvCity.text = orderDetailModel.Data?.bcity
        viewDataBinding.txvState.text = orderDetailModel.Data?.bstate + " - "
        viewDataBinding.txvPincode.text = orderDetailModel.Data?.bpincode
        viewDataBinding.txvMobileNumber.text =
            AppConstants.phoneNumber + orderDetailModel.Data?.bmobileNo

        similersProductOrderAdapter =
            orderDetailModel.Data?.similarProducts.let {
                it?.let { it1 ->
                    SimilersProductOrderAdapter(it1, requireContext()) {
                        if (mViewModel.navigator!!.checkIfInternetOn()) {
                            val bundle = bundleOf(AppConstants.PId to it)
                            navController?.navigate(
                                R.id.action_orderDetailsFragment_to_productDetailFragment,
                                bundle
                            )
                        } else {
                            mViewModel.navigator!!.showAlertDialog1Button(
                                AppConstants.Uoons,
                                resources.getString(R.string.please_check_internet_connection),
                                onClick = {})
                        }
                    }
                }
            }!!
        viewDataBinding.rcvIntrestedItems.adapter = similersProductOrderAdapter
        similersProductOrderAdapter.notifyDataSetChanged()
        viewDataBinding.nestedScrollView.visibility = View.VISIBLE
        viewDataBinding.shimmerOrderDetailsLayout.visibility = View.GONE
    }

    override fun getMyCartItemsResponse() {
        if (view != null) {
            mViewModel.getMyCartItemsDataResponse.observe(
                viewLifecycleOwner,
                Observer<GetMyCartDataModel> {
                    if (it != null) {
                        setMyCartItemsAdapterData(it)
                    }
                })
        }
    }

    private fun setMyCartItemsAdapterData(data: GetMyCartDataModel) {
        if (data.status.equals(AppConstants.SUCCESS)) {
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountMyCartItems.text = data.Data?.itemCount.toString()
        } else {
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE
        }
    }

    override fun getWishListResponse() {
        if (view != null) {
            mViewModel.getWishListDataResponse.observe(
                viewLifecycleOwner,
                Observer<GetWishListDataModel> {
                    if (it != null) {
                        setWishListData(it)
                    } else {
                        context?.let {
                            CommonUtils.showToastMessage(
                                it,
                                AppConstants.ErrorInFetchingData
                            )
                        }
                    }
                })
        }
    }

    override fun orderCancelResponse() {
        if (view != null) {
            mViewModel.getOrderDataResponse.observe(viewLifecycleOwner, Observer<OrderCancelModel> {
                if (it != null) {
                    orderCancelDataResponse(it)
                } else {
                    context?.let {
                        CommonUtils.showToastMessage(
                            it,
                            AppConstants.ErrorInFetchingData
                        )
                    }
                }
            })
        }
    }

    private fun orderCancelDataResponse(orderCancelModel: OrderCancelModel) {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getAllOrdersList(orderId)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    private fun setWishListData(getWishListDataModel: GetWishListDataModel) {
        if (getWishListDataModel.status.equals(AppConstants.SUCCESS)) {
            viewDataBinding.toolbar.crdCountWishList.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountWishList.text =
                getWishListDataModel.Data.size.toString()
        } else {
            viewDataBinding.toolbar.crdCountWishList.visibility = View.GONE
        }
    }


    private fun showCheckUpdateDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_cancel_order_reason)
        Objects.requireNonNull(dialog.window)?.setBackgroundDrawable(ColorDrawable(0))
        val btnLatter = dialog.findViewById<View>(R.id.btnLatter) as TextView
        val btnUpdate = dialog.findViewById<View>(R.id.btnUpdate) as CardView
        val rb_others = dialog.findViewById<View>(R.id.rb_others) as RadioButton
        val edtMassage = dialog.findViewById<View>(R.id.edtMassage) as EditText
        val rb_1 = dialog.findViewById<View>(R.id.rb_1) as RadioButton
        val rb_2 = dialog.findViewById<View>(R.id.rb_2) as RadioButton
        val rb_3 = dialog.findViewById<View>(R.id.rb_3) as RadioButton
        val rb_4 = dialog.findViewById<View>(R.id.rb_4) as RadioButton
        val rb_5 = dialog.findViewById<View>(R.id.rb_5) as RadioButton
        val rb_group = dialog.findViewById<View>(R.id.rb_group) as RadioGroup

        btnLatter.setOnClickListener {
//            onClick1.invoke()
            dialog.dismiss()
        }
        btnUpdate.setOnClickListener {
//            onClick2.invoke()
            if (isSelected) {
                mesg=edtMassage.text.toString()
            }else{
                mViewModel. orderCancelreason(orderId,mesg)
                dialog.dismiss()

            }


        }


        rb_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (checkedId == rb_1.id) {
                edtMassage.visibility = View.GONE
                mesg=    rb_1.getText().toString()
                isSelected = false
            } else if (checkedId == rb_others.id) {
                     edtMassage.visibility = View.VISIBLE
                isSelected = true
            } else if (checkedId == rb_2.id) {
                edtMassage.visibility = View.GONE
                isSelected = false
                mesg=    rb_2.getText().toString()
            } else if (checkedId == rb_3.id) {
                edtMassage.visibility = View.GONE
                mesg=    rb_3.getText().toString()
                isSelected = false
            } else if (checkedId == rb_4.id) {
                edtMassage.visibility = View.GONE
                mesg=    rb_4.getText().toString()
                isSelected = false
            } else if (checkedId == rb_5.id) {
                edtMassage.visibility = View.GONE
                mesg=    rb_5.getText().toString()
                isSelected = false
            }
        })
        dialog.show()
        dialog.window!!.setBackgroundDrawable(null)
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
package com.uoons.india.ui.checkout.checkout_payment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.ActivityCheckOutPaymentBinding
import com.uoons.india.ui.base.BaseActivity
import com.uoons.india.ui.checkout.checkout_payment.model.online_payment_status.OnlinePaymentStatusModel
import com.uoons.india.ui.checkout.confirm_pay.ConfirmPaymentStatusActivity
import com.uoons.india.utils.ActivityNavigator
import com.uoons.india.utils.AppConstants
import maes.tech.intentanim.CustomIntent
import org.json.JSONObject
import java.util.*
import kotlin.math.roundToInt


class CheckOutPaymentActivity : BaseActivity<ActivityCheckOutPaymentBinding, CheckOutPaymentActivityVM>(),
    CheckOutPaymentActivityNavigator, PaymentResultWithDataListener {
    override val bindingVariable: Int = BR.selectLamguageVM
    override val layoutId: Int = R.layout.activity_check_out_payment
    override val viewModelClass: Class<CheckOutPaymentActivityVM> = CheckOutPaymentActivityVM::class.java
    private var payAmount : String = ""
    var orderId : String = ""
    var mobileNo : String = ""
    var errorDescription : String = ""
    var errorReason : String = ""

    override   fun init() {
        mViewModel.navigator = this
        // Start shimmer animation
        viewDataBinding?.shimmerCheckOutPaymentLayout?.startShimmer()

    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding?.shimmerCheckOutPaymentLayout?.stopShimmer()
        viewDataBinding?.shimmerCheckOutPaymentLayout?.visibility = View.GONE
        /*val autoReadOtpHelper = AutoReadOtpHelper(this@CheckOutPaymentActivity)
        unregisterReceiver(autoReadOtpHelper)*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Checkout.preload(applicationContext)
        val extras = intent.extras
        orderId = extras?.getString(AppConstants.orderId).toString()
        payAmount = extras?.getString(AppConstants.payAmount).toString()
        mobileNo = extras?.getString(AppConstants.mobileNo).toString()

        val checkout = Checkout()
        checkout.setKeyID(AppConstants.RAZORPAYKEY)
        Checkout.preload(Objects.requireNonNull(this))
        initialise()
    }

   private fun initialise(){
       val amount = (payAmount.toFloat() * 100).roundToInt()

       val checkout = Checkout()
       checkout.setKeyID(AppConstants.RAZORPAYKEY)
       checkout.setImage(R.mipmap.ic_launcher)
       try {
           val options = JSONObject()
           options.put(AppConstants.Name, AppPreference.getValue(PreferenceKeys.USER_NAME_ORDER))
           options.put(AppConstants.ORDER_ID, orderId)
           options.put(AppConstants.CURRENCY, AppConstants.INR)
           options.put(AppConstants.AMOUNT, amount)
           val preFill = JSONObject()
           preFill.put(AppConstants.email,  AppPreference.getValue(PreferenceKeys.USER_EMAIL_ORDER))
           preFill.put(AppConstants.CONTACT, mobileNo)
           options.put(AppConstants.PREFILL, preFill)
           checkout.open(this@CheckOutPaymentActivity, options)

       } catch (e: Exception) {
           Log.e("LOG_TAG", "Error in starting Razorpay Checkout", e)
       }
    }


    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        mViewModel.checkOnlinePaymentStatus(orderId, AppPreference.getValue(PreferenceKeys.FIREBASE_LINK), p0.toString(), AppConstants.SUCCESS_)
    }

    override fun navigateToSuccessPaymentStatus(onlinePaymentStatusModel: OnlinePaymentStatusModel) {
        //Create the bundle
        val mBundle = Bundle()
        mBundle.putString(AppConstants.orderId, onlinePaymentStatusModel.getData()?.getBundleId().toString())
        mBundle.putString(AppConstants.TXN_ID, onlinePaymentStatusModel.getData()?.getOrderId().toString())
        mBundle.putString(AppConstants.product_name, onlinePaymentStatusModel.getData()?.getProductName().toString())
        mBundle.putString(AppConstants.product_image, onlinePaymentStatusModel.getData()?.getProductImage().toString())
        mBundle.putString(AppConstants.product_quantity, onlinePaymentStatusModel.getData()?.getProductQuantity().toString())
        mBundle.putString(AppConstants.orderAmount, AppPreference.getValue(PreferenceKeys.ORDER_amount))
        mBundle.putString(AppConstants.paymentMode, AppConstants.ONLINE)
        mBundle.putString(AppConstants.orderMassage, AppConstants.Order_Successful)
        mBundle.putString(AppConstants.status, AppConstants.SUCCESS_)
        mBundle.putString(AppConstants.DESCRIPTION, AppConstants.DESCRIPTION)
        mBundle.putString(AppConstants.REASON, AppConstants.REASON)
        ActivityNavigator.clearAllActivityWithData(this@CheckOutPaymentActivity, ConfirmPaymentStatusActivity::class.java,mBundle)
        CustomIntent.customType(this@CheckOutPaymentActivity, AppConstants.LEFT_TO_RIGHT)
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        val obj = JSONObject(p1.toString())
        val error = obj.getJSONObject("error")
        errorDescription = error.get("description").toString()
        Log.d("", "onPaymentError::errorDescription:: $errorDescription")

        errorReason = error.get("reason").toString()
        Log.d("", "onPaymentError::errorReason:: $errorReason")

        mViewModel.checkOnlinePaymentStatus(orderId, AppPreference.getValue(PreferenceKeys.FIREBASE_LINK), errorReason, AppConstants.FAILURE_)
    }

    override fun navigateToCancelPaymentStatus(onlinePaymentStatusModel: OnlinePaymentStatusModel) {
        Log.d("TAG", "navigateToCancelPaymentStatus_orderId:: "+onlinePaymentStatusModel.getData()?.getOrderId())
        Log.d("TAG", "navigateToCancelPaymentStatus_productName:: "+onlinePaymentStatusModel.getData()?.getProductName())
        Log.d("TAG", "navigateToCancelPaymentStatus_productImage:: "+onlinePaymentStatusModel.getData()?.getProductImage())
        Log.d("TAG", "navigateToCancelPaymentStatus_bundleId_OrderNumber:: "+onlinePaymentStatusModel.getData()?.getBundleId())
        Log.d("TAG", "navigateToCancelPaymentStatus_productQuantity:: "+onlinePaymentStatusModel.getData()?.getProductQuantity())
        navigateToCancelPaymentStatusActivity()
    }

    private fun navigateToCancelPaymentStatusActivity(){
        //Create the bundle
        val mBundle = Bundle()
        mBundle.putString(AppConstants.orderId, orderId)
        mBundle.putString(AppConstants.orderAmount, AppPreference.getValue(PreferenceKeys.ORDER_amount))
        mBundle.putString(AppConstants.paymentMode, AppConstants.ONLINE)
        mBundle.putString(AppConstants.orderMassage, AppConstants.Order_Cancelled)
        mBundle.putString(AppConstants.status, AppConstants.FAILED)
        mBundle.putString(AppConstants.DESCRIPTION, "Payment Processing Cancelled")
        mBundle.putString(AppConstants.REASON, "Payment Cancelled")
        ActivityNavigator.clearAllActivityWithData(this@CheckOutPaymentActivity, ConfirmPaymentStatusActivity::class.java,mBundle)
        CustomIntent.customType(this@CheckOutPaymentActivity, AppConstants.LEFT_TO_RIGHT)
     //   Checkout.clearUserData(this@CheckOutPaymentActivity)
    }

}

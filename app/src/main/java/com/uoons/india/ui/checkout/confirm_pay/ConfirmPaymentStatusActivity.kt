package com.uoons.india.ui.checkout.confirm_pay

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.ActivityConfirmPaymentStatusBinding
import com.uoons.india.ui.base.BaseActivity
import com.uoons.india.ui.home.HomeActivity
import com.uoons.india.utils.ActivityNavigator
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import maes.tech.intentanim.CustomIntent

class ConfirmPaymentStatusActivity : BaseActivity<ActivityConfirmPaymentStatusBinding, ConfirmPayFragmentVM>(), ConfirmPayFragmentNavigator {
    override val bindingVariable: Int = BR.confirmPayFragmentVM
    override val layoutId: Int = R.layout.activity_confirm_payment_status
    override val viewModelClass: Class<ConfirmPayFragmentVM> = ConfirmPayFragmentVM::class.java
    private var orderNumber:String = ""
    private var txnId:String = ""
    private var productName:String = ""
    private var productImageUrl:String = ""
    private var productQuantity:String = ""
    private var orderAmount:String = ""
    private var paymentMode:String = ""
    private var orderMassage:String = ""
    private var status:String = ""
    private var errorDescription:String = ""
    private var errorReason:String = ""

    override fun init() {
        mViewModel.navigator = this
        //Get the bundle
        val bundle = intent.extras
        orderNumber = bundle?.getString(AppConstants.orderId).toString()

        productName = bundle?.getString(AppConstants.product_name).toString()
        productImageUrl = bundle?.getString(AppConstants.product_image).toString()
        productQuantity = bundle?.getString(AppConstants.product_quantity).toString()
        txnId = bundle?.getString(AppConstants.TXN_ID).toString()

        orderAmount = bundle?.getString(AppConstants.orderAmount).toString()
        paymentMode = bundle?.getString(AppConstants.paymentMode).toString()
        orderMassage = bundle?.getString(AppConstants.orderMassage).toString()

        errorDescription = bundle?.getString(AppConstants.DESCRIPTION).toString()
        errorReason = bundle?.getString(AppConstants.REASON).toString()

        status = bundle?.getString(AppConstants.status).toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialise()
    }

    @SuppressLint("SetTextI18n")
    private fun initialise(){
        if (status == AppConstants.FAILED){
            viewDataBinding?.cstLayoutSuccesStatus?.visibility = View.GONE
            viewDataBinding?.cstLayoutFailureStatus?.visibility = View.VISIBLE
            viewDataBinding?.txvErrorDescription?.text = errorDescription
            viewDataBinding?.txvErrorReason?.text = errorReason
        } else {
            viewDataBinding?.cstLayoutSuccesStatus?.visibility = View.VISIBLE
            viewDataBinding?.cstLayoutFailureStatus?.visibility = View.GONE

            viewDataBinding?.txvOrderNumber?.text = orderNumber
            viewDataBinding?.txvTransactionId?.text = txnId
            viewDataBinding?.txvOrderProductName?.text = productName

            val newProductQuantity = productQuantity.toInt()
            if (productQuantity == AppConstants.one){
                viewDataBinding?.txvProductCount?.visibility = View.GONE
                viewDataBinding?.crdProductCount?.visibility = View.GONE
            } else {
                viewDataBinding?.crdProductCount?.visibility = View.VISIBLE
                viewDataBinding?.txvProductCount?.text = "+ "+(newProductQuantity-1)
            }

            if (productImageUrl.isEmpty()){
                CommonUtils.loadWithOutBaseUrlImage(viewDataBinding?.ivProductImage,"")
            }else{
                CommonUtils.loadWithOutBaseUrlImage(viewDataBinding?.ivProductImage,productImageUrl)
            }

            viewDataBinding?.txvOrderAmount?.text =  resources.getString(R.string.rupees)+" "+orderAmount
            viewDataBinding?.txvPaymentMode?.text = paymentMode
        }
    }

    override fun naviGateToHomeFragment() {
        ActivityNavigator.clearAllActivity(this@ConfirmPaymentStatusActivity, HomeActivity::class.java)
        CustomIntent.customType(this@ConfirmPaymentStatusActivity, AppConstants.RIGHT_TO_LEFT)
    }

    override fun onBackClick() {
        super.onBackClick()
        ActivityNavigator.clearAllActivity(this@ConfirmPaymentStatusActivity, HomeActivity::class.java)
        CustomIntent.customType(this@ConfirmPaymentStatusActivity, AppConstants.RIGHT_TO_LEFT)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityNavigator.clearAllActivity(this@ConfirmPaymentStatusActivity, HomeActivity::class.java)
        CustomIntent.customType(this@ConfirmPaymentStatusActivity, AppConstants.RIGHT_TO_LEFT)
    }

}
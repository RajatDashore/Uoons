package com.uoons.india.ui.checkout.confirm_pay

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentConfirmPayBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.home.HomeActivity
import com.uoons.india.utils.ActivityNavigator
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ConfirmPayFragment : BaseFragment<FragmentConfirmPayBinding, ConfirmPayFragmentVM>(),
    ConfirmPayFragmentNavigator  {
    override val bindingVariable: Int = BR.confirmPayFragmentVM
    override val layoutId: Int = R.layout.fragment_confirm_pay
    override val viewModelClass: Class<ConfirmPayFragmentVM> = ConfirmPayFragmentVM::class.java
    private var navController: NavController? = null
    private var orderId:String = ""
    private var productName:String = ""
    private var productImageUrl:String = ""
    private var productQuantity:String = ""
    private var orderAmount:String = ""
    private var paymentMode:String = ""
    private var orderMassage:String = ""
    private var status:String = ""
    private var errorDescription:String = ""
    private var errorReason:String = ""
    private var home:String = ""

    override fun init() {
        mViewModel.navigator = this
        orderId = arguments?.getString(AppConstants.orderId).toString()

        productName = arguments?.getString(AppConstants.product_name).toString()
        productImageUrl = arguments?.getString(AppConstants.product_image).toString()
        productQuantity = arguments?.getString(AppConstants.product_quantity).toString()
        Log.d("TAG", "init_productQuantity:: $productQuantity")

        orderAmount = arguments?.getString(AppConstants.orderAmount).toString()
        paymentMode = arguments?.getString(AppConstants.paymentMode).toString()
        orderMassage = arguments?.getString(AppConstants.orderMassage).toString()

        errorDescription = arguments?.getString("description").toString()
        errorReason = arguments?.getString("reason").toString()

        status = arguments?.getString(AppConstants.status).toString()
        home = arguments?.getString("TAG").toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        if (status == AppConstants.FAILED){
            viewDataBinding.cstLayoutSuccessStatus.visibility = View.GONE
            viewDataBinding.cstLayoutFailureStatus.visibility = View.VISIBLE
            viewDataBinding.txvErrorDescription.text = errorDescription
            viewDataBinding.txvErrorReason.text = errorReason
        }else{
            viewDataBinding.cstLayoutSuccessStatus.visibility = View.VISIBLE
            viewDataBinding.cstLayoutFailureStatus.visibility = View.GONE
            viewDataBinding.txvYourOrderConfirmed.text = orderMassage

            viewDataBinding.txvOrderNumber.text = orderId
            viewDataBinding.txvOrderProductName.text = productName
            viewDataBinding.txvOrderAmount.text = resources.getString(R.string.rupees)+" "+orderAmount

            val newProductQuantity = productQuantity.toInt()
            if (productQuantity == AppConstants.one){
                viewDataBinding.crdProductCount.visibility = View.GONE
                viewDataBinding.txvProductCount.visibility = View.GONE
            } else {
                viewDataBinding.crdProductCount.visibility = View.VISIBLE
                viewDataBinding.txvProductCount.text = "+ "+(newProductQuantity-1)
            }

            if (productImageUrl.isEmpty()){
                CommonUtils.loadWithOutBaseUrlImage(viewDataBinding.ivProductImage,"")
            }else{
                CommonUtils.loadWithOutBaseUrlImage(viewDataBinding.ivProductImage,productImageUrl)
            }

            viewDataBinding.txvPaymentMode.text = paymentMode
        }
    }

    override fun naviGateToHomeFragment() {
        if (home == "home"){
            activity?.let { ActivityNavigator.clearAllActivity(it, HomeActivity::class.java) }
        }
        else{
            navController?.navigate(R.id.action_confirmPayFragment_to_homeFragment)
        }
    }

}


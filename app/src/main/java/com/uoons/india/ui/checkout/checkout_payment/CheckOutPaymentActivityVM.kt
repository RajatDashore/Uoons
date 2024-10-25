package com.uoons.india.ui.checkout.checkout_payment

import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import java.util.HashMap

class CheckOutPaymentActivityVM : BaseViewModel<CheckOutPaymentActivityNavigator>() {

    fun checkOnlinePaymentStatus(orderId: String, orderLinkMassage: String, txnId: String, txnStatus: String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        } else {
            viewModelScope.launch {
                val result = Repository.get.checkOutRazorpayUpdate(AppConstants.CHANNEL_MODE, checkOrderIdRequest(orderId, orderLinkMassage, txnId, txnStatus))
                result.fold(
                    {
                        navigator?.handleAPIFailure(it)
                    },
                    {
                        if(it.getStatus().equals(AppConstants.SUCCESS,ignoreCase = true)) {
                            if (txnStatus == AppConstants.FAILURE_){
                                navigator?.navigateToCancelPaymentStatus(it)
                            } else {
                                navigator?.navigateToSuccessPaymentStatus(it)
                            }
                        } else {
                            if (txnStatus == AppConstants.FAILURE_){
                                navigator?.navigateToCancelPaymentStatus(it)
                            } else {
                                navigator?.navigateToSuccessPaymentStatus(it)
                            }
                        }
                    }
                )
            }
        }
    }

    private fun checkOrderIdRequest(orderId: String, orderLinkMassage: String, txn_Id: String, txn_Status: String): HashMap<String, Any> {
        val requestParam: HashMap<String, Any> = HashMap()
        requestParam[AppConstants.ORDER_ID] = orderId
        requestParam[AppConstants.DYNAMIC_URL] = orderLinkMassage
       // requestParam[AppConstants.TXN_ID] = txn_Id
        requestParam[AppConstants.TXN_STATUS] = txn_Status
        return requestParam
    }
}
package com.uoons.india.ui.checkout.checkout_payment

import com.uoons.india.ui.base.CommonNavigator
import com.uoons.india.ui.checkout.checkout_payment.model.online_payment_status.OnlinePaymentStatusModel

interface CheckOutPaymentActivityNavigator : CommonNavigator {
    fun navigateToSuccessPaymentStatus(onlinePaymentStatusModel: OnlinePaymentStatusModel)
    fun navigateToCancelPaymentStatus(onlinePaymentStatusModel: OnlinePaymentStatusModel)
}
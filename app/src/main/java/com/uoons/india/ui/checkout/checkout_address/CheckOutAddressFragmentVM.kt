package com.uoons.india.ui.checkout.checkout_address

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.R
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.checkout.checkout_address.model.DeliverAllAddressModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class CheckOutAddressFragmentVM : BaseViewModel<CheckOutAddressFragmentNavigator>() {

    // GET All Deliver Address
    var getAllDeliverAddressDataResponse: MutableLiveData<DeliverAllAddressModel> =
        MutableLiveData()

    var getAllDeliverAddressDataNotFound: MutableLiveData<DeliverAllAddressModel> =
        MutableLiveData()

    // GET All Deliver Address
    var insertDeliverAddressDataResponse: MutableLiveData<DeliverAllAddressModel> =
        MutableLiveData()

    // Delete Deliver Address
    var deleteAddressDataResponse: MutableLiveData<DeliverAllAddressModel> = MutableLiveData()

    init {
        getAllDeliverAddressDataResponse = MutableLiveData()

        getAllDeliverAddressDataNotFound = MutableLiveData()

        insertDeliverAddressDataResponse = MutableLiveData()

        deleteAddressDataResponse = MutableLiveData()
    }

    fun fetchUserDeliverAddress() {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
           // navigator?.showProgress()
            val result = Repository.get.getUserDeliverAddress(AppConstants.CHANNEL_MODE)
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if (it.status.equals(AppConstants.SUCCESS)) {
                        getAllDeliverAddressDataResponse.value = it
                        navigator?.getAllDeliverAddressResponse()
                    } else {
                        getAllDeliverAddressDataNotFound.value = it
                        navigator?.getAllDeliverAddressNotFound()
                    }
                }
            )
        }
    }

    fun isValidField(strFullName: String = "", strEmail: String = "", strMobileNumber: String = "", strAddress1: String = "", strAddress2: String = "", strAddressType: String = "", strPinCode: String = "", strCity: String = "", strState: String = "", strShpiningAddress: Boolean): Boolean {
        if (CommonUtils.isStringNullOrBlank(strFullName)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_full_name))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strEmail)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_email))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strMobileNumber) && !CommonUtils.isValidMobileNo(
                strMobileNumber
            )
        ) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.mobile_number_length))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strAddress1)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_house_no))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strAddress2)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_road_name))
            return false
        }
        if (AppConstants.select_address_type == strAddressType) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_address_type))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strPinCode)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_pincode))
            return false
        }
        if (CommonUtils.isStringNullOrBlank(strCity)) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_enter_city))
            return false
        }
        if (AppConstants.Select_state == strState) {
            navigator!!.showValidationError(navigator!!.getStringResource(R.string.please_select_state))
            return false
        } else {
            return true
        }

    }

    fun saveDeliverAddressApiCall(strFullName: String, strEmail: String, strMobileNumber: String, strAddress1: String, strAddress2: String, strAddressType: String, strPinCode: String, strCity: String, strState: String, strShpiningAddress: Boolean) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.insertNewDeliverAddress(AppConstants.CHANNEL_MODE, getRequestParams(strFullName, strEmail, strMobileNumber, strAddress1, strAddress2, strAddressType, strPinCode, strCity, strState, strShpiningAddress))
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if (it.status.equals("success")) {
                        getAllDeliverAddressDataResponse.value = it
                        navigator?.insertDeliverAddressResponse()
                    }
                }
            )
        }
    }

    private fun getRequestParams(strFullName: String, strEmail: String, strMobileNumber: String, strAddress1: String, strAddress2: String, strAddressType: String, strPinCode: String, strCity: String, strState: String, strShpiningAddress: Boolean): HashMap<String, Any> {
        val requestParam: HashMap<String, Any> = HashMap()
        requestParam["addressType"] = strAddressType
        requestParam["fullName"] = strFullName
        requestParam["mobileNo"] = strMobileNumber
        requestParam["email"] = strEmail
        requestParam["address1"] = strAddress1
        requestParam["address2"] = strAddress2
        requestParam["country"] = "India"
        requestParam["city"] = strCity
        requestParam["state"] = strState
        requestParam["pinCode"] = strPinCode
        requestParam["saveAsShippingAddress"] = strShpiningAddress
        return requestParam
    }

    fun deleteDeliverAddress(addressId: Int) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.deleteDeliverAddress(AppConstants.CHANNEL_MODE, getRequestDeleteAddress(addressId))
           // navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if (it.status.equals("success")) {
                        getAllDeliverAddressDataResponse.value = it
                        navigator?.deleteDeliverAddressResponse()
                    }
                }
            )
        }
    }


    private fun getRequestDeleteAddress(addressId: Int): HashMap<String, Any> {
        val requestParam: HashMap<String, Any> = HashMap()
        requestParam["addressId"] = addressId
        return requestParam
    }

}
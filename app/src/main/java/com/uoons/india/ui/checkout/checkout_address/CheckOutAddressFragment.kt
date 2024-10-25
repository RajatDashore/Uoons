package com.uoons.india.ui.checkout.checkout_address

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentCheckOutAddressBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.checkout.checkout_address.adapter.AllDeliverAddressAdapter
import com.uoons.india.ui.checkout.checkout_address.model.DeliverAllAddressModel
import com.uoons.india.ui.checkout.checkout_address.model.GetAllDeliverAddressModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class CheckOutAddressFragment :
    BaseFragment<FragmentCheckOutAddressBinding, CheckOutAddressFragmentVM>(),
    CheckOutAddressFragmentNavigator {

    override val bindingVariable: Int = BR.checkOutAddressFragmentVM
    override val layoutId: Int = R.layout.fragment_check_out_address
    override val viewModelClass: Class<CheckOutAddressFragmentVM> =
        CheckOutAddressFragmentVM::class.java
    private var navController: NavController? = null
    private val lstStates = arrayOf(
        "Please select state",
        "Andaman and Nicobar (UT)",
        "Andhra Pradesh",
        "Arunachal Pradesh",
        "Assam",
        "Bihar",
        "Chandigarh (UT)",
        "Chhattisgarh",
        "Dadra and Nagar Haveli (UT)",
        "Daman and Diu (UT)",
        "Delhi",
        "Goa",
        "Gujarat",
        "Haryana",
        "Himachal Pradesh",
        "Jammu and Kashmir",
        "Jharkhand",
        "Karnataka",
        "Kerala",
        "Lakshadweep (UT)",
        "Madhya Pradesh",
        "Maharashtra",
        "Manipur",
        "Meghalaya",
        "Mizoram",
        "Nagaland",
        "Orissa",
        "Puducherry (UT)",
        "Punjab",
        "Rajasthan",
        "Sikkim",
        "Tamil Nadu",
        "Telangana",
        "Tripura",
        "Uttar Pradesh",
        "Uttarakhand",
        "West Bengal"
    )
    private val lstAddressType = arrayOf("Please select Address Type", "House", "Office", "Other")
    lateinit var allDeliverAddressAdapter: AllDeliverAddressAdapter
    private lateinit var getAllDeliverAddressModel: GetAllDeliverAddressModel
    var addressId: Int = 0

    override  fun init() {
        mViewModel.navigator = this

        getAllDeliverAddressModel = GetAllDeliverAddressModel()

        allDeliverAddressAdapter = AllDeliverAddressAdapter(onclick = {
            getAllDeliverAddressModel = it as GetAllDeliverAddressModel
            addressId = getAllDeliverAddressModel.id!!.toInt()
            Log.e("CheckOutAddressFragment", "addressId; $addressId")
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                viewDataBinding.shimmerCheckOutAddressLayout.startShimmer()
                mViewModel.deleteDeliverAddress(addressId)
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(
                    AppConstants.Uoons,
                    resources.getString(R.string.please_check_internet_connection),
                    onClick = {})
            }

        }, onclick1 = {

            getAllDeliverAddressModel = it as GetAllDeliverAddressModel
            val bundle = bundleOf("id" to getAllDeliverAddressModel.id, "name" to getAllDeliverAddressModel.bname, "type" to getAllDeliverAddressModel.type, "country" to getAllDeliverAddressModel.bcountry, "address1" to getAllDeliverAddressModel.baddress1, "city" to getAllDeliverAddressModel.bcity,
                "address2" to getAllDeliverAddressModel.baddress2, "email" to getAllDeliverAddressModel.bemail, "mobileNo" to getAllDeliverAddressModel.bmobileNo, "pincode" to getAllDeliverAddressModel.bpincode, "state" to getAllDeliverAddressModel.bstate, "profileid" to getAllDeliverAddressModel.profileid, "state" to getAllDeliverAddressModel.sstate, "pincode" to getAllDeliverAddressModel.spincode,)

            navController?.navigate(R.id.action_checkOutAddressFragment_to_updatecheckOutAddressFragment, bundle)
        }
        )
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            viewDataBinding.shimmerCheckOutAddressLayout.startShimmer()
            mViewModel.fetchUserDeliverAddress()
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewDataBinding.edtPincode.setText(AppPreference.getValue(PreferenceKeys.PIN_CODE))

        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE


        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.delivery_address)

        viewDataBinding.edtPincode.setOnClickListener {
            context?.let {
                CommonUtils.showToastMessage(
                    it,
                    "Please Change Pincode in Product Detail Page.."
                )
            }
        }

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener{
            super.onBackClick()
        }

        val arrayStateAdapter = ArrayAdapter<String>(
            view.context,
            android.R.layout.simple_dropdown_item_1line,
            lstStates
        )
        viewDataBinding.sppinerState.adapter = arrayStateAdapter

        val arrayAddressTypAdapter = ArrayAdapter<String>(
            view.context,
            android.R.layout.simple_dropdown_item_1line,
            lstAddressType
        )
        viewDataBinding.sppinerTypeOfAddress.adapter = arrayAddressTypAdapter

        viewDataBinding.rcvDeliverAddressList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewDataBinding.txvAddNewAddress.setOnClickListener {
            viewDataBinding.scrollViewAddress.visibility = View.GONE
            viewDataBinding.scrollViewNewAddress.visibility = View.VISIBLE
        }

        allDeliverAddressAdapter.setOnItemClickListener(object :
            AllDeliverAddressAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(addresId: Int) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    Log.e("", "AllDeliverAddressAdapter:- $addresId")
                    navController?.navigate(R.id.action_checkOutAddressFragment_to_checkOutPaymentFragment)
                } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                    return
                }
            }
        })
    }

    override fun naviGateToCheckOutPaymentFragment() {
        if (mViewModel.isValidField(strFullName = viewDataBinding.edtFullName.text.toString(), strEmail = viewDataBinding.edtEmail.text.toString(), strMobileNumber = viewDataBinding.edtPhoneNumber.text.toString(), strAddress1 = viewDataBinding.edtHouseNo.text.toString(),
                strAddress2 = viewDataBinding.edtAddress.text.toString(), strAddressType = viewDataBinding.sppinerTypeOfAddress.selectedItem.toString(), strPinCode = AppPreference.getValue(PreferenceKeys.PIN_CODE), strCity = viewDataBinding.edtCity.text.toString(), strState = viewDataBinding.sppinerState.selectedItem.toString(), strShpiningAddress = viewDataBinding.chkBoxSaveShippingAddress.isChecked)
        ) {
            viewDataBinding.shimmerCheckOutAddressLayout.startShimmer()
            mViewModel.saveDeliverAddressApiCall(viewDataBinding.edtFullName.text.toString(), viewDataBinding.edtEmail.text.toString(), viewDataBinding.edtPhoneNumber.text.toString(), viewDataBinding.edtHouseNo.text.toString(), viewDataBinding.edtAddress.text.toString(),
                viewDataBinding.sppinerTypeOfAddress.selectedItem.toString(), viewDataBinding.edtPincode.text.toString(), viewDataBinding.edtCity.text.toString(), viewDataBinding.sppinerState.selectedItem.toString(), viewDataBinding.chkBoxSaveShippingAddress.isChecked)
        }
    }

    override fun getAllDeliverAddressResponse() {
        if (view != null) {
            mViewModel.getAllDeliverAddressDataResponse.observe(
                viewLifecycleOwner,
                Observer<DeliverAllAddressModel> {
                    if (it != null) {
                        context?.let { it2 -> setAllDeliverAdapterData(it, it2) }
                    } else {
                        context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data))
                        }
                    }
                })
        }

    }

    override fun getAllDeliverAddressNotFound() {
        if (view != null) {
            mViewModel.getAllDeliverAddressDataNotFound.observe(
                viewLifecycleOwner,
                Observer<DeliverAllAddressModel> {
                    if (it != null) {
                        viewDataBinding.shimmerCheckOutAddressLayout.stopShimmer()
                        viewDataBinding.shimmerCheckOutAddressLayout.visibility = View.GONE
                        Log.d("", "Delivery address not found")
                    } else {
                        context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                    }
                })
        }

    }

    override fun insertDeliverAddressResponse() {
        mViewModel.fetchUserDeliverAddress()
        viewDataBinding.rcvDeliverAddressList.visibility = View.VISIBLE
        viewDataBinding.scrollViewAddress.visibility = View.VISIBLE
        viewDataBinding.scrollViewNewAddress.visibility = View.GONE
        viewDataBinding.shimmerCheckOutAddressLayout.stopShimmer()
        viewDataBinding.shimmerCheckOutAddressLayout.visibility = View.GONE
    }

    override fun deleteDeliverAddressResponse() {
        viewDataBinding.rcvDeliverAddressList.visibility = View.VISIBLE
        mViewModel.fetchUserDeliverAddress()
        viewDataBinding.shimmerCheckOutAddressLayout.stopShimmer()
        viewDataBinding.shimmerCheckOutAddressLayout.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAllDeliverAdapterData(deliverAllAddressModel: DeliverAllAddressModel, context: Context) {
        viewDataBinding.rcvDeliverAddressList.visibility = View.VISIBLE
        allDeliverAddressAdapter.setAllDeliverAddressList(deliverAllAddressModel, context)
        viewDataBinding.rcvDeliverAddressList.adapter = allDeliverAddressAdapter
        allDeliverAddressAdapter.notifyDataSetChanged()
        viewDataBinding.shimmerCheckOutAddressLayout.stopShimmer()
        viewDataBinding.shimmerCheckOutAddressLayout.visibility = View.GONE
    }


}
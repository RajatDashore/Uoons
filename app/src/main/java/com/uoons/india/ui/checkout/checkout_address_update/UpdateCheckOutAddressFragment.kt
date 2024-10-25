package com.uoons.india.ui.checkout.checkout_address_update

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentUpdateDeliveryAddressBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.checkout.checkout_address.model.GetAllDeliverAddressModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class UpdateCheckOutAddressFragment :
    BaseFragment<FragmentUpdateDeliveryAddressBinding, UpdateCheckOutAddressFragmentVM>(),
    UpdateCheckOutAddressFragmentNavigator {


    override val bindingVariable: Int = BR.updatecheckOutAddressFragmentVM
    lateinit var Id: String
    lateinit var name: String
    lateinit var type: String
    lateinit var country: String
    lateinit var address1: String
    lateinit var city: String
    lateinit var address2: String
    lateinit var email: String
    lateinit var mobileNo: String
    lateinit var pincode: String
    lateinit var state: String
    lateinit var profileid: String

    override val layoutId: Int = R.layout.fragment_update_delivery_address
    override val viewModelClass: Class<UpdateCheckOutAddressFragmentVM> =
        UpdateCheckOutAddressFragmentVM::class.java

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
     var addressId: Int = 0
    lateinit var updateId: String

    override   fun init() {
        mViewModel.navigator = this

         Id = arguments?.getString("id").toString()
        name = arguments?.getString("name").toString()


        type = arguments?.getString("type").toString()
        country = arguments?.getString("country").toString()
        address1 = arguments?.getString("address1").toString()
        city = arguments?.getString("city").toString()
        address2 = arguments?.getString("address2").toString()
        email = arguments?.getString("email").toString()
        mobileNo = arguments?.getString("mobileNo").toString()
        pincode = arguments?.getString("pincode").toString()
        state = arguments?.getString("state").toString()
        profileid = arguments?.getString("profileid").toString()

        Log.e(TAG, "address1:-------      " +address1 )
        Log.e(TAG, "address2:-------      " +address2 )
        Log.e(TAG, "Id:<>><><><><><><><><   " + Id)


        /*
            allDeliverAddressAdapter = AllDeliverAddressAdapter(onclick = {

            }, onclick1 = {

                getAllDeliverAddressModel = it as GetAllDeliverAddressModel
                var name: String? = getAllDeliverAddressModel.bname
                Log.e(TAG, "name:--------       $name")
                 viewDataBinding.scrollViewNewAddress.visibility = View.VISIBLE

                viewDataBinding.crdPayment.visibility = View.GONE
                viewDataBinding.viewAddress.visibility = View.GONE
                viewDataBinding.txvAddressText.visibility = View.GONE
                viewDataBinding.ivAddressVector.visibility = View.GONE
                viewDataBinding.txvPaymentText.visibility = View.GONE

                viewDataBinding.toolbar.txvTitleName.text = "Update Address"

            })
    */

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated: UpdateCheckOutAddressFragment ")
        init()
        viewDataBinding.toolbar.txvTitleName.text = "Update Address"
        viewDataBinding.toolbar.ivHeartVectorInvisbile.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        navController = Navigation.findNavController(view)

        viewDataBinding.edtPincode.setText(AppPreference.getValue(PreferenceKeys.PIN_CODE))

        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE


        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.delivery_address)

        viewDataBinding.edtFullName.setText(name)
        viewDataBinding.edtEmail.setText(email)
        viewDataBinding.edtPhoneNumber.setText(mobileNo)
          viewDataBinding.edtPincode.setText(pincode)
        viewDataBinding.edtCity.setText(city)
        viewDataBinding.edtHouseNo.setText(address1)
        viewDataBinding.edtAddress.setText(address2)

        viewDataBinding.edtPincode.setOnClickListener(View.OnClickListener { view ->
            context?.let {
                CommonUtils.showToastMessage(
                    it,
                    "Please Change Pincode in Product Detail Page.."
                )
            }
        })

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })

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

        /* viewDataBinding.rcvDeliverAddressList.apply {
             layoutManager =
                 LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
         }*/


    }


    override fun naviGateToCheckOutPaymentFragment() {
        Log.e(
            "",
            "sppinerTypeOfAddress:- " + viewDataBinding.sppinerTypeOfAddress.selectedItem.toString()
        )
        if (mViewModel.isValidField(
                strFullName = viewDataBinding.edtFullName.text.toString(),
                strEmail = viewDataBinding.edtEmail.text.toString(),
                strMobileNumber = viewDataBinding.edtPhoneNumber.text.toString(),
                strAddress1 = viewDataBinding.edtHouseNo.text.toString(),
                strAddress2 = viewDataBinding.edtAddress.text.toString(),
                strAddressType = viewDataBinding.sppinerTypeOfAddress.selectedItem.toString(),
                strPinCode = AppPreference.getValue(PreferenceKeys.PIN_CODE),
                strCity = viewDataBinding.edtCity.text.toString(),
                strState = viewDataBinding.sppinerState.selectedItem.toString(),
                strShpiningAddress = viewDataBinding.chkBoxSaveShippingAddress.isChecked
            )
        ) {
            mViewModel.updateDeliverAddressApiCall(
              Id.toInt(),
                viewDataBinding.edtFullName.text.toString(),
                viewDataBinding.edtEmail.text.toString(),
                viewDataBinding.edtPhoneNumber.text.toString(),
                viewDataBinding.edtHouseNo.text.toString(),
                viewDataBinding.edtAddress.text.toString(),
                viewDataBinding.sppinerTypeOfAddress.selectedItem.toString(),
                viewDataBinding.edtPincode.text.toString(),
                viewDataBinding.edtCity.text.toString(),
                viewDataBinding.sppinerState.selectedItem.toString(),
                viewDataBinding.chkBoxSaveShippingAddress.isChecked
            )
        }
    }

    override fun updateDeliverAddressApiCall() {
        onBackClick()
    }


}
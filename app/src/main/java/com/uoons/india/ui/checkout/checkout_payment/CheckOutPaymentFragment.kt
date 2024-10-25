package com.uoons.india.ui.checkout.checkout_payment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentCheckOutPaymentBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.checkout.checkout_payment.model.CheckOutModel
import com.uoons.india.ui.checkout.checkout_payment.model.online_payment_status.OnlinePaymentStatusModel
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.json.JSONObject
import org.lsposed.lsparanoid.Obfuscate
import java.text.NumberFormat
import java.util.*

@Obfuscate
class CheckOutPaymentFragment : BaseFragment<FragmentCheckOutPaymentBinding, CheckOutPaymentFragmentVM>(), CheckOutPaymentFragmentNavigator{
    private var LOG_TAG = "CheckOutPaymentFragment"
    override val bindingVariable: Int = BR.checkOutPaymentFragmentVM
    override val layoutId: Int = R.layout.fragment_check_out_payment
    override val viewModelClass: Class<CheckOutPaymentFragmentVM> = CheckOutPaymentFragmentVM::class.java
    private var navController: NavController? = null
    private val paymentMethodeZero = arrayOf("SELECT PAYMENT MODE", "CASH ON DELIVERY", "ONLINE")
    private val paymentMethodeOne = arrayOf("SELECT PAYMENT MODE", "ONLINE")
    private var orderId: String = ""
    private var paymentMode: String = ""
    private var newTotalAmount: String = ""
    private var coins: String = ""
    private var orderLink: String = ""
    private var mobileNo :String = ""

    override   fun init() {
        mViewModel.navigator = this

        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLongLink(Uri.parse(AppConstants.ShareDynamicLinkProduct + AppConstants.AUTH + AppConstants.NewShearLinkWebside + AppConstants.AUTH))
            .buildShortDynamicLink()
            .addOnCompleteListener(requireActivity(),
                OnCompleteListener<ShortDynamicLink?> { task ->
                    try {
                        if (task.isSuccessful) {
                            // Short link created
                            val shortLink: Uri? = task.result.shortLink
                            orderLink = shortLink.toString()
                            val flowchartLink: Uri? = task.result.previewLink
                            Log.e(LOG_TAG, "shearLink_shortLink:- $orderLink and flowchartLink:- $flowchartLink")
                        } else {
                            Log.e(LOG_TAG, "shearLinkError:-")
                        }
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "shearLinkCatch===Error:-$e")
                    }

                })
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            viewDataBinding.shimmerCheckOutPaymentLayout.startShimmer()
            mViewModel.getMyCartItemsApiCall()

        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        mobileNo =    AppPreference.getValue(PreferenceKeys.MOBILE_NO)

        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.checkout)
        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener {
            super.onBackClick()
        })

        viewDataBinding.sppinerPaymentMethode.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == AppConstants.ONLINE) {
                    AppPreference.addValue(PreferenceKeys.ONLINE, AppConstants.ONLINE)
                    viewDataBinding.chkBoxSupperCoins.visibility = View.VISIBLE
                    viewDataBinding.ivMyCoins.visibility = View.VISIBLE
                    viewDataBinding.viewUoonsCoins.visibility = View.VISIBLE
                    viewDataBinding.viewTotalAmount.visibility = View.VISIBLE
                    viewDataBinding.txvMyCoins.visibility = View.VISIBLE
                    viewDataBinding.txvCoinsTotalAmmount.visibility = View.VISIBLE
                    viewDataBinding.txvCoinsTotalPrice.visibility = View.VISIBLE
                } else {
                    AppPreference.addValue(PreferenceKeys.COD, AppConstants.COD)
                    viewDataBinding.chkBoxSupperCoins.visibility = View.GONE
                    viewDataBinding.ivMyCoins.visibility = View.GONE
                    viewDataBinding.viewUoonsCoins.visibility = View.GONE
                    viewDataBinding.viewTotalAmount.visibility = View.GONE
                    viewDataBinding.txvMyCoins.visibility = View.GONE
                    viewDataBinding.txvCoinsTotalAmmount.visibility = View.GONE
                    viewDataBinding.txvCoinsTotalPrice.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewDataBinding.chkBoxSupperCoins.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val ammount = newTotalAmount.toInt() - coins.toInt()
                viewDataBinding.txvMyCoins.text = "0"
                viewDataBinding.txvCoinsTotalPrice.text = resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(ammount)
                context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.coins_applied)) }
            } else {
                viewDataBinding.txvMyCoins.text = coins
                viewDataBinding.txvCoinsTotalPrice.text = resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(newTotalAmount.toInt())
                context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.coins_not_applied)) }
            }
        }
    }

    override fun getMyCartItemsResponse() {
        if (view != null) {
            mViewModel.getMyCartItemsDataResponse.observe(
                viewLifecycleOwner,
                Observer<GetMyCartDataModel> {
                    if (it != null) {
                        setMyCartItemsAdapterData(it)
                    } else {
                        context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    fun setMyCartItemsAdapterData(data: GetMyCartDataModel) {
        viewDataBinding.crdCheckoutProduct.visibility = View.VISIBLE
        viewDataBinding.txvSelectPaymentMethodeText.visibility = View.VISIBLE
        viewDataBinding.crdSppiner.visibility = View.VISIBLE
        viewDataBinding.chkBoxSaveThisCard.visibility = View.VISIBLE
        viewDataBinding.txvPriceDetails.visibility = View.VISIBLE
        viewDataBinding.crdPriceDetails.visibility = View.VISIBLE

        viewDataBinding.shimmerCheckOutPaymentLayout.stopShimmer()
        viewDataBinding.shimmerCheckOutPaymentLayout.visibility = View.GONE

        if (data.Data?.payMode == 0) {
            viewDataBinding.txvPayAndConfirm.text = requireContext().getText(R.string.confirm_and_pay)
            val arrayPaymentAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, paymentMethodeZero)
            viewDataBinding.sppinerPaymentMethode.adapter = arrayPaymentAdapter
        } else {
            viewDataBinding.txvPayAndConfirm.text = requireContext().getText(R.string.pay)
            val arrayPaymentAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, paymentMethodeOne)
            viewDataBinding.sppinerPaymentMethode.adapter = arrayPaymentAdapter
        }

        viewDataBinding.txvPriceItems.text = requireContext().getString(R.string.price) + " (" + data.Data!!.itemCount + " " + requireContext().getString(
                R.string.items
            ) + ")"
        viewDataBinding.txvPriceRupees.text = requireContext().getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(data.Data!!.totalAmount)
        val totalAmount = data.Data!!.totalAmount!!.toInt()
        val amount = data.Data!!.totalSaleAmount!!.toInt()
        val discount = totalAmount - amount
        viewDataBinding.txvTotalPriceDicount.text = "- " + requireContext().getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(discount)
        viewDataBinding.txvTotalPrice.text = requireContext().getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(data.Data!!.totalOrderAmount)
        viewDataBinding.txvProductSellingPrice.text = resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(data.Data!!.totalSaleAmount)
        viewDataBinding.txvFree.text = resources.getString(R.string.rupees) + data.Data!!.shipping.toString()

        newTotalAmount = data.Data!!.totalOrderAmount?.toString().toString()
        viewDataBinding.txvMyCoins.text = data.Data!!.coins.toString()
        coins = data.Data!!.coins.toString()

        viewDataBinding.txvCoinsTotalPrice.text = requireContext().getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(data.Data!!.totalOrderAmount)
    }


    override fun checkOutProductResponse() {
        if (view != null) {
            mViewModel.checkOutProductDataResponse.observe(
                viewLifecycleOwner,
                Observer<CheckOutModel> {
                    if (it != null) {
                        if (it.getStatus().equals(AppConstants.SUCCESS, ignoreCase = true)) {
                            checkOutProductResponseData(it)

                        } else if (it.getStatus().equals(AppConstants.FAILURE, ignoreCase = true)) {
                            CommonUtils.showToastMessage(requireContext(), it.getMessage().toString())
                        }
                    } else {
                        context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                    }
                })
        }
    }

    private fun checkOutProductResponseData(checkOutModel: CheckOutModel) {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            Log.d("TAG", "checkOutProductResponseData1221: "+checkOutModel.getData()?.getProductQuantity())
            Log.d("TAG", "checkOutProductResponseDataImage: "+checkOutModel.getData()?.getProductImage().toString())
            val bundle = bundleOf(AppConstants.orderId to checkOutModel.getData()?.getBundleId().toString(), AppConstants.PAYMENT_MODE to checkOutModel.getData()?.getPaymentMode(), AppConstants.orderAmount to checkOutModel.getData()?.getOrderTotalAmount().toString(),
                AppConstants.orderMassage to checkOutModel.getData()?.getOrderMessage().toString(),
                AppConstants.status to AppConstants.Success, AppConstants.product_name to checkOutModel.getData()?.getProductName().toString(), AppConstants.product_image to checkOutModel.getData()?.getProductImage().toString(),
                AppConstants.product_quantity to checkOutModel.getData()?.getProductQuantity().toString())
            navController?.navigate(R.id.action_checkOutPaymentFragment_to_confirmPayFragment, bundle)

        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
        }
    }


    override fun naviGateToConfirmPayFragment() {
        paymentMode = viewDataBinding.sppinerPaymentMethode.selectedItem.toString()
        if (mViewModel.isValidField(paymentMode)) {
            if (AppConstants.CASH_ON_DELIVERY == paymentMode) {
                mViewModel.checkOutProduct(AppConstants.COD, AppPreference.getValue(PreferenceKeys.COUPEN_CODE), AppPreference.getValue(PreferenceKeys.ADDRESS_ID), orderLink)

            } else if (AppConstants.ONLINE == paymentMode) {
                println("$LOG_TAG amount>>>>>>>>>>>>>       $newTotalAmount")
                mViewModel.onlineCheckOutProduct(AppConstants.ONLINE, AppPreference.getValue(PreferenceKeys.COUPEN_CODE), AppPreference.getValue(PreferenceKeys.ADDRESS_ID), orderLink)
            }
        }

    }

    override fun onlineCheckOutProductResponse() {
        if (view != null) {
            mViewModel.onlineCheckOutProductDataResponse.observe(
                viewLifecycleOwner,
                Observer<CheckOutModel> {
                    if (it != null) {
                        if (it.getStatus().equals(AppConstants.SUCCESS)) {
                            AppPreference.addValue(PreferenceKeys.FIREBASE_LINK, orderLink)
                            AppPreference.addValue(PreferenceKeys.ORDER_amount, newTotalAmount)
                            AppPreference.addValue(PreferenceKeys.ORDER_ID, it.getData()?.getOrderId().toString())
                            orderId = it.getData()?.getOrderId().toString()
                            startPayment(orderId = it.getData()?.getOrderId() ,newTotalAmount)
                        } else if (it.getStatus().equals(AppConstants.FAILURE)) {
                            CommonUtils.showToastMessage(requireContext(), it.getMessage().toString())
                        }
                    } else { context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                    }
                })
        }
    }


/* val txnTokenString="2c56fb1e3dc945858617d53ea88770371640447480304"
         val midString=AppConstants.MID
         val orderIdString="3"
         val txnAmountString="2"*/

    /* val txnTokenString = checkOutModel.Data?.online?.body?.txnToken
        val midString = AppConstants.MID
        orderIdString = checkOutModel.Data?.bundleId.toString().trim()
        txnAmountString = checkOutModel.Data?.orderTotalAmount.toString()
        Log.e(
            LOG_TAG,
            "=======orderDetails " + " orderTotalAmount: " + checkOutModel.Data?.orderTotalAmount + " and txnToken: " + checkOutModel.Data?.online?.body?.txnToken
        )

        val orderDetails =
            "MID: $midString, OrderId: $orderIdString, TxnToken: $txnTokenString, newTxnAmountString: $txnAmountString"
        Log.e(LOG_TAG, "=======orderDetails $orderDetails")

        val callBackUrl = AppConstants.PAYTM_HOST + AppConstants.PAYTM_CALL_BACK + orderIdString

        val paytmOrder =
            PaytmOrder(orderIdString, midString, txnTokenString, txnAmountString, callBackUrl)
        val transactionManager =
            TransactionManager(paytmOrder, object : PaytmPaymentTransactionCallback {
                override fun onTransactionResponse(bundle: Bundle?) {
                    Log.e(LOG_TAG, "=======onTransaction " + bundle.toString())
                    val json = JSONObject()
                    val keys = bundle!!.keySet()
                    for (key in keys) {
                        try {
                            json.put(key, JSONObject.wrap(bundle[key]))
                        } catch (e: JSONException) {
                            //Handle exception here
                        }
                    }
                    Log.e(LOG_TAG, "=======onTransaction  jsonObject:- $json")
                    val orderId = json.getString(AppConstants.ORDERID)
                    mViewModel.checkOnlinePaymentStatus(orderId, orderLink)

                }

                override fun networkNotAvailable() {
                    Log.e(LOG_TAG, "=======networkNotAvailable")
                }

                override fun onErrorProceed(p0: String?) {
                    Log.e(LOG_TAG, "=======onErrorProceed " + p0.toString())
                }

                override fun clientAuthenticationFailed(p0: String?) {
                    Log.e(LOG_TAG, "=======Failed " + p0.toString())

                }

                override fun someUIErrorOccurred(p0: String?) {
                    Log.e(LOG_TAG, "=======UIError " + p0.toString())
                }

                override fun onErrorLoadingWebPage(p0: Int, p1: String?, p2: String?) {
                    Log.e(
                        LOG_TAG,
                        "=======onError " + p0.toString() + p1.toString() + p2.toString()
                    )
                }

                override fun onBackPressedCancelTransaction() {
                    Log.e(LOG_TAG, "=======onError " + " onBackPressed")
                }

                override fun onTransactionCancel(p0: String?, p1: Bundle?) {
                    Log.e(LOG_TAG, "=======TransCancel " + p0.toString() + p1.toString())
                }

            })
        // transactionManager.setAppInvokeEnabled(false)
        transactionManager.setShowPaymentUrl(AppConstants.PAYTM_HOST + AppConstants.SHOW_PAYMENT_PAGE)
        transactionManager.setEmiSubventionEnabled(true)
        transactionManager.startTransaction(
            this@CheckOutPaymentFragment.requireActivity(),
            AppConstants.PAYTM_REQUEST_CODE
        )
    }
*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AppConstants.PAYTM_REQUEST_CODE && data != null) {

            if (data.getStringExtra("response").isNullOrEmpty() || data.getStringExtra("nativeSdkForMerchantMessage").equals("onBackPressedCancelTransaction")) {
                Log.e("=======onActivityResult", "$LOG_TAG payment failed")
            } else {
                val json = JSONObject(data.getStringExtra("response"))
                val orderId = json.get(AppConstants.ORDERID).toString()
               // mViewModel.checkOnlinePaymentStatus(orderId, orderLink)
            }
        } else {
            mViewModel.checkOnlinePaymentStatus(AppPreference.getValue(PreferenceKeys.ORDER_ID), orderLink)
        }
    }

    override fun checkOnlinePaymentStatus() {
        if (view != null) {
            mViewModel.checkOnlinePaymentStatus.observe(
                viewLifecycleOwner,
                Observer<OnlinePaymentStatusModel> {
                    if (it != null) {
                        if (it.getStatus().equals(AppConstants.SUCCESS, ignoreCase = true)) {
                            checkStatusOnlinePayment(it)
                        }
                        else if (it.getStatus().equals(AppConstants.FAILURE, ignoreCase = true)) {
                            CommonUtils.showToastMessage(requireContext(), it.getMessage().toString())
                        }
                    } else {
                        context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                    }
                })
        }
    }

    private fun checkStatusOnlinePayment(onlinePaymentStatusModel: OnlinePaymentStatusModel) {
        val orderAmount =  AppPreference.getValue(PreferenceKeys.ORDER_amount)
        if (onlinePaymentStatusModel.getStatus().equals(AppConstants.Success, ignoreCase = true)) {
            val bundle = bundleOf(AppConstants.orderId to orderId, AppConstants.paymentMode to AppConstants.ONLINE, AppConstants.orderAmount to orderAmount,
                AppConstants.orderMassage to onlinePaymentStatusModel.getMessage(),
                AppConstants.status to onlinePaymentStatusModel.getStatus())
            navController?.navigate(R.id.action_checkOutPaymentFragment_to_confirmPayFragment, bundle)

        } else if (onlinePaymentStatusModel.getStatus().equals(AppConstants.Pending, ignoreCase = true)) {
            val bundle = bundleOf(
                AppConstants.orderId to orderId,
                AppConstants.paymentMode to AppConstants.ONLINE,
                AppConstants.orderAmount to orderAmount,
                AppConstants.orderMassage to onlinePaymentStatusModel.getMessage(),
                AppConstants.status to onlinePaymentStatusModel.getStatus()
            )
            navController?.navigate(R.id.action_checkOutPaymentFragment_to_confirmPayFragment, bundle)

        } else if (onlinePaymentStatusModel.getStatus().equals(AppConstants.Failure, ignoreCase = true)) {
            val bundle = bundleOf(AppConstants.orderId to orderId,AppConstants.paymentMode to AppConstants.ONLINE, AppConstants.orderAmount to orderAmount,
                AppConstants.orderMassage to onlinePaymentStatusModel.getMessage(),
                AppConstants.status to onlinePaymentStatusModel.getStatus())
            navController?.navigate(R.id.action_checkOutPaymentFragment_to_confirmPayFragment, bundle)

        }
    }


    //----------------------Razorpay code integration-------------------------------------------

    private fun startPayment(orderId: String?, payAmount: String) {

        val intent = Intent(activity, CheckOutPaymentActivity::class.java)
        intent.putExtra(AppConstants.orderId, orderId)
        intent.putExtra(AppConstants.payAmount, payAmount)
        intent.putExtra(AppConstants.mobileNo, mobileNo)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)


    /*    val amount = Math.round(payAmount.toFloat() * 100)
        Log.e("", "startPayment::orderId: $orderId")
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
            checkout.open(activity, options)

        } catch (e: Exception) {
            Log.d(LOG_TAG, "Error in starting Razorpay Checkout", e)
        }*/
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
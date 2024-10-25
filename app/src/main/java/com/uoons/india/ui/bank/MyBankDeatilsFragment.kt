package com.uoons.india.ui.bank

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentMyBankDetailsBinding
import com.uoons.india.ui.bank.adapter.BankDetailsListAdapter
import com.uoons.india.ui.bank.model.FetchBankDetailsModel
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.json.JSONException

import org.json.JSONObject
import org.lsposed.lsparanoid.Obfuscate
import javax.xml.transform.ErrorListener
import javax.xml.transform.TransformerException

@Obfuscate
class MyBankDeatilsFragment: BaseFragment<FragmentMyBankDetailsBinding, MyBankDeatilsFragmentVM>(), MyBankDeatilsFrgamentNavigator {
    private var LOG_TAG = "MyBankDeatilsFragment"
    override val bindingVariable: Int = BR.myBankDetailsFragmentVM
    override val layoutId: Int = R.layout.fragment_my_bank_details
    override val viewModelClass: Class<MyBankDeatilsFragmentVM> = MyBankDeatilsFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var bankDetailsListAdapter : BankDetailsListAdapter

    override  fun init() {
        mViewModel.navigator = this
        mViewModel.fetchBankDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewDataBinding.rcvBankDetails.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
        init()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(viewDataBinding.edtIFSCCode, InputMethodManager.SHOW_IMPLICIT)

        viewDataBinding.edtIFSCCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(s.length >=9){
                    getDataFromIFSCCode(s.toString().trim())
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.my_bank_details)
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE


        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener { view ->
            naviGateToMyCartFragment()
        })

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener {
            super.onBackClick()
        })
    }

    fun naviGateToMyCartFragment(){
        navController?.navigate(R.id.action_myBankDeatilsFragment_to_myCartFragment)
    }

    override fun addBankAccount() {
      viewDataBinding.cstLayoutAccountDetailsList.visibility = View.GONE
      viewDataBinding.cstLayoutAddAccountDetails.visibility = View.VISIBLE
    }

    override fun saveBankDetails() {
        if (mViewModel.isValidField(strAccountNumber = viewDataBinding.edtAccountNumber.text.toString(), strConfirmAccountNumber = viewDataBinding.edtConfirmAccountNumber.text.toString(),
                strAccountHolderName = viewDataBinding.edtAccountHolderName.text.toString(), strIFSCCode = viewDataBinding.edtIFSCCode.text.toString(),
                strBankName = viewDataBinding.edtBankName.text.toString()))
        {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mViewModel.saveBankDetailsApiCall(viewDataBinding.edtAccountNumber.text.toString(),viewDataBinding.edtConfirmAccountNumber.text.toString(),
                    viewDataBinding.edtAccountHolderName.text.toString(), viewDataBinding.edtIFSCCode.text.toString(),viewDataBinding.edtBankName.text.toString(),requireContext())
            }else{
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {})
            }
        }
    }

    private fun getDataFromIFSCCode(ifscCode : String){
        val url = "https://ifsc.razorpay.com/$ifscCode"
        // below line is use to initialize our request queue.
        val queue: RequestQueue = Volley.newRequestQueue(requireActivity())
        // creating a json object request for our API.
        val objectRequest =
            JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener<JSONObject?> { response ->
                    try {
                        val bankName = response.optString("BANK")
                        val branch = response.optString("BRANCH")
                        val address = response.optString("ADDRESS")
                        val contact = response.optString("CONTACT")
                        val micrcode = response.optString("MICRCODE")
                        val city = response.optString("CITY")
                        viewDataBinding.edtBankName.setText(bankName)
                    } catch (e: JSONException) {
                        // if we get any error while loading data
                        // we are setting our text as invalid IFSC code.
                        e.printStackTrace()
                        CommonUtils.showToastMessage(requireContext(), "Invalid IFSC Code")
                    }
                }, object : ErrorListener, Response.ErrorListener {
                    override fun warning(exception: TransformerException?) {
//                        Log.e(LOG_TAG,""+exception)
                    }

                    override fun error(exception: TransformerException?) {
//                        Log.e(LOG_TAG,""+exception)
                    }

                    override fun fatalError(exception: TransformerException?) {
//                        Log.e(LOG_TAG,""+exception)
                    }

                    override fun onErrorResponse(error: VolleyError?) {
//                        Log.e(LOG_TAG,""+error)
                    }
                })
        // below line is use for adding object
        // request to our request queue.
        queue.add(objectRequest)
    }

    override fun saveBankDetailsResponse() {
        mViewModel.fetchBankDetails()
        viewDataBinding.cstLayoutAccountDetailsList.visibility = View.VISIBLE
        viewDataBinding.cstLayoutAddAccountDetails.visibility = View.GONE

    }

    override fun getBankDetailsData() {
        if(view !=null){
            mViewModel.fetchBankDetailResponse.observe(viewLifecycleOwner, Observer<FetchBankDetailsModel> {
                if (it != null){
                    setBankDetailsList(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }
    }

    override fun getAllBankDetails() {
        if(view !=null){
            mViewModel.fetchAllBankDetailResponse.observe(viewLifecycleOwner, Observer<FetchBankDetailsModel> {
                if (it != null){
                    setBankDetailsList(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBankDetailsList(fetchBankDetailsModel: FetchBankDetailsModel) {
        bankDetailsListAdapter = BankDetailsListAdapter(fetchBankDetailsModel.Data, requireContext(),
            onclick = { mViewModel.selectPrimaryAccount(it,requireContext()) },
            onclick1 = { mViewModel.deleteBankDetails(it,requireContext()) })
        viewDataBinding.rcvBankDetails.adapter = bankDetailsListAdapter
        bankDetailsListAdapter.notifyDataSetChanged()
    }
}
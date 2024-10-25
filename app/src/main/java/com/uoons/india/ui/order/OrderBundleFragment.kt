package com.uoons.india.ui.order

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentOrderBundleBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.order.adapter.FetchAllBundleOrderAdapter
import com.uoons.india.ui.order.model.FecthAllBundleOrderModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OrderBundleFragment : BaseFragment<FragmentOrderBundleBinding, OrderBundleFragmentVM>(), OrderBundleFragmentNavigator{

    override val bindingVariable: Int = BR.orderBundleFragmentVM
    override val layoutId: Int = R.layout.fragment_order_bundle
    override val viewModelClass: Class<OrderBundleFragmentVM> = OrderBundleFragmentVM::class.java
    private var navController: NavController? = null
    private lateinit var fetchAllBundleOrderAdapter : FetchAllBundleOrderAdapter

    override  fun init() {
        mViewModel.navigator = this

        if (mViewModel.navigator!!.checkIfInternetOn()) {
            if (AppPreference.getValue(PreferenceKeys.PROFILE_ID).isNotEmpty()){
                mViewModel.getAllFetchBundle()
                mViewModel.getMyCartItemsApiCall()
                mViewModel.getWishListProductApiCall()
                // Start shimmer animation
                viewDataBinding.shimmerOrderBundleLayout.startShimmer()
            }else{
                viewDataBinding.cstLayoutOrderEmpty.visibility = View.VISIBLE
                viewDataBinding.rcvOrderBundleFragment.visibility = View.GONE
                viewDataBinding.shimmerOrderBundleLayout.visibility = View.GONE
            }
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {
              init()
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerOrderBundleLayout.stopShimmer()
        viewDataBinding.shimmerOrderBundleLayout.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.order)

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener {
            super.onBackClick()
        })

        viewDataBinding.rcvOrderBundleFragment.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener { view ->
            if(view !=null){
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))){
                        val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                        loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                    }else{
                        navController?.navigate(R.id.action_orderBundleFragment_to_myCartFragment)
                    }
                }else if (mViewModel.navigator!!.isConnectedToInternet()){
                    return@OnClickListener
                }
            }
        })


        viewDataBinding.toolbar.ivHeartVector.setOnClickListener(View.OnClickListener { view ->
            if(view !=null){
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))){
                        val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                        loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                    }else{
                        navController?.navigate(R.id.action_orderBundleFragment_to_wishListFragment)
                    }
                }else if (mViewModel.navigator!!.isConnectedToInternet()){
                    return@OnClickListener
                }
            }
        })
    }

    override fun naviGateToHomeDehsBoardFragment() {
        navController?.navigate(R.id.action_orderBundleFragment_to_homeFragment)
    }

    override fun bundleOrdersListResponse() {
        if(view !=null){
            mViewModel.getAllFetchBundleOrdersListResponse.observe(viewLifecycleOwner, Observer<FecthAllBundleOrderModel> {
                if (it != null){
                    setAllBundleOrdersAdapter(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }
    }

    override fun bundleOrdersListFailureResponse() {
        viewDataBinding.cstLayoutOrderEmpty.visibility = View.VISIBLE
        viewDataBinding.rcvOrderBundleFragment.visibility = View.GONE
        viewDataBinding.shimmerOrderBundleLayout.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAllBundleOrdersAdapter(fecthAllBundleOrderModel: FecthAllBundleOrderModel) {
       if (fecthAllBundleOrderModel.message.equals(AppConstants.No_Order_Found)){
            viewDataBinding.cstLayoutOrderEmpty.visibility = View.VISIBLE
            viewDataBinding.rcvOrderBundleFragment.visibility = View.GONE
        }else if(fecthAllBundleOrderModel.message.equals(AppConstants.Authorization_Failed)){
            viewDataBinding.cstLayoutOrderEmpty.visibility = View.VISIBLE
            viewDataBinding.rcvOrderBundleFragment.visibility = View.GONE
        } else{
            fetchAllBundleOrderAdapter = FetchAllBundleOrderAdapter(fecthAllBundleOrderModel.Data,requireContext(), onclick = {
                val bundle = bundleOf("bundle_id" to it)
                navController?.navigate(R.id.action_orderBundleFragment_to_orderFragment,bundle)
            })
            viewDataBinding.rcvOrderBundleFragment.adapter = fetchAllBundleOrderAdapter
            fetchAllBundleOrderAdapter.notifyDataSetChanged()
        }
        viewDataBinding.shimmerOrderBundleLayout.visibility = View.GONE
    }

    override fun getMyCartItemsResponse() {
        if(view !=null){
            mViewModel.getMyCartItemsDataResponse.observe(viewLifecycleOwner, Observer<GetMyCartDataModel> {
                if (it != null){
                    setMyCartItemsAdapterData(it)
                }
            })
        }
    }

    private fun setMyCartItemsAdapterData(data: GetMyCartDataModel) {
        if (data.status.equals(AppConstants.SUCCESS)){
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountMyCartItems.text = data.Data?.itemCount.toString()
        }else{
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE
        }
    }

    override fun getWishListResponse() {
        if(view !=null){
            mViewModel.getWishListDataResponse.observe(viewLifecycleOwner, Observer<GetWishListDataModel> {
                if (it != null){
                    setWishListData(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }
    }

    private fun setWishListData(getWishListDataModel: GetWishListDataModel) {
        if (getWishListDataModel.status.equals(AppConstants.SUCCESS)){
            viewDataBinding.toolbar.crdCountWishList.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountWishList.text = getWishListDataModel.Data.size.toString()
        }else{
            viewDataBinding.toolbar.crdCountWishList.visibility = View.GONE
        }
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


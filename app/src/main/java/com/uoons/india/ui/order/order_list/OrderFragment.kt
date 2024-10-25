package com.uoons.india.ui.order.order_list

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
import com.uoons.india.databinding.FragmentOrderBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.order.order_list.adapter.OrderFragmentAdapter
import com.uoons.india.ui.order.order_list.model.OrderBundleListModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class OrderFragment : BaseFragment<FragmentOrderBinding, OrderFragmentVM>(),
    OrderFragmentNavigator {

    override val bindingVariable: Int = BR.orderFragmentVM
    override val layoutId: Int = R.layout.fragment_order
    override val viewModelClass: Class<OrderFragmentVM> = OrderFragmentVM::class.java
    private var navController: NavController? = null
    private var bundleId : String = ""
    private lateinit var orderFragmentAdapter : OrderFragmentAdapter
    var PID :String = ""

    override   fun init() {
        mViewModel.navigator = this
        bundleId = arguments?.getString("bundle_id").toString()

        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getAllOrdersList(bundleId)
            // Start shimmer animation
            viewDataBinding.shimmerOrderBundleLayout.startShimmer()
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {
                onInternet()
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerOrderBundleLayout.stopShimmer()
        viewDataBinding.shimmerOrderBundleLayout.visibility = View.GONE
    }

    private fun onInternet(){
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getAllOrdersList(bundleId)
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {
                onInternet()
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.order)
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE
        // Start shimmer animation
        viewDataBinding.shimmerOrderBundleLayout.startShimmer()

        PID =    AppPreference.getValue(PreferenceKeys.PROFILE_ID)
        if (PID.isNotEmpty()){
            mViewModel.getMyCartItemsApiCall()
            mViewModel.getWishListProductApiCall()
        }

        viewDataBinding.rcvOrderFragment.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener {
            super.onBackClick()
        })

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener {
            navController?.navigate(R.id.action_orderFragment_to_myCartFragment)
        })

        viewDataBinding.toolbar.ivHeartVector.setOnClickListener(View.OnClickListener {
            navController?.navigate(R.id.action_orderFragment_to_wishListFragment)
        })

    }

    override fun orderListResponse() {
        if(view !=null){
            mViewModel.getAllOrdersListResponse.observe(viewLifecycleOwner, Observer<OrderBundleListModel> {
                if (it != null){
                    setAllOrdersListAdapter(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, AppConstants.ErrorInFetchingData) }
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAllOrdersListAdapter(orderBundleListModel: OrderBundleListModel) {
       orderFragmentAdapter = OrderFragmentAdapter(orderBundleListModel.Data,requireContext(), onclick = {
            val bundle = bundleOf("pId" to it)
            navController?.navigate(R.id.action_orderFragment_to_orderDetailsFragment,bundle)
        })
        viewDataBinding.rcvOrderFragment.adapter = orderFragmentAdapter
        orderFragmentAdapter.notifyDataSetChanged()
        viewDataBinding.shimmerOrderBundleLayout.visibility = View.GONE
    }


    override fun naviGateToHomeDehsBoardFragment() {
        navController?.navigate(R.id.action_orderFragment_to_homeFragment)
    }

    fun naviGateToMyCartFragment(){
        navController?.navigate(R.id.action_orderFragment_to_myCartFragment)
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
                    context?.let { CommonUtils.showToastMessage(it, AppConstants.ErrorInFetchingData) }
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
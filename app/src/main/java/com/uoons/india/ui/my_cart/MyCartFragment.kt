package com.uoons.india.ui.my_cart

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentMyCartBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.my_cart.adapter.MyCartFragmentAdapter
import com.uoons.india.ui.my_cart.coupen_code.CoupenCodeModel
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.order.order_details.OrderDetailsFragment
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.NumberFormat
import java.util.*

@Obfuscate
class MyCartFragment : BaseFragment<FragmentMyCartBinding, MyCartFragmentVM>(), MyCartFragmentNavigator, OnListItemClicked {

    private var LOG_TAG = MyCartFragment::class.java.name
    override val bindingVariable: Int = BR.myCartFragmentVM
    override val layoutId: Int = R.layout.fragment_my_cart
    override val viewModelClass: Class<MyCartFragmentVM> = MyCartFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var myCartFragmentAdapter: MyCartFragmentAdapter
    private lateinit var getMyCartDataModel: GetMyCartDataModel
    private var deleteCartItem: Boolean = false
    private var initializeData: Boolean = true
    private var removePosition: Int = 0
    var itemPosition: Int = 0

    override   fun init() {
        mViewModel.navigator = this
        getMyCartDataModel = GetMyCartDataModel()
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            viewDataBinding.shimmerMyCartLayout.startShimmer()
            mViewModel.getMyCartItemsApiCall()
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.my_cart)
        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener {
            super.onBackClick()
        })
    }

    override fun naviGateToCheckOutAddressFragment() {
        navController?.navigate(R.id.action_myCartFragment_to_checkOutAddressFragment)
    }

    override fun getMyCartItemsResponse() {
        if (view != null) {
            mViewModel.getMyCartItemsDataResponse.observe(viewLifecycleOwner, Observer<GetMyCartDataModel> {
                    if (it != null) {
                        setMyCartItemsAdapterData(it)
                    } else {
                        viewDataBinding.cartEmpty.visibility = View.VISIBLE
                        context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                    }
                })
        }
    }

    override fun getMyCartItemsFailureResponse() {
        if (view != null) {
            mViewModel.getMyCartItemsFailureDataResponse.observe(viewLifecycleOwner, Observer<GetMyCartDataModel> {
                    if (it != null) {
                        getMyCartFailureResponse(it)
                    }
                })
        }
    }

    private fun getMyCartFailureResponse(getMyCartDataModel: GetMyCartDataModel) {
        deleteCartItem = false
        viewDataBinding.cartEmpty.visibility = View.VISIBLE
        viewDataBinding.shimmerMyCartLayout.visibility = View.GONE
        viewDataBinding.nestedScrollView.visibility = View.GONE
    }

    override fun checkProductListCart() {
        if (view != null) {
            mViewModel.getCartRemoveDataResponse.observe(viewLifecycleOwner, Observer<GetMyCartDataModel> {
                    if (it != null) {
                        viewDataBinding.nestedScrollView.visibility = View.GONE
                        viewDataBinding.shimmerMyCartLayout.visibility = View.GONE
                        viewDataBinding.cartEmpty.visibility = View.VISIBLE
                    }
                })
        }
    }

    override fun removeCartItemResponse() {
        mViewModel.getMyCartItemsDeleteApiCall()
    }

    override fun naviGateToHomeDehsBoardFragment() {
        navController?.navigate(R.id.action_myCartFragment_to_homeFragment)
    }

    override fun checkCoupenCode() {
        val coupenCode: String = viewDataBinding.edtCoupenCode.text.toString()
        if (mViewModel.isValidField(coupenCode)) {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mViewModel.checkCoupenCodeApiCall(coupenCode)
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
            }
        }
    }

    override fun coupenCodeResponse() {
        if (view != null) {
            mViewModel.checkCoupenCodeDataResponse.observe(viewLifecycleOwner, Observer<CoupenCodeModel> {
                    if (it != null) {
                        if (it.status.equals(AppConstants.SUCCESS)){
                            saveCoupenCode(it)
                        }else if (it.status.equals(AppConstants.FAILURE)){
                            CommonUtils.showToastMessage(requireContext(),it.message.toString())
                        }
                    } else {
                        CommonUtils.showToastMessage(requireContext(),resources.getString(R.string.error_in_fetching_data))
                   }
                })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun saveCoupenCode(coupenCodeModel: CoupenCodeModel) {
        viewDataBinding.viewCoupen.visibility = View.VISIBLE
        viewDataBinding.txvCoupenAmmountName.visibility = View.VISIBLE
        viewDataBinding.txvCoupenAmmount.visibility = View.VISIBLE
        viewDataBinding.viewCoupenAmmount.visibility = View.VISIBLE
        viewDataBinding.txvTNewTotalAmmountText.visibility = View.VISIBLE
        viewDataBinding.txvNewTotalAmmount.visibility = View.VISIBLE

        viewDataBinding.txvCoupenAmmount.text =
            resources.getString(R.string.rupees) + coupenCodeModel.Data!!.saved
        viewDataBinding.txvNewTotalAmmount.text =
            resources.getString(R.string.rupees) + coupenCodeModel.Data!!.newTotalPrice.toString()
    }

    private fun setMyCartItemsAdapterData(data: GetMyCartDataModel) {
        if (data.Data!!.items.isEmpty()) {
            deleteCartItem = false
            viewDataBinding.cartEmpty.visibility = View.VISIBLE
            viewDataBinding.nestedScrollView.visibility = View.GONE
            viewDataBinding.shimmerMyCartLayout.visibility = View.GONE

        } else {
            viewDataBinding.cartEmpty.visibility = View.GONE
            viewDataBinding.nestedScrollView.visibility = View.VISIBLE
            viewDataBinding.shimmerMyCartLayout.visibility = View.GONE

            for (i in data.Data!!.items.indices) {
                if (data.Data!!.items[i].qty.isNullOrEmpty()) {
                    mViewModel.checkProductQuantityApiCall(data.Data!!.items[i].pid!!.toInt())
                } else {
                    getMyCartDataModel = data
                }
            }

            if (initializeData){
                myCartFragmentAdapter = MyCartFragmentAdapter(getMyCartDataModel, requireContext(), this)
                viewDataBinding.rcvMyCartItems.adapter = myCartFragmentAdapter
                Log.e(LOG_TAG, "First_time_initilize_adapter")
            }

            if (deleteCartItem) {
                viewDataBinding.rcvMyCartItems.itemAnimator = null
                myCartFragmentAdapter.notifyItemRemoved(removePosition)
                deleteCartItem = false
                Log.e(LOG_TAG, "Delete_cart_item:- $deleteCartItem")
            }else if (!initializeData){
                viewDataBinding.rcvMyCartItems.itemAnimator = null
                myCartFragmentAdapter.itemUpdate(getMyCartDataModel)
                myCartFragmentAdapter.notifyItemChanged(itemPosition)
                Log.e(LOG_TAG, "initializeData:- $initializeData")
            }

            viewDataBinding.txvPriceItems.text =
                requireContext().getString(R.string.price) + " (" + data.Data!!.itemCount + " " + requireContext().getString(
                    R.string.items
                ) + ")"
            viewDataBinding.txvPriceRupees.text =
                resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(data.Data!!.totalAmount?.toInt())
            val totalAmount = data.Data!!.totalAmount!!.toInt()
            val amount = data.Data!!.totalSaleAmount!!.toInt()
            val discount = totalAmount - amount
            viewDataBinding.txvTotalPriceDicount.text =
                "- " + resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(discount)
            viewDataBinding.txvTotalPrice.text =
                resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(data.Data!!.totalOrderAmount?.toInt())
            viewDataBinding.txvProductSellingPrice.text =
                resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(data.Data!!.totalSaleAmount?.toInt())
            viewDataBinding.txvFree.text =
                resources.getString(R.string.rupees) + data.Data!!.shipping.toString()
       }

        viewDataBinding.shimmerMyCartLayout.stopShimmer()
        viewDataBinding.shimmerMyCartLayout.visibility = View.GONE
    }


    override fun onItemClicked(position: Int, view: View?, value: String?, pId: Int, pQuantity: Int) {
        when (view?.id) {
            R.id.crdPlusQuantity -> {
                initializeData = false
                itemPosition = position
                val getProductQty = getMyCartDataModel.Data!!.items[position].qty!!.toInt()
                val getProductId = getMyCartDataModel.Data!!.items[position].pid!!.toInt()
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    productQuantityAdd(getProductId, getProductQty + 1)
                } else {
                    mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
                }

                viewDataBinding.txvPriceItems.text =
                    "Price " + "(" + getMyCartDataModel.Data!!.itemCount + " Items" + ")"
                viewDataBinding.txvPriceRupees.text =
                    resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(getMyCartDataModel.Data!!.totalAmount?.toInt())
                val totalAmount = getMyCartDataModel.Data!!.totalAmount!!.toInt()
                val amount = getMyCartDataModel.Data!!.totalSaleAmount!!.toInt()
                val dicount = totalAmount - amount
                viewDataBinding.txvTotalPriceDicount.text =
                    "- " + resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(dicount)
                viewDataBinding.txvTotalPrice.text =
                    resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(getMyCartDataModel.Data!!.totalSaleAmount?.toInt())
            }

            R.id.crdMiniseQuantity -> {
                initializeData = false
                itemPosition = position
                if (getMyCartDataModel.Data!!.items[position].qty!!.toInt() == 1) {
                    val getProductId = getMyCartDataModel.Data!!.items[position].pid!!.toInt()
                    if (mViewModel.navigator!!.checkIfInternetOn()) {
                        mViewModel.getCartItemRemoveApiCall(getProductId)
                    } else {
                        mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
                    }
                } else {
                    val getProductQty = getMyCartDataModel.Data!!.items[position].qty!!.toInt()
                    val getProductId = getMyCartDataModel.Data!!.items[position].pid!!.toInt()
                    if (mViewModel.navigator!!.checkIfInternetOn()) {
                        productQuantityAdd(getProductId, getProductQty - 1)
                    } else {
                        mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
                    }

                    viewDataBinding.txvPriceItems.text =
                        "Price " + "(" + getMyCartDataModel.Data!!.itemCount + " Items" + ")"
                    viewDataBinding.txvPriceRupees.text =
                        resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(getMyCartDataModel.Data!!.totalAmount?.toInt())
                    val totalAmount = getMyCartDataModel.Data!!.totalAmount!!.toInt()
                    val amount = getMyCartDataModel.Data!!.totalSaleAmount!!.toInt()
                    val dicount = totalAmount - amount
                    viewDataBinding.txvTotalPriceDicount.text =
                        "- " + resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(dicount)
                    viewDataBinding.txvTotalPrice.text =
                        resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault()).format(getMyCartDataModel.Data!!.totalSaleAmount?.toInt())
                }
            }
            R.id.ivFillHeart -> {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    navController?.navigate(R.id.action_myCartFragment_to_wishListFragment)
                } else {
                    mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
                }
            }
            R.id.crdDelete -> {
                itemPosition = position
                deleteCartItem = true
                removePosition = position
                initializeData = false
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    mViewModel.getCartItemRemoveApiCall(pId)
                } else {
                    mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
                }
            }
        }
    }

    private fun productQuantityAdd(pId: Int, pQty: Int) {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.addProductQuantity(pId, pQty)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {})
        }
    }
}
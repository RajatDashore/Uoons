package com.uoons.india.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentCategoryBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.category.adapter.CategoryFragmentAdapter
import com.uoons.india.ui.category.model.AllCategoryModel
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CategoryDataSingleton
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class CategoryFragment : BaseFragment<FragmentCategoryBinding, CategoryFragmentVM>(),
    CategoryFragmentNavigator {
    private var LOG_TAG = "CategoryFragment"
    override val bindingVariable: Int = BR.categoryFragmentVM
    override val layoutId: Int = R.layout.fragment_category
    override val viewModelClass: Class<CategoryFragmentVM> = CategoryFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var categoryFragmentAdapter: CategoryFragmentAdapter

    override fun init() {
        mViewModel.navigator = this
        categoryFragmentAdapter = CategoryFragmentAdapter()
        if (CategoryDataSingleton.getAllCategoryData() == null) {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mViewModel.getAllCategoriesApiCall()
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {
                    init()
                })
            }
        } else {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                CategoryDataSingleton.getAllCategoryData()?.let { setCategoriesFragmentAdapterDate(it) }
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {
                    init()
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerCategoriesLayout.stopShimmer()
        viewDataBinding.shimmerCategoriesLayout.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.category)
        // Start shimmer animation
        viewDataBinding.shimmerCategoriesLayout.startShimmer()

        if (AppPreference.getValue(PreferenceKeys.PROFILE_ID).isNotEmpty()) {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mViewModel.getMyCartItemsApiCall()
                mViewModel.getWishListProductApiCall()
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons, resources.getString(R.string.please_check_internet_connection), onClick = {
                    mViewModel.navigator!!.checkIfInternetOn()
                })
            }
        }

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener { view ->
            if (view != null) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                        val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                        loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
                    } else {
                        navController?.navigate(R.id.action_categoryFragment_to_myCartFragment)
                    }
                } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                    return@OnClickListener
                }
            }
        })

        viewDataBinding.toolbar.ivHeartVector.setOnClickListener(View.OnClickListener { view ->
            if (view != null) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.PROFILE_ID))) {
                        val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                        loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
                    } else {
                        navController?.navigate(R.id.action_categoryFragment_to_wishListFragment)
                    }
                } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                    return@OnClickListener
                }
            }
        })

        categoryFragmentAdapter.setOnItemClickListener(object :
            CategoryFragmentAdapter.OnItemClickListener {
            override fun onItemClicked(cId: String, cName: String) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    naviGateToCategoryItemsFragment(cId, cName)
                } else {
                    mViewModel.navigator!!.showAlertDialog1Button(
                        AppConstants.Uoons,
                        resources.getString(R.string.please_check_internet_connection),
                        onClick = {})
                }
            }
        })
    }

    override fun naviGateToCategoryItemsFragment(cId: String, cName: String) {
        val bundle = bundleOf(AppConstants.ParentId to "1", AppConstants.SubId to cId, AppConstants.CName to cName)
        navController?.navigate(R.id.action_categoryFragment_to_productListFragment, bundle)
    }

    override fun getAllCategoriesData() {
        if (view != null) {
            mViewModel.allCategoriesDataResponse.observe(viewLifecycleOwner, Observer<AllCategoryModel> {
                    if (it != null) {
                        if (it.status.equals(AppConstants.SUCCESS,ignoreCase = true)){
                            CategoryDataSingleton.setAllCategoryData(it)
//                            Log.e("TAG", "getAllCategoriesData: "+CategoryDataSingleton.getAllCategoryData().toString() )
                            setCategoriesFragmentAdapterDate(it)
                        }else if (it.status.equals(AppConstants.FAILURE,ignoreCase = true)){
                            CommonUtils.showToastMessage(requireContext(), it.message.toString())
                        }
                    } else {
                        CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data))
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategoriesFragmentAdapterDate(data: AllCategoryModel) {
        categoryFragmentAdapter.setAllCategoriesList(data, requireActivity())
        viewDataBinding.rvCategories.adapter = categoryFragmentAdapter
        categoryFragmentAdapter.notifyDataSetChanged()
        viewDataBinding.shimmerCategoriesLayout.stopShimmer()
        viewDataBinding.shimmerCategoriesLayout.visibility = View.GONE
    }

    override fun getMyCartItemsResponse() {
        if (view != null) {
            mViewModel.getMyCartItemsDataResponse.observe(viewLifecycleOwner, Observer<GetMyCartDataModel> {
                    if (it != null) {
                        setMyCartItemsAdapterData(it)
                    }
                })
        }
    }

    private fun setMyCartItemsAdapterData(data: GetMyCartDataModel) {
        if (data.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountMyCartItems.text = data.Data?.itemCount.toString()
        } else {
            viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE
        }
    }

    override fun getWishListResponse() {
        if (view != null) {
            mViewModel.getWishListDataResponse.observe(
                viewLifecycleOwner, Observer<GetWishListDataModel> {
                    if (it != null) {
                        setWishListData(it)
                    } else {
                        CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data))
                    }
                })
        }
    }

    private fun setWishListData(getWishListDataModel: GetWishListDataModel) {
        if (getWishListDataModel.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
            viewDataBinding.toolbar.crdCountWishList.visibility = View.VISIBLE
            viewDataBinding.toolbar.txvCountWishList.text =
                getWishListDataModel.Data.size.toString()
        } else {
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
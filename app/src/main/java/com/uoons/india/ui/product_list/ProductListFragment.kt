package com.uoons.india.ui.product_list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.gson.GsonBuilder
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentProductListBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.filter.FiltersBottomSheet
import com.uoons.india.ui.filter.model.ParentFilterModel
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.product_list.adapter.ProductListAdapter
import com.uoons.india.ui.product_list.adapter.ProductSubCategoriesAdapter
import com.uoons.india.ui.sorting.SortBottomSheet

import com.uoons.india.ui.product_list.model.*
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductListFragment : BaseFragment<FragmentProductListBinding, ProductListFragmentVM>(), ProductListFragmentNavigator {

    private var LOG_TAG = "ProductListFragment"
    override val bindingVariable: Int = BR.productListFragmentVM
    override val layoutId: Int = R.layout.fragment_product_list
    override val viewModelClass: Class<ProductListFragmentVM> = ProductListFragmentVM::class.java
    private var navController: NavController? = null
    private lateinit var productListAdapter : ProductListAdapter
    private lateinit var productSubCategoriesAdapter : ProductSubCategoriesAdapter
    private var homeItemId :String = ""
    private var homeSubItemId :String = ""
    private var searchKey :String = ""
    private var currentPage = 1
    private var productsList: ArrayList<ProductListModel> = ArrayList<ProductListModel>()
    private var updatedProductsList: ArrayList<ProductListModel> = ArrayList<ProductListModel>()

    private var subProductsList: ArrayList<SubCategoriesModel> = ArrayList<SubCategoriesModel>()
    private var sortingByNameProductsList: ArrayList<SortByNameModel> = ArrayList<SortByNameModel>()
    private var filterProductsList: ArrayList<FilterModel> = ArrayList<FilterModel>()

    private var subCId = ""
    private var subCIdBoolean : Boolean = false
    private lateinit var subCatIdModel : SubCatIdModel
    var subCatPosition : Int = 0

    private var noMoreProductBoolean : Boolean = false
    private var sortByName = ""
    private var sortByNameBoolean : Boolean = false
    var PID :String = ""
    var filterRequest = ""
    private lateinit var parentFilterModel : ParentFilterModel
    private var filterBoolean : Boolean = false

    override   fun init() {
        mViewModel.navigator = this
        parentFilterModel = ParentFilterModel()

        homeSubItemId = requireArguments().getString(AppConstants.SubId).toString()
        homeItemId = requireArguments().getString(AppConstants.ParentId).toString()
        searchKey = requireArguments().getString(AppConstants.SearchKey).toString()
        val titleName = requireArguments().getString(AppConstants.CName)

        if (titleName.isNullOrEmpty()){
            viewDataBinding.toolbar.txvTitleName.text = searchKey
        }else{
            viewDataBinding.toolbar.txvTitleName.text = titleName
        }

        sortByName = AppPreference.getValue(PreferenceKeys.SORT_BY_NAME)
        currentPage = 1
        noMoreProductBoolean = false
        productsList.clear()

        if (mViewModel.navigator!!.checkIfInternetOn()) {
            callProductList()
        }else if (mViewModel.navigator!!.isConnectedToInternet()){
            return
        }

        productSubCategoriesAdapter = ProductSubCategoriesAdapter(onclick = {
            currentPage = 1
            subCatIdModel = it
            subCId = subCatIdModel.subCatId
            subCatPosition = subCatIdModel.SubCatIdPosition
            subCIdBoolean = true

            if (mViewModel.navigator!!.checkIfInternetOn()) {
                // Start shimmer animation
                viewDataBinding.rcvProductList.visibility = View.GONE
                viewDataBinding.ivNoDataFound.visibility = View.GONE
                viewDataBinding.shimmerProductListLayout.visibility = View.VISIBLE
                viewDataBinding.shimmerProductListLayout.startShimmer()
                if (searchKey == AppConstants.Null){
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,"")
                }else{
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,searchKey)
                }
            }else{
                mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {})
            }

            productsList.clear()
            updatedProductsList.clear()
        })
    }

    companion object{
        const val CATEGORIES_TYPE = 1
        const val SLIDERS_ONE_TYPE = 2
        const val PRICE_STORE_TYPE = 3
        const val DEAL_OF_THE_DAY_TYPE = 4
        const val SLIDERS_TWO_TYPE = 5
        const val RECOMMENDED_ITEMS_TYPE = 6
        const val ADVERTISEMENT_TYPE = 7
        const val RECENTLY_VIEW_TYPE = 8
        const val SLIDERS_THREE_TYPE = 9
        const val SUGGESTION_FOR_TYPE = 10
        const val NEW_ARRIVALS_TYPE = 11
        const val MORE_ITEMS_TYPE = 12
        const val TRENDING_NOW_TYPE = 13
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerProductListLayout.stopShimmer()
        viewDataBinding.shimmerProductListLayout.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ProductListFragment","")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewDataBinding.ivNoDataFound.visibility = View.GONE
        // Start shimmer animation
        viewDataBinding.shimmerProductListLayout.visibility = View.VISIBLE
        viewDataBinding.shimmerProductListLayout.startShimmer()

        PID =    AppPreference.getValue(PreferenceKeys.PROFILE_ID)
        if (PID.isNotEmpty()){
            mViewModel.getMyCartItemsApiCall()
            mViewModel.getWishListProductApiCall()
        }

        viewDataBinding.rcvSubCategories.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }
        viewDataBinding.rcvProductList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }

        viewDataBinding.nstScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                currentPage ++
                callProductList()
            }
        })

        viewDataBinding.homeToolBar.crdSorting.setOnClickListener {
            openSortBottomSheet()
        }

        viewDataBinding.homeToolBar.crdFilters.setOnClickListener{
            openFiltersBottomSheet()
        }

        viewDataBinding.homeToolBar.edtSearch.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                naviGateToSearchItemsFragment()
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return@OnClickListener
            }
        })

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener {
            super.onBackClick()
        }

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                }else{
                    navController?.navigate(R.id.action_productListFragment_to_myCartFragment)
                }
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return@OnClickListener
            }
        })

        viewDataBinding.toolbar.ivHeartVector.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                }else{
                    navController?.navigate(R.id.action_productListFragment_to_wishListFragment)
                }
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return@OnClickListener
            }
        })
    }

    private fun callProductList(){
        if(PRICE_STORE_TYPE == homeItemId.toInt()){
            viewDataBinding.homeToolBar.crdSorting.visibility = View.GONE
            viewDataBinding.homeToolBar.crdFilters.visibility = View.GONE
            if (noMoreProductBoolean){
                context?.let { CommonUtils.showToastMessage(it, AppConstants.SorryNoMoreProduct) }
            }else{
                if (searchKey == AppConstants.Null){
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,"","")
                }else{
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,"",searchKey)
                }
            }
        }else if(RECOMMENDED_ITEMS_TYPE == homeItemId.toInt()){
            viewDataBinding.homeToolBar.crdSorting.visibility = View.GONE
            viewDataBinding.homeToolBar.crdFilters.visibility = View.GONE
            if (noMoreProductBoolean){
                context?.let { CommonUtils.showToastMessage(it, AppConstants.SorryNoMoreProduct) }
            }else{
                if (searchKey == AppConstants.Null){
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,"","","")
                }else{
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,"","",searchKey)
                }
            }
        }else if(SUGGESTION_FOR_TYPE == homeItemId.toInt()){
            viewDataBinding.homeToolBar.crdSorting.visibility = View.GONE
            viewDataBinding.homeToolBar.crdFilters.visibility = View.GONE
            if (noMoreProductBoolean){
                context?.let { CommonUtils.showToastMessage(it, AppConstants.SorryNoMoreProduct) }
            }else{
                if (searchKey == AppConstants.Null){
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,"","","")
                }else{
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,"","",searchKey)
                }
            }
        }else if(ADVERTISEMENT_TYPE == homeItemId.toInt()){
            if (noMoreProductBoolean){
                context?.let { CommonUtils.showToastMessage(it, AppConstants.SorryNoMoreProduct) }
            }else{
                if (searchKey == AppConstants.Null){
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,"")
                }else{
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,searchKey)
                }
            }
        }else if(filterBoolean){
            if (noMoreProductBoolean){
                context?.let { CommonUtils.showToastMessage(it, AppConstants.SorryNoMoreProduct) }
            }else{
                if (searchKey == AppConstants.Null){
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,"")
                }else{
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,searchKey)
                }
            }
        }else{
            if (noMoreProductBoolean){
                context?.let { CommonUtils.showToastMessage(it, AppConstants.SorryNoMoreProduct) }
            }else{
                if (searchKey == AppConstants.Null){
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,"")
                }else{
                    mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,searchKey)
                }
            }
        }
    }

    override fun productListResponse() {
        if(view != null){
            mViewModel.getProductListResponse.observe(viewLifecycleOwner, Observer<ProductModel> {
                if (it != null){
                    if (it.status.equals(AppConstants.SUCCESS,ignoreCase = true)){
                        setProductListAdapter(it,requireContext())
                    }else if (it.status.equals(AppConstants.FAILURE,ignoreCase = true)){
                        context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                    }
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setProductListAdapter(productModel: ProductModel, context: Context) {
       // viewDataBinding.rcvProductList.visibility = View.VISIBLE
        noMoreProductBoolean = productModel.data?.items?.size!! <=0
        if (productModel.data?.totalPage == 0){
            viewDataBinding.rcvSubCategories.visibility = View.VISIBLE
            viewDataBinding.ivNoDataFound.visibility = View.VISIBLE
            viewDataBinding.txvNoDataFound.visibility = View.VISIBLE
            viewDataBinding.rcvProductList.visibility = View.GONE
        }else{
            viewDataBinding.ivNoDataFound.visibility = View.GONE
            viewDataBinding.txvNoDataFound.visibility = View.GONE
            viewDataBinding.rcvSubCategories.visibility = View.VISIBLE
            viewDataBinding.rcvProductList.visibility = View.VISIBLE
        }

        subProductsList.clear()
        sortingByNameProductsList.clear()
        filterProductsList.clear()
        for (i in productModel.data?.items!!.indices){
            productsList.add(productModel.data!!.items[i])
        }

        if (currentPage == 1) {
            productListAdapter = ProductListAdapter(productsList,context, onclick = {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    val bundle = bundleOf(AppConstants.PId to it)
                    navController?.navigate(R.id.action_productListFragment_to_productDetailFragment,bundle)
                }else{
                    mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {})
                }
            })
            viewDataBinding.rcvProductList.adapter = productListAdapter
        } else {
            productListAdapter.notifyItemRangeInserted(productsList.size, 10)
        }

        setSubProductListAdapter(productModel,context)
        getSortingByNameData(productModel)
        getFilterData(productModel)
        viewDataBinding.shimmerProductListLayout.visibility = View.GONE
    }


    private fun setSubProductListAdapter(productModel: ProductModel, context: Context) {
        for (i in productModel.data?.subCategories!!.indices){
            subProductsList.add(productModel.data!!.subCategories[i])
        }

        productSubCategoriesAdapter.setSubProductList(subProductsList,context)
        viewDataBinding.rcvSubCategories.adapter = productSubCategoriesAdapter
        viewDataBinding.rcvSubCategories.scrollToPosition(subCatPosition)
    }

    private fun getSortingByNameData(productModel: ProductModel){
        for (i in productModel.data?.sortByName!!.indices){
            sortingByNameProductsList.add(productModel.data!!.sortByName[i])
        }
    }

    private fun getFilterData(productModel: ProductModel){
        for (i in productModel.data?.filter!!.indices){
            filterProductsList.add(productModel.data!!.filter[i])
        }
    }

    private fun openSortBottomSheet(){
        val sortBottomSheet = SortBottomSheet(onclick = { sortingProductList(it) }).newInstance()
        val value = Bundle()
        value.putSerializable(AppConstants.SORTINGLIST, sortingByNameProductsList)
        sortBottomSheet.arguments = value
        sortBottomSheet.show(childFragmentManager, "CategoryItemsFragment")
    }

    private fun sortingProductList(sortingId: String) {
        sortByName = sortingId
        currentPage = 1

        if (mViewModel.navigator!!.checkIfInternetOn()) {
            // Start shimmer animation
            viewDataBinding.ivNoDataFound.visibility = View.GONE
            viewDataBinding.rcvSubCategories.visibility = View.GONE
            viewDataBinding.rcvProductList.visibility = View.GONE
            viewDataBinding.shimmerProductListLayout.visibility = View.VISIBLE
            viewDataBinding.shimmerProductListLayout.startShimmer()
            if (searchKey == AppConstants.Null){
                mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,"")
            }else{
                mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,searchKey)
            }
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {})
        }

        sortByNameBoolean = true
        productsList.clear()
        updatedProductsList.clear()
    }

    private fun openFiltersBottomSheet() {
        val filtersBottomSheet =
            FiltersBottomSheet(onclick = { filterProductList(it) }).newInstance()
        val value = Bundle()
        value.putSerializable(AppConstants.FILTERLIST, filterProductsList)
        filtersBottomSheet.arguments = value
        filtersBottomSheet.show(childFragmentManager, "ProductListFragment")
    }

    private fun filterProductList(obj: ArrayList<ParentFilterModel>) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        filterRequest = gson.toJson(obj)
        AppPreference.addValue(PreferenceKeys.FILTER_LIST,filterRequest)
        Log.e(LOG_TAG,"filterProductList: $filterRequest")
        currentPage = 1
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            // Start shimmer animation
            viewDataBinding.ivNoDataFound.visibility = View.GONE
            viewDataBinding.rcvSubCategories.visibility = View.GONE
            viewDataBinding.rcvProductList.visibility = View.GONE
            viewDataBinding.shimmerProductListLayout.visibility = View.VISIBLE
            viewDataBinding.shimmerProductListLayout.startShimmer()
            if (searchKey == AppConstants.Null){
                mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,"")
            }else{
                mViewModel.productListApiCall(homeItemId,homeSubItemId,currentPage.toString(),subCId,filterRequest,sortByName,searchKey)
            }
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {})
        }

        filterBoolean = true
        productsList.clear()
        updatedProductsList.clear()
    }

    private fun naviGateToSearchItemsFragment(){
        navController?.navigate(R.id.action_productListFragment_to_searchingItemFragment)
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
        if (data.status.equals(AppConstants.SUCCESS,ignoreCase = true)){
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

    @SuppressLint("SetTextI18n")
    private fun setWishListData(getWishListDataModel: GetWishListDataModel) {
        if (getWishListDataModel.status.equals(AppConstants.SUCCESS, ignoreCase = true)){
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
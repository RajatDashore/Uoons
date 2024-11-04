package com.uoons.india.ui.home.fragment

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.uoons.india.BR
import com.uoons.india.BuildConfig
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentHomeBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.filter.FiltersBottomSheet
import com.uoons.india.ui.home.fragment.adapter.DeshBoardRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardModel
import com.uoons.india.ui.home.fragment.more_products.adapter.DeshBordMoreProductsAdapter
import com.uoons.india.ui.home.fragment.more_products.model.HomePageMoreItemsDataModel
import com.uoons.india.ui.home.fragment.more_products.model.MoreProducts
import com.uoons.india.ui.sorting.SortBottomSheet
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import com.uoons.india.utils.DashBoardDataListSingleton
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeFragmentRecyclerVM>(),
    HomeFragmentNavigator {
    private var LOG_TAG = "HomeFragment"
    private var dashBoardData: DeshBoardModel? = null
    override val bindingVariable: Int = BR.homeFragmentRecyclerVM
    override val layoutId: Int = R.layout.fragment_home
    override val viewModelClass: Class<HomeFragmentRecyclerVM> = HomeFragmentRecyclerVM::class.java
    private var navController: NavController? = null
    private lateinit var deshBoardRecyclerAdapter: DeshBoardRecyclerAdapter
    private var deshBordMoreProductsAdapter: DeshBordMoreProductsAdapter? = null
    private var currentPage = 1
    private var visit = 0
    private var noMoreProductBoolean: Boolean = false
    private var moreProductsItemList: ArrayList<MoreProducts> = ArrayList()

    companion object {
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
        const val FOUR_IMAGES = 14

    }

    override fun init() {
        mViewModel.navigator = this
        deshBoardRecyclerAdapter = DeshBoardRecyclerAdapter()
        if (dashBoardData == null && DashBoardDataListSingleton.getDastBoardData() == null && DashBoardDataListSingleton.getMoreProductsItemList().size <= 0) {
            moreProductsItemList.clear()
            currentPage = 1
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                mViewModel.getDeshBoardAPICaLL()
            } else {
                mViewModel.navigator!!.showAlertDialog1Button(
                    AppConstants.Uoons,
                    resources.getString(R.string.please_check_internet_connection),
                    onClick = {})
            }
        } else {
            if (dashBoardData == null) {
                dashBoardData = DashBoardDataListSingleton.getDastBoardData()
                moreProductsItemList = DashBoardDataListSingleton.getMoreProductsItemList()
            }
            setAdapterData(dashBoardData!!)
            setMoreProductsAdapterDataItems()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerLayout.stopShimmer()
        viewDataBinding.shimmerLayout.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        // Start shimmer animation
        viewDataBinding.shimmerLayout.startShimmer()

        val release: String = Build.VERSION.RELEASE
        val sdkVersion: Int = Build.VERSION.SDK_INT
        val versionCode = BuildConfig.VERSION_CODE
        val versionName = BuildConfig.VERSION_NAME

        val VersionCode = requireContext().packageManager.getPackageInfo(
            requireContext().packageName,
            0
        ).versionCode
        Log.e(
            LOG_TAG,
            "release:- $release and sdkVersion:- $sdkVersion versionCode:- $versionCode versionName:- $versionName VersionCode:- $VersionCode"
        )

        try {
            val appVersion: String = requireContext().packageManager.getPackageInfo(
                requireContext().packageName,
                0
            ).versionName
//            Log.e(LOG_TAG, "appVersion:- $appVersion")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        viewDataBinding.deshBoardViewRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        viewDataBinding.setVariable(bindingVariable, mViewModel)
        viewDataBinding.homeToolBar.crdSorting.visibility = View.GONE
        viewDataBinding.homeToolBar.crdFilters.visibility = View.GONE

        viewDataBinding.homeToolBar.crdSorting.setOnClickListener {
            if (CommonUtils.isInternetOn(context)) {
                openSortBottomSheet()
            }
        }

        viewDataBinding.homeToolBar.crdFilters.setOnClickListener {
            if (CommonUtils.isInternetOn(context)) {
                openFiltersBottomSheet()
            }
        }

        viewDataBinding.homeToolBar.edtSearch.setOnClickListener(View.OnClickListener {
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                naviGateToSearchItemsFragment()
            } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                return@OnClickListener
            }
        })

        viewDataBinding.refreshLayout.setOnRefreshListener {
            viewDataBinding.refreshLayout.isRefreshing = false
            visit = 0
            mViewModel.getDeshBoardAPICaLL()
        }

        viewDataBinding.refreshLayout.setColorSchemeColors(requireContext().getColor(R.color.primary_color))

        deshBoardRecyclerAdapter.setOnItemClickListener(object :
            DeshBoardRecyclerAdapter.OnItemClickListener {
            override fun onItemClicked(subId: String, parentID: Int, categoryName: String) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    when (parentID) {
                        CATEGORIES_TYPE -> {
                            naviGateToCategoryItemsFragment(
                                subId,
                                parentID.toString(),
                                categoryName
                            )
                        }

                        SLIDERS_ONE_TYPE -> {
                            if (subId.equals(AppConstants.Null, ignoreCase = true)) {
                                CommonUtils.showToastMessage(
                                    requireContext(),
                                    AppConstants.SorryNoProductFound
                                )
                            } else {
                                naviGateToCategoryItemsFragment(
                                    subId,
                                    parentID.toString(),
                                    AppConstants.SliderProducts
                                )
                            }
                        }

                        PRICE_STORE_TYPE -> {
                            naviGateToCategoryItemsFragment(
                                subId,
                                parentID.toString(),
                                categoryName
                            )
                        }

                        DEAL_OF_THE_DAY_TYPE -> {
                            naviGateToCategoryItemsFragment(
                                AppConstants.EMPTY,
                                parentID.toString(),
                                categoryName
                            )
                        }

                        FOUR_IMAGES -> {
                            Toast.makeText(
                                requireContext(),
                                "Four Images Coming Soon",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        SLIDERS_TWO_TYPE -> {
                            if (subId.equals(AppConstants.Null, ignoreCase = true)) {
                                context?.let {
                                    CommonUtils.showToastMessage(
                                        it,
                                        AppConstants.SorryNoProductFound
                                    )
                                }
                            } else {
                                naviGateToCategoryItemsFragment(
                                    subId,
                                    parentID.toString(),
                                    AppConstants.SliderProducts
                                )
                            }
                        }

                        RECOMMENDED_ITEMS_TYPE -> {
                            naviGateToCategoryItemsFragment(
                                AppConstants.EMPTY,
                                parentID.toString(),
                                categoryName
                            )
                        }

                        ADVERTISEMENT_TYPE -> {
                            naviGateToCategoryItemsFragment(
                                subId,
                                parentID.toString(),
                                categoryName
                            )
                        }

                        SUGGESTION_FOR_TYPE -> {
                            naviGateToCategoryItemsFragment(
                                AppConstants.EMPTY,
                                parentID.toString(),
                                categoryName
                            )
                        }

                        NEW_ARRIVALS_TYPE -> {
                            naviGateToCategoryItemsFragment(
                                AppConstants.EMPTY,
                                parentID.toString(),
                                categoryName
                            )
                        }

                        TRENDING_NOW_TYPE -> {
                            naviGateToCategoryItemsFragment(
                                AppConstants.EMPTY,
                                parentID.toString(),
                                categoryName
                            )
                        }

                        SLIDERS_THREE_TYPE -> {
                            if (subId.equals(AppConstants.Null, ignoreCase = true)) {
                                context?.let {
                                    CommonUtils.showToastMessage(
                                        it,
                                        AppConstants.SorryNoProductFound
                                    )
                                }
                            } else {
                                naviGateToCategoryItemsFragment(
                                    subId,
                                    parentID.toString(),
                                    AppConstants.SliderProducts
                                )
                            }
                        }
                    }
                } else if (mViewModel.navigator!!.isConnectedToInternet()) {
                    return
                }
            }
        })


        viewDataBinding.nstScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                if (noMoreProductBoolean) {
                    CommonUtils.showToastMessage(requireContext(), AppConstants.SorryNoMoreProduct)
                } else {
                    if (currentPage == 18) {
//                        Log.e(LOG_TAG, "currentPage:- $currentPage")
                    } else {
                        currentPage++
                        mViewModel.getMoreDeshBoardProducts(currentPage, true)
                    }
                }
            }
        })

        deshBoardRecyclerAdapter.setOnProductIdClickListener(object :
            DeshBoardRecyclerAdapter.OnProductIdClickListener {
            override fun onProductIdClicked(pId: String) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    naviGateToProductDetail(pId)
                } else {
                    mViewModel.navigator!!.showAlertDialog1Button(
                        AppConstants.Uoons,
                        resources.getString(R.string.please_check_internet_connection),
                        onClick = {})
                }
            }
        })
    }

    override fun getDeshBoardData() {
        if (view != null) {
            mViewModel.deshBoardDataResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        dashBoardData = it
                        if (visit == 0)
                            DashBoardDataListSingleton.setDastBoardData(dashBoardData)
                        setAdapterData(it)
                        mViewModel.getMoreDeshBoardProducts(currentPage, false)
                        AppPreference.addValue(PreferenceKeys.ONE_TIME_REQUEST, AppConstants.FALSE)
                    } else if (it.status.equals(AppConstants.FAILURE, ignoreCase = true)) {
                        CommonUtils.showToastMessage(requireContext(), it.message.toString())
                    }
                } else {
                    CommonUtils.showToastMessage(
                        requireContext(),
                        resources.getString(R.string.error_in_fetching_data)
                    )
                }
            }
        }
    }

    override fun getDeshBoardMoreProductsData() {
        if (view != null) {
            mViewModel.deshBoardMoreProductsDataResponse.observe(viewLifecycleOwner) {
                if (it != null) {
                    if (it.status.equals(AppConstants.SUCCESS, ignoreCase = true)) {
                        setMoreProductsAdapterData(it)
                        visit++
                    } else if (it.status.equals(AppConstants.FAILURE, ignoreCase = true)) {
                        CommonUtils.showToastMessage(
                            requireContext(),
                            resources.getString(R.string.error_in_fetching_data)
                        )
                    }
                } else {
                    CommonUtils.showToastMessage(
                        requireContext(),
                        resources.getString(R.string.error_in_fetching_data)
                    )
                }
            }
        }
    }

    private fun setAdapterData(data: DeshBoardModel) {
        deshBoardRecyclerAdapter.setItemsList(data, requireContext())
        viewDataBinding.deshBoardViewRecycler.adapter = deshBoardRecyclerAdapter
        viewDataBinding.shimmerLayout.stopShimmer()
        viewDataBinding.shimmerLayout.visibility = View.GONE
    }

    private fun setMoreProductsAdapterData(homePageMoreItemsDataModel: HomePageMoreItemsDataModel) {
        if (homePageMoreItemsDataModel.data?.products?.size!! <= 0) {
            noMoreProductBoolean = true
        }

        for (i in homePageMoreItemsDataModel.data?.products?.indices!!) {
            moreProductsItemList.add(homePageMoreItemsDataModel.data!!.products[i])
        }

        setMoreProductsAdapterDataItems()
    }

    private fun setMoreProductsAdapterDataItems() {
        if (deshBordMoreProductsAdapter == null) {
            deshBordMoreProductsAdapter =
                DeshBordMoreProductsAdapter(moreProductsItemList, requireContext(), onclick = {
                    if (mViewModel.navigator!!.checkIfInternetOn()) {
                        naviGateToProductDetail(it)
                    } else {
                        mViewModel.navigator!!.showAlertDialog1Button(
                            AppConstants.Uoons,
                            resources.getString(R.string.please_check_internet_connection),
                            onClick = {})
                    }
                })
        }
        if (currentPage == 1) {
            DashBoardDataListSingleton.setMoreProductsItemList(moreProductsItemList)
            viewDataBinding.moreProductViewRecycler.adapter = deshBordMoreProductsAdapter
        } else {
            deshBordMoreProductsAdapter?.notifyItemRangeInserted(moreProductsItemList.size, 11)
        }

        if (viewDataBinding.moreProductViewRecycler.adapter == null) viewDataBinding.moreProductViewRecycler.adapter =
            deshBordMoreProductsAdapter

        viewDataBinding.shimmerLayout.stopShimmer()
        viewDataBinding.shimmerLayout.visibility = View.GONE
    }

    private fun openSortBottomSheet() {
        val sortBottomSheet = SortBottomSheet(onclick = {}).newInstance()
        sortBottomSheet.show(childFragmentManager, LOG_TAG)
    }

    private fun openFiltersBottomSheet() {
        val filtersBottomSheet = FiltersBottomSheet(onclick = {}).newInstance()
        filtersBottomSheet.show(childFragmentManager, LOG_TAG)
    }

    private fun naviGateToSearchItemsFragment() {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            navController?.navigate(R.id.action_homeFragment_to_searchingItemFragment)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    override fun naviGateToProductDetail(pId: String) {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            val bundle = bundleOf(AppConstants.PId to pId)
            navController?.navigate(R.id.action_homeFragment_to_productDetailFragment, bundle)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }

    override fun naviGateToCategoryItemsFragment(
        subID: String,
        parentID: String,
        categoryName: String,
    ) {
        AppPreference.addValue(PreferenceKeys.HOME_SUB_ID, subID)
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            val bundle = bundleOf(
                AppConstants.ParentId to parentID,
                AppConstants.SubId to subID,
                AppConstants.CName to categoryName
            )
            navController?.navigate(R.id.action_homeFragment_to_productListFragment, bundle)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }


    override fun naviGateToSliderItemsFragment(cId: String) {
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            val bundle = bundleOf(AppConstants.CId to cId)
            navController?.navigate(R.id.action_homeFragment_to_sliderItemsFragment, bundle)
        } else {
            mViewModel.navigator!!.showAlertDialog1Button(
                AppConstants.Uoons,
                resources.getString(R.string.please_check_internet_connection),
                onClick = {})
        }
    }
}
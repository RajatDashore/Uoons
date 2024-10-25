package com.uoons.india.ui.category.category_items

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentCategoryItemsBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.category.category_items.adapter.CategoryItemsAdapter
import com.uoons.india.ui.category.category_items.model.FiltersModel
import com.uoons.india.ui.category.category_items.model.SubCategoriesModel
import com.uoons.india.ui.filter.FiltersBottomSheet
import com.uoons.india.ui.filter.model.FilterDataCutomModel
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.ui.sorting.SortBottomSheet
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class CategoryItemsFragment : BaseFragment<FragmentCategoryItemsBinding, CategoryItemsFragmentVM>(), CategoryItemsFragmentNavigator {
    private var LOG_TAG = "CategoryItemsFragment"
    override val bindingVariable: Int = BR.categoryItemsFragmentVM
    override val layoutId: Int = R.layout.fragment_category_items
    override val viewModelClass: Class<CategoryItemsFragmentVM> = CategoryItemsFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var categoryItemsAdapter : CategoryItemsAdapter
    var cId :String = ""
    var PID :String = ""
    lateinit var filterDataCutomModel : FilterDataCutomModel

    override    fun init() {
        mViewModel.navigator = this
        viewDataBinding.executePendingBindings()
        categoryItemsAdapter = CategoryItemsAdapter()
        filterDataCutomModel = FilterDataCutomModel()
        cId = arguments?.getString("cId").toString()
        val c_Name = arguments?.getString("c_Name")
        viewDataBinding.toolbar.txvTitleName.text = c_Name
        getAllSubCatgegoriesBinding(cId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
//        PID= getActivity()?.let { getEncryptedSharedprefs(it).getString("PROFILE_ID", "") }.toString()
        PID =    AppPreference.getValue(PreferenceKeys.PROFILE_ID)
        init()
        viewDataBinding.homeToolBar.crdSorting.setOnClickListener(View.OnClickListener { view ->
            openSortBottomSheet()
        })

        viewDataBinding.homeToolBar.crdFilters.setOnClickListener(View.OnClickListener { view ->
            openFiltersBottomSheet()
        })

        viewDataBinding.homeToolBar.edtSearch.setOnClickListener(View.OnClickListener { view ->
            naviGateToSearchItemsFragment()
        })

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })
        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener { view ->
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
                }else{
                    navController?.navigate(R.id.action_categoryItemsFragment_to_myCartFragment)
                }
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return@OnClickListener
            }
        })

        viewDataBinding.toolbar.ivHeartVector.setOnClickListener(View.OnClickListener { view ->
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, LOG_TAG)
                }else{
                    navController?.navigate(R.id.action_categoryItemsFragment_to_wishListFragment)
                }
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return@OnClickListener
            }
        })

        categoryItemsAdapter.setOnItemClickListener(object :
            CategoryItemsAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(pId: String) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    naviGateToCategoryItemsDetailFragment(pId)
                }else if (mViewModel.navigator!!.isConnectedToInternet()){
                    return
                }
            }
        })
    }

    private fun getAllSubCatgegoriesBinding(cId: String?){
        mViewModel.getAllSubCategoriesApiCall(cId)
    }

    override fun naviGateToCategoryItemsDetailFragment(pid: String) {
        val bundle = bundleOf(AppConstants.PId to pid)
        navController?.navigate(R.id.action_categoryItemsFragment_to_categoryItemDetailsFragment,bundle)
    }

    override fun getAllSubCategoriesData() {
        if(view !=null){
            mViewModel.allSubCategoriesDataResponse.observe(viewLifecycleOwner, Observer<SubCategoriesModel> {
                if (it != null){
                    context?.let { it2 ->setSubCategoryItemsAdapterData(it,it2) }
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }

    }

    override fun getAllSortingData() {
        if(view !=null){
            mViewModel.allSubCategoriesDataResponse.observe(viewLifecycleOwner, Observer<SubCategoriesModel> {
                if (it != null){
                    context?.let {
                            it2 ->setSubCategoryItemsAdapterData(it,it2)
                    }
                }else{
                    CommonUtils.showToastMessage(requireContext(), resources.getString(R.string.error_in_fetching_data))
                }
            })
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSubCategoryItemsAdapterData(data: SubCategoriesModel, context: Context){
        categoryItemsAdapter.setCategoryItemsList(data,context)
        viewDataBinding.rcvCategoryItemsFragment.adapter = categoryItemsAdapter
        categoryItemsAdapter.notifyDataSetChanged()
    }


    private fun naviGateToSearchItemsFragment(){
        navController?.navigate(R.id.action_categoryItemsFragment_to_searchingItemFragment)
    }

    private fun openSortBottomSheet(){
        val sortBottomSheet = SortBottomSheet(onclick = { sortingProductList(it) }).newInstance()
        sortBottomSheet.show(childFragmentManager, LOG_TAG)
    }

    private fun sortingProductList(sortingId: String) {
        mViewModel.sortingData(sortingId,cId)
        AppPreference.addValue(PreferenceKeys.SORTING_ID,sortingId.toString())
        Log.d(LOG_TAG, "position $sortingId")
    }

    private fun openFiltersBottomSheet(){
        val filtersBottomSheet = FiltersBottomSheet(onclick = {filterProductList(it)}).newInstance()
        filtersBottomSheet.show(childFragmentManager, LOG_TAG)
    }

    private fun filterProductList(obj: Any) {
        filterDataCutomModel = obj as FilterDataCutomModel
        mViewModel.filterData(filterDataCutomModel.parentId.toString(),filterDataCutomModel.childId)
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
package com.uoons.india.ui.searching.search_items

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentSearchItemsBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.category.category_items.adapter.CategoryItemsAdapter
import com.uoons.india.ui.category.category_items.model.SubCategoriesModel
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SearchItemsFragment : BaseFragment<FragmentSearchItemsBinding, SearchItemsFragmentVM>(),
    SearchItemsFragmentNavigator {

    private var LOG_TAG = SearchItemsFragment::class.java.name
    override val bindingVariable: Int = BR.searchItemsFragmentVM
    override val layoutId: Int = R.layout.fragment_search_items
    override val viewModelClass: Class<SearchItemsFragmentVM> = SearchItemsFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var categoryItemsAdapter: CategoryItemsAdapter
    var searchKey: String = ""

    override     fun init() {
        mViewModel.navigator = this
        categoryItemsAdapter = CategoryItemsAdapter()
        searchKey = arguments?.getString(AppConstants.SearchKey).toString()
        viewDataBinding.toolbar.txvTitleName.text = searchKey
        getSearchItems(searchKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "======")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener {
            super.onBackClick()
        })
    }

    private fun getSearchItems(searchKey: String?) {
        mViewModel.getSearchItemsApiCall(searchKey)
    }

    override fun getAllSSearchItemsData() {
        if (view != null) {
            mViewModel.getSearchItemsDataResponse.observe(
                viewLifecycleOwner,
                Observer<SubCategoriesModel> {
                    if (it != null) {
                        context?.let { it2 -> setSearchItemsAdapterData(it, it2) }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Error in fetching data",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSearchItemsAdapterData(data: SubCategoriesModel, context: Context) {
        categoryItemsAdapter.setCategoryItemsList(data, context)
        viewDataBinding.rcvSearchItemsFragment.adapter = categoryItemsAdapter
        categoryItemsAdapter.notifyDataSetChanged()
    }
}
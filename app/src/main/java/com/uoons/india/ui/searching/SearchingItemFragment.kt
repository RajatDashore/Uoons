package com.uoons.india.ui.searching

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentSearchingItemBinding
import com.uoons.india.ui.base.BaseFragment
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import android.view.inputmethod.EditorInfo

import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.uoons.india.ui.searching.adapter.SearchItemHistoryListAdapter
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SearchingItemFragment : BaseFragment<FragmentSearchingItemBinding, SearchingItemFragmentVM>(), SearchingItemFragmentNavigator {
    private var LOG_TAG = SearchingItemFragment::class.java.name
    override val bindingVariable: Int = BR.homeFragmentRecyclerVM
    override val layoutId: Int = R.layout.fragment_searching_item
    override val viewModelClass: Class<SearchingItemFragmentVM> = SearchingItemFragmentVM::class.java
    private var navController: NavController? = null
    private var searchList: ArrayList<String> = ArrayList<String>()
    private lateinit var searchItemHistoryListAdapter : SearchItemHistoryListAdapter

    override  fun init() {
        mViewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
        viewDataBinding.rcvSearchHistoryList.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        // Lets soft keyboard trigger only if no physical keyboard present
        viewDataBinding.edtSearch.requestFocus()
        val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(viewDataBinding.edtSearch, InputMethodManager.SHOW_IMPLICIT)

        viewDataBinding.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            navController?.navigate(R.id.action_searchingItemFragment_to_homeFragment)
        })

        viewDataBinding.edtSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchKey: String = viewDataBinding.edtSearch.text.toString()
                naviGateToSearchItemsFragment(searchKey)
                return@OnEditorActionListener true
            }
            false
        })

        searchItemHistoryListAdapter = SearchItemHistoryListAdapter(searchList,requireContext()) {
            Log.e(LOG_TAG,"SearchingItemFragment:- $it")
            val bundle = bundleOf(AppConstants.ParentId to AppConstants.Searching,AppConstants.SearchKey to it)
            navController?.navigate(R.id.action_searchingItemFragment_to_productListFragment,bundle)
        }
        viewDataBinding.rcvSearchHistoryList.adapter = searchItemHistoryListAdapter
        searchItemHistoryListAdapter.notifyDataSetChanged()
    }

    override fun onBackClick() {
        super.onBackClick()
        navController?.navigate(R.id.action_searchingItemFragment_to_homeFragment)
    }

    override fun naviGateToSearchItemsFragment(searchKey : String) {
        val bundle = bundleOf(AppConstants.ParentId to AppConstants.Searching,AppConstants.SearchKey to searchKey)
        searchList.add(searchKey)
        navController?.navigate(R.id.action_searchingItemFragment_to_productListFragment,bundle)
    }
}
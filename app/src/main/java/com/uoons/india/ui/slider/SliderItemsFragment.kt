package com.uoons.india.ui.slider

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentSliderItemsBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.category.category_items.adapter.CategoryItemsAdapter
import com.uoons.india.ui.category.category_items.model.SubCategoriesModel
import com.uoons.india.ui.filter.FiltersBottomSheet
import com.uoons.india.ui.sorting.SortBottomSheet
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SliderItemsFragment : BaseFragment<FragmentSliderItemsBinding, SliderItemsFragmentVM>(),
    SliderItemsFragmentNavigator {

    override val bindingVariable: Int = BR.sliderItemsFragmentVM
    override val layoutId: Int = R.layout.fragment_slider_items
    override val viewModelClass: Class<SliderItemsFragmentVM> = SliderItemsFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var categoryItemsAdapter : CategoryItemsAdapter

    override   fun init() {
        mViewModel.navigator = this
        viewDataBinding.executePendingBindings()
        categoryItemsAdapter = CategoryItemsAdapter()
        val cId = arguments?.getString("cId")
        viewDataBinding.toolbar.txvTitleName.text = "Result"
        getHomeSliderBinding(cId)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
        viewDataBinding.homeToolBar.crdSorting.setOnClickListener(View.OnClickListener { view ->
            openSortBottomSheet()
        })

        viewDataBinding.homeToolBar.crdFilters.setOnClickListener(View.OnClickListener { view ->
            openFiltersBottomSheet()
        })

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener { view ->
            naviGateToMyCartFragment()
        })

        viewDataBinding.homeToolBar.edtSearch.setOnClickListener(View.OnClickListener { view ->
            naviGateToSearchItemsFragment()
        })

        categoryItemsAdapter.setOnItemClickListener(object :
            CategoryItemsAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(pId: String) {
                if (mViewModel.navigator!!.checkIfInternetOn()) {
                    naviGateToSliderItemsDetailFragment(pId)
                }else if (mViewModel.navigator!!.isConnectedToInternet()){
                    return
                }
            }
        })
    }

    override fun naviGateToSliderItemsDetailFragment(cId: String) {
        val bundle = bundleOf("pId" to cId)
        navController?.navigate(R.id.action_sliderItemsFragment_to_categoryItemDetailsFragment,bundle)
    }

    fun getHomeSliderBinding(cId: String?){
        mViewModel.getHomeSliderApiCall(cId)
    }

    override fun getHomeSliderData() {
        if(view !=null){
            mViewModel.homeSliderDataResponse.observe(viewLifecycleOwner, Observer<SubCategoriesModel> {
                if (it != null){
                    context?.let {
                            it2 ->setHomeSliderItemsAdapterData(it,it2)
                    }
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }

    }



    @SuppressLint("NotifyDataSetChanged")
    fun setHomeSliderItemsAdapterData(data: SubCategoriesModel, context: Context){
        categoryItemsAdapter.setCategoryItemsList(data,context)
        viewDataBinding.rcvSliderItemsFragment.adapter = categoryItemsAdapter
        categoryItemsAdapter.notifyDataSetChanged()
    }

    fun naviGateToMyCartFragment(){
        navController?.navigate(R.id.action_sliderItemsFragment_to_myCartFragment)
    }

    private fun naviGateToSearchItemsFragment(){
        navController?.navigate(R.id.action_sliderItemsFragment_to_searchingItemFragment)
    }

    private fun openSortBottomSheet(){
        val sortBottomSheet = SortBottomSheet(onclick = {}).newInstance()
        sortBottomSheet.show(childFragmentManager, "SliderItemsFragment")
    }

    private fun openFiltersBottomSheet(){
        val filtersBottomSheet = FiltersBottomSheet(onclick = {}).newInstance()
        filtersBottomSheet.show(childFragmentManager, "SliderItemsFragment")
    }

}
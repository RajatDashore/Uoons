package com.uoons.india.ui.product_list.filter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentProductFilterBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.filter.adapter.ChildFilterAdapter
import com.uoons.india.ui.filter.adapter.ParentFilterAdapter
import com.uoons.india.ui.filter.model.ChildFilter
import com.uoons.india.ui.filter.model.ParentFilterModel
import com.uoons.india.ui.product_list.model.FilterItemsListModel
import com.uoons.india.ui.product_list.model.FilterModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductFilterFragment : BaseFragment<FragmentProductFilterBinding, ProductFilterFragmentVM>(), ProductFilterFragmentNavigator{

    override val bindingVariable: Int = BR.productFilterFragmentVM
    override val layoutId: Int = R.layout.fragment_product_filter
    override val viewModelClass: Class<ProductFilterFragmentVM> = ProductFilterFragmentVM::class.java
    private var navController: NavController? = null

    private lateinit var parentFilterAdapter : ParentFilterAdapter
    private lateinit var childFilterAdapter : ChildFilterAdapter

    private var parentFilterModel : ArrayList<ParentFilterModel> = ArrayList<ParentFilterModel>()
    private var childFilter : ArrayList<ChildFilter> = ArrayList<ChildFilter>()

    var parentIdFlag : Boolean = false
    var filterName : String = "1"
    private var childIdFlag : Boolean = true

    private var parentFilterList: ArrayList<FilterModel> = ArrayList<FilterModel>()
    private var filterModel : ArrayList<FilterModel> = ArrayList<FilterModel>()

    private var parentMainFilterList: ArrayList<ArrayList<FilterModel>> = ArrayList<ArrayList<FilterModel>>()
    private var childFilterList : ArrayList<FilterItemsListModel> = ArrayList<FilterItemsListModel>()

    private var newChildsFiltersList : ArrayList<FilterItemsListModel> = ArrayList<FilterItemsListModel>()

    private var emptyParentMainFilterList: ArrayList<FilterModel> = ArrayList<FilterModel>()
    private var emptyChildMainFilterList: ArrayList<FilterItemsListModel> = ArrayList<FilterItemsListModel>()

    override  fun init() {
        mViewModel.navigator = this

        if (arguments != null) {
            parentFilterList =
                (requireArguments().getSerializable("filterList") as ArrayList<FilterModel>?)!!
            context?.let { setParentFilterItemsData(parentFilterList, it) }
            viewDataBinding.txvCategoryType.text = parentFilterList[0].name
        }
        setUpBinding()

        /*childFilterAdapter.setOnItemClickListener(object :
            ChildFilterAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(selected:Boolean, childFilterId:String, childFilterName: String) {
                Log.e("ProductFilterFragment", "OnItemClickListener:- $childFilterId $childFilterName")
            }
        })*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ProductFilterFragment","")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
        viewDataBinding.rcvFiltersType.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }


        viewDataBinding.rcvFiltersTypeCategories.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun setUpBinding(){

        parentFilterAdapter.setOnItemClickListener(object :
            ParentFilterAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(position: Int,parentFilterId: String) {
                parentIdFlag = position != 0
                filterName = parentFilterId
                Log.e("ProductFilterFragment", "ParentFilterAdapter:- $parentFilterId")

                parentMainFilterList[position].add(filterModel[position])
                viewDataBinding.txvCategoryType.text = filterModel[position].name

                setChildFilterItemsData(parentMainFilterList[position][position].items, context!!)
                parentFilterAdapter.notifyDataSetChanged()
            }
        })



    }

    @SuppressLint("NotifyDataSetChanged")
    fun setParentFilterItemsData(filterProductsList: ArrayList<FilterModel>, context: Context) {
        if (filterProductsList.isNotEmpty()){
            filterModel = filterProductsList

            for (j in filterProductsList){
                val filtersList : ArrayList<FilterModel> = ArrayList<FilterModel>()
                parentMainFilterList.add(filtersList)
            }

            for (i in filterProductsList.indices){
                parentMainFilterList[i].add(filterModel[i])
            }

            childFilterList.addAll(filterProductsList[0].items)

            newChildsFiltersList.addAll(filterProductsList[0].items)

            viewDataBinding.txvCategoryType.text = filterProductsList[0].name


            parentFilterAdapter = ParentFilterAdapter(filterProductsList, context)
            viewDataBinding.rcvFiltersType.adapter = parentFilterAdapter
            parentFilterAdapter.notifyDataSetChanged()
            setChildFilterItemsData(newChildsFiltersList, context)
        }
        else {
            parentFilterAdapter = ParentFilterAdapter(emptyParentMainFilterList, context)
            viewDataBinding.rcvFiltersType.adapter = parentFilterAdapter
            parentFilterAdapter.notifyDataSetChanged()
            setChildFilterItemsData(emptyChildMainFilterList, context)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setChildFilterItemsData(items: ArrayList<FilterItemsListModel>, context: Context) {

      /*  childFilterAdapter = ChildFilterAdapter(items,context, onclick = {})
        viewDataBinding.rcvFiltersTypeCategories.adapter = childFilterAdapter
        childFilterAdapter.notifyDataSetChanged()*/
    }

   /* override fun dismissDialog() {
        TODO("Not yet implemented")
    }*/

 /*   override fun doneFilter() {
      //  parentFilterModel.add(ParentFilterModel(filterName,childFilter))
      //  onclick.invoke(parentFilterModel)
       // dismiss()
    }
*/

}
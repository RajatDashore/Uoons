package com.uoons.india.ui.filter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FiltersBottomSheetBinding
import com.uoons.india.ui.base.BaseBottomSheetDialogFrag
import com.uoons.india.ui.filter.adapter.ChildFilterAdapter
import com.uoons.india.ui.filter.adapter.ParentFilterAdapter
import com.uoons.india.ui.filter.model.ChildFilter
import com.uoons.india.ui.filter.model.ParentFilterModel
import com.uoons.india.ui.product_list.model.FilterItemsListModel
import com.uoons.india.ui.product_list.model.FilterModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FiltersBottomSheet(var onclick: (obj: ArrayList<ParentFilterModel>) -> Unit) :
    BaseBottomSheetDialogFrag<FiltersBottomSheetBinding, FiltersBottomSheetVM>(),
    FiltersBottomSheetNavigator {

    private val TAG = "FiltersBottomSheet"
    override val bindingVariable: Int = BR.filtersBottomSheetVM
    override val layoutId: Int = R.layout.filters_bottom_sheet
    override val viewModelClass: Class<FiltersBottomSheetVM> = FiltersBottomSheetVM::class.java

    private lateinit var parentFilterAdapter: ParentFilterAdapter
    private lateinit var childFilterAdapter: ChildFilterAdapter

    private var filterModel: ArrayList<FilterModel> = ArrayList<FilterModel>()

    var filterName: String = "1"
    var filterPosition: Int = 0

    private var requestFilterList: ArrayList<ParentFilterModel> = ArrayList<ParentFilterModel>()
    private var updatedRequestFilterList: ArrayList<ParentFilterModel> =
        ArrayList<ParentFilterModel>()

    private var filterValueOne: ArrayList<ChildFilter> = ArrayList<ChildFilter>()
    private var filterValueTwo: ArrayList<ChildFilter> = ArrayList<ChildFilter>()

    private var filterList: ArrayList<FilterModel> = ArrayList<FilterModel>()

    private var dynamicCreateFilterList: ArrayList<ArrayList<FilterModel>> =
        ArrayList<ArrayList<FilterModel>>()

    private var emptyFilterList: ArrayList<FilterModel> = ArrayList<FilterModel>()

    private var emptyFilterValueList: ArrayList<FilterItemsListModel> =
        ArrayList<FilterItemsListModel>()

    private var filterValueList: ArrayList<FilterItemsListModel> = ArrayList<FilterItemsListModel>()

    override  fun init() {
        mViewModel.navigator = this

        if (arguments != null) {
            filterList =
                (requireArguments().getSerializable("filterList") as ArrayList<FilterModel>?)!!
            context?.let { setParentFilterItemsData(filterList, it) }
            viewDataBinding.txvCategoryType.text = filterList[0].name
        }
        setUpBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate")
    }

    fun newInstance(): FiltersBottomSheet {
        return FiltersBottomSheet(onclick)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.rcvFiltersType.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        init()
        ViewCompat.setNestedScrollingEnabled(viewDataBinding.rcvFiltersType, false)
        viewDataBinding.rcvFiltersTypeCategories.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun setUpBinding() {

        parentFilterAdapter.setOnItemClickListener(object :
            ParentFilterAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(position: Int, parentFilterId: String) {
                filterPosition = position
                filterName = parentFilterId
                viewDataBinding.txvCategoryType.text = filterModel[position].name
                parentFilterAdapter.notifyDataSetChanged()
                dynamicCreateFilterList[position].add(filterModel[position])
                setChildFilterItemsData(
                    dynamicCreateFilterList[position][position].items,
                    filterName,
                    context!!
                )

            }
        })

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setParentFilterItemsData(filterProductsList: ArrayList<FilterModel>, context: Context) {
        if (filterProductsList.isNotEmpty()) {
            filterModel = filterProductsList

            for (j in filterProductsList) {
                val filtersList: ArrayList<FilterModel> = ArrayList<FilterModel>()
                dynamicCreateFilterList.add(filtersList)
            }

            for (i in filterProductsList.indices) {
                dynamicCreateFilterList[i].add(filterModel[i])
            }


            filterValueList.addAll(filterProductsList[0].items)

            viewDataBinding.txvCategoryType.text = filterProductsList[0].name
            parentFilterAdapter = ParentFilterAdapter(filterProductsList, context)
            viewDataBinding.rcvFiltersType.adapter = parentFilterAdapter
            parentFilterAdapter.notifyDataSetChanged()
            setChildFilterItemsData(filterValueList, "1", context)
        } else {

            parentFilterAdapter = ParentFilterAdapter(emptyFilterList, context)
            viewDataBinding.rcvFiltersType.adapter = parentFilterAdapter
            parentFilterAdapter.notifyDataSetChanged()
            setChildFilterItemsData(emptyFilterValueList, "1", context)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setChildFilterItemsData(
        items: ArrayList<FilterItemsListModel>,
        parentId: String,
        context: Context
    ) {

        childFilterAdapter = ChildFilterAdapter(items, parentId, context, onclick = {
            if (parentId == "1") {
                filterValueOne.add(ChildFilter(it.id, it.name))
                requestFilterList.add(ParentFilterModel(it.parentId.toString(), filterValueOne))
            } else if (parentId == "2") {
                filterValueTwo.add(ChildFilter(it.id, it.name))
                requestFilterList.add(ParentFilterModel(it.parentId.toString(), filterValueTwo))
            }
        })
        viewDataBinding.rcvFiltersTypeCategories.adapter = childFilterAdapter
        childFilterAdapter.notifyDataSetChanged()
    }

    override fun dismissDialog() {
        dismiss()
    }

    override fun doneFilter() {
        for (i: ParentFilterModel in requestFilterList) {
            var isFound = false
            for (j: ParentFilterModel in updatedRequestFilterList) {
                if (j.parentId?.equals(i.parentId) == true || (j == i)) {
                    isFound = true
                    break
                }
            }
            if (!isFound) updatedRequestFilterList.add(i)
        }

        onclick.invoke(updatedRequestFilterList)
        dismiss()
    }

}

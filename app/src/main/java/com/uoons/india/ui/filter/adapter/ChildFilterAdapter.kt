package com.uoons.india.ui.filter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowCategoryFiltersItemsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_list.model.FilterItemsListModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ChildFilterAdapter(childFilterList: ArrayList<FilterItemsListModel>,var parentId: String, context: Context,var onclick:(value: FilterItemsListModel)->Unit) :
    BaseRecyclerAdapter<RowCategoryFiltersItemsBinding, Any, ChildFilterAdapter.ViewHolder>() {

    private var childFilterList: ArrayList<FilterItemsListModel> = ArrayList<FilterItemsListModel>()
    private var childFilterCheckList: ArrayList<FilterItemsListModel> = ArrayList<FilterItemsListModel>()
    var context: Context

    var onClickListener: OnItemsClickListener? = null
    var lastSelectedPosition = -1

    init {
        this.childFilterList = childFilterList
        this.context = context
    }

    interface OnItemsClickListener {
        fun onItemsClicked(selected:Boolean, childFilterId:String, childFilterName: String)
    }

    fun setOnItemsClickListener(itemClick: OnItemsClickListener) {
        this.onClickListener = itemClick
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(childFilterList[position])
        childFilterCheckList.clear()

        holder.binding.chkBoxItem.isChecked = childFilterList[position].selected == true

        for (i in childFilterList.indices){
            if (childFilterList[i].selected == true){
                childFilterCheckList.add(FilterItemsListModel( childFilterList[i].id.toString(), parentId , childFilterList[i].name.toString(), true,i))
            }
        }


        holder.binding.chkBoxItem.setOnClickListener(View.OnClickListener {
            if (holder.binding.chkBoxItem.isChecked){
                onclick.invoke(FilterItemsListModel(childFilterList[position].id.toString(), parentId , childFilterList[position].name.toString(), true,position))
                childFilterList[position].selected = holder.binding.chkBoxItem.isChecked
            }else{
                onclick.invoke(FilterItemsListModel( "", "" , "", false,position))
            }

        })

    }

    override fun onCreateViewHolder(viewDataBinding: RowCategoryFiltersItemsBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowCategoryFiltersItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_category_filters_items
    }

    override fun getItemCount(): Int {
        return childFilterList.size
    }

    class ViewHolder(val binding: RowCategoryFiltersItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FilterItemsListModel) {
            binding.items = data
            binding.chkBoxItem.text = data.name
           //
        // binding.executePendingBindings()
        }
    }
}
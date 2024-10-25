package com.uoons.india.ui.filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowCategoryFiltersBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_list.model.FilterModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ParentFilterAdapter(childFilterList: ArrayList<FilterModel>, context: Context) : BaseRecyclerAdapter<RowCategoryFiltersBinding, Any, ParentFilterAdapter.ViewHolder>() {

    private var parentFilterList: ArrayList<FilterModel> = ArrayList<FilterModel>()
    var context: Context
    private var customClickListener: OnItemClickListener? = null
    var category : Int = 0
    init {
        this.parentFilterList = childFilterList
        this.context = context
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int,parentFilterId: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(parentFilterList[position])
       /* val gson = GsonBuilder().setPrettyPrinting().create()
        val ABC = gson.toJson(parentFilterList)
        Log.e("ParentFilterAdapter", "parentFilterList $ABC")*/

        holder.binding.crdCategory.setOnClickListener(View.OnClickListener {
            customClickListener?.onItemClicked(position,parentFilterList[position].id.toString())
            category = position
        })

        if (category == position){
            holder.binding.viewVertical.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_color))
            holder.binding.crdCategory.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white))
            holder.binding.txvCategory.setTextColor(ContextCompat.getColor(context, R.color.primary_color))
        }else{
            holder.binding.viewVertical.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_editext_bg))
            holder.binding.crdCategory.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gray_editext_bg))
            holder.binding.txvCategory.setTextColor(ContextCompat.getColor(context, R.color.gray_light))
        }

    }

    override fun onCreateViewHolder(viewDataBinding: RowCategoryFiltersBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowCategoryFiltersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_category_filters
    }

    override fun getItemCount(): Int {
        return parentFilterList.size
    }

    class ViewHolder(val binding: RowCategoryFiltersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FilterModel) {
            binding.filterData = data
        }
    }
}
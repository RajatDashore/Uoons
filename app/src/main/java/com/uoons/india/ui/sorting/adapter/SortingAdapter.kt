package com.uoons.india.ui.sorting.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowSortingAdapterBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter

import android.widget.CompoundButton
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.ui.product_list.model.SortByNameModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SortingAdapter(var onclick:(sortId:String)->Unit) :
    BaseRecyclerAdapter<RowSortingAdapterBinding, Any, SortingAdapter.ViewHolder>() {

    private var sortingList: ArrayList<SortByNameModel> = ArrayList<SortByNameModel>()
    lateinit var context: Context
 //   private var sortingByNameProductsList: ArrayList<SortByNameModel> = ArrayList<SortByNameModel>()

    private var customClickListener: OnItemClickListener? = null

    var sortByName : Int = 0
    var sortByNameBoolean : Boolean = false

    interface OnItemClickListener {
        fun onItemClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setSortingData(data: ArrayList<SortByNameModel>,context: Context) {
        this.sortingList = data
        this.context = context
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(sortingList[position])

        if (CommonUtils.isStringNullOrBlank(AppPreference.getValue(PreferenceKeys.SORT_BY_NAME))) {
            holder.binding.rdoBtnSorting.isChecked = false
        } else {
            for (i in sortingList.indices) {
                holder.binding.rdoBtnSorting.isChecked = sortingList[position].id.equals(AppPreference.getValue(PreferenceKeys.SORT_BY_NAME))
            }
        }

       // holder.binding.rdoBtnSorting.isChecked = position == lastSelectedPosition


        holder.binding.rdoBtnSorting.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            onclick.invoke(sortingList[position].id.toString())
            AppPreference.addValue(PreferenceKeys.SORT_BY_NAME,sortingList[position].id.toString())
            sortByNameBoolean = true
            sortByName = position
        })

       /* if (sortByNameBoolean.equals(true)) {
            if (sortByName == position) {
                holder.binding.crdSubCategories.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary_color))
                holder.binding.txvSaliantFeaturesThree.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                holder.binding.crdSubCategories.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary_color_light_1))
                holder.binding.txvSaliantFeaturesThree.setTextColor(ContextCompat.getColor(context, R.color.primary_color))
            }

        }*/

    }

    override fun onCreateViewHolder(viewDataBinding: RowSortingAdapterBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowSortingAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_sorting_adapter
    }

    override fun getItemCount(): Int {
        return sortingList.size
    }

    class ViewHolder(val binding: RowSortingAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(data: SortByNameModel) {
            binding.sortingDataModel = data
            binding.executePendingBindings()
        }
    }

}
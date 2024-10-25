package com.uoons.india.ui.product_list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowSubCategoriesListBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_list.model.SubCatIdModel
import com.uoons.india.ui.product_list.model.SubCategoriesModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductSubCategoriesAdapter(var onclick:(value: SubCatIdModel)->Unit) : BaseRecyclerAdapter<RowSubCategoriesListBinding, Any, ProductSubCategoriesAdapter.ViewHolder>(){

    var subProductList: ArrayList<SubCategoriesModel>? = null
    lateinit var context: Context
    var subCategory : Int = 0
    var subCategoryBoolean : Boolean = false

    private var customClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked(subCategoriesId: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setSubProductList(data: ArrayList<SubCategoriesModel>, context: Context){
        this.subProductList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(subProductList!![position])

        holder.binding.crdSubCategories.setOnClickListener(View.OnClickListener { view ->
            onclick.invoke(SubCatIdModel(subProductList!![position].cId.toString(),position))
            subCategoryBoolean = true
            subCategory = position
        })

        if (subCategoryBoolean) {
            if (subCategory == position) {
                holder.binding.crdSubCategories.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary_color))
                holder.binding.txvSaliantFeaturesThree.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                holder.binding.crdSubCategories.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primary_color_light_1))
                holder.binding.txvSaliantFeaturesThree.setTextColor(ContextCompat.getColor(context, R.color.primary_color))
            }

        }
    }

    override fun onCreateViewHolder(viewDataBinding: RowSubCategoriesListBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowSubCategoriesListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_sub_categories_list
    }

    override fun getItemCount(): Int {
        return subProductList!!.size
    }

    class ViewHolder(val binding: RowSubCategoriesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SubCategoriesModel) {
            binding.subCategoriesModel = data
        }
    }
}
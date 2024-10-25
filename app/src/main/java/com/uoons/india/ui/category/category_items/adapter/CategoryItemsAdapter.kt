package com.uoons.india.ui.category.category_items.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowCategoryItemsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.category.category_items.model.Data
import com.uoons.india.ui.category.category_items.model.SubCategoriesModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat

@Obfuscate
class CategoryItemsAdapter:
    BaseRecyclerAdapter<RowCategoryItemsBinding, Any, CategoryItemsAdapter.ViewHolder>() {

    var categoryItemsList: SubCategoriesModel = SubCategoriesModel()
    lateinit var context: Context

    private var customClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setCategoryItemsList(data: SubCategoriesModel, context: Context){
        this.categoryItemsList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(categoryItemsList.Data[position])

        val str : String = categoryItemsList.Data[position].discount.toString()
        val float1 : Float = str.toFloat();
        val discount = DecimalFormat("#").format(float1)
//        holder.binding.txvDisountOff.setText(discount)

        if(discount.equals("0")){
            holder.binding.txvDisountOff.visibility= View.GONE
        }else{
            holder.binding.txvDisountOff.text =  discount+context.resources.getString(R.string.disount)
        }




        holder.binding.crdProduct.setOnClickListener(View.OnClickListener { view ->
            customClickListener?.onItemClicked(categoryItemsList.Data[position].pid.toString())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowCategoryItemsBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowCategoryItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_category_items
    }

    override fun getItemCount(): Int {
        return categoryItemsList.Data.size
    }

    class ViewHolder(val binding: RowCategoryItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Data) {
            binding.categoryItems = data
            binding.executePendingBindings()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(thubmImage: ImageView, url: String) {
            CommonUtils.loadImage(thubmImage, url, thubmImage.id)
        }
    }
}
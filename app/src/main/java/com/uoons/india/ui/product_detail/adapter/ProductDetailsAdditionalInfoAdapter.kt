package com.uoons.india.ui.product_detail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowProductDetailsAdditionalInfoBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_detail.model.ProductAddInfoModel
import org.lsposed.lsparanoid.Obfuscate
import kotlin.collections.ArrayList

@Obfuscate
class ProductDetailsAdditionalInfoAdapter(var addInformation: ArrayList<ProductAddInfoModel>, var context: Context) :
    BaseRecyclerAdapter<RowProductDetailsAdditionalInfoBinding, Any, ProductDetailsAdditionalInfoAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewDataBinding: RowProductDetailsAdditionalInfoBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowProductDetailsAdditionalInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {

        if (position % 2 == 0 ){
            holder.binding.llLayout.setBackgroundColor(Color.parseColor("#F3F3F3"))
        }else{
            holder.binding.llLayout.setBackgroundColor(Color.parseColor("#DFDFDF"))
        }
      //  holder.binding.cstLayout.setBackgroundColor(generateColor())
        holder.binding.txvTitleName.text = addInformation[position].title+" : "
        holder.binding.txvDetailName.text = addInformation[position].description

    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_details_additional_info
    }

    override fun getItemCount(): Int {
        return addInformation.size
    }

    class ViewHolder(val binding: RowProductDetailsAdditionalInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


}
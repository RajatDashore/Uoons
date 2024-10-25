package com.uoons.india.ui.product_detail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowProductDetailsMoreProductsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_detail.model.SimilarProductsModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

@Obfuscate
class MoreProductDetailsMoreProductAdapter(var similarProducts: ArrayList<SimilarProductsModel>, var context: Context, var onclick:(value : String)->Unit) :
    BaseRecyclerAdapter<RowProductDetailsMoreProductsBinding, Any, MoreProductDetailsMoreProductAdapter.ViewHolder>(){

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(similarProducts[position])

        holder.binding.txvSellingPrice.text = context.resources.getString(R.string.rupees)+ NumberFormat.getNumberInstance(Locale.getDefault()).format(similarProducts[position].productSalePrice?.toInt())
        holder.binding.txvMRPPrice.text = context.resources.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(similarProducts[position].productPrice?.toInt())

        val str : String = similarProducts[position].discount.toString()
        val float1 : Float = str.toFloat()
        val discount = DecimalFormat("#").format(float1)
if(discount.equals("0")){
    holder.binding.txvDisountOff.visibility= View.GONE
}else{
    holder.binding.txvDisountOff.text =  discount+context.resources.getString(R.string.disount)
}

      //  holder.binding.txvDisountOff.text = context.data.Data?.productDescription

        holder.binding.cstLayout.setOnClickListener(View.OnClickListener { view ->
            onclick.invoke(similarProducts[position].pid.toString())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowProductDetailsMoreProductsBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowProductDetailsMoreProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_details_more_products
    }

    override fun getItemCount(): Int {
        return similarProducts.size
    }

    class ViewHolder(val binding: RowProductDetailsMoreProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SimilarProductsModel) {
            binding.similarProductsModel = data
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("catIoadImage")
        fun catIoadImage(thubmImage: ImageView, url: String) {
            CommonUtils.catIoadImage(thubmImage, url, thubmImage.id)
        }
    }
}
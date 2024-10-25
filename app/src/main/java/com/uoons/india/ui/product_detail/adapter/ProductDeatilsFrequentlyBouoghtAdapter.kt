package com.uoons.india.ui.product_detail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowProductDetailsFrequentlyBoughtProductsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_detail.model.FreqProdModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

@Obfuscate
class ProductDeatilsFrequentlyBouoghtAdapter(private val freqProd: ArrayList<FreqProdModel>, val context: Context,
                                             var onclick:(value : String)->Unit,var onclick1:(value : String)->Unit, var userCoins :String) :
    BaseRecyclerAdapter<RowProductDetailsFrequentlyBoughtProductsBinding, Any, ProductDeatilsFrequentlyBouoghtAdapter.ViewHolder>(){

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(freqProd[position])

        holder.binding.txvSellingPrice.text = context.resources.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(freqProd[position].productSalePrice?.toInt())
        holder.binding.txvMRPPrice.text = context.resources.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(freqProd[position].productPrice?.toInt())

        val str : String = freqProd[position].discount.toString()
        val float1 : Float = str.toFloat()
        val discount = DecimalFormat("#").format(float1)


        if(discount.equals("0")){
            holder.binding.txvDisountOff.visibility= View.GONE
        }else{
            holder.binding.txvDisountOff.text =  discount+context.resources.getString(R.string.disount)
        }

        holder.binding.txvRatingPoint.text = freqProd[position].rating?.rating.toString()
        holder.binding.txvAllRating.text = "("+freqProd[position].rating?.total.toString()+")"

        Log.e("","userCoins:- "+userCoins)

        if (userCoins.isEmpty() || userCoins == "0"){
            holder.binding.txvOrPay.visibility = View.GONE
            holder.binding.ivMyDoller.visibility = View.GONE
            holder.binding.txvMyCoins.visibility = View.GONE
        }else{
            val newAmount = freqProd[position].productSalePrice?.toInt()?.minus(userCoins.toInt())
            holder.binding.txvOrPay.text = "Or Pay "+context.resources.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(newAmount)+" + "
            holder.binding.txvMyCoins.text = userCoins

            holder.binding.txvOrPay.visibility = View.VISIBLE
            holder.binding.ivMyDoller.visibility = View.VISIBLE
            holder.binding.txvMyCoins.visibility = View.VISIBLE
        }

        holder.binding.crdAddToCart.setOnClickListener(View.OnClickListener { view ->
            onclick.invoke(freqProd[position].pid.toString())
        })

        holder.binding.cstLayoutFrequentBoutgh.setOnClickListener(View.OnClickListener { view ->
            onclick1.invoke(freqProd[position].pid.toString())
        })
    }


    override fun onCreateViewHolder(viewDataBinding: RowProductDetailsFrequentlyBoughtProductsBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowProductDetailsFrequentlyBoughtProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_details_frequently_bought_products
    }

    override fun getItemCount(): Int {
        return freqProd.size
    }

    class ViewHolder(val binding: RowProductDetailsFrequentlyBoughtProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FreqProdModel) {
            binding.freqProdModel = data
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(thubmImage: ImageView, url: String) {
            if (url.isEmpty()){
                CommonUtils.catIoadImage(thubmImage, "", thubmImage.id)
            }else{
                CommonUtils.catIoadImage(thubmImage, url, thubmImage.id)
            }
        }
    }


}
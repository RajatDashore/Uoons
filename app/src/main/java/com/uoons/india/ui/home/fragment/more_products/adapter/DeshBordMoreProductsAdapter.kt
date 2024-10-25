package com.uoons.india.ui.home.fragment.more_products.adapter
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
import com.uoons.india.databinding.RowHomeDeshbordMoreProductsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.more_products.model.MoreProducts
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

@Obfuscate
class DeshBordMoreProductsAdapter(var moreProductsItemList: ArrayList<MoreProducts>,
                                  var context: Context, var onclick:(value: String)->Unit) :
    BaseRecyclerAdapter<RowHomeDeshbordMoreProductsBinding, Any, DeshBordMoreProductsAdapter.ViewHolder>() {

   // private var moreProductsItemList: ArrayList<MoreProducts> = ArrayList<MoreProducts>()
  //private var moreProductsItemList: MoreData = MoreData()
  //  lateinit var context: Context

   /* fun setMoreProductsDate(data:  ArrayList<MoreProducts>, context: Context){
        this.moreProductsItemList = data
        this.context = context
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(moreProductsItemList[position])
    }

    override fun onCreateViewHolder(viewDataBinding: RowHomeDeshbordMoreProductsBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowHomeDeshbordMoreProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_deshbord_more_products
    }

    override fun getItemCount(): Int {
        return moreProductsItemList.size
    }

  inner  class ViewHolder(val binding: RowHomeDeshbordMoreProductsBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(data: MoreProducts){
            binding.recyclerMomeProducts = data

            binding.txvSellingPrice.text = context.getString(R.string.offer_price)+" "+ context.getString(
                R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(moreProductsItemList[position].productSalePrice?.toInt())
            binding.txvMRPPrice.text = context.getString(R.string.m_r_p)+" "+ context.getString(R.string.rupees)+ NumberFormat.getNumberInstance(Locale.getDefault()).format(moreProductsItemList[position].productPrice?.toInt())

            val productPrice = Integer.parseInt(moreProductsItemList[position].productPrice.toString())
            val productSellPrice = Integer.parseInt(moreProductsItemList[position].productSalePrice.toString())
            val youSavePrice = productPrice - productSellPrice

            val percentage = calculatePercentageChange(productPrice.toDouble(), productSellPrice.toDouble())

            val str: String = percentage.toString()
            val float1: Float = str.toFloat()
            val discount = DecimalFormat(AppConstants.hash).format(float1)

            if(youSavePrice.toString() == AppConstants.zero || youSavePrice.toString() == AppConstants.EMPTY){
                binding.txvDisountOff.visibility= View.GONE
            }else{
                binding.txvDisountOff.text = "($discount%) OFF"
            }


           binding.cstLayout.setOnClickListener(View.OnClickListener {
                onclick.invoke(moreProductsItemList[position].pid.toString())
            })
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("moreProducts")
        fun moreProducts(thubmImage: ImageView, url: String?) {
            CommonUtils.loadImage(thubmImage, url, thubmImage.id)
        }
    }

    fun calculatePercentageChange(oldValue: Double, newValue: Double): Double {
        val percentageChange = ((newValue - oldValue) / oldValue) * 100
        return Math.abs(percentageChange)
    }
}
package com.uoons.india.ui.home.fragment.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowHomeSomeProductBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

@Obfuscate
class SomeProductsRecyclerAdapter :
    BaseRecyclerAdapter<RowHomeSomeProductBinding, Any, SomeProductsRecyclerAdapter.ViewHolder>(){

    private var customProductIdClickListener: OnProductIdClickListener? = null
    private var someProductsItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context

    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    fun setAllPriceStoreList(data:  ArrayList<DeshBoardItems>, context: Context){
        this.someProductsItemList = data
        this.context = context
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(someProductsItemList!![position])
        holder.binding.txvSellingPrice.text = context.getString(R.string.offer_price)+" "+context.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(someProductsItemList!![position].productSalePrice?.toInt())
        holder.binding.txvMRPPrice.text = context.getString(R.string.m_r_p)+" "+context.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(someProductsItemList!![position].productPrice?.toInt())

        val productPrice = Integer.parseInt(someProductsItemList!![position].productPrice.toString())
        val productSellPrice = Integer.parseInt(someProductsItemList!![position].productSalePrice.toString())
        val youSavePrice = productPrice - productSellPrice

        val percentage = calculatePercentageChange(productPrice.toDouble(), productSellPrice.toDouble())

        val str: String = percentage.toString()
        val float1: Float = str.toFloat()
        val discount = DecimalFormat(AppConstants.hash).format(float1)

        if(youSavePrice.toString() == AppConstants.zero || youSavePrice.toString() == AppConstants.EMPTY){
            holder.binding.txvDisountOff.visibility= View.GONE
        }else{
            holder.binding.txvDisountOff.text = "($discount%) OFF"
        }

        holder.binding.cstLayoutSomeProducts.setOnClickListener(View.OnClickListener {
            customProductIdClickListener?.onProductIdClicked(someProductsItemList!![position].pid.toString())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowHomeSomeProductBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowHomeSomeProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_some_product
    }

    override fun getItemCount(): Int {
        return someProductsItemList!!.size
    }

    class ViewHolder(val binding: RowHomeSomeProductBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: DeshBoardItems){
            binding.recyclerSomeProductsDataItems = data
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImageSomeProducts")
        fun loadImage(thubmImage: ImageView, url: String) {
            if (url.isEmpty()){
                CommonUtils.loadImage(thubmImage, "", thubmImage.id)
            }else{
                CommonUtils.loadImage(thubmImage, url, thubmImage.id)
            }
        }
    }

    fun calculatePercentageChange(oldValue: Double, newValue: Double): Double {
        val percentageChange = ((newValue - oldValue) / oldValue) * 100
        return Math.abs(percentageChange)
    }
}
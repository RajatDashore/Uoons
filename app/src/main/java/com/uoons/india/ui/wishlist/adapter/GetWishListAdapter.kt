package com.uoons.india.ui.wishlist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowWishListDataBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.ui.wishlist.model.WishListData
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

@Obfuscate
class GetWishListAdapter(var onclick:(pos:Int)->Unit):
    BaseRecyclerAdapter<RowWishListDataBinding, Any, GetWishListAdapter.ViewHolder>() {

    var productWishList: GetWishListDataModel = GetWishListDataModel()
    lateinit var context: Context

    private var customClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setWishListItems(data: GetWishListDataModel, context: Context){
        this.productWishList = data
        this.context = context
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(productWishList.Data[position])
        val str : String = productWishList.Data[position].discount.toString()
        val float1 : Float = str.toFloat();
        val discount = DecimalFormat("#").format(float1)

        holder.binding.txvProductNewPrice.text = context.getString(R.string.rupees)+ NumberFormat.getNumberInstance(Locale.getDefault()).format(productWishList.Data[position].productSalePrice?.toInt())
        holder.binding.txvProductPrice.text = context.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(productWishList.Data[position].productPrice?.toInt())

        if(discount.equals("0")){
            holder.binding.txvDisountOff.visibility= View.GONE
        }else{
            holder.binding.txvDisountOff.text =  discount+context.resources.getString(R.string.disount)
        }

        holder.binding.crdProduct.setOnClickListener(View.OnClickListener { view ->
            customClickListener?.onItemClicked(productWishList.Data[position].pid.toString())
        })

        holder.binding.ivHeartVector.setOnClickListener(View.OnClickListener { view ->
             onclick.invoke(productWishList.Data[position].pid!!.toInt())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowWishListDataBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowWishListDataBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_wish_list_data
    }

    override fun getItemCount(): Int {
        return productWishList.Data.size
    }

    class ViewHolder(val binding: RowWishListDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: WishListData) {
            binding.wishListData = data
            binding.executePendingBindings()
        }
    }
}
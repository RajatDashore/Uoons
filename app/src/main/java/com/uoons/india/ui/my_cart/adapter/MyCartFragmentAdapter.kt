package com.uoons.india.ui.my_cart.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowMyCartProductBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.my_cart.OnListItemClicked
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.my_cart.model.GetMyCartItemsModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

@Obfuscate
class MyCartFragmentAdapter(data: GetMyCartDataModel, context: Context,onListItemClicked : OnListItemClicked) : BaseRecyclerAdapter<RowMyCartProductBinding, Any, MyCartFragmentAdapter.ViewHolder>() {

    private var myCartList: GetMyCartDataModel = GetMyCartDataModel()
    var mContext: Context
    private var mOnListItemClicked: OnListItemClicked? = null

    init {
        myCartList =data
        mContext =context
        mOnListItemClicked =onListItemClicked
    }

    fun itemUpdate(data: GetMyCartDataModel){
        myCartList =data
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(myCartList.Data!!.items[position])
        holder.binding.txvQuantityNo.text = myCartList.Data!!.items[position].qty
        holder.binding.txvSellingPrice.text = mContext.resources.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(myCartList.Data!!.items[position].totalSalePrice?.toInt())
        holder.binding.txvProductPrice.text = mContext.resources.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(myCartList.Data!!.items[position].productTotalPrice?.toInt())

        val str : String = myCartList.Data!!.items[position].discount.toString()
        val float1 : Float = str.toFloat()
        val discount = DecimalFormat("#").format(float1)

//        holder.binding.txtDiscount.text = mContext.resources.getString(R.string.rupees)+discount+"%"+" off"

        if(discount.equals("0")){
            holder.binding.txtDiscount.visibility= View.GONE
        }else{
            holder.binding.txtDiscount.text =  discount+mContext.resources.getString(R.string.disount)
        }
        holder.binding.crdMiniseQuantity.setOnClickListener(View.OnClickListener { view ->
            mOnListItemClicked?.onItemClicked(position, view, "",0,0)
        })

        holder.binding.crdPlusQuantity.setOnClickListener(View.OnClickListener { view ->
            mOnListItemClicked?.onItemClicked(position, view, "",0,0)
        })

        holder.binding.ivFillHeart.setOnClickListener(View.OnClickListener { view ->
            mOnListItemClicked?.onItemClicked(position, view, "",0,0)
        })

        holder.binding.crdDelete.setOnClickListener(View.OnClickListener { view ->
            mOnListItemClicked?.onItemClicked(position, view, "",
                myCartList.Data!!.items[position].pid!!.toInt(),myCartList.Data!!.items[position].qty!!.toInt())
        })

    }

    override fun onCreateViewHolder(viewDataBinding: RowMyCartProductBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowMyCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_my_cart_product
    }

    override fun getItemCount(): Int {
        return myCartList.Data!!.items.size
    }

    class ViewHolder(val binding: RowMyCartProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GetMyCartItemsModel) {
            binding.getMyCartItemsModel = data
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("myCartProductLoadImage")
        fun myCartProductLoadImage(thubmImage: ImageView, url: String) {
            if (url.isEmpty()){
                CommonUtils.loadImage(thubmImage, "", thubmImage.id)
            }else{
                CommonUtils.loadImage(thubmImage, url, thubmImage.id)
            }
        }
    }
}
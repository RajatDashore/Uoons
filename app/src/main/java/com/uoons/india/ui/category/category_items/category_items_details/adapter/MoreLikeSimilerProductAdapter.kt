package com.uoons.india.ui.category.category_items.category_items_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowSimilerProductItemsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.category.category_items.category_items_details.model.SimilarProducts
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat

@Obfuscate
class MoreLikeSimilerProductAdapter : BaseRecyclerAdapter<RowSimilerProductItemsBinding, Any, MoreLikeSimilerProductAdapter.ViewHolder>(){

    private var similerProductList : ArrayList<SimilarProducts> = ArrayList<SimilarProducts>()
    lateinit var context: Context

    private var customClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setSimilerProductList(lstSimilerProduct: ArrayList<SimilarProducts>, context: Context) {
        this.similerProductList = lstSimilerProduct
        this.context = context
    }

    override fun onCreateViewHolder(viewDataBinding: RowSimilerProductItemsBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowSimilerProductItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(similerProductList[position])
        val str : String = similerProductList[position].discount.toString()
        val float1 : Float = str.toFloat();
        val discount = DecimalFormat("#").format(float1)
//        holder.binding.txvDisountOff.text = discount
        if(discount.equals("0")){
            holder.binding.txvDisountOff.visibility= View.GONE
        }else{
            holder.binding.txvDisountOff.text =  discount+context.resources.getString(R.string.disount)
        }

        holder.binding.crdProduct.setOnClickListener(View.OnClickListener { view ->
            customClickListener?.onItemClicked(similerProductList[position].pid.toString())
        })
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_similer_product_items
    }

    override fun getItemCount(): Int {
        return similerProductList.size
    }


    class ViewHolder(val binding: RowSimilerProductItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SimilarProducts) {
            binding.similarProductItems = data
            binding.executePendingBindings()
        }
    }
}
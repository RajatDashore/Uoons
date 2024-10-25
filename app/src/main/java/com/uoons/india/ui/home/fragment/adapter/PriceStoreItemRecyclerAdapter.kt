package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowHomePriceStoreBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class PriceStoreItemRecyclerAdapter :
    BaseRecyclerAdapter<RowHomePriceStoreBinding, Any, PriceStoreItemRecyclerAdapter.ViewHolder>(){

    private var customClickListener: OnItemClickListener? = null
    var priceStoreItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setAllPriceStoreList(data:  ArrayList<DeshBoardItems>, context: Context){
        this.priceStoreItemList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(priceStoreItemList!![position])

        holder.binding.crdPriceStore.setOnClickListener(View.OnClickListener {
            customClickListener?.onItemClicked(priceStoreItemList!![position].id.toString(),priceStoreItemList!![position].title.toString())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowHomePriceStoreBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowHomePriceStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_price_store
    }

    override fun getItemCount(): Int {
        return priceStoreItemList!!.size
    }

    class ViewHolder(val binding:RowHomePriceStoreBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: DeshBoardItems){
            binding.recyclerPriceStoreDataItems = data
            binding.executePendingBindings()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(thubmImage: ImageView, url: String) {
            if (url.isEmpty()){
                CommonUtils.loadImage(thubmImage, "", thubmImage.id)
            }else{
                CommonUtils.loadImage(thubmImage, url, thubmImage.id)
            }
        }
    }
}
package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.Picasso
import com.uoons.india.BuildConfig
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
        @BindingAdapter("loadImagePriceStoreIe")
        fun loadImage(view: ImageView, url: String) {
            try {
                if (url != null) {
                    val newImageURL = BuildConfig.BASE_URL+url
                    Glide.with(view!!.context)  /*.setDefaultRequestOptions(RequestOptions().circleCrop())*/
                        .load(newImageURL)
                        .apply(RequestOptions().override(view.layoutParams.width, view.layoutParams.height))
                        .placeholder(R.drawable.image_gray_color).into(view)
                    //  Log.e("","loadImage:- $newImageURL")
                    /*GlideApp.with(view!!.context)
                        .load(newImageURL)
                        .apply(RequestOptions().override(view.layoutParams.width, view.layoutParams.height))
                        .placeholder(R.drawable.new_logo_uoons_name).into(view)*/
                }else{
                    view!!.setImageResource(R.drawable.image_gray_color)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}
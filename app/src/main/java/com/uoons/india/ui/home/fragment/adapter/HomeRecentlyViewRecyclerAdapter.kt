package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowHomeRecentlyViewsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.lang.Exception

@Obfuscate
class HomeRecentlyViewRecyclerAdapter : BaseRecyclerAdapter<RowHomeRecentlyViewsBinding, Any, HomeRecentlyViewRecyclerAdapter.ViewHolder>(){

    private var customProductIdClickListener: OnProductIdClickListener? = null
    private var recentlyViewsItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context

    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    fun setRecentlyViewsList(data:  ArrayList<DeshBoardItems>, context: Context){
        this.recentlyViewsItemList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(recentlyViewsItemList!![position])

        try {
            if (recentlyViewsItemList!![position].productImages!!.isEmpty()){
                CommonUtils.loadImage(holder.binding.ivItemsImage, "", holder.binding.ivItemsImage.id)
            }else{
                CommonUtils.loadImage(holder.binding.ivItemsImage, recentlyViewsItemList!![position].productImages, holder.binding.ivItemsImage.id)
            }
        } catch (e: Exception) {
//            Log.e("HomeRecentlyViewRecyclerAdapter","Exception:- $e")
        }

        holder.itemView.setOnClickListener(View.OnClickListener {
            customProductIdClickListener?.onProductIdClicked(recentlyViewsItemList!![position].pid.toString())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowHomeRecentlyViewsBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowHomeRecentlyViewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_recently_views
    }

    override fun getItemCount(): Int {
        return recentlyViewsItemList!!.size
    }

    class ViewHolder(val binding: RowHomeRecentlyViewsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: DeshBoardItems){
            binding.homeRecentlyViews = data
        }
    }
}
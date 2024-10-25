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
import com.uoons.india.databinding.RowHomeSliderBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SlidersOneItemsRecyclerAdapter : BaseRecyclerAdapter<RowHomeSliderBinding, Any, SlidersOneItemsRecyclerAdapter.ViewHolder>(){

    private var customClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    var bennerItemList: ArrayList<DeshBoardItems> = ArrayList<DeshBoardItems>()
    lateinit var context: Context

    fun setData(data:  ArrayList<DeshBoardItems>, context: Context){
        this.bennerItemList = data
        this.context = context
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(bennerItemList[position])

        holder.binding.ivItemsImage.setOnClickListener(View.OnClickListener { view ->
            customClickListener?.onItemClicked(bennerItemList[position].catId.toString(),bennerItemList[position].type.toString())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowHomeSliderBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowHomeSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_slider
    }

    override fun getItemCount(): Int {
        return bennerItemList.size
    }

    class ViewHolder(val binding: RowHomeSliderBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: DeshBoardItems){
            binding.homeBannerItems = data
            binding.executePendingBindings()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("sliderOneLoadImage")
        fun sliderOneLoadImage(thubmImage: ImageView, url: String) {
            CommonUtils.loadImage(thubmImage, url, thubmImage.id)
        }
    }
}
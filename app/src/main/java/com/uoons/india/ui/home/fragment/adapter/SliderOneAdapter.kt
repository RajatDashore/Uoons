package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uoons.india.databinding.RowHomeSliderBinding
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import com.uoons.india.utils.CommonUtils
import com.smarteist.autoimageslider.SliderViewAdapter
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SliderOneAdapter : SliderViewAdapter<SliderOneAdapter.ViewHolder>() {

    private var customClickListener: OnItemClickListener? = null
    var bennerItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setData(data:  ArrayList<DeshBoardItems>, context: Context){
        this.bennerItemList = data
        this.context = context
    }

    override fun getCount(): Int {
        return bennerItemList!!.size
    }


    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(RowHomeSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(bennerItemList!![position])
        if (bennerItemList!![position].mobileImage.isNullOrEmpty()){
            CommonUtils.loadImage(  holder.binding.ivItemsImage, bennerItemList!![position].image,   holder.binding.ivItemsImage.id)
        }else{
            CommonUtils.loadImage(  holder.binding.ivItemsImage, bennerItemList!![position].mobileImage,   holder.binding.ivItemsImage.id)
        }

        holder.binding.ivItemsImage.setOnClickListener{
            customClickListener?.onItemClicked(bennerItemList!![position].catId.toString(),
                bennerItemList!![position].type.toString())
        }
    }

    class ViewHolder(val binding: RowHomeSliderBinding): SliderViewAdapter.ViewHolder(binding.root){
        fun bindItems(data: DeshBoardItems){
            binding.homeBannerItems = data
        }
    }
}
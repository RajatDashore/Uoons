package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uoons.india.databinding.RowHomeSliderTwoBinding
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import com.uoons.india.utils.CommonUtils
import com.smarteist.autoimageslider.SliderViewAdapter
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SliderTwoAdapter : SliderViewAdapter<SliderTwoAdapter.ViewHolder>(){

    private var customClickListener: OnItemClickListener? = null
    var sliderTwoItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setData(data:  ArrayList<DeshBoardItems>, context: Context){
        this.sliderTwoItemList = data
        this.context = context
    }

    override fun getCount(): Int {
        return sliderTwoItemList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(RowHomeSliderTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(sliderTwoItemList!![position])

        if (sliderTwoItemList!![position].mobileImage.isNullOrEmpty()){
            CommonUtils.loadImage(  holder.binding.ivItemsImage, sliderTwoItemList!![position].image,   holder.binding.ivItemsImage.id)
        }else{
            CommonUtils.loadImage(  holder.binding.ivItemsImage, sliderTwoItemList!![position].mobileImage,   holder.binding.ivItemsImage.id)
        }

        holder.binding.ivItemsImage.setOnClickListener(View.OnClickListener {
            customClickListener?.onItemClicked(sliderTwoItemList!![position].catId.toString(),sliderTwoItemList!![position].type.toString())
        })
    }

    class ViewHolder(val binding: RowHomeSliderTwoBinding): SliderViewAdapter.ViewHolder(binding.root){
        fun bindItems(data: DeshBoardItems){
            binding.homeSliderTwoItems = data
        }
    }
}
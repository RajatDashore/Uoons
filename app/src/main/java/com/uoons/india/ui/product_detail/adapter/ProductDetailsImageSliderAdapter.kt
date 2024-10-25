package com.uoons.india.ui.product_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.uoons.india.databinding.RowProductDetailsImageSliderBinding
import com.uoons.india.ui.product_detail.model.ImagesArrayListModel
import com.uoons.india.utils.CommonUtils
import com.smarteist.autoimageslider.SliderViewAdapter
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductDetailsImageSliderAdapter(val productImageSliderList: ArrayList<ImagesArrayListModel>,val context: Context) : SliderViewAdapter<ProductDetailsImageSliderAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(RowProductDetailsImageSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(productImageSliderList[position])

    }

    class ViewHolder(val binding: RowProductDetailsImageSliderBinding): SliderViewAdapter.ViewHolder(binding.root){
        fun bindItems(data: ImagesArrayListModel){
            binding.imagesArrayListModel = data
        }
    }

    override fun getCount(): Int {
        return productImageSliderList.size
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(thubmImage: ImageView, url: String) {
            CommonUtils.loadImage(thubmImage, url, thubmImage.id)
        }
    }
}
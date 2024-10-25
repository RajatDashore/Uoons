package com.uoons.india.ui.product_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowProductDetailsReviewImageBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductDetailRatingImagesAdapter() :
    BaseRecyclerAdapter<RowProductDetailsReviewImageBinding, Any, ProductDetailRatingImagesAdapter.ViewHolder>(){
    var raviewImagesList: ArrayList<String> = ArrayList()

    fun setReviewImages(imagesList: ArrayList<String>){
        this.raviewImagesList = imagesList
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        CommonUtils.catIoadImage(holder.binding.ivProductImage, raviewImagesList[position], holder.binding.ivProductImage.id)
    }

    override fun onCreateViewHolder(viewDataBinding: RowProductDetailsReviewImageBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowProductDetailsReviewImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_details_review_image
    }

    override fun getItemCount(): Int {
        return raviewImagesList.size
    }

    class ViewHolder(val binding: RowProductDetailsReviewImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}
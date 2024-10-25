package com.uoons.india.ui.product_detail.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowProductDetailsDescriptionImageBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_detail.model.ImagesArrayListModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductDetailsDescriptionImageAdapter(private val imagesLst: ArrayList<ImagesArrayListModel>?, val requireContext: Context) :
    BaseRecyclerAdapter<RowProductDetailsDescriptionImageBinding, Any, ProductDetailsDescriptionImageAdapter.ViewHolder>(){


    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        CommonUtils.loadImage(holder.binding.ivProductImage, imagesLst?.get(position)?.image, holder.binding.ivProductImage.id)

        Log.e("TAG", "IMAGE<><><>><>    : "+imagesLst?.get(position)?.image)
       val new= imagesLst?.get(position)?.image?.replace("\\","");
        Log.e("check", "IMAGE*******     : $new")
    }

    override fun onCreateViewHolder(viewDataBinding: RowProductDetailsDescriptionImageBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowProductDetailsDescriptionImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_details_description_image
    }

    override fun getItemCount(): Int {
        return imagesLst?.size!!
    }

    class ViewHolder(val binding: RowProductDetailsDescriptionImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
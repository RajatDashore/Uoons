package com.uoons.india.ui.order.order_review_rating.adaper

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowProductReviewPhotosBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import org.lsposed.lsparanoid.Obfuscate
import java.util.*

@Obfuscate
class ProductReviewPhotosAdapter(val productReviewList: ArrayList<Uri>,var onclick:(value : Int)->Unit) : BaseRecyclerAdapter<RowProductReviewPhotosBinding, Any, ProductReviewPhotosAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewDataBinding: RowProductReviewPhotosBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowProductReviewPhotosBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.binding.ivProductImage.setImageURI(productReviewList[position])

        holder.binding.ivRemoveThisPhoto.setOnClickListener(View.OnClickListener { view ->
            onclick.invoke(position)
        })
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_review_photos
    }

    override fun getItemCount(): Int {
        return productReviewList.size
    }

    class ViewHolder(val binding: RowProductReviewPhotosBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}
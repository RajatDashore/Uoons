package com.uoons.india.ui.product_detail.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.ui.product_detail.model.ReviewsModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductDeatilsRatingReviewAdapter(val reviewsLst: ArrayList<ReviewsModel>?, val requireContext: Context,
                                        var onclickThumbUpReview:(value : String)->Unit,var onclickThumbDownReview:(value : String)->Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    // Product Review Images Adapter
    private var productDetailRatingImagesAdapter = ProductDetailRatingImagesAdapter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout: View = LayoutInflater.from(requireContext).inflate(R.layout.row_product_details_rating_reviews,parent,false)
        return LayoutRaviewImagesViewHolder(layout)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)  {
        (holder as LayoutRaviewImagesViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return reviewsLst?.size!!
    }

    private inner class LayoutRaviewImagesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var txvProductReview: TextView = itemView.findViewById(R.id.txvProductReview)
        var txvRatingPoint: TextView = itemView.findViewById(R.id.txvRatingPoint)
        var txvThumbUpCount: TextView = itemView.findViewById(R.id.txvThumbUpCount)
        var txvThumbDownCount: TextView = itemView.findViewById(R.id.txvThumbDownCount)

        var ivThumbUp: ImageView = itemView.findViewById(R.id.ivThumbUp)
        var ivThumbDown: ImageView = itemView.findViewById(R.id.ivThumbDown)

        var rcvReviewImages: RecyclerView = itemView.findViewById(R.id.rcvReviewImages)

        fun bind(position: Int) {
            Log.e("","reviewsLst:-"+reviewsLst!![position].image)

            if (reviewsLst[position].image.isNullOrEmpty()){
                Log.e("","reviewsLst:-"+ reviewsLst[position].image)
            }else{
                setProductImagesItemRecycler(rcvReviewImages, reviewsLst[position].image)
            }



            txvProductReview.text = reviewsLst[position].review
            txvThumbUpCount.text = reviewsLst[position].likes
            txvThumbDownCount.text = reviewsLst[position].unlikes
            txvRatingPoint.text = reviewsLst[position].rating

            ivThumbUp.setOnClickListener(View.OnClickListener {
                onclickThumbUpReview.invoke(reviewsLst[position].orderReviewId.toString())
            })

            ivThumbDown.setOnClickListener(View.OnClickListener {
                onclickThumbDownReview.invoke(reviewsLst[position].orderReviewId.toString())
            })

        }
    }

    private fun setProductImagesItemRecycler(recyclerView: RecyclerView, raviewImagesList: ArrayList<String>) {
        productDetailRatingImagesAdapter.setReviewImages(raviewImagesList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = productDetailRatingImagesAdapter
    }
}
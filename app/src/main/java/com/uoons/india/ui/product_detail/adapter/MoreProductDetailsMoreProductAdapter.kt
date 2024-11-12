package com.uoons.india.ui.product_detail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uoons.india.BuildConfig
import com.uoons.india.R
import com.uoons.india.databinding.RowProductDetailsMoreProductsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_detail.model.SimilarProductsModel
import org.lsposed.lsparanoid.Obfuscate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

@Obfuscate
class MoreProductDetailsMoreProductAdapter(
    var similarProducts: ArrayList<SimilarProductsModel>,
    var context: Context,
    var onclick: (value: String) -> Unit,
) :
    BaseRecyclerAdapter<RowProductDetailsMoreProductsBinding, Any, MoreProductDetailsMoreProductAdapter.ViewHolder>() {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(similarProducts[position])

        holder.binding.txvSellingPrice.text =
            context.resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(similarProducts[position].productSalePrice?.toInt())
        holder.binding.txvMRPPrice.text =
            context.resources.getString(R.string.rupees) + NumberFormat.getNumberInstance(Locale.getDefault())
                .format(similarProducts[position].productPrice?.toInt())

        val str: String = similarProducts[position].discount.toString()
        val float1: Float = str.toFloat()
        val discount = DecimalFormat("#").format(float1)
        if (discount.equals("0")) {
            holder.binding.txvDisountOff.visibility = View.GONE
        } else {
            holder.binding.txvDisountOff.text =
                discount + context.resources.getString(R.string.disount)
        }

        //  holder.binding.txvDisountOff.text = context.data.Data?.productDescription

        holder.binding.cstLayout.setOnClickListener(View.OnClickListener { view ->
            onclick.invoke(similarProducts[position].pid.toString())
        })
    }

    override fun onCreateViewHolder(
        viewDataBinding: RowProductDetailsMoreProductsBinding,
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            RowProductDetailsMoreProductsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_details_more_products
    }

    override fun getItemCount(): Int {
        return similarProducts.size
    }

    class ViewHolder(val binding: RowProductDetailsMoreProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SimilarProductsModel) {
            binding.similarProductsModel = data
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("catIoadImageMoreProductDetails")
        fun catIoadImage(view: ImageView, url: String) {
            try {
                if (url != null) {
                    val newImageURL = BuildConfig.BASE_URL+url
                    Glide.with(view!!.context)  /*.setDefaultRequestOptions(RequestOptions().circleCrop())*/
                        .load(newImageURL)
                        .apply(RequestOptions().override(view.layoutParams.width, view.layoutParams.height))
                        .placeholder(R.drawable.image_gray_color).into(view)
                }else{
                    view!!.setImageResource(R.drawable.image_gray_color)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}
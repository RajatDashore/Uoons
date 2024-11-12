package com.uoons.india.ui.product_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uoons.india.BuildConfig
import com.uoons.india.R
import com.uoons.india.databinding.RowProductDetailsImageSliderBinding
import com.uoons.india.ui.product_detail.model.ImagesArrayListModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class MyImagesViewPagerAdapter(
    var productImageSliderList: ArrayList<ImagesArrayListModel>,
    var context: Context,
    var onClick: (value: String) -> Unit,
) :
    RecyclerView.Adapter<MyImagesViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowProductDetailsImageSliderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(productImageSliderList[position])

        holder.binding.ivItemsImage.setOnClickListener {
            onClick.invoke(productImageSliderList[position].productImage.toString())
        }
    }

    override fun getItemCount(): Int {
        return productImageSliderList.size
    }

    class ViewHolder(val binding: RowProductDetailsImageSliderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(data: ImagesArrayListModel) {
            binding.imagesArrayListModel = data
        }
    }

    companion object {
        fun loadImage(view: ImageView, url: String) {
            try {
                if (url != null) {
                    val newImageURL = BuildConfig.BASE_URL + url
                    Glide.with(view!!.context)  /*.setDefaultRequestOptions(RequestOptions().circleCrop())*/
                        .load(newImageURL)
                        .apply(
                            RequestOptions().override(
                                view.layoutParams.width,
                                view.layoutParams.height
                            )
                        )
                        .placeholder(R.drawable.image_gray_color).into(view)
                } else {
                    view!!.setImageResource(R.drawable.image_gray_color)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }


}
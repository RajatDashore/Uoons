package com.uoons.india.ui.home.fragment.adapter

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
import com.uoons.india.databinding.RowHomeDealOfTheDayBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class DealOfTheDayItemRecyclerAdapter :
    BaseRecyclerAdapter<RowHomeDealOfTheDayBinding, Any, DealOfTheDayItemRecyclerAdapter.ViewHolder>() {
    private var customProductIdClickListener: OnProductIdClickListener? = null
    private var bestSellerItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context
    private var imgList: ArrayList<Int> = arrayListOf()

    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String)
    }

   /* fun setDataPhotes(imagelist: ArrayList<Int>) {
        this.imgList = imagelist
    }
    */

    fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    fun setData(data: ArrayList<DeshBoardItems>, context: Context) {
        this.bestSellerItemList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(bestSellerItemList!![position])
     //   holder.binding.ivItemsImage.setImageResource(imgList[position])

        holder.binding.ivItemsImage.setOnClickListener {
            customProductIdClickListener?.onProductIdClicked(bestSellerItemList!![position].pid.toString())
        }
    }

    override fun onCreateViewHolder(
        viewDataBinding: RowHomeDealOfTheDayBinding,
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            RowHomeDealOfTheDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_deal_of_the_day
    }


    override fun getItemCount(): Int {
        return bestSellerItemList!!.size
    }

    class ViewHolder(val binding: RowHomeDealOfTheDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DeshBoardItems) {
            binding.homeBestSeller = data
            binding.executePendingBindings()
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImageDealOfThe")
        fun loadImage(view: ImageView, url: String) {
            if (url.isEmpty()) {
                try {
                    val newImageURL = BuildConfig.BASE_URL + url
                    Glide.with(view.context)  /*.setDefaultRequestOptions(RequestOptions().circleCrop())*/
                        .load(newImageURL)
                        .apply(
                            RequestOptions().override(
                                view.layoutParams.width,
                                view.layoutParams.height
                            )
                        )
                        .placeholder(R.drawable.image_gray_color).into(view)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    val newImageURL = BuildConfig.BASE_URL + url
                    Glide.with(view.context)  /*.setDefaultRequestOptions(RequestOptions().circleCrop())*/
                        .load(newImageURL)
                        .apply(
                            RequestOptions().override(
                                view.layoutParams.width,
                                view.layoutParams.height
                            )
                        )
                        .placeholder(R.drawable.image_gray_color).into(view)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}
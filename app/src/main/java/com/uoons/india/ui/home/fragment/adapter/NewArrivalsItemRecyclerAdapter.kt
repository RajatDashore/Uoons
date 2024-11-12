package com.uoons.india.ui.home.fragment.adapter

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
import com.uoons.india.databinding.RowHomeCategoryBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class NewArrivalsItemRecyclerAdapter :
    BaseRecyclerAdapter<RowHomeCategoryBinding, Any, NewArrivalsItemRecyclerAdapter.ViewHolder>() {

    private var customProductIdClickListener: OnProductIdClickListener? = null
    var categoryItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context

    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    fun setData(data: ArrayList<DeshBoardItems>, context: Context) {
        this.categoryItemList = data
        this.context = context
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(categoryItemList!![position])

        holder.binding.ivItemsImage.setOnClickListener(View.OnClickListener {
            customProductIdClickListener?.onProductIdClicked(categoryItemList!![position].pid.toString())
        })
    }

    override fun onCreateViewHolder(
        viewDataBinding: RowHomeCategoryBinding,
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            RowHomeCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_category
    }

    override fun getItemCount(): Int {
        return categoryItemList!!.size
    }

    class ViewHolder(val binding: RowHomeCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DeshBoardItems) {
            binding.recyclerDataItems = data
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImageNewArrival")
        fun loadImage(view: ImageView, url: String) {
            if (url.isEmpty()) {
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
            } else {
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
}
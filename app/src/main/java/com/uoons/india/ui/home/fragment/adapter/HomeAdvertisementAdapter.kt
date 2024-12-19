package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uoons.india.BuildConfig
import com.uoons.india.R
import com.uoons.india.databinding.RowHomeAdvertisementBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class HomeAdvertisementAdapter :
    BaseRecyclerAdapter<RowHomeAdvertisementBinding, Any, HomeAdvertisementAdapter.ViewHolder>() {

    private var customProductIdClickListener: OnProductIdClickListener? = null
    private var advertisementItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context

    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String, sponsoredName: String)
    }

    fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    fun setData(data: ArrayList<DeshBoardItems>, context: Context) {
        this.advertisementItemList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(advertisementItemList!![position])

        holder.itemView.setOnClickListener {
            customProductIdClickListener?.onProductIdClicked(
                advertisementItemList!![position].id.toString(),
                advertisementItemList!![position].sponsoredBy.toString()
            )
        }
    }

    override fun onCreateViewHolder(
        viewDataBinding: RowHomeAdvertisementBinding,
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            RowHomeAdvertisementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_advertisement
    }

    override fun getItemCount(): Int {
        return advertisementItemList!!.size
    }

    class ViewHolder(val binding: RowHomeAdvertisementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DeshBoardItems) {
            binding.homeAdvertisement = data
        }
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImageHomeAdvertisement")
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
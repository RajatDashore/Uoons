package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowHomeCategoryAdapterBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import com.uoons.india.utils.CommonUtils
import android.view.animation.AlphaAnimation
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.request.target
import coil3.svg.SvgDecoder
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class HomeCategoryFragmentAdapter : BaseRecyclerAdapter<RowHomeCategoryAdapterBinding, Any, HomeCategoryFragmentAdapter.ViewHolder>(){
    private var categoryItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context
    private var customClickListener: OnItemClickListener? = null
    lateinit var url: String
    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setAllCategoriesList(data: ArrayList<DeshBoardItems>, context: Context){
        this.categoryItemList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(categoryItemList!![position])
        val alpha = AlphaAnimation(1.0f, 1.0f)
        alpha.duration = 0
        alpha.fillAfter = true
        holder.binding.cstLayout.setOnClickListener(View.OnClickListener {
            customClickListener?.onItemClicked(categoryItemList!![position].cId.toString(),
                categoryItemList!![position].category.toString())
        })



        url="https://uoons.com/"+categoryItemList!![position].catIcon.toString()
//        Log.e("TAG", "url: "+url )
        println("url:>>>>>>>>>>>>       "+url)
        holder.binding.ivCategory.loadSvg(url)
    }

    override fun onCreateViewHolder(viewDataBinding: RowHomeCategoryAdapterBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowHomeCategoryAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_category_adapter
    }

    override fun getItemCount(): Int {
        return categoryItemList!!.size
    }

    class ViewHolder(val binding: RowHomeCategoryAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DeshBoardItems) {
            binding.recyclerDataItems = data
        }
    }

    fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .components { add(SvgDecoder.Factory()) }
//            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .placeholder(R.drawable.image_gray_color)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

}
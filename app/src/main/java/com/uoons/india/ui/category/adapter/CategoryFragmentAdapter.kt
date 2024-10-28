package com.uoons.india.ui.category.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uoons.india.R
import com.uoons.india.databinding.RowCategoriesBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.category.model.AllCategoryModel
import com.uoons.india.ui.category.model.CategoriesModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class CategoryFragmentAdapter :
    BaseRecyclerAdapter<RowCategoriesBinding, Any, CategoryFragmentAdapter.ViewHolder>() {

    var categoriesList: AllCategoryModel = AllCategoryModel()
    lateinit var context: Context
    private var customClickListener: OnItemClickListener? = null
    lateinit var url: String
    private var imgList: ArrayList<Int> = arrayListOf<Int>()

    interface OnItemClickListener {
        fun onItemClicked(cId: String, cName: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setAllCategoriesList(data: AllCategoryModel, context: Context, imgList: ArrayList<Int>) {
        this.categoriesList = data
        this.context = context
        this.imgList = imgList
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        try {
            holder.bind(categoriesList.Data.categories[position])
        } catch (e: Exception) {
            Log.e("TAG", "onBindViewHolder: " + e.message)
        }

        try {
            holder.binding.viewBackGround.setBackgroundColor(Color.parseColor(categoriesList.Data.categories[position].backColor?.trim()))
        } catch (e: Exception) {
            Log.e("TAG", "onBindViewHolder: " + e.message)
        }
        if (position < imgList.size) {
            Picasso.get().load(imgList[position]).placeholder(R.drawable.ic_error)
                .error(R.drawable.ic_error)
                .into(holder.binding.ivCategoryIcon)
        } else {
            Log.e(
                "Adapter Error",
                "Attempted to access position $position but list size is ${imgList.size}"
            )
            // Optionally, handle this case (e.g., by setting a placeholder or leaving it empty)
        }

        try {
            url="https://uoons.com/"+categoriesList.Data.categories[position].catMainIcon.toString()
            //    Log.e("TAG", "url: "+url )
            // println("url:>>>>>>>>>>>>       "+url)
            //holder.binding.ivCategoryIcon.loadSvg(url)
        } catch (e: Exception) {
            Log.e("TAG", "onBindViewHolder: " + e.message)
        }



        holder.binding.cstLayout.setOnClickListener(View.OnClickListener { view ->
            categoriesList.Data.categories[position].category?.let {
                categoriesList.Data.categories[position].cId?.let { it1 ->
                    customClickListener?.onItemClicked(it1, it)
                }
            }
        })
    }

    override fun onCreateViewHolder(
        viewDataBinding: RowCategoriesBinding,
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            RowCategoriesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_categories
    }

    override fun getItemCount(): Int {
        return categoriesList.Data.categories.size
    }

    class ViewHolder(val binding: RowCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CategoriesModel) {
            binding.allCategories = data
        }
    }
    /*
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
        }*/
}
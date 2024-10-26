package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uoons.india.R
import com.uoons.india.databinding.RowHomeCategoryAdapterBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import okhttp3.OkHttpClient
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class HomeCategoryFragmentAdapter :
    BaseRecyclerAdapter<RowHomeCategoryAdapterBinding, Any, HomeCategoryFragmentAdapter.ViewHolder>() {
    private var categoryItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context
    private var customClickListener: OnItemClickListener? = null
    private var httpClient: OkHttpClient? = null
    lateinit var url: String

    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setAllCategoriesList(data: ArrayList<DeshBoardItems>, context: Context) {
        this.categoryItemList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(categoryItemList!![position])
        val alpha = AlphaAnimation(1.0f, 1.0f)
        alpha.duration = 0
        alpha.fillAfter = true
        holder.binding.cstLayout.setOnClickListener(View.OnClickListener {
            customClickListener?.onItemClicked(
                categoryItemList!![position].cId.toString(),
                categoryItemList!![position].category.toString()
            )
        })



        url = "https://uoons.com/" + categoryItemList!![position].catIcon.toString()
//        Log.e("TAG", "url: "+url )
        println("url:>>>>>>>>>>>>       " + url)
        //  var url1: String = url.replace("svg", "png")
        // holder.binding.ivCategory.loadSvg(url1)
        //   Log.d("url1",url1)
        Glide.with(context).load(R.drawable.ic_error).centerCrop().into(holder.binding.ivCategory)
        //  fetchSVG(context, url, holder.binding.ivCategory)
        Log.d("UrlPhoto", url)
    }

    override fun onCreateViewHolder(
        viewDataBinding: RowHomeCategoryAdapterBinding,
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            RowHomeCategoryAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
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
        /* val imageLoader = ImageLoader.Builder(this.context)
             .components {
                 add(SvgDecoder.Factory())
                 Log.d("Helo", url)
             }
             .build()

         val request = ImageRequest.Builder(this.context)
             .crossfade(true)
             .placeholder(R.drawable.ic_error)
             .data(url)
             .target(this)
             .build()
         imageLoader.enqueue(request)
         Log.d("helo2", url)
         */

    }


    /* fun fetchSVG(context: Context, url: String, image: ImageView) {

         if (httpClient == null) {
             httpClient =
                 OkHttpClient.Builder().cache(Cache(context.cacheDir, 5 * 1024 * 1014) as Cache)
                     .build()
         }
         var request: Request = Request.Builder().url(url).build()

         httpClient!!.newCall(request).enqueue(object : Callback {
             override fun onFailure(call: Call, e: IOException) {
                 image.setImageResource(R.drawable.ic_home)
             }

             @Throws(IOException::class)
             override fun onResponse(call: Call, response: Response) {
                 val stream: InputStream = response.body!!.byteStream()
              //  Sharp.loadInputStream(stream).into(image)
                 stream.close()
             }
         })
     }
     */

}
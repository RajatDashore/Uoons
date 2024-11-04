package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import coil3.request.target
import coil3.size.Scale
import coil3.svg.SvgDecoder
import com.squareup.picasso.Picasso
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
    private var imgList: ArrayList<Int> = arrayListOf<Int>()

    lateinit var url: String


    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setAllCategoriesList(
        data: ArrayList<DeshBoardItems>,
        context: Context,
        imgList: ArrayList<Int>,
    ) {
        this.categoryItemList = data
        this.context = context
        this.imgList = imgList
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

        if (position < imgList.size) {

            Picasso.get().load(imgList[position]).placeholder(R.drawable.ic_error)
                .error(R.drawable.ic_error)
                .into(holder.binding.ivCategory)
        } else {
            Log.e(
                "Adapter Error",
                "Attempted to access position $position but list size is ${imgList.size}"
            )
            // Optionally, handle this case (e.g., by setting a placeholder or leaving it empty)
        }



        url = "https://uoons.com/" + categoryItemList!![position].catIcon.toString()
        //     Log.e("TAG", "url: " + url)
        //  println("url:>>>>>>>>>>>>       " + url)
        //  var url1: String = url.replace("svg", "png")
        //  holder.binding.ivCategory.loadSvg(url)
        //   Log.d("url1",url1)
//        Glide.with(context).load(arrImg[position]).centerCrop().into(holder.binding.ivCategory)
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

   /* fun ImageView.loadSvg(url: String) {
        // Create an ImageLoader with SvgDecoder enabled
        val imageLoader = ImageLoader.Builder(this.context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()

        // Create an ImageRequest with the target as this ImageView
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .scale(Scale.FIT)
            .placeholder(R.drawable.ic_error) // Replace with your actual placeholder
            .error(R.drawable.ic_error) // Replace with your actual error drawable
            .data(url)
            .target(this)
            .build()

        // Enqueue the request with the imageLoader
        imageLoader.enqueue(request)
    }
    */


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
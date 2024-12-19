package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uoons.india.BuildConfig
import com.uoons.india.R
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FourPhotoesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context: Context? = null
    private var customClickListener: OnItemClickListener? = null
    private var customProductIdClickListener: OnProductIdClickListener? = null
    private var imagelist: ArrayList<Int> = ArrayList()
    private var textData: ArrayList<String> = ArrayList()
    private val cartisonUrl =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrNc_6aYoQQ5NFzj6M9hbld1cXwCZepRILdg&s"
    private var categoryItemList: ArrayList<DeshBoardItems>? = null
    private var boatdataList: ArrayList<DeshBoardItems>? = null
    private val lottieView = 3
    private val viewTypes = 2


    // For the Boat Advertisment
    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String, sponsoredName: String)
    }

    fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    // For the Categories of four images
    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }


    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setBoatData(data: ArrayList<DeshBoardItems>) {
        this.boatdataList = data
    }

    fun setDataPhotes(
        textData: ArrayList<String>,
        imagelist: ArrayList<Int>,
        context: Context,
        categoryItemList: ArrayList<DeshBoardItems>
    ) {
        this.textData = textData
        this.imagelist = imagelist
        this.context = context
        this.categoryItemList = categoryItemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == lottieView) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.jwellary_lottie, parent, false)
            LottieViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.photoes, parent, false)
            SimpleViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == lottieView) {
            val lottieHolder = holder as LottieViewHolder
            lottieHolder.lottieText.text = textData[position]
            lottieHolder.lottieImage.playAnimation()
            categoryItemList!![position].cId.toString()
            holder.itemView.setOnClickListener {
                customClickListener?.onItemClicked(
                    "445",
                    "Jewellery"
                )
            }
        } else if (position == 0) {
            val simpleHolder = holder as SimpleViewHolder
            Glide.with(holder.itemView.context)
                .load(imagelist[position])
                .into(simpleHolder.image)
            simpleHolder.text.text = textData[position]
            holder.itemView.setOnClickListener {
                customProductIdClickListener?.onProductIdClicked(
                    "1", "Kayroo"
                )
            }

        } else if (position == 1) {
            val simpleHolder = holder as SimpleViewHolder
            Glide.with(holder.itemView.context).load(cartisonUrl).into(simpleHolder.image)
            simpleHolder.text.text = textData[position]
            holder.itemView.setOnClickListener {
                customClickListener?.onItemClicked("450", "Artisan Products")
            }
        } else {
            val simpleHolder = holder as SimpleViewHolder
            Glide.with(holder.itemView.context).load(imagelist[position]).into(simpleHolder.image)
            simpleHolder.text.text = textData[position]
            holder.itemView.setOnClickListener {
                customClickListener?.onItemClicked("435", "Services")
            }

        }

    }


    override fun getItemCount(): Int {
        Log.d("size", "size${categoryItemList!!.size}")
        return categoryItemList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == imagelist.size - 1 && imagelist.size >= 4) {
            lottieView
        } else {
            viewTypes
        }
    }

    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ivItemsImagePhotoes)
        val text: TextView = itemView.findViewById(R.id.fourPhotoesText)
    }

    class LottieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lottieImage: LottieAnimationView = itemView.findViewById(R.id.ivJwellaryLottie)
        val lottieText: TextView = itemView.findViewById(R.id.tvJwellaryLottieText)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImageDealOfThe")
        fun loadImage(view: ImageView, url: String) {
            if (url.isNotEmpty()) {
                try {
                    val newImageURL = BuildConfig.BASE_URL + url
                    Glide.with(view.context)
                        .load(newImageURL)
                        .apply(
                            RequestOptions().override(
                                view.layoutParams.width, view.layoutParams.height
                            )
                        )
                        .placeholder(R.drawable.image_gray_color)
                        .into(view)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

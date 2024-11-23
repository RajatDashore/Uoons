package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    private var imagelist: ArrayList<Int> = ArrayList()
    private var textData: ArrayList<String> = ArrayList()
    private val CartisonUrl =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrNc_6aYoQQ5NFzj6M9hbld1cXwCZepRILdg&s"
    private var categoryItemList: ArrayList<DeshBoardItems>? = null
    private val LottieView = 3
    private val ViewTypes = 2

    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setDataPhotes(
        textData: ArrayList<String>,
        imagelist: ArrayList<Int>,
        context: Context,
    ) {
        this.textData = textData
        this.imagelist = imagelist
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LottieView) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.jwellary_lottie, parent, false)
            LottieViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.photoes, parent, false)
            SimpleViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == LottieView) {
            val lottieHolder = holder as LottieViewHolder
            lottieHolder.lottieText.text = textData[position]
            lottieHolder.lottieImage.playAnimation()
            holder.itemView.setOnClickListener(View.OnClickListener {
                customClickListener?.onItemClicked(
                    categoryItemList!![0].cId.toString(),
                    categoryItemList!![0].category.toString()
                )
                Toast.makeText(context, "Clicked lottie", Toast.LENGTH_SHORT).show()
            })
        } else {
            val simpleHolder = holder as SimpleViewHolder
            if (position == 1) {
                Glide.with(holder.itemView.context)
                    .load(CartisonUrl)
                    .into(simpleHolder.image)
                simpleHolder.text.text = textData[position]
            } else {
                Glide.with(holder.itemView.context)
                    .load(imagelist[position])
                    .into(simpleHolder.image)
                simpleHolder.text.text = textData[position]
                simpleHolder.itemView.setOnClickListener {
                    Toast.makeText(context, "Clicked normal", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return imagelist.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == imagelist.size - 1 && imagelist.size >= 4) {
            LottieView
        } else {
            ViewTypes
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

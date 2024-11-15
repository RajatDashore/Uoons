package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uoons.india.BuildConfig
import com.uoons.india.R
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FourPhotoesAdapter : RecyclerView.Adapter<FourPhotoesAdapter.ViewHolder>() {
    lateinit var context: Context
    private lateinit var imagelist: ArrayList<Int>
    private lateinit var textData: ArrayList<String>

    fun setDataPhotes(
        textData: ArrayList<String>,
        imagelist: ArrayList<Int>,
        context: Context,
    ) {
        this.textData = textData
        this.imagelist = imagelist
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      if(position < imagelist.size){
          Glide.with(context).load(imagelist[position]).into(holder.image)
          holder.text.text = textData[position]
          Log.e("TAG", "four: "+imagelist[position]).toString()
      }
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Item Clicked $position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(context).inflate(R.layout.photoes, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imagelist.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ivItemsImagePhotoes)
        val text: TextView = itemView.findViewById(R.id.fourPhotoesText)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImageDealOfThe")
        fun loadImage(view: ImageView, url: String) {
            if (url.isEmpty()) {
                try {
                    if (url != null) {
                        val newImageURL = BuildConfig.BASE_URL + url
                        Glide.with(view!!.context)  /*.setDefaultRequestOptions(RequestOptions().circleCrop())*/
                            .load(newImageURL).apply(
                                RequestOptions().override(
                                    view.layoutParams.width, view.layoutParams.height
                                )
                            ).placeholder(R.drawable.image_gray_color).into(view)
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
                            .load(newImageURL).apply(
                                RequestOptions().override(
                                    view.layoutParams.width, view.layoutParams.height
                                )
                            ).placeholder(R.drawable.image_gray_color).into(view)
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
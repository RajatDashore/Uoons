package com.uoons.india.utils


import android.graphics.drawable.Drawable
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uoons.india.R
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
object DataBindingUtils {

    @JvmStatic
    @BindingAdapter("imageURL")
    fun loadImageResource(view: ImageView, imageUrl: String? = "") {
        if(imageUrl != null){
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .apply(RequestOptions().override(view.layoutParams.width, view.layoutParams.height))
            .into(view)
            }else{
            view.setImageResource(R.mipmap.ic_launcher)
            }
    }
    @JvmStatic
    @BindingAdapter("checkNullText")
    fun checkNullText(view: EditText, str: String? = "") {
        view.setText( CommonUtils.setStringNullOrBlank(str))
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        view.setVisibility(if (value) View.VISIBLE else View.GONE)
    }
  /*
    @BindingAdapter("android:rating")
    fun setRating(view: RatingBar?, rating: String) {
        if (view != null) {
            val rate = rating.toFloat()
            view.rating = rate
        }
    }*/


    @JvmStatic
    @BindingAdapter("imageDrawable")
    fun loadImageDrawable(view: ImageView, image: Drawable) {
        if(image != null){
            view.setImageDrawable(image)
        }else{
            view.setImageResource(R.mipmap.ic_launcher)
        }
    }

    internal fun loadImage(view: ImageView?, imageURL: String?, placeError:Int) {
        try {
            if (imageURL != null) {
                Glide.with(view!!.context)
                    .setDefaultRequestOptions(RequestOptions().circleCrop())
                    .load(imageURL)
                    .apply(RequestOptions().override(view.layoutParams.width, view.layoutParams.height))
                    .placeholder(placeError).into(view)
            }else{
                view!!.setImageResource(placeError)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.PhotoesBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FourPhotoesAdapter :
    BaseRecyclerAdapter<PhotoesBinding, Any, FourPhotoesAdapter.MyViewHolder>() {
    private var customProductIdClickListener: OnProductIdClickListener? = null
    private var bestSellerItemList: ArrayList<DeshBoardItems>? = null
    private lateinit var photoes: ArrayList<Int>
    lateinit var context: Context

    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    fun setAllCategoryListFourPhotos(
        items: ArrayList<Int>,
        catogory: ArrayList<DeshBoardItems>,
        context: Context,
    ) {
        this.bestSellerItemList = catogory
        this.photoes = items
        this.context = context
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, type: Int) {
        holder.bind(bestSellerItemList!![position])
        holder.binding.ivItemsImagePhotoes.setImageResource(photoes[position])

        holder.binding.ivItemsImagePhotoes.setOnClickListener(View.OnClickListener {
            customProductIdClickListener?.onProductIdClicked(bestSellerItemList!![position].pid.toString())
        })
    }


    override fun onCreateViewHolder(
        viewDataBinding: PhotoesBinding,
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.photoes, parent, false)
        return MyViewHolder(PhotoesBinding.bind(view))
    }


    override fun getLayoutId(viewType: Int): Int {
        return R.layout.photoes
    }

    override fun getItemCount(): Int {
        return bestSellerItemList!!.size
    }

    class MyViewHolder(val binding: PhotoesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DeshBoardItems) {
            binding.homeBestSellerPhotoes = data
            binding.executePendingBindings()
        }
    }

    /*  companion object {
           @JvmStatic
           @BindingAdapter("loadImageDealOfThe")
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

     */


}
package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.uoons.india.BuildConfig
import com.uoons.india.R
import com.uoons.india.databinding.PhotoesBinding
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FourPhotoesAdapter : RecyclerView.Adapter<FourPhotoesAdapter.ViewHolder>() {
    private var customProductIdClickListener: OnProductIdClickListener? = null
    private var bestSellerItemList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context
    private lateinit var imagelist: ArrayList<Int>

    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    fun setDataPhotes(data: ArrayList<DeshBoardItems>, imagelist: ArrayList<Int>, context: Context) {
        this.bestSellerItemList = data
        this.imagelist = imagelist
        this.context = context
    }

    /*  override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
          holder.bind(bestSellerItemList!![position])

          holder.binding.ivItemsImage.setOnClickListener(View.OnClickListener {
              customProductIdClickListener?.onProductIdClicked(bestSellerItemList!![position].pid.toString())
          })
      }

     */

    /*  override fun onCreateViewHolder(
          viewDataBinding: RowHomeDealOfTheDayBinding,
          parent: ViewGroup,
          viewType: Int,
      ): ViewHolder {
          return ViewHolder(
              RowHomeDealOfTheDayBinding.inflate(
                  LayoutInflater.from(parent.context),
                  parent,
                  false
              )
          )
      }

     */

    /*  override fun getLayoutId(viewType: Int): Int {
          return R.layout.row_home_deal_of_the_day
      }

     */


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bestSellerItemList!![position])
        holder.binding.ivItemsImagePhotoes.setImageResource(imagelist[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = PhotoesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bestSellerItemList!!.size
    }

    class ViewHolder(val binding: PhotoesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DeshBoardItems) {
            binding.homeBestSeller = data
            binding.executePendingBindings()
        }
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


}
package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uoons.india.R
import com.uoons.india.databinding.PhotoesBinding
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FourPhotoesAdapter : RecyclerView.Adapter<FourPhotoesAdapter.MyViewHolder>() {
    private lateinit var photoes: ArrayList<Int>
    private var customClickListener: OnItemClickListener? = null
    private lateinit var context: Context
    private var categoryItemList: ArrayList<DeshBoardItems>? = null
    private lateinit var info: ArrayList<DeshBoardItems>


    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    interface OnItemClickListener {
        fun onItemClicked(position: String, type: String)
    }

    fun setAllCategoryListFourPhotos(
        photoes: ArrayList<Int>,
        catogory: ArrayList<DeshBoardItems>,
        context: Context,
    ) {
        this.photoes = photoes
        this.context = context
        this.info = catogory
    }

    /*  fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
          //  this.customProductIdClickListener = mItemClick
      }
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Log.d("getViewId", viewType.toString())
        return MyViewHolder(
            PhotoesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return photoes!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("RajatTag", photoes[position].toString())
        notifyDataSetChanged()

        if (position < photoes.size) {
            Picasso.get().load(photoes[position]).placeholder(R.drawable.ic_error)
                .into(holder.image)
        } else {

        }
    }


    inner class MyViewHolder(val binding: PhotoesBinding) : RecyclerView.ViewHolder(binding.root) {
        var image: ImageView = binding.ShowFourImages

    }
}
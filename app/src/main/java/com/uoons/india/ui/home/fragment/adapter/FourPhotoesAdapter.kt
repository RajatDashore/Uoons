package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uoons.india.R
//import com.uoons.india.ui.home.fragment.adapter.DealOfTheDayItemRecyclerAdapter.OnProductIdClickListener
import com.uoons.india.ui.home.fragment.model.DeshBoardItems

class FourPhotoesAdapter : RecyclerView.Adapter<FourPhotoesAdapter.MyViewHolder>() {
    private lateinit var data: ArrayList<Int>
    private lateinit var context: Context
    private lateinit var info: ArrayList<DeshBoardItems>

    fun SetAllCategoryListFourPhotoes(
        data: ArrayList<Int>,
        info: ArrayList<DeshBoardItems>,
        context: Context,
    ) {
        this.data = data
        this.info = info
        this.context = context
    }

    /*  fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
          //  this.customProductIdClickListener = mItemClick
      }
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Log.d("getViewId", viewType.toString())
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.photoes, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("RajatTag", data[position].toString())
        Picasso.get().load(data!![position]).placeholder(R.drawable.ic_error).into(holder.image)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.ShowFourImages)
    }
}
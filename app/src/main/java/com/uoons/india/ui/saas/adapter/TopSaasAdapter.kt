package com.uoons.india.ui.saas.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uoons.india.R
import com.uoons.india.ui.saas.adapter.model.CatImagesItem


class TopSaasAdapter : RecyclerView.Adapter<TopSaasAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var dataList: List<CatImagesItem>


    fun setData(dataList: List<CatImagesItem>, context: Context) {
        this.dataList = dataList
        this.context = context
    }

    fun setOnItemClickListener() {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.row_saas_upper_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val px: CatImagesItem = dataList[position]
        Glide.with(context).load(px.url).into(holder.image)
        Log.d("result", "onBindViewHolder: " + px.url)
    //    val anim:Animation = AnimationUtils

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Clicked $position", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imgSaasUpperRec)
    }

}

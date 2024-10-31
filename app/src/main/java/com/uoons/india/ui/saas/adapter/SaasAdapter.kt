package com.uoons.india.ui.saas.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uoons.india.R

class SaasAdapter(var context: Context, private var imgList: ArrayList<Int>) :
    RecyclerView.Adapter<SaasAdapter.MyViewHolder>(), Adapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.fragment_four_photoes, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val img: Int = imgList.get(position)
        Picasso.get().load(img).placeholder(R.drawable.ic_error).into(holder.imageView)
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /* var image1 = itemView.findViewById<ImageView>(R.id.first)
         var image2 = itemView.findViewById<ImageView>(R.id.second)
         var image3 = itemView.findViewById<ImageView>(R.id.third)
         var image4 = itemView.findViewById<ImageView>(R.id.fourth)
         */
        var imageView = itemView.findViewById<ImageView>(R.id.imageView)
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }
}
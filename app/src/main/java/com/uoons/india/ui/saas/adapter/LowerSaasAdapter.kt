package com.uoons.india.ui.saas.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uoons.india.R
import com.uoons.india.ui.saas.adapter.model.CatImagesItem

class LowerSaasAdapter : RecyclerView.Adapter<LowerSaasAdapter.LowerSaasViewHolder>() {

    private lateinit var context: Context
    private var allItemList: ArrayList<CatImagesItem> = ArrayList()


    @SuppressLint("NotifyDataSetChanged")
    fun setData(context: Context, allItemLists: ArrayList<CatImagesItem>) {
        this.context = context
        this.allItemList = allItemLists
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LowerSaasViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_saas_lower_layout, parent, false)
        return LowerSaasViewHolder(view)
    }

    override fun getItemCount(): Int {
        //  Log.d("result", "getItemCount: ${allItemList.size}")
        return allItemList.size
    }

    override fun onBindViewHolder(holder: LowerSaasViewHolder, position: Int) {
        val item: CatImagesItem = allItemList[position]
        //Log.d("result", "$position  ${item.url.toString()}")
        try {
            Glide.with(context).load(item.url).into(holder.image)
            holder.upperText.text = item.id
            holder.lowerText.text = item.url
            holder.off.text = item.width.toString()
        } catch (e: Exception) {
            Log.d("result", "Exception: $e")
        }
    }

    class LowerSaasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.rowSaasImage)
        val upperText = itemView.findViewById<TextView>(R.id.tvSaasUpperText)
        val lowerText = itemView.findViewById<TextView>(R.id.tvSaasLowerText)
        val off = itemView.findViewById<TextView>(R.id.tvOff)

    }
}
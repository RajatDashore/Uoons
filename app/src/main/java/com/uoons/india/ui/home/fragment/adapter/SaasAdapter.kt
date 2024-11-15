package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uoons.india.R
import com.uoons.india.ui.home.SaasActivity

class SaasAdapter : RecyclerView.Adapter<SaasAdapter.ViewHolder>() {
    private lateinit var context: Context

    fun setData(context: Context) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.slider_saas, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            Glide.with(context).load(R.drawable.saasnew).into(holder.image)
        }

        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Item Clicked $position", Toast.LENGTH_SHORT).show()
            holder.itemView.context.startActivity(Intent(context, SaasActivity::class.java))

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.SaasImage)!!
    }
}
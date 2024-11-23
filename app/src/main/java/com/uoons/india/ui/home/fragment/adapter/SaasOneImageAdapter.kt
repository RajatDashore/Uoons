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
import com.uoons.india.ui.saas.adapter.MainActivitySaas


class SaasOneImageAdapter : RecyclerView.Adapter<SaasOneImageAdapter.ViewHolder>() {
    private lateinit var context: Context


    fun setData(context: Context) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.row_home_saas_image, parent, false)
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
            try {
                Toast.makeText(context, "Item Clicked $position", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivitySaas::class.java)
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Exception $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.SaasImage)!!
    }
}
package com.uoons.india.ui.home.fragment.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uoons.india.R

class SaasOneImageAdapter : RecyclerView.Adapter<SaasOneImageAdapter.ViewHolder>() {

    lateinit var context: Context
    lateinit var contextHome: Context
    lateinit var navController: NavController

    fun setData(context: Context) {
        this.context = context
    }

    /*  fun setContextfromHomeFragment(contextHome: Context) {
          this.contextHome = contextHome
      }
     */


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
        try {

        } catch (e: Exception) {

        }

        holder.itemView.setOnClickListener {
            try {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, SaasActivity::class.java)
                context.startActivity(intent)
            } catch (e: Exception) {
                  Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.SaasImage)!!
    }
}
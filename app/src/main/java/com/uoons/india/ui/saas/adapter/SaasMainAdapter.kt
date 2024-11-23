package com.uoons.india.ui.saas.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.ui.saas.adapter.model.CatImagesItem


class SaasMainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var allItemList: ArrayList<CatImagesItem> = ArrayList()
    private var lowerData: ArrayList<CatImagesItem> = ArrayList()
    private lateinit var context: Context
    private val topSaasAdapter = TopSaasAdapter()
    private val lowerSaasAdapter = LowerSaasAdapter()

    companion object {
        const val SAAS_UPPER_RECYCLER = 0
        const val LOWER_RECYCLER = 1
        const val EMPTY_VIEW = 2
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUpperData(context: Context, allItemLists: ArrayList<CatImagesItem>) {
        this.context = context
        this.allItemList = allItemLists
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLowerData(lowerData: ArrayList<CatImagesItem>) {
        this.lowerData = lowerData
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            allItemList.isNotEmpty() && position == 0 -> SAAS_UPPER_RECYCLER
            lowerData.isNotEmpty() && position == 1 -> LOWER_RECYCLER
            else -> EMPTY_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SAAS_UPPER_RECYCLER -> {
                val layout = inflater.inflate(R.layout.row_saas_upper_rec, parent, false)
                LayoutUpperRecyclerViewHolder(layout)
            }

            LOWER_RECYCLER -> {
                val layout = inflater.inflate(R.layout.row_saas_lower_rec, parent, false)
                LayoutLowerRecyclerViewHolder(layout)
            }
            else -> {
                val view = inflater.inflate(R.layout.empty_view, parent, false)
                EmptyViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LayoutUpperRecyclerViewHolder -> holder.bind()
            is LayoutLowerRecyclerViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return when {
            allItemList.isNotEmpty() && lowerData.isNotEmpty() -> 2
            allItemList.isNotEmpty() || lowerData.isNotEmpty() -> 1
            else -> 1 // For the empty view
        }
    }

    inner class LayoutUpperRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.saasUpperRecycler)

        fun bind() {
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = topSaasAdapter
            topSaasAdapter.setData(allItemList, context)
        }
    }

    inner class LayoutLowerRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.SaasLowerRecyclerView)

        fun bind() {
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            lowerSaasAdapter.setData(context, lowerData)
            Log.d("result", "Hello" + lowerData[position].url)
            recyclerView.adapter = lowerSaasAdapter
        }
    }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

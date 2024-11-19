package com.uoons.india.ui.saas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.ui.saas.adapter.model.PixabayResponse

class SaasMainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var allItemList: ArrayList<PixabayResponse> = ArrayList()
    private lateinit var context: Context
    private val topSaasAdapter = TopSaasAdapter()

    fun setData(context: Context, allItemLists: ArrayList<PixabayResponse>) {
        this.allItemList = allItemLists
        this.context = context
        notifyDataSetChanged()
    }

    companion object {
        const val SAAS_UPPER_RECYCLER = 0
        const val EMPTY_VIEW = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (allItemList.isNotEmpty()) SAAS_UPPER_RECYCLER else EMPTY_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            SAAS_UPPER_RECYCLER -> {
                val view = inflater.inflate(R.layout.saas_upper_rec_layout, parent, false)
                LayoutUpperRecyclerViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.empty_view, parent, false)
                EmptyViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LayoutUpperRecyclerViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int {
        return if (allItemList.isEmpty()) 1 else allItemList.size
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


    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

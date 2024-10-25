package com.uoons.india.ui.searching.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowSearchProductHistoryBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SearchItemHistoryListAdapter(var searchList: ArrayList<String>, var requireContext: Context, var onclick:(value: String)->Unit) : BaseRecyclerAdapter<RowSearchProductHistoryBinding, Any, SearchItemHistoryListAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.binding.cstLayout.setOnClickListener(View.OnClickListener { view ->
            onclick.invoke(searchList[position])
        })
        holder.binding.txvSearchProductKey.text = searchList[position]
    }

    override fun onCreateViewHolder(viewDataBinding: RowSearchProductHistoryBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowSearchProductHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_search_product_history
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class ViewHolder(val binding: RowSearchProductHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
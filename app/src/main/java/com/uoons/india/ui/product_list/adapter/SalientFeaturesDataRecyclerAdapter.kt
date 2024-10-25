package com.uoons.india.ui.product_list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowProductSalientFeaturesBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SalientFeaturesDataRecyclerAdapter : BaseRecyclerAdapter<RowProductSalientFeaturesBinding, Any, SalientFeaturesDataRecyclerAdapter.ViewHolder>(){

    var salientFeaturesDataList: ArrayList<String> = ArrayList<String>()
    lateinit var context: Context

    fun setSalientFeaturesData(data: ArrayList<String>, context: Context){
        this.salientFeaturesDataList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.binding.txvSaliantFeatures.text = salientFeaturesDataList[position]
    }

    override fun onCreateViewHolder(viewDataBinding: RowProductSalientFeaturesBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowProductSalientFeaturesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_salient_features
    }

    override fun getItemCount(): Int {
        return salientFeaturesDataList.size
    }

    class ViewHolder(val binding: RowProductSalientFeaturesBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
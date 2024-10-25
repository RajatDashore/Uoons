package com.uoons.india.ui.product_detail.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowProductDetailsSalientFeaturesBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductDetailsSalientFeaturesAdapter(var salientFeatures: ArrayList<String>, var context: Context) :
    BaseRecyclerAdapter<RowProductDetailsSalientFeaturesBinding, Any, ProductDetailsSalientFeaturesAdapter.ViewHolder>(){


    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        if (salientFeatures[position].isEmpty()){
            Log.d("","Silent Feature is Empty!")
        } else {
            holder.binding.txvSaliantFeatures.text = salientFeatures[position]
        }

    }

    override fun onCreateViewHolder(viewDataBinding: RowProductDetailsSalientFeaturesBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowProductDetailsSalientFeaturesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_details_salient_features
    }

    override fun getItemCount(): Int {
        return salientFeatures.size
    }

    class ViewHolder(val binding: RowProductDetailsSalientFeaturesBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
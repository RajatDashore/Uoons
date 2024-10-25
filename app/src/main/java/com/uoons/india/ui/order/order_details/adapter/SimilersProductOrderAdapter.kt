package com.uoons.india.ui.order.order_details.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowSimilersProductOrderBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.order.order_details.model.SimilersProductOrderModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SimilersProductOrderAdapter(var similerOrderList: ArrayList<SimilersProductOrderModel>, val requireContext: Context,var onclick:(value : String)->Unit) :
    BaseRecyclerAdapter<RowSimilersProductOrderBinding, Any, SimilersProductOrderAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewDataBinding: RowSimilersProductOrderBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowSimilersProductOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(similerOrderList[position])

        if (similerOrderList[position].productImages.isNullOrEmpty()){
            holder.binding.ivProductImage.setImageResource(R.drawable.image_gray_color)
        }else {
            CommonUtils.loadImage(holder.binding.ivProductImage, similerOrderList[position].productImages, holder.binding.ivProductImage.id)
        }

        holder.binding.cstLayout.setOnClickListener(View.OnClickListener {
            onclick.invoke(similerOrderList[position].pid.toString())
        })
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_similers_product_order
    }

    override fun getItemCount(): Int {
        return similerOrderList.size
    }

    class ViewHolder(val binding: RowSimilersProductOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SimilersProductOrderModel) {
            binding.similersProductOrderModel = data
        }
    }
}
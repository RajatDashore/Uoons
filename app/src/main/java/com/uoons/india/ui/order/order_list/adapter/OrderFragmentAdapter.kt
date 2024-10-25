package com.uoons.india.ui.order.order_list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowOrderItemsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.order.order_list.model.OrderListModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Obfuscate
class OrderFragmentAdapter(val ordersList: ArrayList<OrderListModel>,val requireContext: Context,var onclick:(value : String)->Unit) : BaseRecyclerAdapter<RowOrderItemsBinding, Any, OrderFragmentAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewDataBinding: RowOrderItemsBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowOrderItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(ordersList[position])

        val DATE_FORMAT_I = "yyyy-MM-dd HH:mm:ss"
        val DATE_FORMAT_O = "dd-MMM-yyyy hh:mm a"

        val formatInput = SimpleDateFormat(DATE_FORMAT_I)
        val formatOutput = SimpleDateFormat(DATE_FORMAT_O)

        val date: Date = formatInput.parse(ordersList[position].bundleCreated)
        val dateString: String = formatOutput.format(date)
        holder.binding.txvBundleDate.text = dateString

        val amount = ordersList[position].amount?.toDouble()?.roundToInt()

        holder.binding.txvTotalAmmount.text = requireContext.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(amount)

        holder.binding.crdMyOrders.setOnClickListener(View.OnClickListener { view ->
            onclick.invoke(ordersList[position].id.toString())
        })

        if (ordersList[position].productImages.isNullOrEmpty()){
            holder.binding.ivProductImage.setImageResource(R.drawable.image_gray_color)
        }else {
            CommonUtils.loadImage(holder.binding.ivProductImage, ordersList[position].productImages, holder.binding.ivProductImage.id)
        }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_order_items
    }

    override fun getItemCount(): Int {
        return ordersList.size
    }

    class ViewHolder(val binding: RowOrderItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OrderListModel) {
            binding.orderListModel = data
        }
    }

}
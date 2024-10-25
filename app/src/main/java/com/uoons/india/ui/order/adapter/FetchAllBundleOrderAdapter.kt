package com.uoons.india.ui.order.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowOrderBundleItemsBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.order.model.FecthAllBundleOrderListModel
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

@Obfuscate
class FetchAllBundleOrderAdapter(private val fecthAllBundleOrderList: ArrayList<FecthAllBundleOrderListModel>, val requireContext: Context, var onclick:(value : String)->Unit) : BaseRecyclerAdapter<RowOrderBundleItemsBinding, Any, FetchAllBundleOrderAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewDataBinding: RowOrderBundleItemsBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowOrderBundleItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(fecthAllBundleOrderList[position])

        if (fecthAllBundleOrderList[position].orders?.productImages.isNullOrEmpty()){
            holder.binding.ivProductImage.setImageResource(R.drawable.image_gray_color)
        }else {
            CommonUtils.loadImage(holder.binding.ivProductImage, fecthAllBundleOrderList[position].orders?.productImages, holder.binding.ivProductImage.id)
        }
        val DATE_FORMAT_I = "yyyy-MM-dd HH:mm:ss"
        val DATE_FORMAT_O = "dd-MMM-yyyy hh:mm a"

        val formatInput = SimpleDateFormat(DATE_FORMAT_I)
        val formatOutput = SimpleDateFormat(DATE_FORMAT_O)

        val date: Date = formatInput.parse(fecthAllBundleOrderList[position].bundleCreated)
        val dateString: String = formatOutput.format(date)
        holder.binding.txvBundleDate.text = dateString

        val amount = fecthAllBundleOrderList[position].orders?.amount?.toDouble()?.roundToInt()
        holder.binding.txvTotalAmmount.text = requireContext.getString(R.string.rupees)+NumberFormat.getNumberInstance(Locale.getDefault()).format(amount)

        holder.binding.crdBundleOrders.setOnClickListener(View.OnClickListener {
            onclick.invoke(fecthAllBundleOrderList[position].bundleId.toString())
        })
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_order_bundle_items
    }

    override fun getItemCount(): Int {
        return fecthAllBundleOrderList.size
    }

    class ViewHolder(val binding: RowOrderBundleItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FecthAllBundleOrderListModel) {
            binding.fecthAllBundleOrderListModel = data
        }
    }

}
package com.uoons.india.ui.home.fragment.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowHomeTrendingNowBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.home.fragment.model.DeshBoardItems
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class TrendingNowItemRecyclerAdapter : BaseRecyclerAdapter<RowHomeTrendingNowBinding, Any, TrendingNowItemRecyclerAdapter.ViewHolder>(){

    private var customProductIdClickListener: OnProductIdClickListener? = null
    var trendingItemsList: ArrayList<DeshBoardItems>? = null
    lateinit var context: Context

    interface OnProductIdClickListener {
        fun onProductIdClicked(pId: String)
    }

    fun setOnItemClickListener(mItemClick: OnProductIdClickListener) {
        this.customProductIdClickListener = mItemClick
    }

    fun setData(data:  ArrayList<DeshBoardItems>, context: Context){
        this.trendingItemsList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {

        holder.bind(trendingItemsList!![position])
        if (trendingItemsList!![position].productImages!!.isEmpty()){
            CommonUtils.loadImage(holder.binding.ivItemsImage, "", holder.binding.ivItemsImage.id)
        }else{
            CommonUtils.loadImage(holder.binding.ivItemsImage, trendingItemsList!![position].productImages, holder.binding.ivItemsImage.id)
        }


        holder.binding.ivItemsImage.setOnClickListener(View.OnClickListener {
            customProductIdClickListener?.onProductIdClicked(trendingItemsList!![position].pid.toString())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowHomeTrendingNowBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowHomeTrendingNowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_home_trending_now
    }

    override fun getItemCount(): Int {
        return trendingItemsList!!.size
    }

    class ViewHolder(val binding: RowHomeTrendingNowBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: DeshBoardItems){
            binding.trendingItems = data
        }
    }
}
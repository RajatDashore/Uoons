package com.uoons.india.ui.notification_settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowAllNotificationBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.filter.model.FilterItemsDataModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class NotificationsAdapter :
    BaseRecyclerAdapter<RowAllNotificationBinding, Any, NotificationsAdapter.ViewHolder>() {

    var notificationsList: ArrayList<FilterItemsDataModel> = ArrayList<FilterItemsDataModel>()
    lateinit var context: Context

    private var customClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }


    fun setNotificationsList(data: ArrayList<FilterItemsDataModel>, context: Context) {
        this.notificationsList = data
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(notificationsList[position])

        holder.binding.chkNotifications.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                customClickListener?.onItemClicked(position)
            }else{
                customClickListener?.onItemClicked(-1)
            }
        }
    }

    override fun onCreateViewHolder(viewDataBinding: RowAllNotificationBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowAllNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_all_notification
    }

    override fun getItemCount(): Int {
        return notificationsList.size
    }

    class ViewHolder(val binding: RowAllNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FilterItemsDataModel) {
            binding.filtersCategoryItems = data
            binding.executePendingBindings()
        }
    }
}
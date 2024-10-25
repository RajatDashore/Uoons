package com.uoons.india.ui.checkout.checkout_address.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.RowAllDeliverAddressBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.checkout.checkout_address.model.DeliverAllAddressModel
import com.uoons.india.ui.checkout.checkout_address.model.GetAllDeliverAddressModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class AllDeliverAddressAdapter(var onclick:(obj:Any)->Unit,var onclick1:(obj:Any)->Unit ) : BaseRecyclerAdapter<RowAllDeliverAddressBinding, Any, AllDeliverAddressAdapter.ViewHolder>(){

    private var myAllAddressList: DeliverAllAddressModel = DeliverAllAddressModel()
    lateinit var context: Context
    var lastSelectedPosition = -1

    private var customClickListener: OnItemClickListener? = null

     interface OnItemClickListener {
        fun onItemClicked(addresId: Int)
    }

    fun setOnItemClickListener(mItemClick: OnItemClickListener) {
        this.customClickListener = mItemClick
    }

    fun setAllDeliverAddressList(data: DeliverAllAddressModel, context: Context) {
        this.myAllAddressList = data
        this.context = context
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(myAllAddressList.Data[position])

        holder.binding.rdoBtnUserName.setOnCheckedChangeListener(null)
        holder.binding.rdoBtnUserName.isChecked = position == lastSelectedPosition
        if (position == lastSelectedPosition){
            holder.binding.crdDeliverToAddress.visibility = View.VISIBLE
            holder.binding.txvDeleteAddress.visibility = View.VISIBLE
            holder.binding.txvUpdateAddress.visibility = View.VISIBLE
        }else{
            holder.binding.crdDeliverToAddress.visibility = View.GONE
            holder.binding.txvDeleteAddress.visibility = View.GONE
            holder.binding.txvUpdateAddress.visibility = View.GONE
        }

        holder.binding.txvDeleteAddress.setOnClickListener(View.OnClickListener { view ->
            onclick.invoke(myAllAddressList.Data[position] )
        })

        holder.binding.txvUpdateAddress.setOnClickListener(View.OnClickListener { view ->
            onclick1.invoke(myAllAddressList.Data[position]   )
        })

        holder.binding.rdoBtnUserName.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            lastSelectedPosition = position
            holder.binding.crdDeliverToAddress.visibility = View.VISIBLE
            holder.binding.txvDeleteAddress.visibility = View.VISIBLE
            notifyDataSetChanged()
        })

        holder.binding.crdDeliverToAddress.setOnClickListener(View.OnClickListener { view ->
            AppPreference.addValue(PreferenceKeys.USER_NAME_ORDER, myAllAddressList.Data[position].bname.toString())
            AppPreference.addValue(PreferenceKeys.USER_EMAIL_ORDER, myAllAddressList.Data[position].bemail.toString())
            AppPreference.addValue(PreferenceKeys.ADDRESS_ID,myAllAddressList.Data[position].id.toString())
            customClickListener?.onItemClicked(myAllAddressList.Data[position].id!!.toInt())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowAllDeliverAddressBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowAllDeliverAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_all_deliver_address
    }

    override fun getItemCount(): Int {
        return myAllAddressList.Data.size
    }

    class ViewHolder(val binding: RowAllDeliverAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GetAllDeliverAddressModel) {
            binding.getAllDeliverAddressModel = data
            // binding.executePendingBindings()
        }
    }
}
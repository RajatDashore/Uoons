package com.uoons.india.ui.bank.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowBankDetailsListBinding
import com.uoons.india.ui.bank.model.BankDetailsList
import com.uoons.india.ui.base.BaseRecyclerAdapter
import org.lsposed.lsparanoid.Obfuscate
import kotlin.collections.ArrayList

@Obfuscate
class BankDetailsListAdapter(var fetchBankDetailsList: ArrayList<BankDetailsList>, var context: Context, var onclick:(value : String)->Unit,
                             var onclick1:(value : String)->Unit) :
    BaseRecyclerAdapter<RowBankDetailsListBinding, Any, BankDetailsListAdapter.ViewHolder>(){
    var lastSelectedPosition = -1

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(fetchBankDetailsList[position])
        holder.binding.txvSavingAcNo.text = fetchBankDetailsList[position].accountNumber
        holder.binding.rdoBtnBankName.setOnCheckedChangeListener(null)
        holder.binding.rdoBtnBankName.isChecked = position == lastSelectedPosition

        if (fetchBankDetailsList[position].bSelected == "1"){
            holder.binding.rdoBtnBankName.isChecked = true
        }

        holder.binding.rdoBtnBankName.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            lastSelectedPosition = position
            onclick.invoke(fetchBankDetailsList[position].bId.toString())
            notifyDataSetChanged()
        })

        holder.binding.txvDeleteBankDetails.setOnClickListener(View.OnClickListener {
            onclick1.invoke(fetchBankDetailsList[position].bId.toString())
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowBankDetailsListBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowBankDetailsListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_bank_details_list
    }

    override fun getItemCount(): Int {
        return fetchBankDetailsList.size
    }

    class ViewHolder(val binding: RowBankDetailsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BankDetailsList) {
            binding.bankDetailsList = data
        }
    }
}
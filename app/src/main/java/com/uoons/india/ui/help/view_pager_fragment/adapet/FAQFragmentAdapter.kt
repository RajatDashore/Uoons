package com.uoons.india.ui.help.view_pager_fragment.adapet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.FAQLayoutRowBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.help.view_pager_fragment.model.FAQDataModel
import com.uoons.india.ui.help.view_pager_fragment.model.FAQListDataModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FAQFragmentAdapter(var fAQList: FAQDataModel,var context: Context) :
    BaseRecyclerAdapter<FAQLayoutRowBinding, Any, FAQFragmentAdapter.ViewHolder>(){

   // lateinit var context: Context
    private var clickQuestion : Boolean = true
   // var fAQList: FAQDataModel = FAQDataModel()

    /*fun setFAQList(data: FAQDataModel, context: Context) {
        this.fAQList = data
        this.context = context
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.bind(fAQList.Data[position])

        holder.binding.txvFAQQuation.setOnClickListener(View.OnClickListener { view ->
            if (clickQuestion){
                holder.binding.txvFAQAnswer.visibility = View.VISIBLE
                holder.binding.ivUpArrow.visibility = View.VISIBLE
                holder.binding.ivDownArrow.visibility = View.GONE
                clickQuestion = false
            }else{
                holder.binding.txvFAQAnswer.visibility = View.GONE
                holder.binding.ivDownArrow.visibility = View.VISIBLE
                holder.binding.ivUpArrow.visibility = View.GONE
                clickQuestion = true
            }
        })
    }

    override fun onCreateViewHolder(viewDataBinding: FAQLayoutRowBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FAQLayoutRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getLayoutId(viewType: Int): Int {
        return R.layout.f_a_q_layout_row
    }

    override fun getItemCount(): Int {
        return fAQList.Data.size
    }

    class ViewHolder(val binding: FAQLayoutRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FAQListDataModel) {
            binding.faqListDataModel = data
            binding.executePendingBindings()
        }
    }
}
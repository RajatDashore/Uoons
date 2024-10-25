package com.uoons.india.ui.product_detail.suggestion_for_enhance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowSuggestionForEnhanceAnswersBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_detail.suggestion_for_enhance.model.QuestionModel
import android.widget.RadioButton

import com.uoons.india.ui.product_detail.suggestion_for_enhance.model.PostSuggestionsQuestions
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SuggestionForEnhanceAnswersAdapter(val options: QuestionModel, val requireContext: Context,var onclick:(values: PostSuggestionsQuestions)->Unit) : BaseRecyclerAdapter<RowSuggestionForEnhanceAnswersBinding, Any, SuggestionForEnhanceAnswersAdapter.ViewHolder>(){

    private var lastCheckedRB: RadioButton? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {

        holder.binding.rdoButton.text = options.options[position].option

      /*  holder.binding.rdoButton.setOnClickListener(View.OnClickListener {
            //holder.binding.rdoButton.isChecked=false

            lastSelectedPosition = position

            selectedPosition = true

            if (! holder.binding.rdoButton.isSelected) {
                holder.binding.rdoButton.isChecked = true
                holder.binding.rdoButton.isSelected = true
            } else {
                holder.binding.rdoButton.isChecked = false
                holder.binding.rdoButton.isSelected = false
            }

            Log.e("SuggestionQuestionsOptionsAdapter","answerOptionsList:- "+options.options[position].option)
         //   answerOptionsList[position].option?.let { it1 -> customClickListener?.onClicked(it1) }
        })*/


        holder.binding.rdoButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            lastCheckedRB?.isChecked = false
            lastCheckedRB =  holder.binding.rdoButton
            if (lastCheckedRB!!.isChecked){
                //   onclick.invoke(options.options[position].option.toString())
                onclick.invoke(PostSuggestionsQuestions(options.question,options.options[position].option.toString()))
            }
        })



    }

    override fun onCreateViewHolder(viewDataBinding: RowSuggestionForEnhanceAnswersBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowSuggestionForEnhanceAnswersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_suggestion_for_enhance_answers
    }

    override fun getItemCount(): Int {
        return options.options.size
    }

    class ViewHolder(val binding: RowSuggestionForEnhanceAnswersBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
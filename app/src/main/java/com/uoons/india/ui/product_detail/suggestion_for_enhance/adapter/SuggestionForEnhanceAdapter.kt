package com.uoons.india.ui.product_detail.suggestion_for_enhance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.ui.product_detail.suggestion_for_enhance.model.PostSuggestionsQuestions
import com.uoons.india.ui.product_detail.suggestion_for_enhance.model.QuestionModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SuggestionForEnhanceAdapter(val questionList: ArrayList<QuestionModel>, val requireContext: Context, var onclick:(values: PostSuggestionsQuestions)->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout: View = LayoutInflater.from(requireContext).inflate(R.layout.row_suggestion_for_enhance_quesntions,parent,false)
        return LayoutSuggestionsAnswerViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)  {

        (holder as LayoutSuggestionsAnswerViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    private inner class LayoutSuggestionsAnswerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var rcvSuggestionForEnhanceAns: RecyclerView = itemView.findViewById(R.id.rcvSuggestionForEnhanceAns)
        var textFeedBackQuestion: TextView = itemView.findViewById(R.id.textFeedBackQuestion)

        fun bind(position: Int) {
            textFeedBackQuestion.text = questionList[position].question
            setSuggestionQuestionsOptionsItemRecycler(rcvSuggestionForEnhanceAns, questionList[position])
        }
    }

    private fun setSuggestionQuestionsOptionsItemRecycler(rcvSuggestionForEnhanceAns: RecyclerView, options: QuestionModel) {
        val suggestionQuestionsOptionsAdapter = SuggestionForEnhanceAnswersAdapter(options, requireContext,onclick = {
            onclick.invoke(PostSuggestionsQuestions(it.question,it.answer))
        })
        rcvSuggestionForEnhanceAns.adapter = suggestionQuestionsOptionsAdapter
    }
}
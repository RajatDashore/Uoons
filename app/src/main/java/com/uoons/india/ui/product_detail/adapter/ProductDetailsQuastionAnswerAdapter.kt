package com.uoons.india.ui.product_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uoons.india.R
import com.uoons.india.databinding.RowProductDetailsQuestionAnswersBinding
import com.uoons.india.ui.base.BaseRecyclerAdapter
import com.uoons.india.ui.product_detail.model.QuestionanswerModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductDetailsQuastionAnswerAdapter(var questionanswer: ArrayList<QuestionanswerModel>, var context: Context,
                                          var onclickThumbUp:(value : String)->Unit,var onclickThumbDown:(value : String)->Unit) :
    BaseRecyclerAdapter<RowProductDetailsQuestionAnswersBinding, Any, ProductDetailsQuastionAnswerAdapter.ViewHolder>() {
    private var currentLike = 0
    private var currentUnLike = 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        holder.binding.txvQuestion.text = questionanswer[position].question
        holder.binding.txvAnswer.text = questionanswer[position].answer
        holder.binding.txvThumbUpCount.text = questionanswer[position].qaLike
        currentLike = questionanswer[position].qaLike!!.toInt()
        holder.binding.txvThumbDownCount.text = questionanswer[position].qaUnlike
        currentUnLike = questionanswer[position].qaUnlike!!.toInt()

        holder.binding.ivThumbUp.setOnClickListener(View.OnClickListener {
            onclickThumbUp.invoke(questionanswer[position].id.toString())

           /* if (questionanswer[position].qaLike!!.toInt() == 0){
                holder.binding.txvThumbUpCount.text = "0"
            }else{
                holder.binding.txvThumbUpCount.text = (questionanswer[position].qaLike!!.toInt() + 1).toString()
            }
*/

        })

        holder.binding.ivThumbDown.setOnClickListener(View.OnClickListener {
            onclickThumbDown.invoke(questionanswer[position].id.toString())

          /*  currentUnLike--
            if (questionanswer[position].qaUnlike!!.toInt() == 0){
                holder.binding.txvThumbDownCount.text = "0"
            }else{
                holder.binding.txvThumbDownCount.text = (questionanswer[position].qaUnlike!!.toInt() - 1).toString()
            }*/
        })
    }

    override fun onCreateViewHolder(viewDataBinding: RowProductDetailsQuestionAnswersBinding, parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowProductDetailsQuestionAnswersBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_product_details_question_answers
    }

    override fun getItemCount(): Int {
        return questionanswer.size
    }

    class ViewHolder(val binding: RowProductDetailsQuestionAnswersBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}
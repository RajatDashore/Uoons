package com.uoons.india.ui.question_answers.post_question

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.question_answers.post_question.model.PostYourQuestionModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class PostYourQuestionFragmentVM : BaseViewModel<PostYourQuestionFragmentNavigator>(){

    var getResponsePostYourQuestion : MutableLiveData<PostYourQuestionModel> = MutableLiveData()

    init {
        getResponsePostYourQuestion = MutableLiveData()
    }

    fun postYourQuestionApiCall(question: String,product_id : String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()
            val result = Repository.get.postYourQuestion(AppConstants.CHANNEL_MODE,getRequestPostQuestion(question,product_id))
            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getResponsePostYourQuestion.value=it
                        navigator?.postYourQuestionResponse()
                    }
                }
            )
        }

    }

    private fun getRequestPostQuestion(question: String,product_id : String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["question"] = question
        requestParam["product_id"] = product_id
        return requestParam
    }
}
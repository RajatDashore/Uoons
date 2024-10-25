package com.uoons.india.ui.product_detail.quetion_and_answer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.product_detail.model.QuestionLikeUnlikeModel
import com.uoons.india.ui.product_detail.quetion_and_answer.model.QuestionAnswerModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductDetailsQuestionAndAnswerFragmentVM : BaseViewModel<ProductDetailsQuestionAndAnswerFragmentNavigator>(){

    var getAllQuestionsAnswersResponse : MutableLiveData<QuestionAnswerModel> = MutableLiveData()

    // Like Unlike Questions
    var getLikeUnlikeQuestionsResponse : MutableLiveData<QuestionLikeUnlikeModel> = MutableLiveData()

    init {
        getAllQuestionsAnswersResponse = MutableLiveData()

        getLikeUnlikeQuestionsResponse = MutableLiveData()
    }

    fun getAllQuestionAnswersApiCall(pId: String){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.getAllQuestionsAnswers(AppConstants.CHANNEL_MODE,pId.toInt())
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getAllQuestionsAnswersResponse.value = it
                        navigator?.getAllQuestionAnswersData()
                    }
                }
            )
        }
    }

    fun likeUnlikeQuestionAPICall(questionId: String, action : String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.postlikeUnlikeQuestion(AppConstants.CHANNEL_MODE,getRequestLikeUnlike(questionId,action))
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                    Log.e("navigator", "handleAPIFailure: $it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getLikeUnlikeQuestionsResponse.value=it
                        navigator?.likeUnlikeQuestionResponse()
                    }
                }
            )
        }
    }

    private fun getRequestLikeUnlike(questionId: String, action : String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["question_id"] = questionId
        requestParam["action"] = action
        return requestParam
    }

}
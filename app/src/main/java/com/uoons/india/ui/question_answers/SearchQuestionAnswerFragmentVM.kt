package com.uoons.india.ui.question_answers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.home.fragment.SingleLiveEvent
import com.uoons.india.ui.product_detail.model.QuestionLikeUnlikeModel
import com.uoons.india.ui.product_detail.quetion_and_answer.model.QuestionAnswerModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SearchQuestionAnswerFragmentVM : BaseViewModel<SearchQuestionAnswerFragmentNavigator>() {

   // var getProductQuestionsResponse : MutableLiveData<QuestionAnswerModel> = MutableLiveData()

    private val _getProductQuestionsResponse = SingleLiveEvent<QuestionAnswerModel>()
    val getProductQuestionsResponse : SingleLiveEvent<QuestionAnswerModel>

        get() = _getProductQuestionsResponse

    // Like Unlike Questions
    var getLikeUnlikeQuestionsResponse : MutableLiveData<QuestionLikeUnlikeModel> = MutableLiveData()

    init {
        getLikeUnlikeQuestionsResponse = MutableLiveData()
    }


    fun productQuestionsListApiCall(searchKey: String,product_id : String) {
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.searchQuestionAnswers(AppConstants.CHANNEL_MODE,getRequestQuestionsList(searchKey,product_id))
         //   navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                Log.e("navigator", "handleAPIFailure: $it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getProductQuestionsResponse.value=it
                        navigator?.productQuestionListResponse()
                    }else{
                        getProductQuestionsResponse.value=it
                        navigator?.productQuestionListResponse()
                    }
                }
            )
        }

    }

    private fun getRequestQuestionsList(searchKey: String,product_id : String): HashMap<String, Any> {
        val requestParam : HashMap<String, Any> = HashMap()
        requestParam["searchKey"] = searchKey
        requestParam["product_id"] = product_id
        return requestParam
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
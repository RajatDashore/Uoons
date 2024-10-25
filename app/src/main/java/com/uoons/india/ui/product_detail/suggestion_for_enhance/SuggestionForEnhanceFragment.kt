package com.uoons.india.ui.product_detail.suggestion_for_enhance

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentSuggestionForEnhanceBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.product_detail.suggestion_for_enhance.adapter.SuggestionForEnhanceAdapter
import com.uoons.india.ui.product_detail.suggestion_for_enhance.model.*
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.json.JSONArray
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class SuggestionForEnhanceFragment : BaseFragment<FragmentSuggestionForEnhanceBinding, SuggestionForEnhanceFragmentVM>(),
    SuggestionForEnhanceFragmentNavigator {

    override val bindingVariable: Int = BR.suggestionForEnhanceFragmentVM
    override val layoutId: Int = R.layout.fragment_suggestion_for_enhance
    override val viewModelClass: Class<SuggestionForEnhanceFragmentVM> = SuggestionForEnhanceFragmentVM::class.java
    private var navController: NavController? = null
    private lateinit var suggestionForEnhanceAdapter : SuggestionForEnhanceAdapter
    private var questionList: ArrayList<QuestionModel> = ArrayList()
    private var pId : String = ""

    private var postSuggestionQuestionList : ArrayList<PostSuggestionsQuestions> = ArrayList()
    private var postYourSuggestionList : ArrayList<PostSuggestionModel> = ArrayList()

    override  fun init(){
        mViewModel.navigator = this
        pId = arguments?.getString(AppConstants.PId).toString()
        // Start shimmer animation
        viewDataBinding.shimmerSuggestionForEnhanceLayout.startShimmer()
        mViewModel.getSuggestionForEnhaceApiCall()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewDataBinding.rcvSuggestionForEnhance.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun getSuggestionToEnhanceData() {
        if(view !=null){
            mViewModel.getSuggestionEnhanceDataResponse.observe(viewLifecycleOwner, Observer<SuggestionToEnhanceModel> {
                if (it != null){
                    setSuggestionToEnhanceData(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }
    }

    override fun getSuggestionToEnhanceDataResponse() {
        navController?.navigate(R.id.action_suggestionForEnhanceFragment_to_homeFragment)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setSuggestionToEnhanceData(suggestionForEnhanceModel: SuggestionToEnhanceModel) {
        viewDataBinding.cstLayout.visibility = View.VISIBLE
        for (i in suggestionForEnhanceModel.data?.suggestions?.indices!!){
            viewDataBinding.textFeedBackQuestion.text = suggestionForEnhanceModel.data!!.suggestions[i].feedback
            questionList.addAll(suggestionForEnhanceModel.data!!.suggestions[i].question)
        }

        suggestionForEnhanceAdapter = SuggestionForEnhanceAdapter(questionList,requireContext(), onclick = {
            postSuggestionQuestionList.add(PostSuggestionsQuestions(it.question,it.answer))
        })

        viewDataBinding.rcvSuggestionForEnhance.adapter = suggestionForEnhanceAdapter
        suggestionForEnhanceAdapter.notifyDataSetChanged()

        viewDataBinding.txvSuggestionToEnhance.text = resources.getString(R.string.suggestion_to_enhance)
        viewDataBinding.txvFeedBackText.text = resources.getString(R.string.feedback)
        viewDataBinding.txvQuestionText.text = resources.getString(R.string.q)
        viewDataBinding.ivButtonClose.setImageResource(R.drawable.ic_close)
        viewDataBinding.ediEnterFeedback.visibility = View.VISIBLE
        viewDataBinding.sendFeedback.visibility = View.VISIBLE

        viewDataBinding.shimmerSuggestionForEnhanceLayout.stopShimmer()
        viewDataBinding.shimmerSuggestionForEnhanceLayout.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerSuggestionForEnhanceLayout.stopShimmer()
        viewDataBinding.shimmerSuggestionForEnhanceLayout.visibility = View.GONE
    }

    override fun postSuggestionToEnhance() {

        postYourSuggestionList.add(PostSuggestionModel(postSuggestionQuestionList,PostSuggestionFeedbackQuestion(viewDataBinding.textFeedBackQuestion.text.toString(),viewDataBinding.ediEnterFeedback.text.toString())))

        val gson = GsonBuilder().setPrettyPrinting().create()
        val postSuggestion = gson.toJson(postYourSuggestionList)
        val jsonArray = JSONArray(postSuggestion)
        val postSuggestionObject = jsonArray.getJSONObject(0)
        Log.e("", "postSuggestion $postSuggestionObject")

        mViewModel.suggestionEnhaceQuestionPostApiCall("",postSuggestionObject.toString())
    }

    override fun closeThisPage() {
        super.onBackClick()
    }

}
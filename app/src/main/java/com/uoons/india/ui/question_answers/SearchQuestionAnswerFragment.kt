package com.uoons.india.ui.question_answers

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentSearchQuestionAnswerBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.product_detail.adapter.ProductDetailsQuastionAnswerAdapter
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer

import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.remote.error.Failure
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.ui.product_detail.model.QuestionLikeUnlikeModel
import com.uoons.india.ui.product_detail.quetion_and_answer.model.QuestionAnswerModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SearchQuestionAnswerFragment : BaseFragment<FragmentSearchQuestionAnswerBinding, SearchQuestionAnswerFragmentVM>(), SearchQuestionAnswerFragmentNavigator {
    private var LOG_TAG = SearchQuestionAnswerFragment::class.java.name
    override val bindingVariable: Int = BR.searchQuestionAnswerFragmentVM
    override val layoutId: Int = R.layout.fragment_search_question_answer
    override val viewModelClass: Class<SearchQuestionAnswerFragmentVM> = SearchQuestionAnswerFragmentVM::class.java
    private var navController: NavController? = null
    private var pId : String = ""
    private var postYourQuestion : String = ""
    private lateinit var productDetailsQuastionAnswerAdapter : ProductDetailsQuastionAnswerAdapter
    var PID :String = ""
    override  fun init(){
        mViewModel.navigator = this
        pId = arguments?.getString(AppConstants.PId).toString()
//        Log.e(LOG_TAG,"pId:- $pId")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
//        PID= getActivity()?.let { getEncryptedSharedprefs(it).getString("PROFILE_ID", "") }.toString()
        PID =    AppPreference.getValue(PreferenceKeys.PROFILE_ID)
        viewDataBinding.didnotGetAnswerText.visibility = View.GONE
        viewDataBinding.crdPostYourQuestion.visibility = View.GONE
        init()
        viewDataBinding.rcvQuestionsAnswer.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }

        // Lets soft keyboard trigger only if no physical keyboard present
        viewDataBinding.edtSearchQuestions.requestFocus()
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(viewDataBinding.edtSearchQuestions, InputMethodManager.SHOW_IMPLICIT)

        viewDataBinding.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })

        viewDataBinding.edtSearchQuestions.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(s.length >=3){
                    Log.e(LOG_TAG, "content:- $s")
                    mViewModel.productQuestionsListApiCall(s.toString(),pId)
                    postYourQuestion = s.toString()
                    Log.e(LOG_TAG,"content:- $s.toString()")
                    viewDataBinding.didnotGetAnswerText.visibility = View.VISIBLE
                    viewDataBinding.crdPostYourQuestion.visibility = View.VISIBLE
                }
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {}
        })

        viewDataBinding.crdPostYourQuestion.setOnClickListener(View.OnClickListener { view ->
            if (mViewModel.navigator!!.checkIfInternetOn()) {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                }else{
                    val bundle = bundleOf(AppConstants.PId to pId, AppConstants.PostQuestion to postYourQuestion)
                    navController?.navigate(R.id.action_searchQuestionAnswerFragment_to_postYourQuestionFragment,bundle)
                }
            }else if (mViewModel.navigator!!.isConnectedToInternet()){
                return@OnClickListener
            }
        })
    }

    override fun productQuestionListResponse() {
        if(view !=null){
            mViewModel.getProductQuestionsResponse.observe(viewLifecycleOwner, Observer<QuestionAnswerModel> {
                if (it != null){
                    setAllQuestionAnswerData(it)
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun likeUnlikeQuestionResponse() {
        if(view !=null){
            mViewModel.getLikeUnlikeQuestionsResponse.observe(viewLifecycleOwner, Observer<QuestionLikeUnlikeModel> {
                if (it != null){
                    questionLikeUnlike(it)
                }
            })
        }
    }

    private fun questionLikeUnlike(questionLikeUnlikeModel: QuestionLikeUnlikeModel) {
        Log.e(LOG_TAG,"likeUnlikeQuestionResponse:- "+questionLikeUnlikeModel.message)
        mViewModel.productQuestionsListApiCall(postYourQuestion,pId)
        context.let { CommonUtils.showToastMessage(requireContext(), questionLikeUnlikeModel.message) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAllQuestionAnswerData(questionAnswerModel: QuestionAnswerModel) {
        if (questionAnswerModel.status.equals(AppConstants.SUCCESS)){
            productDetailsQuastionAnswerAdapter = ProductDetailsQuastionAnswerAdapter(questionAnswerModel.Data,requireContext(),onclickThumbUp = {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                }else{
                    mViewModel.likeUnlikeQuestionAPICall(it,"1")
                }
            },
                onclickThumbDown = {
                    if (CommonUtils.isStringNullOrBlank(PID)){
                        val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                        loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                    }else{
                        mViewModel.likeUnlikeQuestionAPICall(it,"0")
                    }
                })
            viewDataBinding.rcvQuestionsAnswer.adapter = productDetailsQuastionAnswerAdapter
            productDetailsQuastionAnswerAdapter.notifyDataSetChanged()
        }else{
            productDetailsQuastionAnswerAdapter = ProductDetailsQuastionAnswerAdapter(questionAnswerModel.Data,requireContext(),onclickThumbUp = {
                if (CommonUtils.isStringNullOrBlank(PID)){
                    val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                    loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                }else{
                    mViewModel.likeUnlikeQuestionAPICall(it,AppConstants.one)
                }
            },
                onclickThumbDown = {
                    if (CommonUtils.isStringNullOrBlank(PID)){
                        val loginMobileNoBottomSheet = LoginMobileNoBottomSheet().newInstance()
                        loginMobileNoBottomSheet.show(childFragmentManager, "loginScreen")
                    }else{
                        mViewModel.likeUnlikeQuestionAPICall(it,AppConstants.zero)
                    }
                })
            viewDataBinding.rcvQuestionsAnswer.adapter = productDetailsQuastionAnswerAdapter
            productDetailsQuastionAnswerAdapter.notifyDataSetChanged()
        }
    }

    override fun handleAPIFailure(failure: Failure) {
        super.handleAPIFailure(failure)
    }

/*
    fun getEncryptedSharedprefs(context: Context): SharedPreferences {

        val masterkeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            "secured_prefs", masterkeyAlias, context ,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }
*/

}
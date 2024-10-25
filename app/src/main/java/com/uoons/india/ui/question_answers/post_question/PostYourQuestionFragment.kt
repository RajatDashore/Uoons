package com.uoons.india.ui.question_answers.post_question

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentPostYourQuestionBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.question_answers.post_question.model.PostYourQuestionModel
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class PostYourQuestionFragment : BaseFragment<FragmentPostYourQuestionBinding, PostYourQuestionFragmentVM>(), PostYourQuestionFragmentNavigator {
    private var LOG_TAG = PostYourQuestionFragment::class.java.name
    override val bindingVariable: Int = BR.postYourQuestionFragmentVM
    override val layoutId: Int = R.layout.fragment_post_your_question
    override val viewModelClass: Class<PostYourQuestionFragmentVM> = PostYourQuestionFragmentVM::class.java
    private var navController: NavController? = null
    private var pId : String = ""
    private var postYourQuestion : String = ""

    override   fun init(){
        mViewModel.navigator = this
        pId = arguments?.getString(AppConstants.PId).toString()
        postYourQuestion = arguments?.getString(AppConstants.PostQuestion).toString()
        Log.e(LOG_TAG,"pId:- $pId and postYourQuestion:- $postYourQuestion")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewDataBinding.edtEnterPostQuestion.setText(postYourQuestion)
        viewDataBinding.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })
        init()
        viewDataBinding.crdSubmitYourQuestion.setOnClickListener(View.OnClickListener { view ->
            val postQuestion = viewDataBinding.edtEnterPostQuestion.text.toString()
            Log.e(LOG_TAG,"postQuestion $postQuestion and Product_id $pId")
            mViewModel.postYourQuestionApiCall(postQuestion,pId)
        })
    }

    override fun postYourQuestionResponse() {
        if(view !=null){
            mViewModel.getResponsePostYourQuestion.observe(viewLifecycleOwner, Observer<PostYourQuestionModel> {
                if (it != null){
                    context?.let { it2 ->productDetailPage(it,it2) }
                }else{
                    Toast.makeText(requireActivity(), "Error in fetching data", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun productDetailPage(postYourQuestionModel: PostYourQuestionModel, it2: Context) {
        val bundle = bundleOf(AppConstants.PId to pId)
          navController?.navigate(R.id.action_postYourQuestionFragment_to_productDetailFragment,bundle)
    }
}
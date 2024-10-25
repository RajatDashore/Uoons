package com.uoons.india.ui.product_detail.quetion_and_answer

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.databinding.FragmentProductDetailsQuestionAndAnswerBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.login_module.login_mobile_no.LoginMobileNoBottomSheet
import com.uoons.india.ui.product_detail.adapter.ProductDetailsQuastionAnswerAdapter
import com.uoons.india.ui.product_detail.model.QuestionLikeUnlikeModel
import com.uoons.india.ui.product_detail.quetion_and_answer.model.QuestionAnswerModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ProductDetailsQuestionAndAnswerFragment : BaseFragment<FragmentProductDetailsQuestionAndAnswerBinding, ProductDetailsQuestionAndAnswerFragmentVM>(),
    ProductDetailsQuestionAndAnswerFragmentNavigator {
    override val bindingVariable: Int = BR.productDetailsQuestionAndAnswerFragmentVM
    override val layoutId: Int = R.layout.fragment_product_details_question_and_answer
    override val viewModelClass: Class<ProductDetailsQuestionAndAnswerFragmentVM> = ProductDetailsQuestionAndAnswerFragmentVM::class.java
    private var navController: NavController? = null
    private var pId : String = ""
    var PID :String = ""
    private lateinit var productDetailsQuastionAnswerAdapter : ProductDetailsQuastionAnswerAdapter

    override  fun init(){
        mViewModel.navigator = this
        pId = arguments?.getString(AppConstants.PId).toString()
        // Start shimmer animation
        viewDataBinding.shimmerQuestionAndAnswerLayout.startShimmer()
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getAllQuestionAnswersApiCall(pId)
        }else if (mViewModel.navigator!!.isConnectedToInternet()){
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDataBinding.shimmerQuestionAndAnswerLayout.stopShimmer()
        viewDataBinding.shimmerQuestionAndAnswerLayout.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
//        PID= getActivity()?.let { getEncryptedSharedprefs(it).getString("PROFILE_ID", "") }.toString()
        PID    =    AppPreference.getValue(PreferenceKeys.PROFILE_ID)
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        init()
        viewDataBinding.homeToolBar.crdSorting.visibility = View.GONE
        viewDataBinding.homeToolBar.crdFilters.visibility = View.GONE

        viewDataBinding.toolbar.txvTitleName.text = AppConstants.QuestionAnser

        viewDataBinding.rcvQuestionsAnswer.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })

        viewDataBinding.homeToolBar.edtSearch.setOnClickListener(View.OnClickListener { view ->
            val bundle = bundleOf(AppConstants.PId to pId)
            navController?.navigate(R.id.action_productDetailsQuestionAndAnswerFragment_to_searchQuestionAnswerFragment,bundle)
        })
    }

    override fun getAllQuestionAnswersData() {
        if(view !=null){
            mViewModel.getAllQuestionsAnswersResponse.observe(viewLifecycleOwner, Observer<QuestionAnswerModel> {
                if (it != null){
                    setAllQuestionAnswerData(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAllQuestionAnswerData(questionAnswerModel: QuestionAnswerModel) {
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

        viewDataBinding.shimmerQuestionAndAnswerLayout.stopShimmer()
        viewDataBinding.shimmerQuestionAndAnswerLayout.visibility = View.GONE
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
        Log.e("","likeUnlikeQuestionResponse:- "+questionLikeUnlikeModel.message)
        mViewModel.getAllQuestionAnswersApiCall(pId)
        context.let { CommonUtils.showToastMessage(requireContext(), questionLikeUnlikeModel.message) }
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
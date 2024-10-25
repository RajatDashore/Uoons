package com.uoons.india.ui.help.view_pager_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentFAQBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.help.view_pager_fragment.adapet.FAQFragmentAdapter
import com.uoons.india.ui.help.view_pager_fragment.model.FAQDataModel
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.CommonUtils
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FAQFragment : BaseFragment<FragmentFAQBinding, FAQFragmentVM>(),
    FAQFragmentNavigator {

    override val bindingVariable: Int = BR.faqFragmentVM
    override val layoutId: Int = R.layout.fragment_f_a_q
    override val viewModelClass: Class<FAQFragmentVM> = FAQFragmentVM::class.java
    lateinit var faqFragmentAdapter : FAQFragmentAdapter
    private var navController: NavController? = null

    override  fun init() {
        mViewModel.navigator = this

        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getFAQApiCall()
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {
                onInternet()
            })
        }

    }

    private fun onInternet(){
        if (mViewModel.navigator!!.checkIfInternetOn()) {
            mViewModel.getFAQApiCall()
        }else{
            mViewModel.navigator!!.showAlertDialog1Button(AppConstants.Uoons,resources.getString(R.string.please_check_internet_connection), onClick = {
                onInternet()
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.e("FAQFragment:","onCreate:-")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
        viewDataBinding.rcvFAQData.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun getAllFAQData() {
        if(view !=null){
            mViewModel.faqFragmentRCVListData.observe(viewLifecycleOwner, Observer<FAQDataModel> {
                if (it != null){
                    setFAQAdapterData(it)
                }else{
                    context?.let { CommonUtils.showToastMessage(it, resources.getString(R.string.error_in_fetching_data)) }
                }
            })
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setFAQAdapterData(data: FAQDataModel){
        faqFragmentAdapter = FAQFragmentAdapter(data,requireContext())
        viewDataBinding.rcvFAQData.adapter = faqFragmentAdapter
        faqFragmentAdapter.notifyDataSetChanged()
    }


}
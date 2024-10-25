package com.uoons.india.ui.refferal_code

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.MyRefferalCodeBottomSheetLayoutBinding
import com.uoons.india.ui.base.BaseBottomSheetDialogFrag
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class MyRefferalCodeFragment : BaseBottomSheetDialogFrag<MyRefferalCodeBottomSheetLayoutBinding, MyRefferalCodeFragmentVM>(),
    MyRefferalCodeFragmentNavigator {
    private  val TAG = "MyRefferalCodeFragment"
    override val bindingVariable: Int= BR.myRefferalCodeFragmentVM
    override val layoutId: Int = R.layout.my_refferal_code_bottom_sheet_layout
    override val viewModelClass: Class<MyRefferalCodeFragmentVM> = MyRefferalCodeFragmentVM::class.java
    var title : String = ""
    var myCoins : String = ""
    var referralCode : String = ""

    @SuppressLint("SetTextI18n")
    override  fun init() {
        mViewModel.navigator = this
        title = requireArguments().getString(AppConstants.MyReferralCode).toString()
        myCoins = requireArguments().getString(AppConstants.MyCoins).toString()
        referralCode = requireArguments().getString(AppConstants.ReferalCode).toString()
        if (title == requireContext().getString(R.string.my_referral_code)){
            viewDataBinding.txvTitle.text = requireContext().getString(R.string.my_referral_code)
            viewDataBinding.txvDownloadApp.visibility = View.VISIBLE
            viewDataBinding.crdRefferalCode.visibility = View.VISIBLE
            viewDataBinding.txvRefarralCode.text = referralCode

            viewDataBinding.ivUoonsLogo.visibility = View.VISIBLE
            viewDataBinding.viewClose.visibility = View.VISIBLE
            viewDataBinding.viewCrdRefferalCode.visibility = View.VISIBLE

            viewDataBinding.txvMyCoins.visibility = View.GONE
            viewDataBinding.crdMyCoins.visibility = View.GONE
        }else{
            viewDataBinding.txvTitle.text = requireContext().getString(R.string.my_total_coins)
            viewDataBinding.txvMyCoins.visibility = View.VISIBLE
            viewDataBinding.crdMyCoins.visibility = View.VISIBLE
            viewDataBinding.txvMyCoinsAmmount.text = myCoins

            viewDataBinding.ivUoonsLogo.visibility = View.VISIBLE
            viewDataBinding.viewClose.visibility = View.VISIBLE
            viewDataBinding.viewCrdRefferalCode.visibility = View.VISIBLE

            viewDataBinding.txvDownloadApp.visibility = View.GONE
            viewDataBinding.crdRefferalCode.visibility = View.GONE
        }
        Log.e(TAG,"LoginInit:- $title")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun newInstance(): MyRefferalCodeFragment {
        return MyRefferalCodeFragment()
    }

    override fun dismissDialog() {
        dismiss()
        Log.e(TAG,"dismissDialog")
    }
}
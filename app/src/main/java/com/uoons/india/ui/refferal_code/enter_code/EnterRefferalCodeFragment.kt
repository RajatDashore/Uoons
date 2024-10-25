package com.uoons.india.ui.refferal_code.enter_code

import android.os.Bundle
import android.util.Log
import android.view.View
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.EnterRefferalCodeBottomSheetLayoutBinding
import com.uoons.india.ui.base.BaseBottomSheetDialogFrag
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class EnterRefferalCodeFragment : BaseBottomSheetDialogFrag<EnterRefferalCodeBottomSheetLayoutBinding, EnterRefferalCodeFragmentVM>(),
    EnterRefferalCodeFragmentNavigatore {
    private  val TAG = "EnterRefferalCodeFragment"
    override val bindingVariable: Int= BR.enterRefferalCodeFragmentVM
    override val layoutId: Int = R.layout.enter_refferal_code_bottom_sheet_layout
    override val viewModelClass: Class<EnterRefferalCodeFragmentVM> = EnterRefferalCodeFragmentVM::class.java

    override    fun init() {
        mViewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun newInstance(): EnterRefferalCodeFragment {
        return EnterRefferalCodeFragment()
    }

    override fun dismissDialog() {
        dismiss()
        Log.e(TAG,"dismissDialog")
    }

    override fun submitRefferalCode() {
        dismiss()
        Log.e(TAG,"dismissDialog")
    }
}
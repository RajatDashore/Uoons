package com.uoons.india.ui.rate_us

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.RateUsBottomSheetLayoutBinding
import com.uoons.india.ui.base.BaseBottomSheetDialogFrag
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class RateUsBottomSheet : BaseBottomSheetDialogFrag<RateUsBottomSheetLayoutBinding, RateUsBottomSheetVM>(),
    RateUsBottomSheetNavigator {

    private  val TAG = "RateUsBottomSheet"
    override val bindingVariable: Int= BR.rateUsBottomSheetVM
    override val layoutId: Int = R.layout.rate_us_bottom_sheet_layout
    override val viewModelClass: Class<RateUsBottomSheetVM> = RateUsBottomSheetVM::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun newInstance(): RateUsBottomSheet {
        return RateUsBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override   fun init() {
        mViewModel.navigator = this
    }

    override fun dismissDialog() {
        dismiss()
        Log.e(TAG,"dismissDialog")
    }

    override fun ratingOnPlayStore() {
        this@RateUsBottomSheet.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.Update_PlayStore_App)))
        dismiss()
    }

    override fun ratingDialogClose() {
        dismiss()
        Log.e(TAG,"dismissDialog")
    }
}
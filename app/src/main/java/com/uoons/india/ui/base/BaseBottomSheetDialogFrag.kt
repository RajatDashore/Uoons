package com.uoons.india.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.uoons.india.R
import com.uoons.india.data.remote.error.Failure
import android.util.TypedValue
import android.view.WindowManager
import com.uoons.india.BR
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
abstract class BaseBottomSheetDialogFrag<T:ViewDataBinding,V:BaseViewModel<*>>:BottomSheetDialogFragment(),CommonNavigator {

    lateinit var baseActivity: BaseActivity<*, *>
    private var  mRootView: View? = null
    lateinit var viewDataBinding: T
    abstract val bindingVariable: Int
    abstract val layoutId: Int
    abstract val viewModelClass: Class<V>
    lateinit var mViewModel: V
    var heighOnBottomSheet : Float = 0.0f
    val layoutID: Int= BR.filtersBottomSheetVM

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceType")
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        val bottomSheet = dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val fId = R.layout.filters_bottom_sheet
//        Log.e("BaseBottomSheetDialogFrag","$bindingVariable")

        if (bindingVariable == layoutID){
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            val view = view
            view!!.post {
                val parent = view.parent as View
                val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior
                val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
                bottomSheetBehavior!!.peekHeight = view.measuredHeight
                (bottomSheet.parent as View).setBackgroundColor(Color.TRANSPARENT)
                bottomSheet.background = requireContext().resources.getDrawable(R.drawable.bottom_sheet_background)

            }
        }else{
            val height = Resources.getSystem().displayMetrics.heightPixels
            heighOnBottomSheet = (height / 2).toFloat()
            val view = view
            view!!.post {
                val parent = view.parent as View
                val params = parent.layoutParams as CoordinatorLayout.LayoutParams
                val behavior = params.behavior
                val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
                bottomSheetBehavior!!.peekHeight = view.measuredHeight
                bottomSheetBehavior.peekHeight = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, heighOnBottomSheet, resources.displayMetrics
                ).toInt()
                (bottomSheet.parent as View).setBackgroundColor(Color.TRANSPARENT)
                bottomSheet.background = requireContext().resources.getDrawable(R.drawable.bottom_sheet_background)
            }
        }

        /*Log.e("bindingVariable","BaseBottomSheetDialogFrag:- $bindingVariable")
        val view = view
        view!!.post {
            val parent = view.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            bottomSheetBehavior!!.peekHeight = view.measuredHeight
            bottomSheetBehavior.peekHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, heighOnBottomSheet, resources.displayMetrics
            ).toInt()
            (bottomSheet.parent as View).setBackgroundColor(Color.TRANSPARENT)
            bottomSheet.background = requireContext().resources.getDrawable(R.drawable.bottom_sheet_background)
        }*/
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.baseActivity = context
        }
    }

    override fun hideStatusBar(){
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        requireActivity().window.statusBarColor = Color.TRANSPARENT
    }

    override fun setStatusBarColor(color: Int) {
        val window: Window =  requireActivity().window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor( requireActivity(), color);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        mViewModel = getViewModel2()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding.lifecycleOwner = this
        mRootView = viewDataBinding.root
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, mViewModel)
        init()
    }

    protected open fun getViewModel2() : V {
        val factory = ViewModelFactory()
        return ViewModelProvider(this, factory).get(viewModelClass)
    }

    fun hideKeyboard() {
        baseActivity.hideKeyboard()
    }

    override fun showProgress() {
        baseActivity.showProgress()
    }

    override fun hideProgress() {
        baseActivity.hideProgress()
    }

    override fun showValidationError(error: String) {
        baseActivity.showValidationError(error)
    }

    override fun showToast(error: String?) {
        baseActivity.showToast(error)
    }

    override fun handleAPIFailure(failure: Failure) {
        baseActivity.handleAPIFailure(failure)
    }

    override fun showNetworkError(error: String) {
        baseActivity.showNetworkError(error)
    }

    override fun showSessionExpireAlert(error: String) {
        baseActivity.showSessionExpireAlert(error)
    }

    override fun getStringResource(id: Int): String {
        return baseActivity.getStringResource(id)
    }

    override fun getIntegerResource(id: Int): Int {
        return baseActivity.getIntegerResource(id)
    }

    override fun showAlertDialog1Button(alertTitle: String, alertMsg: String, buttonTitle: String, onClick: () -> Unit) {
        baseActivity.showAlertDialog1Button(
            alertTitle = alertTitle,
            alertMsg = alertMsg,
            buttonTitle = buttonTitle,
            onClick = onClick
        )
    }

    override fun showAlertDialog2Button(alertTitle: String, alertMsg: String, button1Title: String, button2Title: String, onClick1: () -> Unit, onClick2: () -> Unit) {
        baseActivity.showAlertDialog2Button(
            alertTitle = alertTitle,
            alertMsg = alertMsg,
            button1Title = button1Title,
            button2Title = button2Title,
            onClick1 = onClick1,
            onClick2 = onClick2
        )
    }

    override fun onBackClick() {
        baseActivity.onBackClick()
    }

    override fun checkIfInternetOn(): Boolean {
        return baseActivity.checkIfInternetOn()
    }

    override fun isConnectedToInternet(): Boolean {
        return baseActivity.isConnectedToInternet()
    }

}
package com.uoons.india.ui.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.remote.error.Failure
import io.reactivex.disposables.CompositeDisposable
import org.lsposed.lsparanoid.Obfuscate

@Suppress("DEPRECATION", "DEPRECATION")
@Obfuscate
abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment(),
    CommonNavigator {
    lateinit var baseActivity: BaseActivity<*, *>
    private var mRootView: View? = null
    lateinit var viewDataBinding: T
    abstract val bindingVariable: Int
    abstract val layoutId: Int
    abstract val viewModelClass: Class<V>
    lateinit var mViewModel: V
    private val disposable = CompositeDisposable()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.baseActivity = context
        }
    }

    override fun hideStatusBar() {
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        requireActivity().window.statusBarColor = Color.TRANSPARENT
    }

    override fun setStatusBarColor(color: Int) {
        val window: Window = requireActivity().window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireActivity(), color);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        mViewModel = getViewModel2()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //   viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewDataBinding.lifecycleOwner = this
        mRootView = viewDataBinding.root
        init()
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, mViewModel)
        viewDataBinding.executePendingBindings()


    }

    protected open fun getViewModel2(): V {
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

    override fun showAlertDialog1Button(
        alertTitle: String,
        alertMsg: String,
        buttonTitle: String,
        onClick: () -> Unit,
    ) {
        baseActivity.showAlertDialog1Button(
            alertTitle = alertTitle,
            alertMsg = alertMsg,
            buttonTitle = buttonTitle,
            onClick = onClick
        )
    }

    override fun showAlertDialog2Button(
        alertTitle: String,
        alertMsg: String,
        button1Title: String,
        button2Title: String,
        onClick1: () -> Unit,
        onClick2: () -> Unit,
    ) {
        baseActivity.showAlertDialog2Button(
            alertTitle = alertTitle,
            alertMsg = alertMsg,
            button1Title = button1Title,
            button2Title = button2Title,
            onClick1 = onClick1,
            onClick2 = onClick2
        )
    }

    //rx method to check permission
    fun checkPermission(context: Context, vararg permissions: String) {
        disposable.add(
            RxPermissions(this)
                .request(*permissions)
                .subscribe { granted ->
                    if (granted!!) {
                        rxPermissionGranted()
                    } else {
                        rxPermissionDenied()
                    }
                })
    }

    /*invoked when permission granted*/
    protected open fun rxPermissionGranted() {
        AppPreference.addValue(PreferenceKeys.GRANTED_PERMISSION, "true")
    }

    /*invoked when permission denied*/
    protected open fun rxPermissionDenied() {

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
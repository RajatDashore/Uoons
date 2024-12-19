package com.uoons.india.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.local.contextDataStore
import com.uoons.india.databinding.FragmentSettingsBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.utils.DialogConstant
import com.uoons.india.utils.PropertiChangeListener
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsFragmentVM>(),
    SettingsFragmentNavigator, PropertiChangeListener {

    override val bindingVariable: Int = BR.settingsFragmentVM
    override val layoutId: Int = R.layout.fragment_settings
    override val viewModelClass: Class<SettingsFragmentVM> = SettingsFragmentVM::class.java
    private var navController: NavController? = null
    private lateinit var thiscontext: Context

    override fun init() {
        mViewModel.navigator = this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        thiscontext = context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("AccountFragment:", "onCreate:-")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thiscontext = view.context
        init()
        navController = Navigation.findNavController(view)
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.settings)
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        clickListners()
    }

    private fun clickListners() {
        viewDataBinding.toolbar.ivBackBtn.setOnClickListener { view ->
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewDataBinding.txvLanguage.setOnClickListener { view ->
            DialogConstant.showChangeLanguageDialog(thiscontext, this)
        }

        viewDataBinding.toolbar.ivCartVector.setOnClickListener { view ->
            naviGateToMyCartFragment()
        }
    }

    override fun naviGateToNotificationsSettingsFragment() {
        navController?.navigate(R.id.action_settingsFragment_to_notificationsSettingsFragment)
    }

    fun naviGateToMyCartFragment() {
        navController?.navigate(R.id.action_settingsFragment_to_myCartFragment)
    }


    override fun onPropertiChanged(isChanged: Boolean) {
        if (isChanged) {
            // CustomProgressDialog(thiscontext).showProgressDialog()
            AppPreference.getInstance(thiscontext.contextDataStore)
            val languageCode: String = AppPreference.getValue(PreferenceKeys.MY_LANGUAGE)
            DialogConstant.setLocalLanguage(thiscontext, languageCode)
            getActivity()?.recreate()
            //  CustomProgressDialog(thiscontext).hideProgressDialog()
        }
    }

}
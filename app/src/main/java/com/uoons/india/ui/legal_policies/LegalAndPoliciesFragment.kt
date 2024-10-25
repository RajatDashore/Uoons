package com.uoons.india.ui.legal_policies

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentLegalAndPoliciesBinding
import com.uoons.india.ui.base.BaseFragment
import android.webkit.WebViewClient
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class LegalAndPoliciesFragment : BaseFragment<FragmentLegalAndPoliciesBinding, LegalAndPoliciesFragmentVM>(), LegalAndPoliciesFragmentNavigator {

    override val bindingVariable: Int = BR.legalAndPoliciesFragmentVM
    override val layoutId: Int = R.layout.fragment_legal_and_policies
    override val viewModelClass: Class<LegalAndPoliciesFragmentVM> = LegalAndPoliciesFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var thiscontext: Context
    var uri :String = ""

    override  fun init() {
        mViewModel.navigator = this
    }

    fun newInstance(): LegalAndPoliciesFragment {
        return LegalAndPoliciesFragment()
    }

    companion object {
        fun newInstance() = LegalAndPoliciesFragment().apply {
            arguments = Bundle().apply {
                uri = arguments?.getString("uri").toString()
                Log.e("LegalAndPoliciesFragment", "LegalAndPoliciesFragment:- $uri")
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        thiscontext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("LegalAndPoliciesFragment:","onCreate:-")
        init()
        uri = arguments?.getString("uri").toString()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thiscontext = view.context
        uri = arguments?.getString("uri").toString()

        // loading http://www.google.com url in the the WebView.
        viewDataBinding.webview.loadUrl(uri)
        // this will enable the javascript.
        viewDataBinding.webview.settings.javaScriptEnabled = true
        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        viewDataBinding.webview.webViewClient = WebViewClient()

        navController = Navigation.findNavController(view)
        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.legal_and_policies)
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE
        viewDataBinding.toolbar.ivBackBtn.setOnClickListener(View.OnClickListener { view ->
            super.onBackClick()
        })

        viewDataBinding.toolbar.ivCartVector.setOnClickListener(View.OnClickListener { view ->
            naviGateToMyCartFragment()
        })
    }

    fun naviGateToMyCartFragment(){
        navController?.navigate(R.id.action_legalAndPoliciesFragment_to_myCartFragment)
    }
}
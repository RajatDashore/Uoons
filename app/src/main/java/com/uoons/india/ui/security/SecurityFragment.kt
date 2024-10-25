package com.uoons.india.ui.security

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentSecurityBinding
import com.uoons.india.ui.base.BaseFragment


class SecurityFragment : BaseFragment<FragmentSecurityBinding, SecurityFragmentVM>(),
    SecurityFrgamentNavigator {
    override val bindingVariable: Int = BR.settingsFragmentVM
    override val layoutId: Int = R.layout.fragment_security
    override val viewModelClass: Class<SecurityFragmentVM> = SecurityFragmentVM::class.java
    private var navController: NavController? = null

    override fun init() {
        mViewModel.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

}
package com.uoons.india.ui.help

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.FragmentHelpBinding
import com.uoons.india.ui.base.BaseFragment
import com.uoons.india.ui.help.adapter.SectionPageAdapter
import com.uoons.india.ui.help.adapter.ViewPagerAdapter
import com.uoons.india.ui.help.view_pager_fragment.ContactUsFragment
import com.uoons.india.ui.help.view_pager_fragment.FAQFragment
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class HelpFragment : BaseFragment<FragmentHelpBinding, HelpFragmentVM>(),
    HelpFragmentNavigator {

    override val bindingVariable: Int = BR.helpFragmentVM
    override val layoutId: Int = R.layout.fragment_help
    override val viewModelClass: Class<HelpFragmentVM> = HelpFragmentVM::class.java
    private var navController: NavController? = null
    lateinit var adapter: ViewPagerAdapter

    override  fun init() {
        mViewModel.navigator = this
        // Start shimmer animation
        viewDataBinding.shimmerHelpLayout.startShimmer()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        viewDataBinding.toolbar.txvTitleName.text = resources.getString(R.string.help)
        viewDataBinding.toolbar.ivHeartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVector.visibility = View.GONE
        viewDataBinding.toolbar.ivCartVectorInvisible.visibility = View.GONE
        viewDataBinding.toolbar.crdCountMyCart.visibility = View.GONE

        viewDataBinding.toolbar.ivBackBtn.setOnClickListener {
            super.onBackClick()
        }

        setUpNewViewPager(viewDataBinding.viewPager)
        viewDataBinding.tabLayout.setupWithViewPager(viewDataBinding.viewPager)

        viewDataBinding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {}
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    fun naviGateToMyCartFragment() {
        navController?.navigate(R.id.action_helpFragment_to_myCartFragment2)
    }

    private fun setUpNewViewPager(viewPager: ViewPager) {
        val sectionPageAdapter = SectionPageAdapter(childFragmentManager)
        sectionPageAdapter.addFragmentsAdapter(ContactUsFragment(), requireContext().getString(R.string.contact_us))
        sectionPageAdapter.addFragmentsAdapter(FAQFragment(), requireContext().getString(R.string.faq))
        viewPager.adapter = sectionPageAdapter
        viewDataBinding.cvMain.visibility = View.VISIBLE
        viewDataBinding.shimmerHelpLayout.visibility = View.GONE
    }
}
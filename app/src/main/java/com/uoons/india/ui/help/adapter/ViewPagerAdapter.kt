package com.uoons.india.ui.help.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.uoons.india.ui.help.view_pager_fragment.ContactUsFragment
import com.uoons.india.ui.help.view_pager_fragment.FAQFragment
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ViewPagerAdapter( fm: FragmentManager, private var totalTabs: Int) : FragmentPagerAdapter(fm,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    // this is for fragment tabs

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = ContactUsFragment()
        } else if (position == 1) {
            fragment = FAQFragment()
        }
        return fragment!!
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}
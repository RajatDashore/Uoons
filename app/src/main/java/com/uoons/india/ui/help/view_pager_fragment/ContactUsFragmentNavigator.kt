package com.uoons.india.ui.help.view_pager_fragment

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface ContactUsFragmentNavigator : CommonNavigator{
    fun submitHelpTopic()
    fun saveContactHelpTopicResponse()
}
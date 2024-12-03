package com.uoons.india.ui.home.fragment

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface HomeFragmentNavigator : CommonNavigator {
    fun naviGateToProductDetail(pId: String)
    fun naviGateToCategoryItemsFragment(subID: String, parentID: String, categoryName: String)
    fun naviGateToSliderItemsFragment(cId: String)
    fun getDeshBoardData()
    fun navigateToFourPhoto(subID: String, parentID: String, categoryName: String)
    fun navigateToSaasFragment()
    fun getDeshBoardMoreProductsData()
    fun getJwellaryData()
    //  fun addRecentlyViewProductResponse()
}
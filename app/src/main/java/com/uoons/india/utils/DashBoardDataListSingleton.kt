package com.uoons.india.utils

import com.uoons.india.ui.home.fragment.model.DeshBoardModel
import com.uoons.india.ui.home.fragment.more_products.model.MoreProducts
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
object DashBoardDataListSingleton {

    private var dashBoardData: DeshBoardModel? = null
    private var deshBoardJwellary: DeshBoardModel? = null
    private var moreProductsItemList: ArrayList<MoreProducts> = ArrayList<MoreProducts>()


    fun setDastBoardData(dashBoardData: DeshBoardModel?) {
        this.dashBoardData = dashBoardData
    }

    fun setJwellaryData(deshBoardJwellary: DeshBoardModel?) {
        this.deshBoardJwellary = deshBoardJwellary
    }

    fun setMoreProductsItemList(moreProductsItemList: ArrayList<MoreProducts>) {

        this.moreProductsItemList = moreProductsItemList
    }

    fun getDastBoardData(): DeshBoardModel? {
        return dashBoardData
    }

    fun getJwellaryData(): DeshBoardModel? {
        return deshBoardJwellary
    }

    fun getMoreProductsItemList(): ArrayList<MoreProducts> {
        return moreProductsItemList
    }
}
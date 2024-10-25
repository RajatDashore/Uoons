package com.uoons.india.ui.my_cart

import android.view.View
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface OnListItemClicked {
    fun onItemClicked(position: Int, view: View?, value: String?, pId:Int, pQuantity:Int)
}
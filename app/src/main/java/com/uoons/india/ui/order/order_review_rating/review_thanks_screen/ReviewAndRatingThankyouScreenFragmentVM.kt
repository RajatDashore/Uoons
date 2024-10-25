package com.uoons.india.ui.order.order_review_rating.review_thanks_screen

import com.uoons.india.ui.base.BaseViewModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class ReviewAndRatingThankyouScreenFragmentVM : BaseViewModel<ReviewAndRatingThankyouScreenFragmentNavigator>(){
    fun naviGateToHomeFragment(){
        navigator?.naviGateToHomeFragment()
    }
}
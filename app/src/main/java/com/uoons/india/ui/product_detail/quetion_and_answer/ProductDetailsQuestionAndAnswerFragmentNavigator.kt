package com.uoons.india.ui.product_detail.quetion_and_answer

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface ProductDetailsQuestionAndAnswerFragmentNavigator : CommonNavigator{
    fun getAllQuestionAnswersData()
    fun likeUnlikeQuestionResponse()
}
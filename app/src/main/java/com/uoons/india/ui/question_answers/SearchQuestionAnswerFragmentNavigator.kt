package com.uoons.india.ui.question_answers

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface SearchQuestionAnswerFragmentNavigator : CommonNavigator{
    fun productQuestionListResponse()
    fun likeUnlikeQuestionResponse()
}
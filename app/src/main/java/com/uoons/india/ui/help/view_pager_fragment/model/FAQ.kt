package com.uoons.india.ui.help.view_pager_fragment.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class FAQ(@SerializedName("Faq" ) var Faq : ArrayList<FAQModel> = arrayListOf())
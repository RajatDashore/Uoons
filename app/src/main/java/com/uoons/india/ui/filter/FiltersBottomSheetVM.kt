package com.uoons.india.ui.filter

import androidx.lifecycle.MutableLiveData
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.filter.model_class.FilterDataModel
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class FiltersBottomSheetVM : BaseViewModel<FiltersBottomSheetNavigator>(){

    var getFilterDataResponse : MutableLiveData<FilterDataModel> = MutableLiveData()

    init {
        getFilterDataResponse = MutableLiveData()
    }





}
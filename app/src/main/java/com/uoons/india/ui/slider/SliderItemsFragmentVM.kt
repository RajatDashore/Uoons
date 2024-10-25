package com.uoons.india.ui.slider

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.category.category_items.model.ChildIds
import com.uoons.india.ui.category.category_items.model.FiltersModel
import com.uoons.india.ui.category.category_items.model.SubCategoriesModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class SliderItemsFragmentVM : BaseViewModel<SliderItemsFragmentNavigator>(){
    // GET SUB CATEGORIES
    var homeSliderDataResponse : MutableLiveData<SubCategoriesModel> = MutableLiveData()

    var filterRequestModel: List<FiltersModel> = ArrayList<FiltersModel>()
    var childIds: ArrayList<ChildIds> = ArrayList<ChildIds>()

    init {
        homeSliderDataResponse = MutableLiveData()
    }

    fun getHomeSliderApiCall(cId: String?){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()

            val sliderId = Integer.parseInt(cId)
            Log.e("","SliderItemsFragmentVM:- $sliderId")

            val gson = GsonBuilder().setPrettyPrinting().create()
            val subCategories = gson.toJson(filterRequestModel)

            val result = Repository.get.getAllSubCategories(AppConstants.CHANNEL_MODE,0,sliderId,subCategories)

            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        homeSliderDataResponse.value = it
                        navigator?.getHomeSliderData()
                    }
                }
            )
        }
    }
}
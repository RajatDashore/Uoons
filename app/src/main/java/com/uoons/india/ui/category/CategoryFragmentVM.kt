package com.uoons.india.ui.category
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.category.model.AllCategoryModel
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import com.uoons.india.utils.AppConstants
import kotlinx.coroutines.launch
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class CategoryFragmentVM : BaseViewModel<CategoryFragmentNavigator>(){
    var allCategoriesDataResponse : MutableLiveData<AllCategoryModel> = MutableLiveData()
    var getMyCartItemsDataResponse : MutableLiveData<GetMyCartDataModel> = MutableLiveData()
    var getWishListDataResponse : MutableLiveData<GetWishListDataModel> = MutableLiveData()

    init {
        allCategoriesDataResponse = MutableLiveData()
        getMyCartItemsDataResponse = MutableLiveData()
        getWishListDataResponse = MutableLiveData()
    }

    fun getAllCategoriesApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
          //  navigator?.showProgress()
            val result = Repository.get.getAllCategories(AppConstants.CHANNEL_MODE)
          //  navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        allCategoriesDataResponse.value = it
                        navigator?.getAllCategoriesData()
                        Log.e("VM", "getAllCategoriesApiCall:>>>>>>>>> "+navigator?.getAllCategoriesData().toString())
                    }else if(it.status.equals(AppConstants.FAILURE,ignoreCase = true)) {
                        allCategoriesDataResponse.value = it
                        navigator?.getAllCategoriesData()
                    }
                }
            )
        }
    }

    fun getMyCartItemsApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.getMyCartItems(AppConstants.CHANNEL_MODE)
            result.fold(
                {
                   // navigator?.handleAPIFailure(it)
                  Log.e("getMyCartItemsApiCall", "handleAPIFailure:- $it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS,ignoreCase = true)) {
                        getMyCartItemsDataResponse.value = it
                        navigator?.getMyCartItemsResponse()
                    }else{
                        getMyCartItemsDataResponse.value = it
                        navigator?.getMyCartItemsResponse()
                    }
                }
            )
        }
    }

    fun getWishListProductApiCall(){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            val result = Repository.get.getWishList(AppConstants.CHANNEL_MODE)
            result.fold(
                {
                    //  navigator?.handleAPIFailure(it)
                    Log.e("","handleAPIFailure$it")
                },
                {
                    if(it.status.equals(AppConstants.SUCCESS)) {
                        getWishListDataResponse.value=it
                        navigator?.getWishListResponse()
                    }else{
                        Log.e("CategoryFragmentVM ","getWishListProductApiCall :: "+it.message.toString())
                    }
                }
            )
        }
    }
}
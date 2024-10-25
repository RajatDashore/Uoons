package com.uoons.india.ui.category.category_items

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uoons.india.data.remote.Repository
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.category.category_items.model.*
import kotlinx.coroutines.launch
import com.google.gson.GsonBuilder
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
class CategoryItemsFragmentVM : BaseViewModel<CategoryItemsFragmentNavigator>(){

    // GET SUB CATEGORIES
    var allSubCategoriesDataResponse : MutableLiveData<SubCategoriesModel> = MutableLiveData()

    lateinit var filterRequestModel: List<FiltersModel>
    var childIds: ArrayList<ChildIds> = ArrayList<ChildIds>()

    init {
        allSubCategoriesDataResponse = MutableLiveData()
    }

    fun getAllSubCategoriesApiCall(childId: String?){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()

            childIds.add(ChildIds(childId))
            filterRequestModel = listOf(FiltersModel("1",childIds))
            val gson = GsonBuilder().setPrettyPrinting().create()
            val subCategories = gson.toJson(filterRequestModel)

            val result = Repository.get.getAllSubCategories(AppConstants.CHANNEL_MODE,0,0,subCategories)

            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        allSubCategoriesDataResponse.value = it
                        navigator?.getAllSubCategoriesData()
                    }
                }
            )
        }
    }

    fun sortingData(sortingId: String,childId: String?){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()

            childIds.add(ChildIds(childId))
            filterRequestModel = listOf(FiltersModel("1",childIds))
            val gson = GsonBuilder().setPrettyPrinting().create()
            val subCategories = gson.toJson(filterRequestModel)

            val result = Repository.get.getAllSubCategories(AppConstants.CHANNEL_MODE,sortingId.toInt(),0,subCategories)

            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        allSubCategoriesDataResponse.value = it
                        navigator?.getAllSortingData()
                    }
                }
            )
        }
    }

    fun filterData(parentId: String,childId: String?){
        if (!navigator!!.isConnectedToInternet()) {
            return
        }
        viewModelScope.launch {
            navigator?.showProgress()

            childIds.add(ChildIds(childId))
            filterRequestModel = listOf(FiltersModel(parentId,childIds))
            val gson = GsonBuilder().setPrettyPrinting().create()
            val subCategories = gson.toJson(filterRequestModel)

            val result = Repository.get.getAllSubCategories(AppConstants.CHANNEL_MODE,0,0,subCategories)

            navigator?.hideProgress()
            result.fold(
                {
                    navigator?.handleAPIFailure(it)
                },
                {
                    if(it.status.equals("success")) {
                        allSubCategoriesDataResponse.value = it
                        navigator?.getAllSortingData()
                    }
                }
            )
        }
    }

    // --- Category Items List
   /* fun geCategoryItemsAdapter(): CategoryItemsAdapter {
        return categoryItemsAdapter
    }*/

    // --- Category Items List
    /*@SuppressLint("NotifyDataSetChanged")
    fun setCategoryItemsAdapterData(data: ArrayList<Earphones>, context: Context){
        categoryItemsAdapter.setCategoryItemsList(data,context)
        categoryItemsAdapter.notifyDataSetChanged()
    }
*/

    // --- Category Items List
   /* fun getRecyclerDataObserver(): MutableLiveData<ArrayList<Earphones>>{
        return categoryItemsRCVListData
    }*/

    /*fun makeAPICall(input: String, context: Context) {
        val obj = context.let { getAssetJsonData(it) }
        val collectionType = object : TypeToken<ArrayList<CategoryItems?>?>() {}.type
        val categoryItemslstData: ArrayList<CategoryItems> = Gson().fromJson(obj, collectionType) as ArrayList<CategoryItems>

        val categoryItemsDataList : ArrayList<Earphones> = ArrayList<Earphones>()
        categoryItemsDataList.addAll(categoryItemslstData.get(0).Earphones)
        categoryItemsRCVListData.postValue(categoryItemsDataList)

        categoryItemsAdapter.setOnItemClickListener(object :
            CategoryItemsAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(position: Int) {
                naviGateToCategoryItemsDetailFragment()
            }
        })
    }*/

    /*fun naviGateToCategoryItemsDetailFragment(){
        navigator?.naviGateToCategoryItemsDetailFragment()
    }*/

   /* fun getAssetJsonData(context: Context): String {
        val json: String
        json = try {
            val `is`: InputStream = context.getAssets().open("category_items.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return toString()
        }
        Log.e("", "FAQFragmentVM $json")
        return json
    }*/
}
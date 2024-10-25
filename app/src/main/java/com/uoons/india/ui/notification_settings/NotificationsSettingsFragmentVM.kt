package com.uoons.india.ui.notification_settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uoons.india.ui.base.BaseViewModel
import com.uoons.india.ui.filter.model.FilterAllCategoryDataModel
import com.uoons.india.ui.filter.model.FilterItemsDataModel
import com.uoons.india.ui.notification_settings.adapter.NotificationsAdapter
import org.lsposed.lsparanoid.Obfuscate
import java.io.IOException
import java.io.InputStream

@Obfuscate
class NotificationsSettingsFragmentVM : BaseViewModel<NotificationsSettingsFragmentNavigator>(){
    // --- Notifications List
    var notificationsRCVListData : MutableLiveData<ArrayList<FilterItemsDataModel>> = MutableLiveData()
    lateinit var notificationsAdapter : NotificationsAdapter

    // --- Notifications click
    var notificationClick: MutableLiveData<Int> = MutableLiveData()

    init {
        // --- Notifications List
        notificationsRCVListData = MutableLiveData()
        notificationsAdapter = NotificationsAdapter()
    }

    // --- Notifications List
    fun getnotificationsAdapter(): NotificationsAdapter {
        return notificationsAdapter
    }

    // --- Notifications List
    @SuppressLint("NotifyDataSetChanged")
    fun setNotificationsAdapterData(data: ArrayList<FilterItemsDataModel>, context: Context){
        notificationsAdapter.setNotificationsList(data,context)
        notificationsAdapter.notifyDataSetChanged()
    }

    // --- Notifications List
    fun getRecyclerListNotificationsDataObserver(): MutableLiveData<ArrayList<FilterItemsDataModel>>{
        return notificationsRCVListData
    }

    // --- Notifications click
    fun getNotification():MutableLiveData<Int>{
        return notificationClick
    }

    fun makeAPICall(input: String, context: Context){
        val obj = context.let { getAssetJsonData(it) }
        val collectionType = object : TypeToken<List<FilterAllCategoryDataModel?>?>() {}.type
        val lstCategoryTypesData: ArrayList<FilterAllCategoryDataModel> = Gson().fromJson(obj, collectionType) as ArrayList<FilterAllCategoryDataModel>

        val lstCategoryTypesItemsData : ArrayList<FilterItemsDataModel> = ArrayList<FilterItemsDataModel>()

        lstCategoryTypesItemsData.addAll(lstCategoryTypesData.get(0).Items)
        notificationsRCVListData.postValue(lstCategoryTypesItemsData)

        notificationsAdapter.setOnItemClickListener(object :
            NotificationsAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClicked(position: Int) {
                notificationClick.postValue(position)
            }
        })

    }


    fun getAssetJsonData(context: Context): String {
        val json: String
        json = try {
            val `is`: InputStream = context.getAssets().open("filters_data.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return toString()
        }
        return json
    }
}
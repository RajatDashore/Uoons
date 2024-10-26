package com.uoons.india.data.local

import androidx.datastore.preferences.core.stringPreferencesKey
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
object PreferenceKeys{

    val MY_LANGUAGE = stringPreferencesKey("My_Lang")
    val GET_STARTED = stringPreferencesKey("GET_STARTED")
    val ONLINE = stringPreferencesKey("ONLINE")
    val COD = stringPreferencesKey("COD")
    val CHANNEL_CODE = stringPreferencesKey("CHANNEL_CODE")
    val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
    val MOBILE_NO = stringPreferencesKey("MOBILE_NO")
    val NEW_MOBILE_NO = stringPreferencesKey("NEW_MOBILE_NO")
    val PROFILE_ID = stringPreferencesKey("PROFILE_ID")
    val USER_NAME = stringPreferencesKey("USER_NAME")
    val USER_NAME_ORDER = stringPreferencesKey("USER_NAME_ORDER")
    val USER_EMAIL_ORDER = stringPreferencesKey("USER_EMAIL_ORDER")
    val PROFILE_IMAGE = stringPreferencesKey("PROFILE_IMAGE")
    val PIN_CODE = stringPreferencesKey("PIN_CODE")
    val PIN_CODE_EXPECTED_DELIVERY = stringPreferencesKey("PIN_CODE_EXPECTED_DELIVERY")
    val GRANTED_PERMISSION = stringPreferencesKey("permission")
    val COUPEN_CODE = stringPreferencesKey("COUPEN_CODE")
    val ADDRESS_ID = stringPreferencesKey("ADDRESS_ID")
    val HOME_SUB_ID = stringPreferencesKey("HOME_SUB_ID")
    val SORTING_ID = stringPreferencesKey("SORTING_ID")
    val SORT_BY_NAME = stringPreferencesKey("SORT_BY_NAME")
    val SUB_CATE_ID = stringPreferencesKey("SUB_CATE_ID")
    val FILTER_LIST = stringPreferencesKey("FILTER_LIST")
    val FIREBASE_LINK = stringPreferencesKey("Firebaselink")
    val ORDER_ID = stringPreferencesKey("Order_id")
    val ORDER_amount = stringPreferencesKey("Order_amount")
    val ONE_TIME_REQUEST = stringPreferencesKey("true")

}

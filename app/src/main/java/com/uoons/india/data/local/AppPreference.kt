package com.uoons.india.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.uoons.india.utils.CommonUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.lsposed.lsparanoid.Obfuscate
import java.util.*

val Context.contextDataStore by preferencesDataStore(name = "uoons")

@Obfuscate
object AppPreference {


    var dataStore: DataStore<Preferences>? = null

    /**
     * Create DataStore with Shared Preference Migration
     * */
    fun getInstance(context: Context) {
//        dataStore = context.createDataStore(name = "uoons")
    }

    fun getInstance(dataStore: DataStore<Preferences>) {
        this.dataStore = dataStore
    }

    /**
     * Store value in DataStore
     *
     * @param
     * key
     * value
     *
     * */

    private suspend inline fun <reified T> storeValue(key: Preferences.Key<T>, value: Any) {
        dataStore!!.edit {
            it[key] = value as T
        }
    }

    /**
     * Read value from DataStore
     *
     * @param
     * key
     *
     * */
    inline fun <reified T> readValue(key: Preferences.Key<T>): T? {

        return runBlocking {
            dataStore!!.data.first()[key]
        }


    }

    /**
     * clear value from DataStore
     */
    fun clearDataStore() {
        runBlocking {
            dataStore!!.edit {

                it.clear()

            }
        }
    }

    /**
     * add string value
     */
    fun addValue(key: Preferences.Key<String>, value: String) {
        runBlocking {
            storeValue(key, value)
        }
    }

    /**
     * add int value
     */
    fun addInt(key: Preferences.Key<Int>, value: Int) {
        runBlocking {
            storeValue(key, value)
        }
    }

    /**
     * add boolean value
     */
    fun addBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        runBlocking {
            storeValue(key, value)
        }
    }

    /**
     * add float value
     */
    fun addFloat(key: Preferences.Key<Float>, value: Float) {
        runBlocking {
            storeValue(key, value)
        }
    }

    /**
     * add long value
     */
    fun addLong(key: Preferences.Key<Long>, value: Long) {
        runBlocking {
            storeValue(key, value)
        }
    }

    /**
     * get string value
     */
    fun getValue(key: Preferences.Key<String>): String {
        val value = readValue(key)
        return if (CommonUtils.isStringNullOrBlank(value)) {
            ""
        } else {
            value!!
        }
    }

    /**
     * get int value
     */
    fun getInt(key: Preferences.Key<Int>): Int {
        return try {
            readValue(key) as Int
        } catch (e: Exception) {
            0
        }
    }

    /**
     * get boolean value
     */
    fun getBoolean(key: Preferences.Key<Boolean>, default: Boolean = false): Boolean {
        return try {
            readValue(key) as Boolean
        } catch (e: Exception) {
            default
        }
    }

    /**
     * get float value
     */
    fun getFloat(key: Preferences.Key<Float>): Float {
        return try {
            readValue(key) as Float
        } catch (e: Exception) {
            0f
        }
    }

    /**
     * get long value
     */
    fun getLong(key: Preferences.Key<Long>): Long {
        return try {
            readValue(key) as Long
        } catch (e: Exception) {
            0
        }
    }


    open fun putByGSON(key: Preferences.Key<String>, `object`: Any?) {
        val gson = Gson()
        val json = gson.toJson(`object`)
        addValue(key, json)
    }

    fun <T> put(`object`: T, key: Preferences.Key<String>) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)

        addValue(key, jsonString)
    }


    inline fun <reified T> get(key: Preferences.Key<String>): T? {
        //We read JSON String which was saved.
        val value = getValue(key)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    fun getArrayList(key: Preferences.Key<String>): ArrayList<Any>? {
        val gson = Gson()
        val json: String? = getValue(key)
        val type = object : TypeToken<ArrayList<Any?>?>() {}.type
        return gson.fromJson<ArrayList<Any>>(json, type)
    }


    fun clearSharedPreference(){
        clearDataStore()
    }





    /*private const val prefName = "ridepro_student"
    var sharedPreferences: SharedPreferences? = null

    fun getInstance(context: Context) {
        sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }
    fun addValue(preferencesKey: PreferenceKeys, value: String) {
        val editor = sharedPreferences!!.edit()
        editor.putString(preferencesKey.toString(), value)
        editor.apply()
    }

    fun addBoolean(preferencesKey: PreferenceKeys, value: Boolean) {
        val editor = sharedPreferences!!.edit()
        editor.putBoolean(preferencesKey.toString(), value)
        editor.apply()
    }


    fun addInt(preferencesKey: PreferenceKeys, value: Int) {
        val editor = sharedPreferences!!.edit()
        editor.putInt(preferencesKey.toString(), value)
        editor.apply()
    }


    fun getValue(key: PreferenceKeys): String? {
        return sharedPreferences!!.getString(key.toString(), "")
    }


    fun getInt(key: PreferenceKeys): Int {
        return sharedPreferences!!.getInt(key.toString(), 0)
    }

    fun getBoolean(key: PreferenceKeys, default: Boolean = false): Boolean {
        return sharedPreferences!!.getBoolean(key.toString(), default)
    }

    fun clearSharedPreference() {
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.apply()
    }


    open fun putByGSON(preferencesKey: PreferenceKeys?, `object`: Any?) {
        val editor = sharedPreferences!!.edit()
        val gson = Gson()
        val json = gson.toJson(`object`)
        editor.putString(preferencesKey.toString(), json)
        editor.commit()
        editor.apply()

    }

    fun getArrayList(key: PreferenceKeys?): ArrayList<Any>? {
        val gson = Gson()
        val json: String? = sharedPreferences!!.getString(key.toString(), null)
        val type = object : TypeToken<ArrayList<Any?>?>() {}.type
        return gson.fromJson<ArrayList<Any>>(json, type)
    }

    *//**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **//*
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        sharedPreferences!!.edit().putString(key, jsonString).apply()
    }

    *//**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **//*
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = sharedPreferences!!.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type “T” is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }*/
}

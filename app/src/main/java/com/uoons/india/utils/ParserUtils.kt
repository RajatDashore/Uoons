package com.uoons.india.utils

import org.json.JSONArray
import org.json.JSONObject
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
object ParserUtils {

    fun getErrorMsgAndData(e : retrofit2.HttpException) : Pair<String, JSONObject?> {
        val jObject = JSONObject(e.response()?.errorBody()?.string()!!)
        val msg = getErrorMsg(jObject)
        val errorData = getErrorData(jObject)
        return Pair(first = msg, second = errorData)
    }

    private fun getErrorMsg(jObject : JSONObject) : String {
        try {
            if (jObject.has("error")) {
                val error = jObject.get("error")
                val msg = getJsonErrorBody(error)
                if(msg.isEmpty()) {
                    return jObject.optString("message")
                }
                return getJsonErrorBody(error)
            } else {
                return jObject.optString("message")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getErrorData(jObject : JSONObject) : JSONObject? {
        try {
            return jObject.optJSONObject("data")
        }catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    private fun getJsonErrorBody(error: Any): String {
        try {

            if (error is JSONObject) {
                var message = ""

                if (error.has("message")) {
                    val msgObj = error.get("message")
                    if (msgObj is JSONObject) {
                        message = msgObj.optString("message")
                    }else if (msgObj is String){
                        message = msgObj
                    }
                }
                return message
            } else if (error is JSONArray) {

                try {
                    return error.getJSONObject(0).optString("message")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        } catch (je: Exception) {
            je.printStackTrace()
        }

        return ""
    }

}
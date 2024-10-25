package com.uoons.india.utils

import android.util.Log
import org.lsposed.lsparanoid.Obfuscate


@Obfuscate
object Logger {

    private val YES_ENABLED = true
    private val NOT_ENABLED = false
    private val LOG_STATUS = YES_ENABLED
    private const val LOG_PREFIX = " :-> "
    const val LOG_CAT = "Project Name"
    const val EXCEPTION_MSG = "Some Exception while printing log :->"

    fun e(prefixP: String, textToLog: String) {
        try {
            val prefix = prefixP + LOG_PREFIX
            Log.e(LOG_CAT, prefix + textToLog)
        } catch (e: Exception) {
            Log.e(LOG_CAT, EXCEPTION_MSG + e.toString())
        }

    }

    fun v(prefixP: String, textToLog: String) {
        try {
            val prefix = prefixP + LOG_PREFIX
            Log.v(LOG_CAT, prefix + textToLog)
        } catch (e: Exception) {
            Log.v(LOG_CAT, EXCEPTION_MSG + e.toString())
        }

    }


    fun d(prefixP: String, textToLog: String) {
        try {
            val prefix = prefixP + LOG_PREFIX
            Log.d(LOG_CAT, prefix + textToLog)
        } catch (e: Exception) {
            Log.d(LOG_CAT, EXCEPTION_MSG + e.toString())
        }

    }


    fun i(prefixP: String, textToLog: String) {
        try {
            val prefix = prefixP + LOG_PREFIX
//            Log.i(LOG_CAT, prefix + textToLog)
        } catch (e: Exception) {
//            Log.i(LOG_CAT, EXCEPTION_MSG + e.toString())
        }

    }


    fun w(prefixP: String, textToLog: String) {

        try {

            val prefix = prefixP + LOG_PREFIX
            Log.w(LOG_CAT, prefix + textToLog)

        } catch (e: Exception) {

            Log.w(LOG_CAT, EXCEPTION_MSG + e.toString())

        }

    }
}

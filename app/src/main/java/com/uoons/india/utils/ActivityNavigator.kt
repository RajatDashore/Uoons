package com.uoons.india.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
object ActivityNavigator
{
    fun startActivity(mContext: Context, cls: Class<*>) {
        val mainIntent = Intent(mContext, cls)
        mContext.startActivity(mainIntent)

    }

    fun startActivityAndFinish(mContext: Context, cls: Class<*>) {
        val mainIntent = Intent(mContext, cls)
         mContext.startActivity(mainIntent)
    }

    fun startActivity(mContext: Context, cls: Class<*>, bundle: Bundle, requestCode: Int) {
        val mainIntent = Intent(mContext, cls)
        mainIntent.putExtras(bundle)
        (mContext as Activity).startActivityForResult(mainIntent, requestCode)

    }

    fun startActivity(mContext: Context, cls: Class<*>, requestCode: Int) {
        val mainIntent = Intent(mContext, cls)
        (mContext as Activity).startActivityForResult(mainIntent, requestCode)
    }

    fun startActivity(mContext: Context, cls: Class<*>, bundle: Bundle) {
        val mainIntent = Intent(mContext, cls)
        mainIntent.putExtras(bundle)
        mContext.startActivity(mainIntent, bundle)
    }

    fun startActivityForResult2(fragment: Fragment, cls: Class<*>, requestCode: Int) {
        val mainIntent = Intent(fragment.requireContext(), cls)
        fragment.startActivityForResult(mainIntent, requestCode)
    }

    fun startActivityForResult(mContext: Context, cls: Class<*>, bundle: Bundle? = null, requestCode: Int) {
        val mainIntent = Intent(mContext, cls)
        if(bundle != null) {
            mainIntent.putExtras(bundle)
        }
        (mContext as Activity).startActivityForResult(mainIntent, requestCode)
    }

    fun clearAllActivity(activity: Activity, tClass: Class<*>) {
        val intent = Intent(activity, tClass)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
    }

    fun clearAllActivityWithData(activity: Activity, tClass: Class<*>, bundle: Bundle) {
        val intent = Intent(activity, tClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtras(bundle)
        activity.startActivity(intent)
    }

    fun startActivityWithDataClearTop(mContext: Context, cls: Class<*>, bundle: Bundle) {
        val mainIntent = Intent(mContext, cls)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        mainIntent.putExtras(bundle)
        mContext.startActivity(mainIntent)
    }

    fun startActivityClearTop(mContext: Context, cls: Class<*>) {
        val mainIntent = Intent(mContext, cls)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        mContext.startActivity(mainIntent)
    }

    fun startActivity(mContext: Context, fragment: Fragment, cls: Class<*>, requestCode: Int, bundle: Bundle) {
        val mainIntent = Intent(mContext, cls)
        mainIntent.putExtras(bundle)
        fragment.startActivityForResult(mainIntent, requestCode)
    }

    fun setOkResult(mContext: Context) {
        val output = Intent()
        (mContext as Activity).setResult(Activity.RESULT_OK, output)
    }

    fun setOkResult(mContext: Context, bn: Bundle) {
        val output = Intent()
        output.putExtras(bn)
        (mContext as Activity).setResult(Activity.RESULT_OK, output)
    }

    fun setOkResult(mContext: Context, resultCode: Int) {
        val output = Intent()
        (mContext as Activity).setResult(resultCode, output)
    }

    fun startActivityfinish(mContext: Activity, cls: Class<*>) {
        val mainIntent = Intent(mContext, cls)
        mContext.startActivity(mainIntent)
    }

    fun startActivity(mContext: Activity, cls: Class<*>,bundle: Bundle? = null, resultLauncher: ActivityResultLauncher<Intent>){
        val mainIntent = Intent(mContext, cls)
        if(bundle != null) {
            mainIntent.putExtras(bundle)
        }
        resultLauncher.launch(mainIntent)
    }
}
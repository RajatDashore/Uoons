package com.uoons.india.push_notification

import android.content.ContentValues
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
object PushNotification {

    fun FirebaseMessaging(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.e("HomeFragment","FirebaseMessaging:- "+token)
        })
    }

}
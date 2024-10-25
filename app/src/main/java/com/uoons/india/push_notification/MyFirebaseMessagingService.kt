package com.uoons.india.push_notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uoons.india.R
import com.uoons.india.ui.splash.SplashActivity
import com.uoons.india.utils.AppConstants
import org.lsposed.lsparanoid.Obfuscate
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException


@Obfuscate
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // TODO(developer): Handle FCM messages here.
        Log.d("MyFirebaseMessagingService", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MyFirebaseMessagingService", "Message data payload: ${remoteMessage.data}")

            Log.e("MyFirebaseMessagingService", "MyFirebaseMessagingService:- $remoteMessage")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            showNotification(remoteMessage.data)
        }

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification(notification: Map<String, String>) {

        val messagesType = notification[AppConstants.Type]
        val messageTitle = notification[AppConstants.Title]
        val messageBody = notification[AppConstants.Body]
        val messagesId = notification[AppConstants.Id]
        val messagesName = notification[AppConstants.Name]
        val messagesImage = notification[AppConstants.Image]

        var bmp2: Bitmap? = null
        try {
            val in2 = URI(messagesImage).toURL().openStream()
            bmp2 = BitmapFactory.decodeStream(in2)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        val style2 = NotificationCompat.BigPictureStyle()
            .bigPicture(bmp2)
        val builder2: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "MyNotifications")
                .setSmallIcon(R.drawable.new_logo_uoons_name)
                .setStyle(style2)
                .setLargeIcon(bmp2)
                .setContentTitle(messageTitle)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true) //.addAction(0,"OPEN", pendingNotificationIntent)
                .setAutoCancel(true)
                .setContentText(messageBody)
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH) //must give priority to High, Max which will considered as heads-up notification
                .setDefaults(Notification.DEFAULT_SOUND) // must requires VIBRATE permission;

        // I set the notification priority to max, but it is still at the bottom of the notification area.

        val notificationIntent = Intent(this@MyFirebaseMessagingService, SplashActivity::class.java)
        notificationIntent.putExtra(AppConstants.Type, messagesType)
        notificationIntent.putExtra(AppConstants.Title, messageTitle)
        notificationIntent.putExtra(AppConstants.Body, messageTitle)
        notificationIntent.putExtra(AppConstants.Id, messagesId)
        notificationIntent.putExtra(AppConstants.Name, messagesName)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingNotificationIntent = PendingIntent.getActivity(this@MyFirebaseMessagingService, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val manager = NotificationManagerCompat.from(this)
        builder2.setContentIntent(pendingNotificationIntent)
        manager.notify(0, builder2.build())

   /*     val pushPendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder2.setContentIntent(pushPendingIntent)*/

        /*val notifyMgr = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notifyMgr.notify(count, mBuilder.build())*/
    }

   /* @SuppressLint("RemoteViewLayout")
    private fun getCustomDesign(title: String, message: String): RemoteViews? {
        val remoteViews = RemoteViews(
            applicationContext.packageName,
            R.layout.notification_design
        )
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(R.id.icon, R.drawable.new_logo_uoons_name)
        return remoteViews
    }*/



}
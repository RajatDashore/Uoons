package com.uoons.india.push_notification

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.uoons.india.BR
import com.uoons.india.R
import com.uoons.india.databinding.ActivityNotificationServiceBinding
import com.uoons.india.ui.base.BaseActivity
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
class NotificationServiceActivity : BaseActivity<ActivityNotificationServiceBinding, NotificationServiceActivityVM>(), NotificationServiceActivityNavigator {

    override val bindingVariable: Int = BR.notificationServiceActivityVM
    override val layoutId: Int = R.layout.activity_notification_service
    override val viewModelClass: Class<NotificationServiceActivityVM> = NotificationServiceActivityVM::class.java

    override  fun init() {
        mViewModel.navigator = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val title = intent.getStringExtra("title")
        val notificationDetails = intent.getStringExtra("notificationDetails")

        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        Log.e("NotificationServiceActivity", "imageUri:- $imageUri")
        init()
     /*   viewDataBinding?.txvTitleName?.text = title
        viewDataBinding?.txvBody?.text = notificationDetails
        viewDataBinding?.ivProductImage?.let { Glide.with(this).load(imageUri).into(it) }*/

    }
}
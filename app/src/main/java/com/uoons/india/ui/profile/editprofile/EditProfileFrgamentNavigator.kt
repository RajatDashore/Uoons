package com.uoons.india.ui.profile.editprofile

import com.uoons.india.ui.base.CommonNavigator
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
interface EditProfileFrgamentNavigator : CommonNavigator {
    fun saveUserData()
    fun saveUserDetailResponse()
    fun opneGalleryCamera()
    fun saveUserProfileImageResponse()
    fun getUserDetailsData()
}

package com.uoons.india.ui.profile.editprofile.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ProfileImageResponseDataModel(@SerializedName("file_name"      ) var fileName     : String?  = null,
                                         @SerializedName("file_type"      ) var fileType     : String?  = null,
                                         @SerializedName("file_path"      ) var filePath     : String?  = null,
                                         @SerializedName("full_path"      ) var fullPath     : String?  = null,
                                         @SerializedName("raw_name"       ) var rawName      : String?  = null,
                                         @SerializedName("orig_name"      ) var origName     : String?  = null,
                                         @SerializedName("client_name"    ) var clientName   : String?  = null,
                                         @SerializedName("file_ext"       ) var fileExt      : String?  = null,
                                         @SerializedName("file_size"      ) var fileSize     : Double?  = null,
                                         @SerializedName("is_image"       ) var isImage      : Boolean? = null,
                                         @SerializedName("image_width"    ) var imageWidth   : Int?     = null,
                                         @SerializedName("image_height"   ) var imageHeight  : Int?     = null,
                                         @SerializedName("image_type"     ) var imageType    : String?  = null,
                                         @SerializedName("image_size_str" ) var imageSizeStr : String?  = null)
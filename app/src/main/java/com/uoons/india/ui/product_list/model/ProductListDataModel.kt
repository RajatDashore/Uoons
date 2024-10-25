package com.uoons.india.ui.product_list.model

import com.google.gson.annotations.SerializedName
import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
data class ProductListDataModel (@SerializedName("items"          ) var items         : ArrayList<ProductListModel>         = arrayListOf(),
                                 @SerializedName("total_page"     ) var totalPage     : Int?                     = null,
                                 @SerializedName("total_items"    ) var totalItems    : Int?                     = null,
                                 @SerializedName("sub_categories" ) var subCategories : ArrayList<SubCategoriesModel> = arrayListOf(),
                                 @SerializedName("filter"         ) var filter        : ArrayList<FilterModel>        = arrayListOf(),
                                 @SerializedName("sort_by_name"   ) var sortByName    : ArrayList<SortByNameModel>    = arrayListOf())
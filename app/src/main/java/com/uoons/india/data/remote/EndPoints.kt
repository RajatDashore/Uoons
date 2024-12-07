package com.uoons.india.data.remote

import org.lsposed.lsparanoid.Obfuscate

@Obfuscate
object EndPoints {
    const val JWELLARY = "api/getAllProductsByCategory?cat_id=445"
    const val FAKEIMAGEURL = "search?limit=10"
    const val SEND_OTP = "api/sendOTP"
    const val FORCE_UPDATE = "api/forceUpdate"
    const val OTP_VERIFICATION = "api/otpVerification"
    const val SAVE_USER_DETAILS = "api/saveUserDetails"
    const val SAVE_BANK_DETAILS = "api/saveBankDetails"
    const val FETCH_BANK_DETAILS = "api/getBankDetails"
    const val GET_USER_DETAILS = "api/getUserDetails"
    const val DELETE_BANK_DETAILS = "api/deleteBankDetails"
    const val SELECTED_PRIMARY_ACCOUNT = "api/selectPrimaryBank"
    const val DESHBOARD_DATA = "api/homepageData"
    const val DESHBOARD_MORE_PRODUCTS = "api/dashboardAllProducts"
    const val HOME_PAGE_ITEMS_DATA = "api/getHomepageItemsData"
    const val GET_ALL_CATEGORIES =
        "api/getAllCategories?offset=0"// this is only API working for data
    const val SEARCH_ALL_PRODUCTS = "api/searchAllProducts"
    const val ADD_RECENTLY_VIEW_PRODUCT = "api/addRecentyViewedProduct"
    const val PRODUCT_DETAIL = "api/productDetail"
    const val ADD_ITEM_TO_CART = "api/addItemToCart"
    const val GET_MY_CART_ITEMS = "api/getMyCart"
    const val GET_ALL_QUESTION_ANSWERS = "api/fetchAllQuestionAnswers"
    const val SEARCH_QUESTIONS = "api/askQuestion"
    const val LIKE_UNLIKE_QUESTION = "api/likeUnlikeQuestion"
    const val LIKE_UNLIKE_REVIEW = "api/orderLikeUnlike"
    const val POST_YOUR_QUESTION = "api/postYourQuestion"
    const val CANCEL_ORDER = "api/cancelOrder"
    const val ORDERCANCEL_REASON = "api/order-cancel-reason"
    const val GET_FILTER_OPTION = "api/getFilterOptions"
    const val ADD_WISH_LIST = "api/addItemToWishlist"
    const val GET_WISH_LIST = "api/getMyWishlist"
    const val GET_SORTING_OPTIONS = "api/getSortingOptions"
    const val GET_FAQ = "api/faq"
    const val REMOVE_CART_ITEM = "api/removeCart"
    const val REMOVE_WISH_LIST = "api/removeWishlist"
    const val CONTACT_US = "api/contactUs"
    const val PROFILE_IMAGE_SAVE = "api/profileImageUpdate"
    const val GET_USER_DELIVER_ADDRESS = "api/getUserAddress"
    const val INSERT_USER_DELIVER_ADDRESS = "api/insertUserAddress"
    const val UPDATE_USER_DELIVER_ADDRESS = "api/updateUserAddress"
    const val DELETE_DELIVER_ADDRESS = "api/deleteUserAddress"
    const val PRODUCT_LOCATION_AVAILABILITY = "api/productLocationAvailability"
    const val CHECK_COUPEN = "api/checkCoupon"
    const val CHECKOUT = "api/checkout"
    const val UPDATE_CHECKOUT = "api/updateCheckout"
    const val SUGGESTION_TO_ENHANCE = "api/suggestToEnhance"
    const val POST_SUGGESTION = "api/postSuggestion"
    const val FETCH_ALL_BUNDLE_ORDERS = "api/bundleOrders"
    const val ODERS_OF_BUNDLES = "api/ordersOfBundle"
    const val FETCH_ORDER = "api/fetch_order"
    const val ADD_RATING_REVIEW = "api/addOrderReviewRating"
    const val RAZORPAY_CHECKOUT = "api/update-checkout-razorpay"

    const val SOCKET = "socket"
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "account/forgot-password"

    // Dynamic link generate in shear product link on other app.
    const val DYNAMIC_LINK = "https://uoons.page.link"

}


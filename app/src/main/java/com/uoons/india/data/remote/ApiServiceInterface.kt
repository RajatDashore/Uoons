package com.uoons.india.data.remote


import com.uoons.india.data.remote.EndPoints.ADD_ITEM_TO_CART
import com.uoons.india.data.remote.EndPoints.ADD_RATING_REVIEW
import com.uoons.india.data.remote.EndPoints.ADD_RECENTLY_VIEW_PRODUCT
import com.uoons.india.data.remote.EndPoints.ADD_WISH_LIST
import com.uoons.india.data.remote.EndPoints.CANCEL_ORDER
import com.uoons.india.data.remote.EndPoints.CHECKOUT
import com.uoons.india.data.remote.EndPoints.CHECK_COUPEN
import com.uoons.india.data.remote.EndPoints.CONTACT_US
import com.uoons.india.data.remote.EndPoints.DELETE_BANK_DETAILS
import com.uoons.india.data.remote.EndPoints.DELETE_DELIVER_ADDRESS
import com.uoons.india.data.remote.EndPoints.DESHBOARD_DATA
import com.uoons.india.data.remote.EndPoints.DESHBOARD_MORE_PRODUCTS
import com.uoons.india.data.remote.EndPoints.FETCH_ALL_BUNDLE_ORDERS
import com.uoons.india.data.remote.EndPoints.FETCH_BANK_DETAILS
import com.uoons.india.data.remote.EndPoints.FETCH_ORDER
import com.uoons.india.data.remote.EndPoints.FORCE_UPDATE
import com.uoons.india.data.remote.EndPoints.GET_ALL_CATEGORIES
import com.uoons.india.data.remote.EndPoints.GET_ALL_QUESTION_ANSWERS
import com.uoons.india.data.remote.EndPoints.GET_FAQ
import com.uoons.india.data.remote.EndPoints.GET_MY_CART_ITEMS
import com.uoons.india.data.remote.EndPoints.GET_SORTING_OPTIONS
import com.uoons.india.data.remote.EndPoints.GET_USER_DELIVER_ADDRESS
import com.uoons.india.data.remote.EndPoints.GET_USER_DETAILS
import com.uoons.india.data.remote.EndPoints.GET_WISH_LIST
import com.uoons.india.data.remote.EndPoints.HOME_PAGE_ITEMS_DATA
import com.uoons.india.data.remote.EndPoints.INSERT_USER_DELIVER_ADDRESS
import com.uoons.india.data.remote.EndPoints.JWELLARY
import com.uoons.india.data.remote.EndPoints.LIKE_UNLIKE_QUESTION
import com.uoons.india.data.remote.EndPoints.LIKE_UNLIKE_REVIEW
import com.uoons.india.data.remote.EndPoints.ODERS_OF_BUNDLES
import com.uoons.india.data.remote.EndPoints.ORDERCANCEL_REASON
import com.uoons.india.data.remote.EndPoints.OTP_VERIFICATION
import com.uoons.india.data.remote.EndPoints.POST_SUGGESTION
import com.uoons.india.data.remote.EndPoints.POST_YOUR_QUESTION
import com.uoons.india.data.remote.EndPoints.PRODUCT_DETAIL
import com.uoons.india.data.remote.EndPoints.PRODUCT_LOCATION_AVAILABILITY
import com.uoons.india.data.remote.EndPoints.PROFILE_IMAGE_SAVE
import com.uoons.india.data.remote.EndPoints.RAZORPAY_CHECKOUT
import com.uoons.india.data.remote.EndPoints.REMOVE_CART_ITEM
import com.uoons.india.data.remote.EndPoints.REMOVE_WISH_LIST
import com.uoons.india.data.remote.EndPoints.SAVE_BANK_DETAILS
import com.uoons.india.data.remote.EndPoints.SAVE_USER_DETAILS
import com.uoons.india.data.remote.EndPoints.SEARCH_ALL_PRODUCTS
import com.uoons.india.data.remote.EndPoints.SEARCH_QUESTIONS
import com.uoons.india.data.remote.EndPoints.SELECTED_PRIMARY_ACCOUNT
import com.uoons.india.data.remote.EndPoints.SEND_OTP
import com.uoons.india.data.remote.EndPoints.SUGGESTION_TO_ENHANCE
import com.uoons.india.data.remote.EndPoints.UPDATE_CHECKOUT
import com.uoons.india.data.remote.EndPoints.UPDATE_USER_DELIVER_ADDRESS
import com.uoons.india.ui.bank.model.FetchBankDetailsModel
import com.uoons.india.ui.bank.model.SaveBankDetailsModel
import com.uoons.india.ui.category.category_items.category_items_details.model.AddItemToCartDataResponse
import com.uoons.india.ui.category.category_items.category_items_details.model.ProductAvailabilityResponseModel
import com.uoons.india.ui.category.category_items.model.SubCategoriesModel
import com.uoons.india.ui.category.model.AllCategoryModel
import com.uoons.india.ui.checkout.checkout_address.model.DeliverAllAddressModel
import com.uoons.india.ui.checkout.checkout_payment.model.CheckOutModel
import com.uoons.india.ui.checkout.checkout_payment.model.TokenModel
import com.uoons.india.ui.checkout.checkout_payment.model.online_payment_status.OnlinePaymentStatusModel
import com.uoons.india.ui.help.view_pager_fragment.model.ContactUsModel
import com.uoons.india.ui.help.view_pager_fragment.model.FAQDataModel
import com.uoons.india.ui.home.fragment.model.AddRecentlyViewModel
import com.uoons.india.ui.home.fragment.model.DeshBoardModel
import com.uoons.india.ui.home.fragment.more_products.model.HomePageMoreItemsDataModel
import com.uoons.india.ui.login_module.login_mobile_no.model.LogingResponseModel
import com.uoons.india.ui.login_module.otp_verification.model.OTPResponseModel
import com.uoons.india.ui.my_cart.coupen_code.CoupenCodeModel
import com.uoons.india.ui.my_cart.model.AddQuantityModelResponse
import com.uoons.india.ui.my_cart.model.GetMyCartDataModel
import com.uoons.india.ui.my_cart.model.RemoveCartItemResponse
import com.uoons.india.ui.order.model.FecthAllBundleOrderModel
import com.uoons.india.ui.order.order_details.model.OrderCancelModel
import com.uoons.india.ui.order.order_details.model.OrderDetailModel
import com.uoons.india.ui.order.order_list.model.OrderBundleListModel
import com.uoons.india.ui.order.order_review_rating.RatingAndReviewModel
import com.uoons.india.ui.product_detail.model.ProductDetailsModel
import com.uoons.india.ui.product_detail.model.QuestionLikeUnlikeModel
import com.uoons.india.ui.product_detail.quetion_and_answer.model.QuestionAnswerModel
import com.uoons.india.ui.product_detail.suggestion_for_enhance.model.PostSuggestionResponse
import com.uoons.india.ui.product_detail.suggestion_for_enhance.model.SuggestionToEnhanceModel
import com.uoons.india.ui.product_list.model.ProductModel
import com.uoons.india.ui.profile.editprofile.model.ProfileImageModel
import com.uoons.india.ui.profile.editprofile.model.SaveUserDetailsResponse
import com.uoons.india.ui.profile.model.UserDetailsModel
import com.uoons.india.ui.question_answers.post_question.model.PostYourQuestionModel
import com.uoons.india.ui.sorting.model.SortingModel
import com.uoons.india.ui.splash.model.ForceUpdatedModel
import com.uoons.india.ui.wishlist.model.AddWishListResponseModel
import com.uoons.india.ui.wishlist.model.GetWishListDataModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.lsposed.lsparanoid.Obfuscate
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

@Obfuscate
interface ApiServiceInterface {

    @GET(JWELLARY)
    suspend fun JwellaryData(
        @Header("Channel-Code") ChannelCode: String
    ): DeshBoardModel

    @POST(SEND_OTP)
    suspend fun login(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): LogingResponseModel


    @GET(FORCE_UPDATE)
    suspend fun checkAPKUpdatedVersion(
        @Header("Channel-Code") ChannelCode: String,
        @Query("versionCode") versionCode: String,
    ): ForceUpdatedModel

    @POST(OTP_VERIFICATION)
    suspend fun otp_Verification(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): OTPResponseModel

    @POST(SAVE_USER_DETAILS)
    suspend fun saveUserDetails(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): SaveUserDetailsResponse

    @POST(SAVE_BANK_DETAILS)
    suspend fun saveBankDetails(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): SaveBankDetailsModel

    @GET(FETCH_BANK_DETAILS)
    suspend fun fetchBankDetails(@Header("Channel-Code") ChannelCode: String): FetchBankDetailsModel

    @POST(DELETE_BANK_DETAILS)
    suspend fun deleteBankDetails(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): FetchBankDetailsModel

    @POST(SELECTED_PRIMARY_ACCOUNT)
    suspend fun selectPrimaryAccount(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): FetchBankDetailsModel

    @GET(GET_USER_DETAILS)
    suspend fun getUserDetails(@Header("Channel-Code") ChannelCode: String): UserDetailsModel

    @GET(DESHBOARD_DATA)
    suspend fun getDeshBoardData(@Header("Channel-Code") ChannelCode: String): DeshBoardModel

    @GET(DESHBOARD_MORE_PRODUCTS)
    suspend fun getMoreDeshBoardProducts(
        @Header("Channel-Code") ChannelCode: String,
        @Query("page") page: Int,
    ): HomePageMoreItemsDataModel

    @POST(HOME_PAGE_ITEMS_DATA)
    suspend fun getHomepageItemsData(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): ProductModel

    @POST(ADD_RECENTLY_VIEW_PRODUCT)
    suspend fun addRecentlyView(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): AddRecentlyViewModel

    @POST(HOME_PAGE_ITEMS_DATA)
    suspend fun getFilterItemsData(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): ProductModel

    @GET(GET_ALL_CATEGORIES)
    suspend fun getAllCategories(@Header("Channel-Code") ChannelCode: String): AllCategoryModel

    @GET(SEARCH_ALL_PRODUCTS)
    suspend fun getAllSubCategories(
        @Header("Channel-Code") ChannelCode: String, @Query("sortById") sortById: Int,
        @Query("catId") catId: Int, @Query("filters") filters: String,
    ): SubCategoriesModel

    @GET(PRODUCT_DETAIL)
    suspend fun getSingleProductDetail(
        @Header("Channel-Code") ChannelCode: String,
        @Query("pid") pid: Int,
    ): ProductDetailsModel

    @GET(GET_ALL_QUESTION_ANSWERS)
    suspend fun getAllQuestionsAnswers(
        @Header("Channel-Code") ChannelCode: String,
        @Query("pid") pid: Int,
    ): QuestionAnswerModel

    @POST(SEARCH_QUESTIONS)
    suspend fun searchQuestionAnswers(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): QuestionAnswerModel

    @POST(LIKE_UNLIKE_QUESTION)
    suspend fun postlikeUnlikeQuestion(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): QuestionLikeUnlikeModel

    @POST(LIKE_UNLIKE_REVIEW)
    suspend fun postlikeUnlikeReview(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): QuestionLikeUnlikeModel

    @GET(SUGGESTION_TO_ENHANCE)
    suspend fun getSuggestionToEnhance(@Header("Channel-Code") ChannelCode: String): SuggestionToEnhanceModel
    //PostSuggestionModel

    @POST(POST_SUGGESTION)
    suspend fun postSuggestion(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): PostSuggestionResponse

    @POST(POST_YOUR_QUESTION)
    suspend fun postYourQuestion(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): PostYourQuestionModel

    @POST(CANCEL_ORDER)
    suspend fun orderCancel(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): OrderCancelModel

    @POST(ORDERCANCEL_REASON)
    suspend fun orderCancel_reason(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): OrderCancelModel

    @POST(ADD_ITEM_TO_CART)
    suspend fun addItemToCart(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): AddItemToCartDataResponse

    @POST(ADD_ITEM_TO_CART)
    suspend fun addQuantityToCart(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): AddQuantityModelResponse

    @GET(GET_MY_CART_ITEMS)
    suspend fun getMyCartItems(@Header("Channel-Code") ChannelCode: String): GetMyCartDataModel

    @GET(SEARCH_ALL_PRODUCTS)
    suspend fun getSearchItems(
        @Header("Channel-Code") ChannelCode: String,
        @Query("searchKey") searchKey: String,
    ): SubCategoriesModel

    @POST(ADD_WISH_LIST)
    suspend fun addToWishList(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): AddWishListResponseModel

    @GET(GET_WISH_LIST)
    suspend fun getWishList(@Header("Channel-Code") ChannelCode: String): GetWishListDataModel

    @GET(GET_SORTING_OPTIONS)
    suspend fun getSortingOptions(@Header("Channel-Code") ChannelCode: String): SortingModel

    @GET(GET_FAQ)
    suspend fun getFAQ(@Header("Channel-Code") ChannelCode: String): FAQDataModel

    @GET(SEARCH_ALL_PRODUCTS)
    suspend fun getSortingData(
        @Header("Channel-Code") ChannelCode: String,
        @Query("sortById") sortById: Int,
        @Query("filters") filters: String,
    ): SubCategoriesModel

    @POST(REMOVE_CART_ITEM)
    suspend fun removeCartItem(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): RemoveCartItemResponse

    @POST(REMOVE_WISH_LIST)
    suspend fun removeWishLitItem(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): RemoveCartItemResponse

    @POST(CONTACT_US)
    suspend fun contactUs(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): ContactUsModel

    @Multipart
    @POST(PROFILE_IMAGE_SAVE)
    suspend fun profileImageSave(
        @Header("Channel-Code") ChannelCode: String,
        @Part profile_image: MultipartBody.Part,
    ): ProfileImageModel

    @GET(GET_USER_DELIVER_ADDRESS)
    suspend fun getUserDeliverAddress(@Header("Channel-Code") ChannelCode: String): DeliverAllAddressModel

    @POST(INSERT_USER_DELIVER_ADDRESS)
    suspend fun insertNewDeliverAddress(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): DeliverAllAddressModel


    @POST(UPDATE_USER_DELIVER_ADDRESS)
    suspend fun updateNewDeliverAddress(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): DeliverAllAddressModel


    @POST(DELETE_DELIVER_ADDRESS)
    suspend fun deleteDeliverAddress(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): DeliverAllAddressModel

    @GET(PRODUCT_LOCATION_AVAILABILITY)
    suspend fun checkProductLocationAvailable(
        @Header("Channel-Code") ChannelCode: String,
        @Query("pincode") pinCode: Int,
        @Query("pid") pId: Int,
    ): ProductAvailabilityResponseModel

    @POST(CHECK_COUPEN)
    suspend fun checkCoupenCode(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): CoupenCodeModel

    @POST(CHECKOUT)
    suspend fun checkOutProduct(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): CheckOutModel

    @POST(UPDATE_CHECKOUT)
    suspend fun checkOutProductUpdate(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): OnlinePaymentStatusModel

    @POST(RAZORPAY_CHECKOUT)
    suspend fun checkOutRazorpayUpdate(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): OnlinePaymentStatusModel

    @GET(FETCH_ALL_BUNDLE_ORDERS)
    suspend fun getAllFetchBundleOrders(@Header("Channel-Code") ChannelCode: String): FecthAllBundleOrderModel

    @GET(ODERS_OF_BUNDLES)
    suspend fun getOrderBundlesList(
        @Header("Channel-Code") ChannelCode: String,
        @Query("bundle_id") bundleId: String,
    ): OrderBundleListModel

    @GET(FETCH_ORDER)
    suspend fun getOrderList(
        @Header("Channel-Code") ChannelCode: String,
        @Query("order_id") orderId: String,
    ): OrderDetailModel

    @POST(ADD_RATING_REVIEW)
    suspend fun addRatingReview(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): RatingAndReviewModel

    @Multipart
    @POST(ADD_RATING_REVIEW)
    suspend fun uploadReview(
        @Header("Channel-Code") ChannelCode: String,
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part product_image: ArrayList<MultipartBody.Part>,
    ): RatingAndReviewModel

    /*    @Multipart
        @POST(PROFILE_IMAGE_SAVE)
        suspend fun profileImageSave(@Header("Channel-Code") ChannelCode: String,@Part profile_image : MultipartBody.Part): ProfileImageModel*/

    /*    @Multipart
        @POST(ADD_RATING_REVIEW)
        fun uploadReview(@Header("Channel-Code") ChannelCode: String, @PartMap map: HashMap<String, RequestBody>): RatingAndReviewModel*/

    @POST(CHECKOUT)
    suspend fun getToken(
        @Header("Channel-Code") ChannelCode: String,
        @Body body: HashMap<String, Any>,
    ): TokenModel

//    @POST(PROFILE_IMAGE_SAVE)
//    suspend fun profileImageSave(@Header("Authorization") authorizationToken: String, @Body body: HashMap<String, Any>): ProfileImageModel

}
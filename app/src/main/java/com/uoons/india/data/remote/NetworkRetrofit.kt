package com.uoons.india.data.remote

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.uoons.india.BuildConfig
import com.uoons.india.data.local.AppPreference
import com.uoons.india.data.local.PreferenceKeys
import com.uoons.india.data.remote.error.*
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
import com.uoons.india.utils.AppConstants
import com.uoons.india.utils.ParserUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.CertificatePinner
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.lsposed.lsparanoid.Obfuscate
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

@Obfuscate
open class NetworkRetrofit : Repository() {

    private val apiService: ApiServiceInterface

    init {
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(getHttpClient())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        apiService = retrofit.create(ApiServiceInterface::class.java)
    }

    private fun getHttpClient(): OkHttpClient {
             /*val  certPinner = CertificatePinner.Builder()
            .add("uoons.com","sha256/ReEGXY0pnGBAqrASIqBCmOjSELZno6Vv++TuaIekQ9s=")
            .build()*/

        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor { chain ->
            var request = chain.request()
            if (AppPreference.getValue(PreferenceKeys.ACCESS_TOKEN) != "") {
                val requestBuilder = request.newBuilder().addHeader(AppConstants.Auth, AppPreference.getValue(PreferenceKeys.ACCESS_TOKEN))
                request = requestBuilder.build()
            }
            chain.proceed(request)
        }
        httpClientBuilder.connectTimeout(60, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(60, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(60, TimeUnit.SECONDS)
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//        httpClientBuilder.addInterceptor(loggingInterceptor)
           // .certificatePinner(certPinner)
        return httpClientBuilder.build()
    }

     override suspend fun checkAPKUpdatedVersion(
        channelCode: String,
        versionCode: String
    ): Either<Failure, ForceUpdatedModel> {
        return callAPI { apiService.checkAPKUpdatedVersion(channelCode, versionCode) }
    }

    override suspend fun login(channelCode: String, body: HashMap<String, Any>): Either<Failure, LogingResponseModel> {
        return callAPI { apiService.login(channelCode, body) }
    }
    override suspend fun otp_Verification(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, OTPResponseModel> {
        return callAPI { apiService.otp_Verification(channelCode, body) }
    }

    override suspend fun saveUserDetails(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, SaveUserDetailsResponse> {
        return callAPI { apiService.saveUserDetails(channelCode, body) }
    }

    override suspend fun saveBankDetails(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, SaveBankDetailsModel> {
        return callAPI { apiService.saveBankDetails(channelCode, body) }
    }

    override suspend fun fetchBankDetails(channelCode: String): Either<Failure, FetchBankDetailsModel> {
        return callAPI { apiService.fetchBankDetails(channelCode) }
    }

    override suspend fun deleteBankDetails(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, FetchBankDetailsModel> {
        return callAPI { apiService.deleteBankDetails(channelCode, body) }
    }

    override suspend fun selectPrimaryAccount(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, FetchBankDetailsModel> {
        return callAPI { apiService.selectPrimaryAccount(channelCode, body) }
    }

    override suspend fun getUserDetails(channelCode: String): Either<Failure, UserDetailsModel> {
        return callAPI { apiService.getUserDetails(channelCode) }
    }

    override suspend fun getDeshBoardData(channelCode: String): Either<Failure, DeshBoardModel> {
        return callAPI { apiService.getDeshBoardData(channelCode) }
    }

    override suspend fun getMoreDeshBoardProducts(
        channelCode: String,
        page: Int
    ): Either<Failure, HomePageMoreItemsDataModel> {
        return callAPI { apiService.getMoreDeshBoardProducts(channelCode, page) }
    }

    override suspend fun getHomepageItemsData(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, ProductModel> {
        return callAPI { apiService.getHomepageItemsData(channelCode, body) }
    }

    override suspend fun addRecentlyView(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, AddRecentlyViewModel> {
        return callAPI { apiService.addRecentlyView(channelCode, body) }
    }

    override suspend fun getFilterItemsData(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, ProductModel> {
        return callAPI { apiService.getFilterItemsData(channelCode, body) }
    }

    override suspend fun getAllCategories(channelCode: String): Either<Failure, AllCategoryModel> {
        return callAPI { apiService.getAllCategories(channelCode) }
    }

    override suspend fun getAllSubCategories(
        channelCode: String,
        sortById: Int,
        catId: Int,
        filters: String
    ): Either<Failure, SubCategoriesModel> {
        return callAPI { apiService.getAllSubCategories(channelCode, sortById, catId, filters) }
    }

    override suspend fun getSingleProductDetail(
        channelCode: String,
        pid: Int
    ): Either<Failure, ProductDetailsModel> {
        return callAPI { apiService.getSingleProductDetail(channelCode, pid) }
    }

    override suspend fun getAllQuestionsAnswers(
        channelCode: String,
        pid: Int
    ): Either<Failure, QuestionAnswerModel> {
        return callAPI { apiService.getAllQuestionsAnswers(channelCode, pid) }
    }

    override suspend fun searchQuestionAnswers(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, QuestionAnswerModel> {
        return callAPI { apiService.searchQuestionAnswers(channelCode, body) }
    }

    override suspend fun postlikeUnlikeQuestion(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, QuestionLikeUnlikeModel> {
        return callAPI { apiService.postlikeUnlikeQuestion(channelCode, body) }
    }

    override suspend fun postlikeUnlikeReview(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, QuestionLikeUnlikeModel> {
        return callAPI { apiService.postlikeUnlikeReview(channelCode, body) }
    }

    override suspend fun orderCancel(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, OrderCancelModel> {
        return callAPI { apiService.orderCancel(channelCode, body) }
    }

    override  suspend fun ordercancelreason(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, OrderCancelModel> {
        return callAPI { apiService.orderCancel_reason(channelCode, body) }
    }






    override suspend fun postYourQuestion(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, PostYourQuestionModel> {
        return callAPI { apiService.postYourQuestion(channelCode, body) }
    }

    override suspend fun getSuggestionToEnhance(channelCode: String): Either<Failure, SuggestionToEnhanceModel> {
        return callAPI { apiService.getSuggestionToEnhance(channelCode) }
    }

    override suspend fun postSuggestion(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, PostSuggestionResponse> {
        return callAPI { apiService.postSuggestion(channelCode, body) }
    }

    override suspend fun addItemToCart(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, AddItemToCartDataResponse> {
        return callAPI { apiService.addItemToCart(channelCode, body) }
    }

    override suspend fun addQuantityToCart(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, AddQuantityModelResponse> {
        return callAPI { apiService.addQuantityToCart(channelCode, body) }
    }

    override suspend fun getMyCartItems(channelCode: String): Either<Failure, GetMyCartDataModel> {
        return callAPI { apiService.getMyCartItems(channelCode) }
    }

    override suspend fun getSearchItems(
        channelCode: String,
        searchKey: String
    ): Either<Failure, SubCategoriesModel> {
        return callAPI { apiService.getSearchItems(channelCode, searchKey) }
    }

    override suspend fun addToWishList(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, AddWishListResponseModel> {
        return callAPI { apiService.addToWishList(channelCode, body) }
    }

    override suspend fun getWishList(channelCode: String): Either<Failure, GetWishListDataModel> {
        return callAPI { apiService.getWishList(channelCode) }
    }

    override suspend fun getSortingOptions(channelCode: String): Either<Failure, SortingModel> {
        return callAPI { apiService.getSortingOptions(channelCode) }
    }

    override suspend fun getFAQ(channelCode: String): Either<Failure, FAQDataModel> {
        return callAPI { apiService.getFAQ(channelCode) }
    }

    override suspend fun getSortingData(
        channelCode: String,
        sortById: Int,
        filters: String
    ): Either<Failure, SubCategoriesModel> {
        return callAPI { apiService.getSortingData(channelCode, sortById, filters) }
    }

    override suspend fun removeCartItem(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, RemoveCartItemResponse> {
        return callAPI { apiService.removeCartItem(channelCode, body) }
    }

    override suspend fun removeWishLitItem(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, RemoveCartItemResponse> {
        return callAPI { apiService.removeWishLitItem(channelCode, body) }
    }

    override suspend fun contactUs(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, ContactUsModel> {
        return callAPI { apiService.contactUs(channelCode, body) }
    }

    override suspend fun profileImageSave(channelCode: String, body: MultipartBody.Part): Either<Failure, ProfileImageModel> {
        return callAPI { apiService.profileImageSave(channelCode, body) }
    }

    override suspend fun getUserDeliverAddress(channelCode: String): Either<Failure, DeliverAllAddressModel> {
        return callAPI { apiService.getUserDeliverAddress(channelCode) }
    }

    override suspend fun insertNewDeliverAddress(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, DeliverAllAddressModel> {
        return callAPI { apiService.insertNewDeliverAddress(channelCode, body) }
    }

    suspend fun updatdeNewDeliverAddress(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, DeliverAllAddressModel> {
        return callAPI { apiService.updateNewDeliverAddress(channelCode, body) }
    }


    override suspend fun deleteDeliverAddress(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, DeliverAllAddressModel> {
        return callAPI { apiService.deleteDeliverAddress(channelCode, body) }
    }

    override suspend fun checkProductLocationAvailable(
        channelCode: String,
        pinCode: Int,
        pId: Int
    ): Either<Failure, ProductAvailabilityResponseModel> {
        return callAPI { apiService.checkProductLocationAvailable(channelCode, pinCode, pId) }
    }

    override suspend fun checkCoupenCode(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, CoupenCodeModel> {
        return callAPI { apiService.checkCoupenCode(channelCode, body) }
    }

    override suspend fun checkOutProduct(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, CheckOutModel> {
        return callAPI { apiService.checkOutProduct(channelCode, body) }
    }

    override suspend fun checkOutProductUpdate(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, OnlinePaymentStatusModel> {
        return callAPI { apiService.checkOutProductUpdate(channelCode, body) }
    }

      suspend fun checkOutRazorpayUpdate(channelCode: String, body: HashMap<String, Any>): Either<Failure, OnlinePaymentStatusModel> {
        return callAPI { apiService.checkOutRazorpayUpdate(channelCode, body) }
    }


    override suspend fun getToken(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, TokenModel> {
        return callAPI { apiService.getToken(channelCode, body) }
    }


    override suspend fun getAllFetchBundleOrders(channelCode: String): Either<Failure, FecthAllBundleOrderModel> {
        return callAPI { apiService.getAllFetchBundleOrders(channelCode) }
    }

    override suspend fun getOrderBundlesList(
        channelCode: String,
        bundleId: String
    ): Either<Failure, OrderBundleListModel> {
        return callAPI { apiService.getOrderBundlesList(channelCode, bundleId) }
    }

    override suspend fun getOrderList(
        channelCode: String,
        orderId: String
    ): Either<Failure, OrderDetailModel> {
        return callAPI { apiService.getOrderList(channelCode, orderId) }
    }

    override suspend fun addRatingReview(
        channelCode: String,
        body: HashMap<String, Any>
    ): Either<Failure, RatingAndReviewModel> {
        return callAPI { apiService.addRatingReview(channelCode, body) }
    }

    override suspend fun uploadReview(
        channelCode: String, partMap: HashMap<String, RequestBody>,
        product_image: ArrayList<MultipartBody.Part>
    ): Either<Failure, RatingAndReviewModel> {
        return callAPI { apiService.uploadReview(channelCode, partMap, product_image) }
    }

    /* suspend fun forgotPassword(body: HashMap<String, Any>): Either<Failure, BaseResponse<Any>> {
         return callAPI { apiService.forgotPassword(body) }
     }

      suspend fun makeSocketConnection(): Either<Failure, BaseResponse<SocketConnectionResponse>> {
         return callAPI { apiService.makeSocketConnection() }
     }*/


    private suspend fun <T> callAPI(apiCallingMethod: suspend () -> T): Either<Failure, T> {
        return withContext(Dispatchers.IO) {
            try {
                Right(apiCallingMethod.invoke())
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is HttpException) {
//                    Log.e("=========@@", e.message())
                    try {
                        val errorMsgAndData = ParserUtils.getErrorMsgAndData(e)
                        Left(
                            ServerError(
                                data = errorMsgAndData.second,
                                statusCode = e.code(),
                                message = errorMsgAndData.first
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Left(UnknownError(statusCode = 0, message = e.message.toString()))
                    }
                } else if (e is SocketException) {
                    Left(
                        NoInternetError(
                            statusCode = 0,
                            message = "something went wrong while connecting to server! Please try again later."
                        )
                    )
                } else if (e is SocketTimeoutException || e is ConnectException) {
                    Left(TimeoutError(statusCode = 0, message = "request time out"))
                } else if (e is UnknownHostException) {
                    Left(
                        UnknownHostError(
                            statusCode = 0,
                            message = "something went wrong while connecting to server! Please try again later."
                        )
                    )
                } else {
                    e.printStackTrace()
//                    Log.e("", "apiCallingMethod:- " + e.message.toString())
                    Left(UnknownError(statusCode = 0, message = e.message.toString()))
                }
            }
        }
    }
}
package com.uoons.india.data.remote

import arrow.core.Either
import com.uoons.india.data.remote.error.Failure
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

@Obfuscate
abstract class Repository {

    object get : NetworkRetrofit()

    abstract suspend fun getJwellaryData(
        channelCode: String,
    ): Either<Failure, DeshBoardModel>

    abstract suspend fun checkAPKUpdatedVersion(
        channelCode: String,
        versionCode: String,
    ): Either<Failure, ForceUpdatedModel>

    abstract suspend fun login(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, LogingResponseModel>

    abstract suspend fun otp_Verification(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, OTPResponseModel>

    abstract suspend fun saveUserDetails(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, SaveUserDetailsResponse>

    abstract suspend fun saveBankDetails(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, SaveBankDetailsModel>

    abstract suspend fun fetchBankDetails(channelCode: String): Either<Failure, FetchBankDetailsModel>

    abstract suspend fun deleteBankDetails(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, FetchBankDetailsModel>

    abstract suspend fun selectPrimaryAccount(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, FetchBankDetailsModel>

    abstract suspend fun getUserDetails(channelCode: String): Either<Failure, UserDetailsModel>

    abstract suspend fun getDeshBoardData(channelCode: String): Either<Failure, DeshBoardModel>

    abstract suspend fun getMoreDeshBoardProducts(
        channelCode: String,
        page: Int,
    ): Either<Failure, HomePageMoreItemsDataModel>

    abstract suspend fun getHomepageItemsData(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, ProductModel>

    abstract suspend fun addRecentlyView(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, AddRecentlyViewModel>

    abstract suspend fun getFilterItemsData(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, ProductModel>

    abstract suspend fun getAllCategories(channelCode: String): Either<Failure, AllCategoryModel>

    abstract suspend fun getAllSubCategories(
        channelCode: String,
        sortById: Int,
        catId: Int,
        filters: String,
    ): Either<Failure, SubCategoriesModel>

    abstract suspend fun getSingleProductDetail(
        channelCode: String,
        pid: Int,
    ): Either<Failure, ProductDetailsModel>

    abstract suspend fun getAllQuestionsAnswers(
        channelCode: String,
        pid: Int,
    ): Either<Failure, QuestionAnswerModel>

    abstract suspend fun searchQuestionAnswers(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, QuestionAnswerModel>

    abstract suspend fun postlikeUnlikeQuestion(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, QuestionLikeUnlikeModel>

    abstract suspend fun postlikeUnlikeReview(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, QuestionLikeUnlikeModel>

    abstract suspend fun orderCancel(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, OrderCancelModel>

    abstract suspend fun postYourQuestion(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, PostYourQuestionModel>

    abstract suspend fun addItemToCart(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, AddItemToCartDataResponse>

    abstract suspend fun getSuggestionToEnhance(channelCode: String): Either<Failure, SuggestionToEnhanceModel>

    abstract suspend fun postSuggestion(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, PostSuggestionResponse>

    abstract suspend fun addQuantityToCart(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, AddQuantityModelResponse>

    abstract suspend fun getMyCartItems(channelCode: String): Either<Failure, GetMyCartDataModel>

    abstract suspend fun getSearchItems(
        channelCode: String,
        searchKey: String,
    ): Either<Failure, SubCategoriesModel>

    //  abstract suspend fun getFilterOption(channelCode: String)  : Either<Failure, FilterDataModel>

    abstract suspend fun addToWishList(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, AddWishListResponseModel>

    abstract suspend fun getWishList(channelCode: String): Either<Failure, GetWishListDataModel>

    abstract suspend fun getSortingOptions(channelCode: String): Either<Failure, SortingModel>

    abstract suspend fun getFAQ(channelCode: String): Either<Failure, FAQDataModel>

    abstract suspend fun getSortingData(
        channelCode: String,
        sortById: Int,
        filters: String,
    ): Either<Failure, SubCategoriesModel>

    abstract suspend fun removeCartItem(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, RemoveCartItemResponse>

    abstract suspend fun removeWishLitItem(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, RemoveCartItemResponse>

    abstract suspend fun contactUs(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, ContactUsModel>

    abstract suspend fun profileImageSave(
        channelCode: String,
        body: MultipartBody.Part,
    ): Either<Failure, ProfileImageModel>

    abstract suspend fun getUserDeliverAddress(channelCode: String): Either<Failure, DeliverAllAddressModel>

    abstract suspend fun insertNewDeliverAddress(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, DeliverAllAddressModel>

    abstract suspend fun deleteDeliverAddress(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, DeliverAllAddressModel>

    abstract suspend fun checkProductLocationAvailable(
        channelCode: String,
        pinCode: Int,
        pId: Int,
    ): Either<Failure, ProductAvailabilityResponseModel>

    abstract suspend fun checkCoupenCode(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, CoupenCodeModel>

    abstract suspend fun checkOutProduct(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, CheckOutModel>

    abstract suspend fun checkOutProductUpdate(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, OnlinePaymentStatusModel>

    abstract suspend fun getAllFetchBundleOrders(channelCode: String): Either<Failure, FecthAllBundleOrderModel>

    abstract suspend fun getOrderBundlesList(
        channelCode: String,
        bundleId: String,
    ): Either<Failure, OrderBundleListModel>

    abstract suspend fun getOrderList(
        channelCode: String,
        orderId: String,
    ): Either<Failure, OrderDetailModel>

    abstract suspend fun addRatingReview(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, RatingAndReviewModel>

    abstract suspend fun uploadReview(
        channelCode: String, partMap: HashMap<String, RequestBody>,
        product_image: ArrayList<MultipartBody.Part>,
    ): Either<Failure, RatingAndReviewModel>

    //   abstract suspend fun uploadReview(channelCode: String, body:HashMap<String, RequestBody>) : Either<Failure, RatingAndReviewModel>

    abstract suspend fun getToken(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, TokenModel>

    //  abstract suspend fun profileImageSave(authorizationToken: String, body: HashMap<String, Any>) : Either<Failure, ProfileImageModel>

    /*   abstract suspend fun getDeshBoardData(body: HashMap<String, Any>) : Either<Failure, BaseResponse<HomePageDataModel>>*/

    /* abstract suspend fun login(body: HashMap<String, Any>) : Either<Failure, BaseResponse<LoginResponse>>

     abstract suspend fun doctorList(doctorId:String,body: HashMap<String, Any>) : Either<Failure, BaseResponse<DoctorListResponse>>

     abstract suspend fun favourite(body: HashMap<String, Any>) : Either<Failure, BaseResponse<Nothing>>

     abstract suspend fun logout() : Either<Failure, BaseResponse<Nothing>>

     abstract suspend fun changePassword(body: HashMap<String, Any>) : Either<Failure, BaseResponse<Nothing>>

     abstract suspend fun appointmentList(type:String,body: HashMap<String, Any>) : Either<Failure, BaseResponse<AppointmentResponse>>

     abstract suspend fun clinicList(body: HashMap<String, Any>) : Either<Failure, BaseResponse<ClinicListResponse>>

     abstract suspend fun mediaUpload(mediaFor: String, mediaType: String, files: HashMap<String, File> = HashMap()) : Either<Failure, BaseResponse<MediaUploadResponse>>

     abstract suspend fun addSchedule(body: HashMap<String, Any>) : Either<Failure, BaseResponse<Nothing>>*/


    abstract suspend fun ordercancelreason(
        channelCode: String,
        body: HashMap<String, Any>,
    ): Either<Failure, OrderCancelModel>
}
package com.satvick.retrofit;


import com.google.gson.JsonObject;
import com.satvick.ccavenue.RSAResponseModel;
import com.satvick.model.AddAddressModel;
import com.satvick.model.ApplyCouponModel;
import com.satvick.model.ArticleModel;
import com.satvick.model.CODAvailableModel;
import com.satvick.model.CancelCouponModel;
import com.satvick.model.CartListModel;
import com.satvick.model.CartListModel2;
import com.satvick.model.CartListModelResponse;
import com.satvick.model.CategoryModel;
import com.satvick.model.ChangePasswordModel;
import com.satvick.model.CheckPhoneDataModel;
import com.satvick.model.CheckPhoneModel;
import com.satvick.model.CommonModel;
import com.satvick.model.CouponsListModel;
import com.satvick.model.EditAddressModel;
import com.satvick.model.EditProfileModel;
import com.satvick.model.FilterProductListModel;
import com.satvick.model.ForgotPasswordModel;
import com.satvick.model.GenerateOrderIdModel;
import com.satvick.model.HomeModel;
import com.satvick.model.HomeResponseModel;
import com.satvick.model.InnerPagesModel;
import com.satvick.model.LifeResponseModel;
import com.satvick.model.LoginModel;
import com.satvick.model.MyCurrentDetailsModel;
import com.satvick.model.MyOrderDetailsModel;
import com.satvick.model.MyOrderHelp;
import com.satvick.model.MyWishListResponse;
import com.satvick.model.NotificationListResponse;
import com.satvick.model.Offer;
import com.satvick.model.PlaceOrderModel;
import com.satvick.model.ProductDetailsResponse;
import com.satvick.model.ProductListingResponse;
import com.satvick.model.ReferalListModel;
import com.satvick.model.RequestForOrderModel;
import com.satvick.model.SearchListModel;
import com.satvick.model.SettingNotificationModel;
import com.satvick.model.SignUpModel;
import com.satvick.model.SocialLoginModel;
import com.satvick.model.SubCategoriesModel;
import com.satvick.model.UpdateCartQuantity;
import com.satvick.model.ViewAddressModel;
import com.satvick.model.ViewProfileModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by mobua06 on 16/6/17.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("signup")
    Call<SignUpModel> getSignUpResult(
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("country") String country,
            @Field("password") String password,
            @Field("deviceType") String deviceType,
            @Field("deviceToken") String deviceToken,
            @Field("referral_code") String referral_code,
            @Field("cartlist") String cartlist,
            @Field("color") String color,
            @Field("quantity") String quantity,
            @Field("size") String size);


    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> getLoginResult(
            @Field("email") String email,
            @Field("password") String password,
            @Field("deviceType") String deviceType,
            @Field("deviceToken") String deviceToken,
            @Field("cartlist") String cartlist,
            @Field("color") String color,
            @Field("quantity") String quantity,
            @Field("size") String size);


    @FormUrlEncoded
    @POST("viewprofile")
    Call<ViewProfileModel> getViewProfileResult(
            @Field("token") String token,
            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("changepassword")
    Call<ChangePasswordModel> getChangePasswordResult(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("oldpassword") String oldpassword,
            @Field("password") String password);


    @FormUrlEncoded
    @POST("notifystatus")
    Call<SettingNotificationModel> getSettingNotificationtResult(
            @Field("token") String token,
            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("addaddress")
    Call<AddAddressModel> getAddAddressResult(
            @Field("user_id") String user_id,
            @Field("token") String token,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("pincode") String pincode,
            @Field("address") String address,
            @Field("town") String town,
            @Field("city") String city,
            @Field("state") String state,
            @Field("type") String type,
            @Field("remark") String remark,
            @Field("popup") String popup,
            @Field("country") String country,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude);


    @FormUrlEncoded
    @POST("editaddress")
    Call<EditAddressModel> getEditAddressResult(
            @Field("user_id") String user_id,
            @Field("token") String token,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("pincode") String pincode,
            @Field("address") String address,
            @Field("town") String town,
            @Field("city") String city,
            @Field("state") String state,
            @Field("remark") String remark,
            @Field("address_id") String address_id,
            @Field("country") String country,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude);


    @FormUrlEncoded
    @POST("removeaddress")
    Call<ViewAddressModel> getRemoveAddressResult(
            @Field("user_id") String user_id,
            @Field("token") String token,
            @Field("address_id") String address_id);

    @FormUrlEncoded
    @POST("set_as_default")
    Call<ViewAddressModel> getSetAsDefaultAddressResult(
            @Field("user_id") String user_id,
            @Field("token") String token,
            @Field("address_id") String address_id,
            @Field("remark") String remark);


    @FormUrlEncoded
    @POST("homescreenapi")
    Call<HomeResponseModel> getHomeResult(
            @Field("user_id") String user_id,
            @Field("token") String token);


    @Multipart
    @POST("editprofile")
    Call<EditProfileModel> getEditProfileResult(
            @PartMap Map<String, RequestBody> partMapData,
            @Part MultipartBody.Part profileImage);


    @FormUrlEncoded
    @POST("cartoffer")
    Call<Offer> getCartOffer(@Field("catlist[]") String[] catlist);



    @FormUrlEncoded
    @POST("moreproductlist")
    Call<ProductListingResponse> getProductListResult(
            @Field("user_id") String user_id,
            @Field("brand") String brand,
            @Field("defaultcolor") String defaultcolor,
            @Field("subcatid") String subcatid,
            @Field("subsubcatid") String subsubcatid,
            @Field("catid") String catid,
            @Field("filter_data") String filter_data,
            @Field("filter_size") String filter_size,
            @Field("filter_color") String filter_color,
            @Field("filter_brand") String filter_brand,
            @Field("flash_sale") String flashSale,
            @Field("search") String search,
            @Field("search_key") String search_key,
            @Field("minValue") String minValue,
            @Field("maxValue") String maxValue,
            @Field("sortby") String sortby,
            @Field("theme") String theme,
            @Field("theme_id") String theme_id);



    @FormUrlEncoded
    @POST("productdetails")
    Call<ProductDetailsResponse> getProductDetailsResult(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id);


    @FormUrlEncoded
    @POST("wishlistproduct")
    Call<MyWishListResponse> getWishListProductListResult(
            @Field("token") String token,
            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("addtowish")
    Call<MyWishListResponse> getAddToWishListResult(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id);


    @FormUrlEncoded
    @POST("checkphone")
    Call<CheckPhoneModel> getCheckPhoneResult(@Field("phone") String phone);


    @FormUrlEncoded
    @POST("verifysignup")
    Call<CheckPhoneModel> verifysignup(@Field("phone") String phone,@Field("email") String email);


    @FormUrlEncoded
    @POST("sociallogin")
    Call<SocialLoginModel> socialLogin(@Field("social_id") String social_id,
                                       @Field("social_type") String social_type,
                                       @Field("deviceToken") String deviceToken,
                                       @Field("deviceType") String deviceType,
                                       @Field("name") String name,
                                       @Field("email") String email,
                                       @Field("image") String image,
                                       @Field("referral_code") String referral_code,
                                       @Field("cartlist") String cartlist,
                                       @Field("color") String color,
                                       @Field("quantity") String quantity,
                                       @Field("size") String size);






    @FormUrlEncoded
    @POST("Cartlistproduct")
    Call<CartListModel> getCartListResult(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("cartlist") String cartlist,
            @Field("color") String color,
            @Field("quantity") String quantity,
            @Field("size") String size,
            @Field("giftwrap") String giftwrap);


    @FormUrlEncoded
    @POST("addtocart")
    Call<CartListModelResponse> getAddToCartResult(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id,
            @Field("color") String color,
            @Field("size") String size,
            @Field("quantity") String quantity);


    @FormUrlEncoded
    @POST("addtocart")
    Call<CartListModel2> getAddToCartResult3(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id,
            @Field("color") String color,
            @Field("size") String size,
            @Field("quantity") String quantity);

//    @FormUrlEncoded
//    @POST("addtocart")
//    Call<CartListModelResponse> getAddToCartResult2(
//            @Field("token") String token,
//            @Field("user_id") String user_id,
//            @Field("product_id") String product_id,
//            @Field("color") String color,
//            @Field("size") String size,
//            @Field("quantity") String quantity);


    @FormUrlEncoded
    @POST("changepasswordbyphone")
    Call<ForgotPasswordModel> getChangePassByPhoneResult(
            @Field("phone") String phone,
            @Field("password") String password);


    @FormUrlEncoded
    @POST("checkphonedata")
    Call<CheckPhoneDataModel> getCheckPhoneDataForLoginUsingOTPResult(
            @Field("phone") String phone,
            @Field("deviceToken") String deviceToken,
            @Field("deviceType") String deviceType);


    @FormUrlEncoded
    @POST("searchlist")
    Call<SearchListModel> getSearchListResult(
            @Field("key") String key);


    @FormUrlEncoded
    @POST("placeorder")
    Call<PlaceOrderModel> getPlaceOrderResult(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("gift_wrapup_status") String gift_wrapup_status,
            @Field("location") String location,
            @Field("coupan_code") String coupan_code,
            @Field("payment_type") String payment_type,
            @Field("order_id") String order_id,
            @Field("tracking_id") String tracking_id,
            @Field("state") String state,
            @Field("city") String city,
            @Field("country") String country,
            @Field("pincode") String pincode,
            @Field("phone") String phone,
            @Field("name") String name,
            @Field("address_type") String address_type,
            @Field("currency") String currency);



    @FormUrlEncoded
    @POST("myorderdetails")
    Call<MyOrderDetailsModel> getMyOrderResult(
            @Field("token") String token,
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("request_for_order")
    Call<RequestForOrderModel> getRequestForOrderResult(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("reason") String reason,
            @Field("description") String description,
            @Field("size") String size,
            @Field("color") String color,
            @Field("order_id") String order_id,
            @Field("request_type") String request_type,
            @Field("refund_mode") String refund_mode,
            @Field("product_default") String product_default,
            @Field("product_id") String product_id);

    @FormUrlEncoded
    @POST("apply_cupon")
    Call<ApplyCouponModel> getApplyCouponResult(
            @Field("code") String code,
            @Field("total_price") String total_price,
            @Field("user_id") String user_id,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("innerpageapi")
    Call<InnerPagesModel> getInnerPageResult(
            @Field("subcat") String subcat,
            @Field("user_id") String user_id);


//    @GET("getsubcategorylist/{Categorylist}")
//    Call<SubCategoriesModel> getSubCategoriesListResult(
//            @Path("Categorylist") String Categorylist);

    @FormUrlEncoded
    @POST("getsubcategorylist")
    Call<SubCategoriesModel> getSubCategoriesListResult(@Field("subcat") String Categorylist);

    @GET("viewaddress/{user_id}")
    Call<ViewAddressModel> getViewAddressResult(
            @Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("filter_section")
    Call<FilterProductListModel> getFilterListResult(@Field("key") String key, @Field("type") String type);

    @GET("cupons")
    Call<CouponsListModel> getCouponsListResult();

    @GET("referal_list/{user_id}")
    Call<ReferalListModel> getReferalListResult(@Path("user_id") String user_id,@Header("token") String token);


    @FormUrlEncoded
    @POST("notficationlist")
    Call<NotificationListResponse> getNotificationListResult(@Field("user_id") String user_id,@Field("token") String token);

    @FormUrlEncoded
    @POST("updatecart")
    Call<UpdateCartQuantity> updatecart(
            @Field("token") String token,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id,
            @Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("removetocart")
    Call<CartListModelResponse> removeToCart(@Field("user_id") String user_id,
                                             @Field("token") String token,
                                             @Field("product_id") String productId);


    @FormUrlEncoded
    @POST("myorderhelp")
    Call<MyOrderHelp> getMyOrderHelp(@Field("token") String token,
                                    @Field("user_id") String user_id);



    @GET("generateordercode")
    Call<GenerateOrderIdModel> getOrderId();



    @FormUrlEncoded
    @POST("getImageByColor")
    Call<CommonModel> getImageByColor(@Field("color_code") String color_code,
                                      @Field("product_id") String productId,
                                      @Field("size") String size);


    @GET("json")
    Call<MyCurrentDetailsModel> getMyDeatails();

    @GET("alpha/{code}")
    Call<MyCurrentDetailsModel> getCurrency(@Path("code") String paramString);


    @GET("latest")
    Call<JsonObject> getCurrencyAmount(@Query("base") String paramString1, @Query("symbols") String paramString2);


    @GET("currencymap.json")
    Call<JsonObject> getAllSymbols();

    @FormUrlEncoded
    @POST("cancel_cupon")
    Call<CancelCouponModel> cancelCoupon(@Field("user_id") String paramString);

    @GET("codavailable/{userId}")
    Call<CODAvailableModel> codAvailable(@Path("userId") String paramString);

    @GET("catandsubcat")
    Call<CategoryModel> getCategories();

    @GET("getRSAPublicKey")
    Call<RSAResponseModel> getRSAPublicKey();

    @GET("appsatvicklife")
    Call<LifeResponseModel> appsatvicklife();

    @GET("appsatvicklifeblog/{id}")
    Call<LifeResponseModel> lifeCategoryApi(@Path("id") String id);


    @FormUrlEncoded
    @POST("appsatvicklifecontent")
    Call<LifeResponseModel> lifeContentApi(@Field("life_id") String id,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("myarticles")
    Call<ArticleModel> articlesApi(@Field("token") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("placelifeorder")
    Call<LifeResponseModel> placeLifeOrder(@Field("token") String token,@Field("user_id") String user_id,@Field("life_id") String life_id,@Field("order_id") String order_id,@Field("tracking_id") String tracking_id);
}

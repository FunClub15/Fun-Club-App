package com.techminds.funclub.utils.config;

/*
import com.bsqptech.enexus.realtime_app.model.service.BasicResponse;
import com.bsqptech.enexus.realtime_app.model.service.CreateChannelResponse;
import com.bsqptech.enexus.realtime_app.model.service.CreateReportChannelListResponse;
import com.bsqptech.enexus.realtime_app.model.service.FollowingChannelResponse;
import com.bsqptech.enexus.realtime_app.model.service.ForgotPasswordResponse;
import com.bsqptech.enexus.realtime_app.model.service.ListChannelResponse;
import com.bsqptech.enexus.realtime_app.model.service.MyChannelInfoResponse;
import com.bsqptech.enexus.realtime_app.model.service.NewsListResponse;
import com.bsqptech.enexus.realtime_app.model.service.NotificationResponse;
import com.bsqptech.enexus.realtime_app.model.service.PayPerVideoResponse;
import com.bsqptech.enexus.realtime_app.model.service.ReportContributionResponse;
import com.bsqptech.enexus.realtime_app.model.service.TickerResponse;
import com.bsqptech.enexus.realtime_app.model.service.UserResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
*/


import com.techminds.funclub.model.bean.ForgotPasswordModel;
import com.techminds.funclub.model.bean.UserModel;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 import com.bsqptech.enexus.realtime_app.model.service.ChannelInfoResponse;
 * Created by alkesh.chimnani on 2/6/2017.
 */

public interface APIService {

    @FormUrlEncoded
    @POST("/member_login.php")
    void login(@Field("username") String username, @Field("password") String password,
               Callback<UserModel> callback);

    @Multipart
    @POST("/member_register.php")
    void signUp(@Part("email") String email, @Part("userName") String username, @Part("password") String password,
                @Part("gender") String gender, @Part("phoneNumber") String phone,@Part("type") String type,
                @Part("fullName") String fullname, @Part("picture") TypedFile imageFile,
                Callback<UserModel> callback);

    @Multipart
    @POST("/update_profile.php")
    void update(@Part("id") String id,
                @Part("email") String email,
                @Part("password") String password,
                @Part("fullname") String fullName,
                @Part("phoneNumber") String phoneNumber,
                @Part("gender") String gender,
                @Part("type") String type,
                @Part("picture") TypedFile imageFile,
                @Part("userName") String userName,
                Callback<UserModel> callback);


    @FormUrlEncoded
    @POST("/forgot_password.php")
    void forgotPassword(@Field("email") String email, Callback<ForgotPasswordModel> callback);




 /*   @Multipart
    @POST("/User/edit/format/json")
    void editProfile(@Part("user") String userId, @Part("email") String email, @Part("password") String password, @Part("first_name") String firstName,
                     @Part("last_name") String lastName, @Part("country") String country, @Part("gender") String gender, @Part("age") String age,
                     @Part("image") TypedFile ProfilePicture, Callback<UserResponse> callback);



    @FormUrlEncoded
    @POST("/user/login/format/json")
    void login(@Field("Email") String email, @Field("Password") String password,
               @Field("MobileId") String token, @Field("deviceType") String type,
               Callback<UserResponse> callback);

    @FormUrlEncoded
    @POST("/User/forgotPassword/format/json")
    void forgotPassword(@Field("email") String email, Callback<ForgotPasswordResponse> callback);

    *//*
    * Create Channel
    * *//*
    @Multipart
    @POST("/channel/create/format/json")
    void createChannel(@Part("Name") String name, @Part("CreatedBy") String userId, @Part("Category") String category,
                       @Part("SubscriptionFee") String subscriptionFee, @Part("Location") String location,
                       @Part("Description") String description, @Part("Image") TypedFile image, @Part("Video") TypedFile video,
                       Callback<CreateChannelResponse> callback);


    @Multipart
    @POST("/Channel/edit/format/json")
    void UpdateChannel(@Part("channel") String channel, @Part("name") String name, @Part("user") String userId, @Part("category") String category,
                       @Part("location") String location, @Part("description") String description,
                       @Part("Image") TypedFile image, Callback<CreateChannelResponse> callback);


    @GET("/Channel/delete/format/json")
    void deleteChannel(@Query("channel") String channel, @Query("user") String user, Callback<BasicResponse> callback);


    *//*
    * ContributorRequest list
    * Channel list
    * MyChannelChannelFragment
    * *//*
    @GET("/channel/list/format/json")
    void getChannelList(@Query("idUser") String userId, Callback<ListChannelResponse> callback);




    @GET("/channel/show/format/json")
    void getMyChannelDetail(@Query("user") String user, @Query("channel") String channel, Callback<MyChannelInfoResponse> callback);

    *//*
    * ListChannelsFragment HomeFragment->News OtherChannelCategoryFragment->Category
    * SubscriptionCategoryFragment->Category->SubscriptionCategoryDetailFragment->SubscriptionChannelFragment *//*
    @GET("/channel/list/format/json")
    void getChannelListByCategory(@Query("subscriptionFee") int subscriptionFee, @Query("Category") String category, @Query("idUser") String userId, Callback<ListChannelResponse> callback);


    @GET("/channel/payPerView/format/json")
    void getSubscriptionChannelListByCategory(@Query("category") String category, @Query("user") String userId, Callback<PayPerVideoResponse> callback);

    *//*
    * ContributorRequest list (PAID only)
    * Channel list (PAID only)
    * *//*

    @FormUrlEncoded
    @POST("/userchannel/approveReporter/format/json")
    void approveReporter(@Field("user") String user, @Field("channel") String channel, Callback<ListChannelResponse> callback);



    @FormUrlEncoded
    @POST("/userchannel/declineReporter/format/json")
    void declineReporter(@Field("user") String user, @Field("channel") String channel, Callback<ListChannelResponse> callback);


    @GET("/channel/list/format/json")
    void getChannelListByUserId(@Query("subscriptionFee") int subscriptionFee, @Query("idUser") String userId, Callback<ListChannelResponse> callback);


    *//*
    * Create report channel list
    * CreateReportFragment
    * *//*
    @GET("/channel/channelsList/format/json")
    void getCreateReportChannelList(@Query("user") String userId, Callback<CreateReportChannelListResponse> callback);

//    @GET("/userchannel/requestReporter/format/json")
//    void getReporterRequestList(@Query("idUser") String userId, Callback<ListChannelResponse> callback);


    @FormUrlEncoded
    @POST("/userchannel/requestReporter/format/json")
    void requestReporter(@Field("user") String user, @Field("channel") String channelId, @Field("dis") String description, Callback<BasicResponse> callback);

    *//*
    * list of following fragment
    * MyChannelFollowingFragment
    * *//*
    @GET("/userchannel/followingChannels/format/json")
    void getChannelFollowingList(@Query("user") String userId, Callback<FollowingChannelResponse> callback);

    *//*
   * unfollow channel
   * MyChannelFollowingFragment
   * *//*
    @FormUrlEncoded
    @POST("/userchannel/unFollow/format/json")
    void unFollowChannel(@Field("user") String userId, @Field("channel") String channelId, Callback<BasicResponse> callback);


    @FormUrlEncoded
    @POST("/userchannel/add/format/json")
    void followChannel(@Field("user") String userId, @Field("channel") String channelId, Callback<BasicResponse> callback);


    *//*
    * Create Report
    * *//*
    @Multipart
    @POST("/report/create/format/json")
    void createReport(@Part("user") String userId, @Part("channel") String channelId, @Part("comment") String comment, @Part("title") String title,
                      @Part("address") String address, @Part("fee") String fee, @Part("Image") TypedFile image, @Part("Video") TypedFile video,
                      Callback<CreateChannelResponse> callback);

    *//*
    * list of Report Contribution
    * MyChannelReportContributionFragment
    * *//*
    @GET("/userchannel/reportContribution/format/json")
    void getChannelReportContributionList(@Query("user") String userId, Callback<ReportContributionResponse> callback);

    *//*
    * list of Report Contribution
    * MyChannelReportContributionFragment
    * *//*
    @GET("/user/ticker/format/json")
    void getTickerList(@Query("user") String userId, Callback<TickerResponse> callback);

    *//*
    * list of Notifications
    * NotificationFragment
    * *//*
    @GET("/user/notifications/format/json")
    void getNotificationList(@Query("user") String userId, Callback<NotificationResponse> callback);

    *//*
    * list of news feed
    * BreakingNewsFragment
    * *//*
    @GET("/user/newsFeed/format/json")
    void getNewsFeedList(@Query("user") String userId, Callback<NewsListResponse> callback);

    *//*
   * Add News
   * *//*
    @Multipart
    @POST("/user/addNewsFeed/format/json")
    void addNews(@Part("user") String userId, @Part("comment") String comment, @Part("image") TypedFile image, @Part("video") TypedFile video,
                 Callback<BasicResponse> callback);

    *//*
   * submit feedback
   * FeedbackFragment
   * *//*
*//*
    http://olxseventyseven.com/realtime/realtime/user/payPerVideo/format/j
    user,report,amount,token
*//*
    @FormUrlEncoded
    @POST("/user/payPerVideo/format/json")
    void payPerView(@Field("user") String userId, @Field("report") String report, @Field("amount") String amount, @Field("token") String token,
                    Callback<BasicResponse> callback);



   @FormUrlEncoded
    @POST("/user/feedback/format/json")
    void sendFeedback(@Field("user") String userId, @Field("feedback") String feedback, Callback<BasicResponse> callback);
*/
}
package com.example.fixx.Support

import com.example.fixx.constants.Constants.Companion.CONTENT_TYPE
import com.example.fixx.constants.Constants.Companion.SERVER_KEY
import com.example.fixx.inAppChatScreens.model.ChatPushNotification
import com.example.fixx.showTechnicianScreen.models.MultiJobRequestPushNotification
import com.example.fixx.showTechnicianScreen.models.SingleJobRequestPushNotification
import com.example.fixx.techOrderDetailsScreen.models.TechReplyPushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: ChatPushNotification
    ): Response<ResponseBody>

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postSingleJobNotification(
        @Body notification: SingleJobRequestPushNotification
    ): Response<ResponseBody>

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postAreaJobNotification(
        @Body notification: MultiJobRequestPushNotification
    ): Response<ResponseBody>

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postTechReplyNotification(
        @Body notification: TechReplyPushNotification
    ): Response<ResponseBody>
}
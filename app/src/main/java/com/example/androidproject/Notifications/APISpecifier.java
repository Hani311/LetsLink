package com.example.androidproject.Notifications;

import com.example.androidproject.Chat.Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APISpecifier {


    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAnmnwK_k:APA91bHwscgbvulAHYj_IxFkRXae_ORYTRbUzTh6f9adOZIyexdc6gHkyrqHKGzZ2K3djIeR6-RPxCTBpLbfKH040Yid-CgdxT-R8pGn_fJhWfnD7DQBvSIbTKmXe1QXMAvS-m-kWZD2"
    })

    @POST("fcm/send")
        Call<Response> sendNotification(@Body NotifSender sender);



}

package com.example.androidproject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotifReceiver {

    private static Retrofit retrofit=null;

    public static Retrofit getNotifReceiver(String url){
        if (retrofit==null){

            retrofit=new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
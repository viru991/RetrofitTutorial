package com.viru.retrofittutorial;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofitClient {
    //private static String BASE_URL="https://10.138.233.28/UserApi/";
    private static String BASE_URL="http://searchkero.com/UserApi/";
    private static MyRetrofitClient myClient;
    private Retrofit retrofit;

    private MyRetrofitClient(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized MyRetrofitClient getInstance(){
        if (myClient == null){
            myClient = new MyRetrofitClient();
        }
        return myClient;
    }

    public MyApi getAPI(){
        return retrofit.create(MyApi.class);

    }

}

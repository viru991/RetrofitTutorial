package com.viru.retrofittutorial;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //private static String BASE_URL="https://10.138.233.28/UserApi/";
    private static String BASE_URL="http://searchkero.com/UserApi/";
    private static RetrofitClient retrofitClient;
    private static Retrofit retrofit;

    // initializing the retrofit object in the constructor of class RetrofitClient
    private  RetrofitClient(){
        retrofit = new Retrofit.Builder()
                  .baseUrl(BASE_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
    }

    // create a skeleton class to check whether any instance of RetrofitClient
    // is created or not.
    // here getInstance is method name which returns type RetrofirClient
    public static synchronized RetrofitClient getInstance(){
        if (retrofitClient==null){
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }
    // method to get the API
    public Api getApi(){
        return retrofit.create(Api.class);
    }

}

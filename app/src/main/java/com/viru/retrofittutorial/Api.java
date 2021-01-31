package com.viru.retrofittutorial;

import com.viru.retrofittutorial.ModelResponse.DeleteResponse;
import com.viru.retrofittutorial.ModelResponse.FetchUserResponse;
import com.viru.retrofittutorial.ModelResponse.LoginResponse;
import com.viru.retrofittutorial.ModelResponse.RegisterResponse;
import com.viru.retrofittutorial.ModelResponse.UpdatePassResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("register.php")
    Call <RegisterResponse> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
            /* in field names @Field annotation must be same as in API php file*/
    );

    @FormUrlEncoded
    @POST("login.php")
    Call <LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
            /* in field names @Field annotation must be same as in API php file*/
    );

    //we will not uses     @FormUrlEncoded here since we are using GET method]
    @GET("fetchusers.php")
    Call<FetchUserResponse> fetchAllUsers();

    @FormUrlEncoded
    @POST("updateuser.php")
    Call <LoginResponse> updateuseraccount(
            @Field("id") int userid,
            @Field("username") String username,
            @Field("email") String email
            /* in field names @Field annotation must be same as in API php file*/
    );
    @FormUrlEncoded
    @POST("updatepassword.php")
    Call <UpdatePassResponse> updatepassword(
            @Field("email") String email,
            @Field("current") String curretpassword,
            @Field("new") String newpassword
            /* in field names @Field annotation must be same as in API php file*/
    );

    @FormUrlEncoded
    @POST("deleteaccount.php")
    Call <DeleteResponse> deleteUser(
            @Field("id") int userId
            /* in field names @Field annotation must be same as in API php file*/
    );
}

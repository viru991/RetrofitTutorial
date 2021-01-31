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

public interface MyApi {
    @FormUrlEncoded
    @POST("upload_document.php")
    Call<ResponsePOJO> uploadDocument(
            @Field("PDFName") String encodedName,
            @Field("PDF") String encodedPDF,
            @Field("PDF1") String encodedPDF1,
            @Field("meeting") String meeting,
            @Field("meetingtime") String meetingtime,
            @Field("venue") String venue,
            @Field("oic") String oic

    );
}

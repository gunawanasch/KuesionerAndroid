package com.gunawan.kuesioner.api;

import com.gunawan.kuesioner.model.Kuesioner;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("Kuesioner_controller/getKuesioner")
    Call<ArrayList<Kuesioner>> getKuesioner();

    @FormUrlEncoded
    @POST("Kuesioner_controller/addUser")
    Call<ResponseBody> addUser(@Field("name") String name,
                               @Field("email") String email);

    @FormUrlEncoded
    @POST("Kuesioner_controller/addAnswer")
    Call<ResponseBody> addAnswer(@Field("idTitle[]") ArrayList<Integer> listIdTitle,
                                 @Field("idQuestion[]") ArrayList<Integer> listIdQuestion,
                                 @Field("value[]") ArrayList<String> listValue,
                                 @Field("email") String email);
}

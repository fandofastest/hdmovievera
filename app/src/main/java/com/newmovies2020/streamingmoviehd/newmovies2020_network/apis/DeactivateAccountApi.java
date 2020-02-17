package com.newmovies2020.streamingmoviehd.newmovies2020_network.apis;

import com.newmovies2020.streamingmoviehd.newmovies2020_network.model.ResponseStatus;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DeactivateAccountApi  {

    @POST("deactivate_account")
    @FormUrlEncoded
    Call<ResponseStatus> deactivateAccount(@Field("id") String id, @Field("password") String password,
                                           @Field("reason") String reason,
                                           @Field("api_secret_key") String key);


}

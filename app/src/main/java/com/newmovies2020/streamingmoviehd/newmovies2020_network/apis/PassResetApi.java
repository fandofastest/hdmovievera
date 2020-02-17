package com.newmovies2020.streamingmoviehd.newmovies2020_network.apis;

import com.newmovies2020.streamingmoviehd.newmovies2020_network.model.PasswordReset;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PassResetApi {


    @GET("password_reset")
    Call<PasswordReset> resetPassword(@Query("api_secret_key") String key,
                                      @Query("email") String email);

}

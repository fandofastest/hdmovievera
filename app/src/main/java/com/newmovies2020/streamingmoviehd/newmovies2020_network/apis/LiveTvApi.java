package com.newmovies2020.streamingmoviehd.newmovies2020_network.apis;

import com.newmovies2020.streamingmoviehd.newmovies2020_network.model.LiveTvCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LiveTvApi {

    @GET("get_all_tv_channel_by_category")
    Call<List<LiveTvCategory>> getLiveTvCategories(@Query("api_secret_key") String key);

}

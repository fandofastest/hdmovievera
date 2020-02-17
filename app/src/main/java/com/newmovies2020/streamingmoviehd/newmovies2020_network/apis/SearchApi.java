package com.newmovies2020.streamingmoviehd.newmovies2020_network.apis;

import com.newmovies2020.streamingmoviehd.newmovies2020_network.model.SearchModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchApi {

    @GET("search")
    Call<SearchModel> getSearchData(@Query("api_secret_key") String apiSecreteKey,
                                    @Query("q") String query);

}

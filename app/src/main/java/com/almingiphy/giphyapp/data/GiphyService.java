package com.almingiphy.giphyapp.data;

import com.almingiphy.giphyapp.data.model.GiphyModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiphyService {

    @GET("trending")
    Call<GiphyModel> getTrendingGIFs(@Query("api_key") String api_key, @Query("limit") Integer limit,
                                @Query("offset") Integer offset);

    @GET("search")
    Call<GiphyModel> searchGIFs(@Query("api_key") String api_key, @Query("q") String q,
                                @Query("limit") Integer limit, @Query("offset") Integer offset);
}

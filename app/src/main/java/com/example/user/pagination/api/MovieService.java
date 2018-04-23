package com.example.user.pagination.api;

import com.example.user.pagination.model.TopRatedMovieData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 4/23/2018.
 */

public interface MovieService {
    @GET("top_rated?api_key=c37d3b40004717511adb2c1fbb15eda4&page=1")
    Call<TopRatedMovieData> getTopRatedMovies( @Query("api_key") String apiKey,
                                            @Query("language") String language,
                                            @Query("page") int pageIndex);
//    @GET("movie/top_rated")
//    Call<TopRatedMovieData>getTopRatedMovies(
//            @Query("api_key")String apikey,
//        @Query("language")String language,
//    @Query("page")int pageIndex);




}

package com.example.user.pagination.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 4/23/2018.
 */

public class MovieApi {
    public static final String apikey = "c37d3b40004717511adb2c1fbb15eda4";

    private static Retrofit retrofit = null;


    public static Retrofit getClient(){

        if (retrofit == null){

            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.themoviedb.org/3/ ")
                    .build();
        }

        return retrofit;
    }
}

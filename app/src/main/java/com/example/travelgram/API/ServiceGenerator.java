package com.example.travelgram.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* The ServiceGenerator class used to create a new REST client with the given API base url */

public class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create());

    private static final Retrofit retrofit = retrofitBuilder.build();

    private static final WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

    public static WeatherAPI getWeatherAPI() {
        return weatherAPI;
    }
}

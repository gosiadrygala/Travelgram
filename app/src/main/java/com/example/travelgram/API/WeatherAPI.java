package com.example.travelgram.API;

import com.example.travelgram.Models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("/data/2.5/weather?appid=8f5b10c2df67380b924b2e5e69e6bb42")
    Call<WeatherResponse> getWeather(@Query("lat") double lat, @Query("lon") double lon);
}

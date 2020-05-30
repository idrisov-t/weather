package com.idrisov.weather;

import com.idrisov.weather.Models.WeatherMain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface WeatherAPI {
    @GET ("weather?q=Махачкала&units=metric&APPID=0b08836a21c5d5280dbc3e634a3712a7&lang=ru")
    Call<WeatherMain> getWeather();
}

package com.idrisov.weather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//

public class NetworkRequest {
    private static NetworkRequest request;
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private Retrofit retrofit;

    private NetworkRequest() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkRequest getRequest(){
        if(request == null){
            request = new NetworkRequest();
        }
        return request;
    }

    public WeatherAPI getWeatherApi(){
        return  retrofit.create(WeatherAPI.class);
    }


}

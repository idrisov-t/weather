package com.idrisov.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.idrisov.weather.Models.Weather;
import com.idrisov.weather.Models.WeatherMain;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvLocation = findViewById(R.id.tv_location);
        final TextView tvTemperature = findViewById(R.id.tv_temperature);
        final TextView tvDescription = findViewById(R.id.tv_description);
        final TextView tvHumidity = findViewById(R.id.tv_humidity);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);


        showProgressBar(progressBar);
        NetworkRequest.getRequest()
                .getWeatherApi()
                .getWeather()
                .enqueue(new Callback<WeatherMain>() {
                    @Override
                    public void onResponse(Call<WeatherMain> call, Response<WeatherMain> response) {
                        WeatherMain weatherMain = response.body();
                        tvLocation.setText(weatherMain.getName());
                        //Проверяю температуру на положительные и отрицательные значения и вывожу
                        int temp = (int) weatherMain.getMain().getTemp();
                        if (temp > 0) {
                            tvTemperature.setText(String.valueOf("+" + temp + "°"));
                        } else {
                            tvTemperature.setText(String.valueOf("-" + temp + "°"));
                        }
                        tvHumidity.setText("Влажность - " + String.valueOf(weatherMain.getMain().getHumidity()) + "%");
                        tvDescription.setText( weatherMain.getWeather().get(0).getDescription());


                        hideProgressBar(progressBar);
                    }

                    @Override
                    public void onFailure(Call<WeatherMain> call, Throwable t) {

                    }
                });

    }


    public void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public void showProgressBar(ProgressBar progressBar){
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }
    public void hideProgressBar(ProgressBar progressBar){
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }
}



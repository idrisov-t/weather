package com.idrisov.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.idrisov.weather.Models.WeatherMain;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvLocation;
    TextView tvTemperature;
    TextView tvDescription;
    TextView tvHumidity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        showProgressBar(progressBar);
        checkInternetConnect(MainActivity.this);
        hideProgressBar(progressBar);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        showProgressBar(progressBar);
        checkInternetConnect(MainActivity.this);
        hideProgressBar(progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        showProgressBar(progressBar);
        checkInternetConnect(MainActivity.this);
        hideProgressBar(progressBar);
    }

    //Вывод в тост
    public void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    //Запуск progressBar'a
    public void showProgressBar(ProgressBar progressBar){
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    //Завершение progressBar'a
    public void hideProgressBar(ProgressBar progressBar){
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    //Запрос и получение результата
    public void networkRequest(){
        tvLocation = findViewById(R.id.tv_location);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvDescription = findViewById(R.id.tv_description);
        tvHumidity = findViewById(R.id.tv_humidity);
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
                        //Вывожу влажность воздуха
                        tvHumidity.setText("Влажность - " + String.valueOf(weatherMain.getMain().getHumidity()) + "%");
                        //Вывожу описание погоды
                        String s = toUpperLetter(weatherMain.getWeather().get(0).getDescription());
                        tvDescription.setText(s);



                    }

                    @Override
                    public void onFailure(Call<WeatherMain> call, Throwable t) {
                        showToast(MainActivity.this, "Не удалось получить данные");

                    }
                });

    }

    //Проверка на интернет соединение
    public void checkInternetConnect(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            networkRequest();
        }
        else
            showToast(MainActivity.this, getString(R.string.no_connection));

    }

    //Изменяет регистр первой буквы на большой
    public String toUpperLetter (String string){
        StringBuilder stringBuilder = new StringBuilder(string);
        Character.isAlphabetic(string.codePointAt(0));
        stringBuilder.setCharAt(0, Character.toUpperCase(string.charAt(0)));
        return stringBuilder.toString();
    }
}




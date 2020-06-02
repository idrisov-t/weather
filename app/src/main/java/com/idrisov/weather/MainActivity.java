package com.idrisov.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.idrisov.weather.Models.WeatherMain;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView tvLocation, tvTemperature, tvDescription, tvHumidity, tvTemperatureMaxMin, tvWindSpeed, tvData;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        checkInternetConnect(MainActivity.this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkInternetConnect(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetConnect(MainActivity.this);
    }

    //Вывод в тост
    public void showToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
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

        tvLocation = findViewById(R.id.tv_location_toolbar);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvDescription = findViewById(R.id.tv_description);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvTemperatureMaxMin = findViewById(R.id.tv_temperature_max_min);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
        tvData = findViewById(R.id.tv_data);

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
                        int tempMax = (int) weatherMain.getMain().getTempMax();
                        int tempMin = (int) weatherMain.getMain().getTempMin();
                        if (temp > 0 & tempMax > 0 & tempMin > 0) {
                            tvTemperature.setText(String.valueOf("+" + temp + "°"));
                            tvTemperatureMaxMin.setText(getString(R.string.temperature_max_and_min) + " " + "+" + tempMax + "°" + " до " + "+" +tempMin + "°");
                        } else {
                            tvTemperature.setText(String.valueOf("-" + temp + "°"));
                            tvTemperatureMaxMin.setText(getString(R.string.temperature_max_and_min) + " " + "-" + tempMax + "°" + " до " + "-" +tempMin + "°");
                        }

                        //Вывожу влажность воздуха
                        tvHumidity.setText(getString(R.string.humidity) + weatherMain.getMain().getHumidity() + getString(R.string.percent));
                        //Вывожу описание погоды
                        String s = toUpperLetter(weatherMain.getWeather().get(0).getDescription());
                        tvDescription.setText(s);

                        //Вывожу скорость ветра
                        tvWindSpeed.setText(getString(R.string.tv_wind_speed) + " " + weatherMain.getWind().getSpeed() + " м/с");

                        //Вывожу дату обновления
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                        date.getTime();
                        tvData.setText(getString(R.string.last_update_in) + " " + dateFormat.format(date));
                    }
                    @Override
                    public void onFailure(Call<WeatherMain> call, Throwable t) {
                        showToast("Не удалось получить данные");
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
            showToast(getString(R.string.no_connection));

    }

    //Изменяет регистр первой буквы на большой
    public String toUpperLetter (String string){
        StringBuilder stringBuilder = new StringBuilder(string);
        Character.isAlphabetic(string.codePointAt(0));
        stringBuilder.setCharAt(0, Character.toUpperCase(string.charAt(0)));
        return stringBuilder.toString();
    }
}




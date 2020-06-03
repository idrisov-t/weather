package com.idrisov.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    EditText etChangeCity;
    Button btnChangeCity;
    String keyLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        etChangeCity = findViewById(R.id.et_change_city);
        etChangeCity.setVisibility(View.INVISIBLE);
        btnChangeCity = findViewById(R.id.btn_change_city);
        btnChangeCity.setVisibility(View.INVISIBLE);
        keyLocation = "Москва";
        networkRequest(keyLocation);
        btnChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyLocation = etChangeCity.getText().toString();
                networkRequest(keyLocation);
                etChangeCity.getText().clear();
            }
        });

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

    //Запрос и получение результата
    public void networkRequest(String keyLoc){

        tvLocation = findViewById(R.id.tv_location_toolbar);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvDescription = findViewById(R.id.tv_description);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvTemperatureMaxMin = findViewById(R.id.tv_temperature_max_min);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
        tvData = findViewById(R.id.tv_data);
        //keyLoc = "Махачкала";
        String keyUnits = "metric";
        String keyAPPID = "0b08836a21c5d5280dbc3e634a3712a7";
        String keyLang = "ru";

        NetworkRequest.getRequest()
                .getWeatherApi()
                .getWeather(keyLoc, keyUnits, keyAPPID, keyLang)
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
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                        date.getTime();
                        tvData.setText(getString(R.string.last_update_in) + " " + dateFormat.format(date));

                        //Появление скрытых элементов
                        etChangeCity.setVisibility(View.VISIBLE);
                        btnChangeCity.setVisibility(View.VISIBLE);
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
            //networkRequest();
        }
        else
            showToast(getString(R.string.no_connection));

    }

    //Изменяет регистр первой буквы на большой
    public String toUpperLetter (String string){
        StringBuilder stringBuilder = new StringBuilder(string);
        stringBuilder.setCharAt(0, Character.toUpperCase(string.charAt(0)));
        return stringBuilder.toString();
    }

}




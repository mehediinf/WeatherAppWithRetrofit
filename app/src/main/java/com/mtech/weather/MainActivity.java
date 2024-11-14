package com.mtech.weather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView tVWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tVWeatherData = findViewById(R.id.tVId);

        // Retrofit ইন্সটেন্স তৈরি করা
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);

        // API কল করা এবং ডেটা ফেচ করা
        // আপনার API কী এখানে বসান
        String apiKey = "230b7c0afce588d4f27e852c1a1a24d7";
        Call<WeatherResponse> call = weatherService.getWeather("dhaka", apiKey, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherData = response.body();

                    // JSON থেকে ডেটা পড়ে UI এ সেট করা
                    String base = weatherData.getBase();
                    String cityName = weatherData.getCityName();
                    float temp = weatherData.getMain().getTemp();
                    float minTemp = weatherData.getMain().getTempMin();
                    float maxTemp = weatherData.getMain().getTempMax();
                    String description = weatherData.getWeather().get(0).getDescription();

                    // TextView এ ডেটা প্রদর্শন করা
                    tVWeatherData.setText("Base: " + base + "\nCity Name: " + cityName +
                            "\nTemp: " + temp + "°C\nMin Temp: " + minTemp + "°C\nMax Temp: " +
                            maxTemp + "°C\nDescription: " + description);
                } else {
                    Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.newapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView textViewCatFact;
    private ProgressBar progressBar;
    private CatFactApiService catFactApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCatFact = findViewById(R.id.textViewCatFact);
        progressBar = findViewById(R.id.progressBar);

        catFactApiService = RetrofitClient.getRetrofitInstance().create(CatFactApiService.class);
        fetchRandomCatFact();
    }

    private void fetchRandomCatFact() {
        progressBar.setVisibility(View.VISIBLE);

        catFactApiService.getRandomCatFact().enqueue(new Callback<CatFact>() {
            @Override
            public void onResponse(Call<CatFact> call, Response<CatFact> response) {
                if(!isFinishing()) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        // Устанавливаем полученный факт о кошках в TextView
                        textViewCatFact.setText(response.body().getFact());
                    } else {
                        Toast.makeText(MainActivity.this, "Ошибка сервера", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            // Вот тут просиходит правильная обраобтка данных, то есть вывод сообщения и закртие
            @Override
            public void onFailure(Call<CatFact> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

                View rootView = findViewById(android.R.id.content);
                if (t instanceof IOException) {
                    Snackbar.make(rootView, "Ошибка соединения с интернетом", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Закрыть", view -> {
                                // Она по умолчанию сама закрываетr
                            })
                            .show();
                } else {
                    Snackbar.make(rootView, "Произошла ошибка: " + t.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("Close", view -> {

                            })
                            .show();
                }
            }
        });
    }
}

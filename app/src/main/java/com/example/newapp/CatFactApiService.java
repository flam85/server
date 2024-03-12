package com.example.newapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CatFactApiService {
    @GET("fact")
    Call<CatFact> getRandomCatFact();
}


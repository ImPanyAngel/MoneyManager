package com.example.moneymanager;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ExchangeRateApi {
    @GET("v6/{apiKey}/latest/{baseCurrency}")
    Call<JsonObject> getExchangeRates(@Path("apiKey") String apiKey, @Path("baseCurrency") String baseCurrency);
}

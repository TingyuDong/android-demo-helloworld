package com.thoughtworks.androidtrain.data.source.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientInstance {
    companion object{
        private const val BASE_URL = "https://thoughtworks-mobile-2018.herokuapp.com"
        fun getRetrofitInstance(): Retrofit{
            val gson = GsonBuilder()
                .serializeNulls()
                .create()
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()
        }
    }
}
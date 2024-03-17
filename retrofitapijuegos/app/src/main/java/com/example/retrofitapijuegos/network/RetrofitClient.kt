package com.example.retrofitapijuegos.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.retrofitapijuegos.utils.Constantes

object RetrofitClient {
     val retrofit: APIVideojuegos by lazy {
        Retrofit
            .Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIVideojuegos::class.java)
    }
}
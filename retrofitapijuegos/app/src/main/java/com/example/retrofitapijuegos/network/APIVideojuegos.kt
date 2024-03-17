package com.example.retrofitapijuegos.network

import com.example.retrofitapijuegos.models.VideojuegoModel
import com.example.retrofitapijuegos.utils.Constantes
import retrofit2.Response
import retrofit2.http.GET

interface APIVideojuegos {

    @GET("games${Constantes.API_KEY}")
    suspend fun obtenerJuegos(): Response<VideojuegoModel>

}
package com.example.retrofitapijuegos.models

import com.google.gson.annotations.SerializedName

data class VideojuegoModel(
    @SerializedName("count")
    val total: Int,
    @SerializedName("results")
    val listaVideojuegos: List<VideoJuegosLista>
)

data class VideoJuegosLista(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val nombre: String,
    @SerializedName("background_image")
    val imagen: String
)
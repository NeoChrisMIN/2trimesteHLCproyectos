package com.example.retrofitapijuegos.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapijuegos.models.VideoJuegosLista
import com.example.retrofitapijuegos.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoJuegosViewModel: ViewModel() {
    private val _juegos = MutableStateFlow<List<VideoJuegosLista>>(emptyList())
    val juegos = _juegos.asStateFlow()

    init {
        obtenerJuegos()
    }

    private fun obtenerJuegos(){
        viewModelScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val response = RetrofitClient.retrofit.obtenerJuegos()
                _juegos.value = response.body()?.listaVideojuegos ?: emptyList()
            }
        }
    }
}
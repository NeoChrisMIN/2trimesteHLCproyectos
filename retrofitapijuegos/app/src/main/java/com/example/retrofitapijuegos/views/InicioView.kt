package com.example.retrofitapijuegos.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.retrofitapijuegos.componentes.CardJuego
import com.example.retrofitapijuegos.componentes.MainTopBar
import com.example.retrofitapijuegos.utils.Constantes
import com.example.retrofitapijuegos.viewmodels.VideoJuegosViewModel

@Composable
fun InicioView(
    navController: NavController,
    viewModel: VideoJuegosViewModel
){
    Scaffold (
        topBar = {
            MainTopBar(titulo = "API JUEGOS")
        }
    ){
        ContenidoInicioView(
            navController = navController,
            viewModel = viewModel,
            pad = it
        )
    }
}

@Composable
fun ContenidoInicioView(
    navController: NavController,
    viewModel: VideoJuegosViewModel,
    pad: PaddingValues
){
    val juegos by viewModel.juegos.collectAsState()

    LazyColumn (
        modifier = Modifier
            .padding(pad)
            .background(Color(Constantes.COLOR_ROJO))
    ){
        items(juegos){
            CardJuego(juego = it) {}
            Text(
                text = it.nombre,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 12.dp)
            )
        }
    }
}
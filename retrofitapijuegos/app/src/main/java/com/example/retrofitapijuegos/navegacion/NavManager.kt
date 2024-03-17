package com.example.retrofitapijuegos.navegacion

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.retrofitapijuegos.viewmodels.VideoJuegosViewModel
import com.example.retrofitapijuegos.views.InicioView

@Composable
fun NavManager(viewModel: VideoJuegosViewModel){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio"){
            InicioView(navController, viewModel)
        }
    }
}
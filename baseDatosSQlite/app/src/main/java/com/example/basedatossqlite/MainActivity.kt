package com.example.basedatossqlite

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactosRepository = ContactosRepository(this)

        setContent {
            App(contactosRepository)
        }
    }
}

@Composable
fun App(contactosRepository: ContactosRepository) {
    var textoBusqueda by remember { mutableStateOf("") }
    var listaContactos by remember { mutableStateOf(emptyList<Contacto>()) }
    var contactoSeleccionado by remember { mutableStateOf<Contacto?>(null) }
    var movilEditado by remember { mutableStateOf("") }
    var emailEditado by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(PaddingValues(16.dp))) {
        contactoSeleccionado?.let { contacto ->
            // Mostrar los detalles del contacto
            Text("Datos del contacto seleccionado: ")
            Text("${contacto.nick}")
            Text("${contacto.movil}")
            Text("${contacto.nombre}")
            Text("${contacto.apellido1}")
            Text("${contacto.apellido2}")
            Text("${contacto.email}")
        }
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            label = { Text("Buscar por Nick o Móvil") },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Button(onClick = {
            scope.launch {
                // Asegúrate de que esta operación está recuperando el contacto
                val resultado = contactosRepository.consultarContactoPorNick(textoBusqueda)
                if (resultado != null) {
                    contactoSeleccionado = resultado
                }
                // Agregar un log para ver si se encuentra un contacto
                Log.d("MainActivity", "Buscar por Nick: $resultado")
            }
        }) {
            Text("Buscar por Nick")
        }
        Button(
            onClick = {
                scope.launch {
                    contactoSeleccionado = contactosRepository.consultarContactoPorMovil(textoBusqueda)
                }
            },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("Buscar por Móvil")
        }
        Button(
            onClick = {
                contactoSeleccionado?.let { contacto ->
                    scope.launch {
                        contactosRepository.eliminarContactoPorNick(contacto.nick)
                        listaContactos = contactosRepository.obtenerTodosLosContactos()
                        contactoSeleccionado = null // Resetear el contacto seleccionado
                    }
                }
            },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("Eliminar Contacto")
        }

        /*Button(onClick = {
            scope.launch {
                listaContactos = contactosRepository.obtenerTodosLosContactos()
            }
        }) {
            Text("Cargar Todos los Contactos")
        }*/

        contactoSeleccionado?.let { contacto ->
            LaunchedEffect(contacto) {
                movilEditado = contacto.movil
                emailEditado = contacto.email
            }

            OutlinedTextField(
                value = movilEditado,
                onValueChange = { movilEditado = it },
                label = { Text("Editar Móvil") }
            )

            OutlinedTextField(
                value = emailEditado,
                onValueChange = { emailEditado = it },
                label = { Text("Editar Email") }
            )

            Button(onClick = {
                scope.launch {
                    contactosRepository.actualizarMovilYEmailDeContacto(contacto.nick, movilEditado, emailEditado)
                    listaContactos = contactosRepository.obtenerTodosLosContactos()
                    // También actualiza el contacto seleccionado con la nueva información
                    contactoSeleccionado = contacto.copy(movil = movilEditado, email = emailEditado)
                    contactoSeleccionado = null
                }
            }) {
                Text("Guardar Cambios")
            }
        }
        // Esto mostrará la lista de contactos y se actualizará cuando listaContactos cambie
        ListaContactos(contactos = listaContactos)
    }

    // Este efecto se lanzará cuando el composable se cree por primera vez
    LaunchedEffect(key1 = true) {
        listaContactos = contactosRepository.obtenerTodosLosContactos()
    }
}

@Composable
fun ListaContactos(contactos: List<Contacto>) {
    LazyColumn {
        items(contactos) { contacto ->
            ItemContacto(contacto)
        }
    }
}

@Composable
fun ItemContacto(contacto: Contacto) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Nick: ${contacto.nick}", modifier = Modifier.padding(bottom = 4.dp))
        Text("Móvil: ${contacto.movil}", modifier = Modifier.padding(bottom = 4.dp))
        Text("Apellido1: ${contacto.apellido1}", modifier = Modifier.padding(bottom = 4.dp))
        Text("Apellido2: ${contacto.apellido2}", modifier = Modifier.padding(bottom = 4.dp))
        Text("Nombre: ${contacto.nombre}", modifier = Modifier.padding(bottom = 4.dp))
        Text("Email: ${contacto.email}", modifier = Modifier.padding(bottom = 4.dp))
    }
}
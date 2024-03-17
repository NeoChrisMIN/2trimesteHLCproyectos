package com.example.firebase

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val contactosRepository = ContactosRepository()

        setContent {
            App(contactosRepository)
        }

        contactosRepository.agregarDatosDePrueba(contactosRepository)
    }
}

@Composable
fun App(contactosRepository: ContactosRepository) {
    var contactos by remember { mutableStateOf<List<Contacto>>(listOf()) }
    var nick by remember { mutableStateOf("") }
    var movil by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var buscarPorNick by remember { mutableStateOf("") }
    var buscarPorMovil by remember { mutableStateOf("") }
    //var contactoEncontrado by remember { mutableStateOf<Contacto?>(null) }
    var contactoEncontrado by remember { mutableStateOf<Contacto?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        contactosRepository.obtenerTodosLosContactos { nuevosContactos ->
            contactos = nuevosContactos
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Contactos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = buscarPorNick,
            onValueChange = { buscarPorNick = it },
            label = { Text("Nick") },
            singleLine = true
        )
        Button(onClick = {
            scope.launch {
                contactosRepository.consultarContactoPorNick(buscarPorNick) { contacto ->
                    contactoEncontrado = contacto // Actualizar el contacto encontrado
                }
            }
        }) {
            Text("Buscar por Nick")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = buscarPorMovil,
            onValueChange = { buscarPorMovil = it },
            label = { Text("Móvil") },
            singleLine = true
        )
        Button(onClick = {
            scope.launch {
                contactosRepository.consultarContactoPorMovil(buscarPorMovil) { contacto ->
                    contactoEncontrado = contacto // Actualizar el contacto encontrado
                }
            }
        }) {
            Text("Buscar por Móvil")
        }

        contactoEncontrado?.let { contacto ->
            DetallesContacto(
                nick = contacto.nick,
                movil = contacto.movil,
                email = contacto.email,
                onMovilChanged = { nuevoMovil -> contactoEncontrado = contacto.copy(movil = nuevoMovil) },
                onEmailChanged = { nuevoEmail -> contactoEncontrado = contacto.copy(email = nuevoEmail) },
                onSave = {
                    scope.launch {
                        contactoEncontrado?.let { actualizado ->
                            contactosRepository.actualizarMovilYEmailDeContacto(actualizado.nick, actualizado.movil, actualizado.email)
                        }
                        contactoEncontrado = null // Limpiar después de guardar
                    }
                },
                onDelete = {
                    scope.launch {
                        contactosRepository.eliminarContactoPorNick(contacto.nick)
                        contactoEncontrado = null // Limpiar después de eliminar
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        ListaContactos(contactos = contactos)
    }
}

@Composable
fun DetallesContacto(
    nick: String,
    movil: String,
    email: String,
    onMovilChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    Column {
        Text("Editar Contacto", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = movil, onValueChange = onMovilChanged, label = { Text("Móvil") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = email, onValueChange = onEmailChanged, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = onSave) {
                Text("Guardar Cambios")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onDelete) {
                Text("Eliminar Contacto")
            }
        }
    }
}

@Composable
fun ListaContactos(contactos: List<Contacto>) {
    if (contactos.isNotEmpty()) {
        LazyColumn {
            items(contactos) { contacto ->
                ItemContacto(contacto)
            }
        }
    } else {
        Text("No hay contactos disponibles")
    }
}

@Composable
fun ItemContacto(contacto: Contacto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nick: ${contacto.nick}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Móvil: ${contacto.movil}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Email: ${contacto.email}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}



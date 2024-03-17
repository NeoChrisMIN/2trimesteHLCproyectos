package com.example.datastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.datastore.ui.theme.DatastoreTheme

import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStoreManager = DataStoreManager(context = this)

        setContent {
            var id by remember { mutableStateOf("") }
            var nombre by remember { mutableStateOf("") }
            var apellidos by remember { mutableStateOf("") }
            var anoNacimiento by remember { mutableStateOf("") }
            var showDialog by remember { mutableStateOf(false) }
            var datosUsuarioShow by remember { mutableStateOf<DataStoreManager.DatosUsuario?>(null) }
            val coroutineScope = rememberCoroutineScope()

            Column(modifier = Modifier.padding(16.dp)) {
                TextField(value = id, onValueChange = { id = it }, label = { Text("ID") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = anoNacimiento, onValueChange = { anoNacimiento = it }, label = { Text("Año de Nacimiento") })
                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(onClick = {
                        coroutineScope.launch {
                            dataStoreManager.guardarDatosUsuario(id, nombre, apellidos, anoNacimiento.toIntOrNull() ?: 0)
                        }
                    }) {
                        Text("Guardar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            datosUsuarioShow = dataStoreManager.leerDatosUsuario.first()
                            showDialog = true
                        }
                    }) {
                        Text("Visualizar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            dataStoreManager.limpiarDatosUsuario()
                        }
                    }) {
                        Text("Borrar")
                    }
                }

                // Diálogo para mostrar los datos del usuario
                if (showDialog && datosUsuarioShow != null) {
                    AlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        title = { Text(text = "Datos del Usuario") },
                        text = {
                            Text(text = "ID: ${datosUsuarioShow?.id}\nNombre: ${datosUsuarioShow?.nombre}\nApellidos: ${datosUsuarioShow?.apellidos}\nAño de Nacimiento: ${datosUsuarioShow?.anoNacimiento}")
                        },
                        confirmButton = {
                            Button(
                                onClick = { showDialog = false }
                            ) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}

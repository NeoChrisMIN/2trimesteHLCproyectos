package com.example.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "user_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        private val ID_KEY = stringPreferencesKey("id_key")
        private val NOMBRE_KEY = stringPreferencesKey("nombre_key")
        private val APELLIDOS_KEY = stringPreferencesKey("apellidos_key")
        private val ANO_NACIMIENTO_KEY = intPreferencesKey("anio_nacimiento_key")
    }

    suspend fun guardarDatosUsuario(id: String, nombre: String, apellidos: String, anioNacimiento: Int) {
        context.dataStore.edit { preferencias ->
            preferencias[ID_KEY] = id
            preferencias[NOMBRE_KEY] = nombre
            preferencias[APELLIDOS_KEY] = apellidos
            preferencias[ANO_NACIMIENTO_KEY] = anioNacimiento
        }
    }

    val leerDatosUsuario: Flow<DatosUsuario> = context.dataStore.data.map { preferencias ->
        DatosUsuario(
            id = preferencias[ID_KEY] ?: "",
            nombre = preferencias[NOMBRE_KEY] ?: "",
            apellidos = preferencias[APELLIDOS_KEY] ?: "",
            anoNacimiento = preferencias[ANO_NACIMIENTO_KEY] ?: 0
        )
    }

    suspend fun limpiarDatosUsuario() {
        context.dataStore.edit { it.clear() }
    }

    data class DatosUsuario(
        val id: String,
        val nombre: String,
        val apellidos: String,
        val anoNacimiento: Int
    )
}

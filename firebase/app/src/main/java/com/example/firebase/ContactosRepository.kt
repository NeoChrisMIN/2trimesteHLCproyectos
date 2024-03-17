package com.example.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactosRepository {

    private val databaseReference = FirebaseDatabase.getInstance().getReference("contactos")

    fun agregarDatosDePrueba(contactosRepository: ContactosRepository) {
        val contactosDePrueba = listOf(
            Contacto(nick = "usuario1", movil = "1234567890", email = "usuario1@example.com", apellido1 = "lope", apellido2 = "vega", nombre = "paco"),
            Contacto(nick = "usuario2", movil = "0987654321", email = "usuario2@example.com", apellido1 = "shirayuki", apellido2 = "gimenez", nombre = "mizore"),
            Contacto(nick = "usuario3", movil = "1231231233", email = "usuario3@example.com", apellido1 = "kakaroto", apellido2 = "koloto", nombre = "goku"),
            Contacto(nick = "usuario4", movil = "1472583699", email = "usuario4@example.com", apellido1 = "angel", apellido2 = "rubine", nombre = "miguel"),
        )

        contactosDePrueba.forEach { contacto ->
            contactosRepository.agregarContacto(contacto)
        }
    }

    fun agregarContacto(contacto: Contacto) {
        val safeNick = contacto.nick.replace(".", "_") // Ejemplo de sanitización simple.

        databaseReference.child(safeNick).setValue(contacto).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("ContactosRepository", "Contacto agregado con éxito con nick: ${contacto.nick}")
            } else {
                Log.e("ContactosRepository", "Error al agregar contacto con nick: ${contacto.nick}", it.exception)
            }
        }
    }

    fun obtenerTodosLosContactos(callback: (List<Contacto>) -> Unit) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val contactos = dataSnapshot.children.mapNotNull { it.getValue(Contacto::class.java) }
                callback(contactos)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun consultarContactoPorNick(nick: String, callback: (Contacto?) -> Unit) {
        databaseReference.child(nick).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contacto = snapshot.getValue(Contacto::class.java)
                Log.e("miFirebase", "leer datos salio bien " + contacto?.nick.toString())
                callback(contacto)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("miFirebase", "Error al leer datos", error.toException())
                callback(null)
            }
        })
    }

    fun consultarContactoPorMovil(movil: String, callback: (Contacto?) -> Unit) {
        databaseReference.orderByChild("movil").equalTo(movil)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contacto = if (snapshot.exists()) {
                        snapshot.children.first().getValue(Contacto::class.java)
                    } else {
                        null
                    }
                    callback(contacto)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }

    fun eliminarContactoPorNick(nick: String) {
        databaseReference.child(nick).removeValue()
    }

    fun actualizarMovilYEmailDeContacto(nick: String, nuevoMovil: String, nuevoEmail: String) {
        val childUpdates = hashMapOf<String, Any>(
            "/$nick/movil" to nuevoMovil,
            "/$nick/email" to nuevoEmail
        )
        databaseReference.updateChildren(childUpdates)
    }
}

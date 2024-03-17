package com.example.basedatossqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log

class ContactosRepository(context: Context) {
    private val dbHelper = DBHelper(context)

    /*fun agregarContacto(contacto: Contacto) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_NICK, contacto.nick)
            put(DBHelper.COLUMN_MOVIL, contacto.movil)
            put(DBHelper.COLUMN_APELLIDO1, contacto.apellido1)
            put(DBHelper.COLUMN_APELLIDO2, contacto.apellido2)
            put(DBHelper.COLUMN_NOMBRE, contacto.nombre)
            put(DBHelper.COLUMN_EMAIL, contacto.email)
        }
        db.insert(DBHelper.TABLE_CONTACTS, null, values)
    }*/

    fun obtenerTodosLosContactos(): List<Contacto> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DBHelper.TABLE_CONTACTS, null, null, null, null, null, null
        )
        val contactos = mutableListOf<Contacto>()

        with(cursor) {
            while (moveToNext()) {
                val contacto = Contacto(
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_NICK)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_MOVIL)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_APELLIDO1)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_APELLIDO2)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_NOMBRE)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_EMAIL))
                )
                contactos.add(contacto)
            }
        }
        cursor.close()
        return contactos
    }

    fun consultarContactoPorNick(nick: String): Contacto? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBHelper.TABLE_CONTACTS,
            null,
            "${DBHelper.COLUMN_NICK}=?",
            arrayOf(nick),
            null,
            null,
            null
        )
        with(cursor) {
            if (moveToFirst()) {
                val contacto = Contacto(
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_NICK)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_MOVIL)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_APELLIDO1)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_APELLIDO2)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_NOMBRE)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_EMAIL))
                )
                close()
                return contacto
            }
        }
        cursor.close()
        return null
    }

    // Método para consultar un contacto por móvil
    fun consultarContactoPorMovil(movil: String): Contacto? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBHelper.TABLE_CONTACTS,
            null,
            "${DBHelper.COLUMN_MOVIL}=?",
            arrayOf(movil),
            null,
            null,
            null
        )
        with(cursor) {
            if (moveToFirst()) {
                val contacto = Contacto(
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_NICK)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_MOVIL)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_APELLIDO1)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_APELLIDO2)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_NOMBRE)),
                    getString(getColumnIndexOrThrow(DBHelper.COLUMN_EMAIL))
                )
                close()
                return contacto
            }
        }
        cursor.close()
        return null
    }


    // Método para eliminar un contacto por nick
    fun eliminarContactoPorNick(nick: String) {
        val db = dbHelper.writableDatabase
        db.delete(DBHelper.TABLE_CONTACTS, "${DBHelper.COLUMN_NICK}=?", arrayOf(nick))
    }

    // Método para actualizar móvil y email de un contacto
    fun actualizarMovilYEmailDeContacto(nick: String, nuevoMovil: String, nuevoEmail: String) {
        val db = dbHelper.writableDatabase
        val valores = ContentValues().apply {
            put(DBHelper.COLUMN_MOVIL, nuevoMovil)
            put(DBHelper.COLUMN_EMAIL, nuevoEmail)
        }
        db.update(DBHelper.TABLE_CONTACTS, valores, "${DBHelper.COLUMN_NICK}=?", arrayOf(nick))
    }

}
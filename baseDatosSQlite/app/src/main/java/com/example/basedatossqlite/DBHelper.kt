package com.example.basedatossqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "agenda.db"
        const val DATABASE_VERSION = 1
        const val TABLE_CONTACTS = "contactos"
        const val COLUMN_NICK = "nick"
        const val COLUMN_MOVIL = "movil"
        const val COLUMN_APELLIDO1 = "apellido1"
        const val COLUMN_APELLIDO2 = "apellido2"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = """
            CREATE TABLE $TABLE_CONTACTS (
                $COLUMN_NICK TEXT PRIMARY KEY,
                $COLUMN_MOVIL TEXT,
                $COLUMN_APELLIDO1 TEXT,
                $COLUMN_APELLIDO2 TEXT,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_EMAIL TEXT
            )
        """.trimIndent()

        db.execSQL(createTableStatement)
        llenarConDatosDeEjemplo(db) // Llamar al método para llenar la base de datos con datos de ejemplo
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    // Método para llenar la base de datos con datos de ejemplo
    private fun llenarConDatosDeEjemplo(db: SQLiteDatabase) {
        // Array de contactos de ejemplo
        val contactosDeEjemplo = arrayOf(
            // Agrega los datos de ejemplo que desees aquí
            ContentValues().apply {
                put(COLUMN_NICK, "nick1")
                put(COLUMN_MOVIL, "123456789")
                put(COLUMN_APELLIDO1, "Apellido1")
                put(COLUMN_APELLIDO2, "Apellido2")
                put(COLUMN_NOMBRE, "Nombre1")
                put(COLUMN_EMAIL, "email1@ejemplo.com")
            },
            ContentValues().apply {
                put(COLUMN_NICK, "nick2")
                put(COLUMN_MOVIL, "987654321")
                put(COLUMN_APELLIDO1, "Apellido3")
                put(COLUMN_APELLIDO2, "Apellido4")
                put(COLUMN_NOMBRE, "Nombre2")
                put(COLUMN_EMAIL, "email2@ejemplo.com")
            },
            ContentValues().apply {
                put(COLUMN_NICK, "nick3")
                put(COLUMN_MOVIL, "123123123")
                put(COLUMN_APELLIDO1, "Apellido4")
                put(COLUMN_APELLIDO2, "Apellido5")
                put(COLUMN_NOMBRE, "Nombre3")
                put(COLUMN_EMAIL, "email3@ejemplo.com")
            },
            ContentValues().apply {
                put(COLUMN_NICK, "nick4")
                put(COLUMN_MOVIL, "789123456")
                put(COLUMN_APELLIDO1, "Apellido6")
                put(COLUMN_APELLIDO2, "Apellido7")
                put(COLUMN_NOMBRE, "Nombre4")
                put(COLUMN_EMAIL, "email4@ejemplo.com")
            }
        )

        // Insertar los contactos de ejemplo en la base de datos
        for (values in contactosDeEjemplo) {
            db.insert(TABLE_CONTACTS, null, values)
        }
    }
}
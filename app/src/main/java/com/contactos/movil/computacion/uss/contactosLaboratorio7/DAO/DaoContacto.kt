package com.contactos.movil.computacion.uss.contactosLaboratorio7.DAO

/**
 * Maria Isabel Ortiz Naranjo
 * 18176
 */
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.contactos.movil.computacion.uss.contactosLaboratorio7.DAO.DaoContacto.Companion.TABLE_contacto

import com.contactos.movil.computacion.uss.contactosLaboratorio7.Modelo.Contacto

import java.util.ArrayList

class DaoContacto(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var db: SQLiteDatabase? = null

    val allStudentsList: List<Contacto>
        get() {
            val studentsArrayList = ArrayList<Contacto>()
            val selectQuery = "SELECT  * FROM $TABLE_contacto"
            Log.d(TAG, selectQuery)
            val db = this.readableDatabase
            val c = db.rawQuery(selectQuery, null)
            if (c.moveToFirst()) {
                do {
                    val students = Contacto()
                    students.id = c.getInt(c.getColumnIndex(KEY_ID))
                    students.telefono = c.getString(c.getColumnIndex(KEY_PHONENUMBER))
                    students.nombre = c.getString(c.getColumnIndex(KEY_NAME))
                    students.email = c.getString(c.getColumnIndex(KEY_email))
                    studentsArrayList.add(students)
                } while (c.moveToNext())
            }
            return studentsArrayList
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_Contacto)
    }

    fun open() {
        try { // Aqui se abre la base de datos
            db = this.writableDatabase
        } catch (e: Exception) {
            throw RuntimeException("Error al abrir la base de datos.")
        }

    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS; " + CREATE_TABLE_Contacto)
        onCreate(db)
    }

    fun addContactoDetail(student: Contacto): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, student.nombre)
        values.put(KEY_PHONENUMBER, student.telefono)
        values.put(KEY_email, student.email)
        return db.insert(TABLE_contacto, null, values)
    }

    fun updateEntry(student: Contacto): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, student.nombre)
        values.put(KEY_PHONENUMBER, student.telefono)
        values.put(KEY_email, student.email)
        return db.update(TABLE_contacto, values, "$KEY_ID = ?",
                arrayOf(student.id.toString()))
    }

    fun deleteEntry(id: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_contacto, "$KEY_ID = ?",
                arrayOf(id.toString()))
    }

    fun getStudent(id: Long): Contacto {
        val db = this.readableDatabase
        val selectQuery = ("SELECT  * FROM " + TABLE_contacto + " WHERE "
                + KEY_ID + " = " + id)
        Log.d(TAG, selectQuery)
        val c = db.rawQuery(selectQuery, null)
        c?.moveToFirst()
        val students = Contacto()
        students.id = c!!.getInt(c.getColumnIndex(KEY_ID))
        students.telefono = c.getString(c.getColumnIndex(KEY_PHONENUMBER))
        students.nombre = c.getString(c.getColumnIndex(KEY_NAME))
        students.email = c.getString(c.getColumnIndex(KEY_email))

        return students
    }

    fun searchContact(name: String): List<Contacto> {
        val studentsArrayList = ArrayList<Contacto>()
        val selectQuery = "SELECT  * FROM $TABLE_contacto WHERE $KEY_NAME LIKE '%$name%'"
        Log.d(TAG, selectQuery)
        val db = this.readableDatabase
        val c = db.rawQuery(selectQuery, null)
        if (c.moveToFirst()) {
            do {
                val students = Contacto()
                students.id = c.getInt(c.getColumnIndex(KEY_ID))
                students.telefono = c.getString(c.getColumnIndex(KEY_PHONENUMBER))
                students.nombre = c.getString(c.getColumnIndex(KEY_NAME))
                students.email = c.getString(c.getColumnIndex(KEY_email))
                studentsArrayList.add(students)
            } while (c.moveToNext())
        }
        return studentsArrayList
    }

    companion object {
        var DATABASE_NAME = "DBContactos"
        private val DATABASE_VERSION = 1
        private val TABLE_contacto = "contacto"
        private val KEY_ID = "id"
        private val KEY_NAME = "nombre"
        private val KEY_PHONENUMBER = "telefono"
        private val KEY_email = "email"
        var TAG = "tag"
        private val CREATE_TABLE_Contacto = ("CREATE TABLE "
                + TABLE_contacto + "(" + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PHONENUMBER + " TEXT," + KEY_email + " TEXT);")
    }
}


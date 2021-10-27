package com.example.notesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
    context: Context?,
    name: String?= "details.db",
    factory: SQLiteDatabase.CursorFactory?= null,
    version: Int= 2,
    private val tableName: String= "notes"
) : SQLiteOpenHelper(context, name, factory, version) {

    private val sqLiteDatabase: SQLiteDatabase= writableDatabase

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table $tableName (PK INTEGER PRIMARY KEY AUTOINCREMENT, Note Text)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(p0)
    }

    fun saveNotes(note: String): Long {
        val cv= ContentValues()
        cv.put("Note",note)
        return sqLiteDatabase.insert(tableName,null,cv)
    }

    fun gettingNotes(): ArrayList<String>{
        return try{
            val notes= arrayListOf<String>()
            val cursor =
                sqLiteDatabase.query(tableName, arrayOf("Note"), null, null, null, null, null)
            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                notes.add(cursor.getString(0))
                cursor.moveToNext()
            }
            notes
        } catch (e:Exception){
            arrayListOf("Error")
        }
    }
    fun updateNotes(pk:Int, newNote: String): Int{
        val contentValue= ContentValues()
        contentValue.put("Note",newNote)
        return sqLiteDatabase.update(tableName,contentValue,"PK=?", arrayOf("$pk"))
    }
    fun deleteNotes(pk: Int): Int{
        return sqLiteDatabase.delete(tableName,"PK=?", arrayOf("$pk"))
    }
}
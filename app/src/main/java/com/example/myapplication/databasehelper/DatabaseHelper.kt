package com.example.myapplication.databasehelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.model.Mot


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "YourDatabaseName.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Mot"
        const val COLUMN_ID = "id"
        const val COLUMN_LETTRES = "lettre"
        const val COLUMN_LANGUE = "langue"
        const val COLUMN_DIFFICULTE = "difficulte"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_LETTRES TEXT, $COLUMN_LANGUE TEXT, $COLUMN_DIFFICULTE TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun insertMot(mot: Mot){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LETTRES,mot.lettres)
            put(COLUMN_LANGUE, mot.langue)
            put(COLUMN_DIFFICULTE, mot.difficulte)
        }

        db.insert(TABLE_NAME,null,values)
        db.close()
    }

    fun deleteMot(mot: Mot){
        val db = writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(mot.id.toString())
        db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()
    }
}
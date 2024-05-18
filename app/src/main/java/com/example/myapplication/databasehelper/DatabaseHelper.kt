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
        const val COLUMN_MOT_FRANCAIS = "motfrancais"
        const val COLUMN_MOT_ANGLAIS = "motanglais"
        const val COLUMN_DIFFICULTE = "difficulte"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_MOT_FRANCAIS TEXT, $COLUMN_MOT_ANGLAIS TEXT, $COLUMN_DIFFICULTE TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }


}
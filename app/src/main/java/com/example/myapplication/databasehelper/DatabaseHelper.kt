package com.example.myapplication.databasehelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Table des mots
        const val DATABASE_NAME = "jeupendu.db"
        const val DATABASE_VERSION = 3
        const val TABLE_NAME = "Mot"
        const val COLUMN_ID = "id"
        const val COLUMN_MOT_FRANCAIS = "motfrancais"
        const val COLUMN_MOT_ANGLAIS = "motanglais"
        const val COLUMN_DIFFICULTE = "difficulte"

        // Table des parties jouées
        const val COLUMN_ID_PARTIE_JOUE = "id"
        const val COLUMN_DIFFICULTE_PARTIE_JOUE = "difficulte"
        const val TABLE_NAME_PARTIE_JOUE = "Partiejouee"
        const val COLUMN_MOT = "mot"
        const val COLUMN_TEMPS = "temps"
        const val COLUMN_REUSSITE = "reussite"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Table des mots
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_MOT_FRANCAIS TEXT, $COLUMN_MOT_ANGLAIS TEXT, $COLUMN_DIFFICULTE TEXT)"
        db?.execSQL(createTable)

        // Table des parties jouées
        val createTableParties = "CREATE TABLE $TABLE_NAME_PARTIE_JOUE " +
                "($COLUMN_ID_PARTIE_JOUE INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_MOT TEXT, " +
                "$COLUMN_DIFFICULTE_PARTIE_JOUE TEXT, $COLUMN_TEMPS LONG, $COLUMN_REUSSITE INTEGER)"
        db?.execSQL(createTableParties)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Table des mots
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTable)

        // Table des parties jouées
        val dropTableParties = "DROP TABLE IF EXISTS $TABLE_NAME_PARTIE_JOUE"
        db?.execSQL(dropTableParties)

        onCreate(db)
    }
}
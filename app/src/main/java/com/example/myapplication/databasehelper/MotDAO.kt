package com.example.demorecycleviewgroupe1

import android.content.ContentValues
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.model.Mot

class MotDAO(private val helper: DatabaseHelper) {

    fun insertMot(mot : Mot){
        val db = helper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ID,mot.id)
            put(DatabaseHelper.COLUMN_LETTRES,mot.lettres)
            put(DatabaseHelper.COLUMN_LANGUE, mot.langue)
            put(DatabaseHelper.COLUMN_DIFFICULTE,mot.difficulte)
        }
        db.insert(DatabaseHelper.TABLE_NAME,null,values)
        db.close()
    }

    fun getAllMot():List<Mot>{
        val motList = mutableListOf<Mot>()
        val db = helper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_NAME}"
        val cursor = db.rawQuery(query,null)
        while (cursor.moveToNext()){
            val id = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
            val lettres = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LETTRES))
            val langue = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LANGUE))
            val difficulte = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DIFFICULTE))

            val mot = Mot(id,lettres,langue,difficulte)
            motList.add(mot)
        }
        cursor.close()
        db.close()
        return motList
    }



    fun deleteMot(numero: String) {
        val db = helper.writableDatabase
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(numero)
        db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs)
        db.close()
    }
}
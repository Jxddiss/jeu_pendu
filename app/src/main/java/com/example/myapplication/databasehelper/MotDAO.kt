package com.example.myapplication.databasehelper

import android.content.ContentValues
import com.example.myapplication.R
import com.example.myapplication.model.Mot

class MotDAO(private val helper: DatabaseHelper) {

    fun insertMot(motFr : String, motEn : String, dif : String){
        val db = helper.writableDatabase
        val values = ContentValues().apply {

            put(DatabaseHelper.COLUMN_MOT_FRANCAIS,motFr)
            put(DatabaseHelper.COLUMN_MOT_ANGLAIS, motEn)
            put(DatabaseHelper.COLUMN_DIFFICULTE,dif)
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
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
            val motAnglais = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOT_ANGLAIS))
            val motFrancais = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOT_FRANCAIS))
            val difficulte = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DIFFICULTE))

            val mot = Mot(motFrancais,motAnglais,difficulte,id)
            motList.add(mot)
        }
        cursor.close()
        db.close()
        return motList
    }

    fun getAllMotStringByLangue(langue : String, difficulte : String):List<String>{
        val motList = mutableListOf<String>()
        val db = helper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_DIFFICULTE} = ? " +
                "OR ${DatabaseHelper.COLUMN_DIFFICULTE} = ?"
        var tradDifficulte = ""

        when(difficulte.lowercase()){
            "facile" -> tradDifficulte = "easy"
            "normal" -> tradDifficulte = "normal"
            "difficile" -> tradDifficulte = "hard"
            "easy" -> tradDifficulte = "facile"
            "hard" -> tradDifficulte = "difficile"
        }
        val cursor = db.rawQuery(query, arrayOf(difficulte.lowercase(),tradDifficulte))

        while (cursor.moveToNext()){
            when(langue.lowercase()){
                "français" -> {
                    val motFrancais = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOT_FRANCAIS))
                    motList.add(motFrancais)
                }
                "anglais" -> {
                    val motAnglais = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOT_ANGLAIS))
                    motList.add(motAnglais)
                }
                "french" -> {
                    val motFrancais = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOT_FRANCAIS))
                    motList.add(motFrancais)
                }
                "english" -> {
                    val motAnglais = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOT_ANGLAIS))
                    motList.add(motAnglais)
                }
            }
        }
        cursor.close()
        db.close()
        return motList
    }

    fun getMotById(mot: Mot): Mot? {
        val db = helper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.COLUMN_ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(mot.id.toString()))

        var unMot: Mot? = null
        if (cursor.moveToFirst()) {
            val motAnglais = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOT_ANGLAIS))
            val motFrancais = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOT_FRANCAIS))
            val difficulte = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DIFFICULTE))

            unMot = Mot(motFrancais,motAnglais ,difficulte, mot.id)
        }

        cursor.close()
        db.close()
        return unMot
    }
    fun deleteMot(numero: String) {
        val db = helper.writableDatabase
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(numero)
        db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs)
        db.close()
    }
}
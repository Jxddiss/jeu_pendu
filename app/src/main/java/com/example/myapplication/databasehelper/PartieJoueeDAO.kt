package com.example.myapplication.databasehelper

import android.content.ContentValues
import com.example.myapplication.model.PartieJouee

class PartieJoueeDAO(private val helper: DatabaseHelper) {

    fun insertPartie(partieJouee: PartieJouee){
        val db = helper.writableDatabase

        val reussiteBool = partieJouee.reussite
        val reussiteInt = if(reussiteBool) 1 else 0

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_MOT, partieJouee.mot)
            put(DatabaseHelper.COLUMN_DIFFICULTE, partieJouee.difficulte)
            put(DatabaseHelper.COLUMN_TEMPS, partieJouee.temps)
            put(DatabaseHelper.COLUMN_REUSSITE,reussiteInt)
        }

        db.insert(DatabaseHelper.TABLE_NAME_PARTIE_JOUE,null,values)
        //db.close()
    }

    fun getAllPartiesJouees():List<PartieJouee>{
        val partieList = mutableListOf<PartieJouee>()
        val db = helper.readableDatabase
        val query = "SELECT * FROM ${DatabaseHelper.TABLE_NAME_PARTIE_JOUE} ORDER BY id DESC"
        val cursor = db.rawQuery(query,null)
        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID_PARTIE_JOUE))
            val mot = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MOT))
            val difficulte = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DIFFICULTE_PARTIE_JOUE))
            val temp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TEMPS))
            val intTemp:Int = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REUSSITE))
            val reussite = intTemp == 1

            val partie = PartieJouee(mot,difficulte,temp,reussite,id)
            partieList.add(partie)
        }
        cursor.close()
        db.close()
        return partieList
    }
}
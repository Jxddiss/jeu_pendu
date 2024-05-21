package com.example.myapplication.databasehelper

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.myapplication.model.Mot
import org.junit.Test

class MotDAOTest {
    lateinit var motDAO : MotDAO
    lateinit var helper: DatabaseHelper
    lateinit var context: Context
    @Test
    fun testInsertMot() {
        context = ApplicationProvider.getApplicationContext()
        helper = DatabaseHelper(context)
        motDAO = MotDAO(helper)


        // When
        motDAO.insertMot("salut","hi","facile")
        motDAO.insertMot("salut","hi","facile")
        motDAO.insertMot("salut","hi","facile")
        motDAO.insertMot("salut","hi","facile")

        // Then
        var list :List<Mot> =motDAO.getAllMot()
        assert(list!=null)
        println(list.size)
    }

    @Test
    fun testGetAllMot() {
    }

    @Test
    fun getMotById() {
    }

    @Test
    fun deleteMot() {
    }
}
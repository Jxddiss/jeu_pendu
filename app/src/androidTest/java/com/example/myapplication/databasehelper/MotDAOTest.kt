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
        motDAO.insertMot("bonjour","hi","facile")
        motDAO.insertMot("biscuit","cookie","facile")
        motDAO.insertMot("aéroport","airport","normal")
        motDAO.insertMot("journee","day","facile")

        // Then
        var list :List<Mot> =motDAO.getAllMot()
        assert(list!=null)
        assert(list.size==4)
    }

    @Test
    fun testGetAllMot() {
        context = ApplicationProvider.getApplicationContext()
        helper = DatabaseHelper(context)
        motDAO = MotDAO(helper)


        // When
        motDAO.insertMot("bonjour","hi","facile")
        motDAO.insertMot("biscuit","cookie","facile")
        motDAO.insertMot("aéroport","airport","normal")
        motDAO.insertMot("journee","day","facile")

        // Then
        var list :List<Mot> =motDAO.getAllMot()
        assert(list!=null)
        assert(list.size==4)
    }

    @Test
    fun deleteMot() {
        context = ApplicationProvider.getApplicationContext()
        helper = DatabaseHelper(context)
        motDAO = MotDAO(helper)


        // When
        motDAO.insertMot("bonjour","hi","facile")
        motDAO.insertMot("biscuit","cookie","facile")
        motDAO.insertMot("aéroport","airport","normal")
        motDAO.insertMot("journee","day","facile")

        motDAO.deleteMot("1")
        // Then
        var list :List<Mot> =motDAO.getAllMot()
        assert(list!=null)
        assert(list.size==3)
    }

    @Test
    fun getAllMotStringByLangue() {
        context = ApplicationProvider.getApplicationContext()
        helper = DatabaseHelper(context)
        motDAO = MotDAO(helper)


        // When
        motDAO.insertMot("bonjour","hi","facile")
        motDAO.insertMot("biscuit","cookie","facile")
        motDAO.insertMot("aéroport","airport","normal")
        motDAO.insertMot("journée","day","facile")


        // Then
        var listfr : List<String> =motDAO.getAllMotStringByLangue("français","facile")
        var listen : List<String> =motDAO.getAllMotStringByLangue("anglais","facile")
        assert(listfr.elementAt(0)=="bonjour")
        assert(listfr.elementAt(1)=="biscuit")
        assert(listfr.elementAt(2)=="journée")
        assert(listen.elementAt(0)=="hi")
        assert(listen.elementAt(1)=="cookie")
        assert(listen.elementAt(2)=="day")

    }
}
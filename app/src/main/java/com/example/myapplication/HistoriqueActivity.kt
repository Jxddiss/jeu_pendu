package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.databasehelper.PartieJoueeDAO
import com.example.myapplication.model.PartieJouee
import com.example.myapplication.recycleradapter.RecyclerAdapter
import com.example.myapplication.recycleradapter.RecyclerAdapterPartieJouer

class HistoriqueActivity : AppCompatActivity() {
    lateinit var btnRetourMenu : Button
    lateinit var recyclerPartie : RecyclerView
    lateinit var adapter : RecyclerAdapterPartieJouer
    lateinit var partieList : ArrayList<PartieJouee>

    val databaseHelper = DatabaseHelper(this)
    val partieJoueeDAO = PartieJoueeDAO(databaseHelper)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historique)

        recyclerPartie = findViewById(R.id.partieJoueeRecycler)
        btnRetourMenu = findViewById(R.id.btnMenuPartie)

        btnRetourMenu.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        partieList = partieJoueeDAO.getAllPartiesJouees() as ArrayList
        setInfoAdapter()
    }

    fun setInfoAdapter(){
        adapter = RecyclerAdapterPartieJouer(this,partieList,resources)
        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)

        recyclerPartie.layoutManager = layoutManager
        recyclerPartie.itemAnimator = DefaultItemAnimator()
        recyclerPartie.adapter = adapter
    }
}
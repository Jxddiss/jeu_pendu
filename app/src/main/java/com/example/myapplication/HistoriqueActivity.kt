package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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
    lateinit var btnDeleteHistorique : Button
    lateinit var dialog: Dialog
    lateinit var btnDialogCancel : Button
    lateinit var btnDialogConfirm : Button
    lateinit var recyclerPartie : RecyclerView
    lateinit var adapter : RecyclerAdapterPartieJouer
    lateinit var partieList : ArrayList<PartieJouee>

    private val databaseHelper = DatabaseHelper(this)
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
            finish()
        }

        partieList = partieJoueeDAO.getAllPartiesJouees() as ArrayList
        setInfoAdapter()

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)

        btnDialogCancel  = dialog.findViewById(R.id.btnCancelDialog)
        btnDialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnDialogConfirm  = dialog.findViewById(R.id.btnConfirmerlDialog)
        btnDialogConfirm.setOnClickListener {
            partieJoueeDAO.clearHistorique()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnDeleteHistorique = findViewById(R.id.btnDeleteHistorique)
        btnDeleteHistorique.setOnClickListener {
            dialog.show()
        }
    }

    fun setInfoAdapter(){
        adapter = RecyclerAdapterPartieJouer(this,partieList,resources)
        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)

        recyclerPartie.layoutManager = layoutManager
        recyclerPartie.itemAnimator = DefaultItemAnimator()
        recyclerPartie.adapter = adapter
    }
}
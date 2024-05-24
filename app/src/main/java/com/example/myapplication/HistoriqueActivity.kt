package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
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
    @SuppressLint("ClickableViewAccessibility")
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

        // boutton gris pendant click long
        btnRetourMenu.setOnTouchListener { _, event ->
            when (event.action){
                MotionEvent.ACTION_DOWN -> {
                    btnRetourMenu.background.setTint(Color.GRAY)
                    btnRetourMenu.setTextColor(Color.GRAY)
                    btnRetourMenu.background.alpha = 200
                }
                MotionEvent.ACTION_UP -> {
                    btnRetourMenu.background.setTint(Color.BLACK)
                    btnRetourMenu.setTextColor(Color.BLACK)
                    btnRetourMenu.background.alpha = 255
                    btnRetourMenu.performClick()
                }
            }
            true
        }

        partieList = partieJoueeDAO.getAllPartiesJouees() as ArrayList
        setInfoAdapter()

        dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
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

    private fun setInfoAdapter(){
        adapter = RecyclerAdapterPartieJouer(this,partieList,resources)
        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)

        recyclerPartie.layoutManager = layoutManager
        recyclerPartie.itemAnimator = DefaultItemAnimator()
        recyclerPartie.adapter = adapter
    }
}
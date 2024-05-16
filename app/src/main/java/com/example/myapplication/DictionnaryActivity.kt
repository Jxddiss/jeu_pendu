package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demorecycleviewgroupe1.MotDAO
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.model.Mot
import com.example.myapplication.recycleradapter.RecyclerAdapter

class DictionnaryActivity : AppCompatActivity() {
    lateinit var motList : ArrayList<Mot>
    lateinit var recycler : RecyclerView

    lateinit var btnAjouter : Button
    lateinit var btnRetirer : Button
    lateinit var radioGroup: RadioGroup
    lateinit var spinner : Spinner
    lateinit var adapter : RecyclerAdapter
    lateinit var motDAO : MotDAO
    val ADD_CODE = 300




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.menu)
        recycler = findViewById(R.id.recycler)
        radioGroup = findViewById(R.id.choixLangue)
        motList = ArrayList()
        btnRetirer = findViewById(R.id.boutonRetirer)

        val helper = DatabaseHelper(this)
        motDAO = MotDAO(helper)

        motList = motDAO.getAllMot() as ArrayList<Mot>

        setInfoAdapter()
        radioGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.boutonAnglais -> updateFrancais()
                R.id.boutonFrancais -> updateAnglais()
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when (position) {
                    0 -> updateDif("Facile")
                    1 -> updateDif("Normal")
                    2 -> updateDif("Difficile")

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                set

            }

            private fun getDataForMode(mode: String): List<String> {
                // Replace this with your actual data retrieval logic
                val data = mutableListOf<String>()
                // Example: Retrieve data based on the selected mode
                when (mode) {
                    "Mode 1" -> {
                        data.add("Item A1")
                        data.add("Item A2")
                    }

                    "Mode 2" -> {
                        data.add("Item B1")
                        data.add("Item B2")
                    }
                    // Add more cases as needed for different modes
                }
                return data
            }

        }
    }


    fun setInfoAdapter(){
        adapter = RecyclerAdapter(motList)
        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)

        recycler.layoutManager = layoutManager
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.adapter = adapter

    }



    override fun onResume() {
        super.onResume()

        motList.clear()
        motList.addAll(motDAO.getAllMot())
        adapter.notifyDataSetChanged()
    }
}
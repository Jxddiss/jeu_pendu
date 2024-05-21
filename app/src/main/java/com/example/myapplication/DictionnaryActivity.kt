package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity.Companion.choixLangue
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.databasehelper.MotDAO
import com.example.myapplication.model.Mot
import com.example.myapplication.recycleradapter.RecyclerAdapter

class DictionnaryActivity : AppCompatActivity() {
    lateinit var motList : ArrayList<Mot>
    lateinit var recycler : RecyclerView

    lateinit var btnAjouter : Button
    lateinit var btnRetirer : Button
    lateinit var radioGroup: RadioGroup
    lateinit var spinnerRecherche : Spinner
    lateinit var spinnerAjout : Spinner
    lateinit var francaisAjout : EditText
    lateinit var anglaisAjout : EditText



    lateinit var adapter : RecyclerAdapter
    lateinit var motDAO : MotDAO
    val ADD_CODE = 300




    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionnary)

        //premier spinner (menu déroulant) pour filtrer la base de données
        spinnerRecherche = findViewById(R.id.menu)

        recycler = findViewById(R.id.recycler)
        radioGroup = findViewById(R.id.choixLangue)
        motList = ArrayList()
        btnAjouter = findViewById(R.id.boutonAjouter)

        //deuxième spinner (menu déroulant) pour choisir la difficulté du mot ajouté
        spinnerAjout = findViewById(R.id.menuAjout)

        francaisAjout = findViewById(R.id.nouveauMotFrancais)
        anglaisAjout = findViewById(R.id.nouveauMotAnglais)


        val helper = DatabaseHelper(this)
        motDAO = MotDAO(helper)

        motList = motDAO.getAllMot() as ArrayList<Mot>

        when (choixLangue.lowercase()) {
            "français" -> {
                radioGroup.check(R.id.boutonFrancais)
                setInfoAdapter(true)
                adapter.notifyDataSetChanged()
            }
            "anglais" -> {
                radioGroup.check(R.id.boutonAnglais)
                setInfoAdapter(false)
                adapter.notifyDataSetChanged()
            }
            "french" -> {
                radioGroup.check(R.id.boutonFrancais)
                setInfoAdapter(true)
                adapter.notifyDataSetChanged()
            }
            "english" -> {
                radioGroup.check(R.id.boutonAnglais)
                setInfoAdapter(false)
                adapter.notifyDataSetChanged()
            }
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.boutonAnglais -> setInfoAdapter(false)
                R.id.boutonFrancais -> setInfoAdapter(true)
            }
        }


        spinnerRecherche.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Switch case pour le choix de la difficulté
                when (position) {
                    0 -> updateDif("Facile")
                    1 -> updateDif("Normal")
                    2 -> updateDif("Difficile")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {


            }
        }


        //Ajout d'un mot dans la base de données
        btnAjouter.setOnClickListener {

            val dao = MotDAO(DatabaseHelper(this))
            var motfr = francaisAjout.text.toString().lowercase()
            var moten = anglaisAjout.text.toString().lowercase()
            var diff = spinnerAjout.selectedItem.toString().lowercase()
            dao.insertMot(motfr,moten,diff)
            onResume()
        }

    }

    fun updateDif(string : String){
        
    }

    fun setInfoAdapter(francais : Boolean){
        adapter = RecyclerAdapter(this,motList,francais)
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
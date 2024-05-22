package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.databasehelper.MotDAO

class MainActivity : AppCompatActivity() {
    lateinit var btnStart: Button
    lateinit var btnPreference: Button
    lateinit var btnHistorique : Button
    var databaseHelper = DatabaseHelper(this)
    var motDAO = MotDAO(databaseHelper)

    /*
    * Companion object qui contient le choix de langue et le niveau de difficulté choisi par
    * l'utilisateur, ainsi que la liste des mots spécifiques à ces paramètres.
    * */
    companion object {
        var choixLangue: String = "français"
        var choixDifficulte: String = "facile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btnJeu)
        btnPreference = findViewById(R.id.btnPreference)
        btnHistorique = findViewById(R.id.btnHistorique)

        // == Vérification si la base de données est vide et on la remplit si c'est le cas
        val listeMotString = motDAO.getAllMot() as ArrayList
        if(listeMotString.isEmpty()){
            motDAO.insertMot("bonjour","hi","easy")
            motDAO.insertMot("test","test","easy")
            motDAO.insertMot("histoire","history","normal")
            motDAO.insertMot("salopette","overall","hard")
        }

        val extras = intent.extras
        if (extras != null) {
            choixLangue = extras.getString("choixLangue", "")
            choixDifficulte = extras.getString("choixDifficulte", "")

            Log.d("MainActivity", "choixLangue: $choixLangue")
            Log.d("MainActivity", "choixDifficulte: $choixDifficulte")
        }

        btnStart.setOnClickListener {
            val intent: Intent = Intent(this, PenduJeuActivity::class.java)
            startActivity(intent)
        }

        btnPreference.setOnClickListener {
            val intentPreference = Intent(this, PreferencesActivity::class.java)

            val bundle = Bundle().apply {
                putString("choixLangue", choixLangue)
                putString("choixDifficulte", choixDifficulte)
            }
            intentPreference.putExtras(bundle)

            startActivity(intentPreference)
        }

        btnHistorique.setOnClickListener {
            val intent = Intent(this,HistoriqueActivity::class.java)
            startActivity(intent)
        }
    }
}
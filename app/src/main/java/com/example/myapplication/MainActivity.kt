package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.databasehelper.MotDAO
import com.example.myapplication.model.Mot

class MainActivity : AppCompatActivity() {
    lateinit var btnStart: Button
    lateinit var btnPreference: Button
    lateinit var btnHistorique : Button
    var databaseHelper = DatabaseHelper(this)
    var motDAO = MotDAO(databaseHelper)

    /*
    * Companion object qui contient le choix de langue et le niveau de difficulter choisie par
    * l'utilisateur, ainsi que la liste de mot spécifique à ces paramétre.
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

        // == Vérification si la base de données est vide et on la remplie si jamais
        val listeMotString = motDAO.getAllMot() as ArrayList
        if(listeMotString.isEmpty()){
            /*
            * mot a être inséré dans la base de données si elle est  vide
            *
            * source : ChatGPT
            * */
            motDAO.insertMot("bonjour","hi","easy")
            motDAO.insertMot("test","test","easy")
            motDAO.insertMot("histoire","history","normal")
            motDAO.insertMot("salopette","overall","hard")
            motDAO.insertMot("chien", "dog", "easy")
            motDAO.insertMot("chat", "cat", "easy")
            motDAO.insertMot("maison", "house", "normal")
            motDAO.insertMot("ordinateur", "computer", "normal")
            motDAO.insertMot("livre", "book", "hard")
            motDAO.insertMot("papillon", "butterfly", "easy")
            motDAO.insertMot("voiture", "car", "normal")
            motDAO.insertMot("velo", "bike", "easy")
            motDAO.insertMot("ecole", "school", "normal")
            motDAO.insertMot("stylo", "pen", "easy")
            motDAO.insertMot("souris", "mouse", "normal")
            motDAO.insertMot("jardin", "garden", "easy")
            motDAO.insertMot("travail", "work", "hard")
            motDAO.insertMot("cafe", "coffee", "easy")
            motDAO.insertMot("plage", "beach", "normal")
            motDAO.insertMot("soleil", "sun", "easy")
            motDAO.insertMot("lune", "moon", "normal")
            motDAO.insertMot("etoile", "star", "easy")
            motDAO.insertMot("chapeau", "hat", "normal")
            motDAO.insertMot("crayon", "pencil", "easy")
            motDAO.insertMot("robe", "dress", "normal")
            motDAO.insertMot("chemise", "shirt", "easy")
            motDAO.insertMot("pantalon", "pants", "normal")
            motDAO.insertMot("montagne", "mountain", "easy")
            motDAO.insertMot("foret", "forest", "normal")
            motDAO.insertMot("lac", "lake", "easy")
            motDAO.insertMot("riviere", "river", "normal")
            motDAO.insertMot("chocolat", "chocolate", "easy")
            motDAO.insertMot("gateau", "cake", "normal")
            motDAO.insertMot("fromage", "cheese", "easy")
            motDAO.insertMot("cryptographie", "cryptography", "hard")
            motDAO.insertMot("bureaucratie", "bureaucracy", "hard")
            motDAO.insertMot("electromagnétisme", "electromagnetism", "hard")
            motDAO.insertMot("phenomenologie", "phenomenology", "hard")
            motDAO.insertMot("revolutionnaire", "revolutionary", "hard")
            motDAO.insertMot("philosophique", "philosophical", "hard")
            motDAO.insertMot("astronomique", "astronomical", "hard")
            motDAO.insertMot("microscopique", "microscopic", "hard")
            motDAO.insertMot("bioluminescence", "bioluminescence", "hard")
            motDAO.insertMot("archeologie", "archaeology", "hard")
            motDAO.insertMot("socioeconomique", "socioeconomic", "hard")
            motDAO.insertMot("paradoxal", "paradoxical", "hard")
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
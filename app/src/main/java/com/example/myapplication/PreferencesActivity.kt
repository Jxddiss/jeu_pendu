package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity.Companion.choixDifficulte
import com.example.myapplication.MainActivity.Companion.choixLangue

/**
 * La class préférence permet à l'utilisateur de définir la langue voulue
 * et le niveau de difficulté. Lorsque l'utilisateur clique sur le bouton,
 * "btnRetourAccueil", l'information est sauvegardée à travers un companion object
 * et elle est renvoyée à la page accueil.
 * */
class PreferencesActivity : AppCompatActivity() {
    lateinit var btnRetourAccueil: Button
    lateinit var btnDictionnaire: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        btnRetourAccueil = findViewById(R.id.btnRetourAccueil)
        btnDictionnaire = findViewById(R.id.btnDictionnaire)

        val radioGroupLangue: RadioGroup = findViewById(R.id.btnLangue)
        val radioGroupDifficulter: RadioGroup = findViewById(R.id.btnDifficulter)

        /*
         * Lit la variable choixLangue et coche la bonne case sur l'écran
         * */
        when (choixLangue.lowercase()) {
            "français" -> radioGroupLangue.check(R.id.btnLangueFrancais)
            "anglais" -> radioGroupLangue.check(R.id.btnLangueAnglais)
            "french" -> radioGroupLangue.check(R.id.btnLangueFrancais)
            "english" -> radioGroupLangue.check(R.id.btnLangueAnglais)
        }

        /*
         * Lit la variable choixDifficulte et coche la bonne case sur l'écran
         * */
        when (choixDifficulte.lowercase()) {
            "facile" -> radioGroupDifficulter.check(R.id.btnDifficulterFacile)
            "normal" -> radioGroupDifficulter.check(R.id.btnDifficulterNormal)
            "difficile" -> radioGroupDifficulter.check(R.id.btnDifficulterDifficile)
            "easy" -> radioGroupDifficulter.check(R.id.btnDifficulterFacile)
            "hard" -> radioGroupDifficulter.check(R.id.btnDifficulterDifficile)
        }

        /*
         * Fonction qui définit un event listener sur le groupe de bouton radio du choix de langue
         * et ensuite, vérifie lequel à été cliqueé et l'assigne à la variable nécessaire.
         * */
        radioGroupLangue.setOnCheckedChangeListener { group, checkedId ->

            val choixLangueBouton: RadioButton = findViewById(checkedId)
            val choixLangueSelectionner: String = choixLangueBouton.text.toString()

            when (checkedId) {
                R.id.btnLangueFrancais -> {
                    choixLangue = choixLangueSelectionner.lowercase()
                }
                R.id.btnLangueAnglais  -> {
                    choixLangue = choixLangueSelectionner.lowercase()
                }
            }
        }

        /*
         * Fonction qui définit un event listener sur le groupe de bouton radio du choix de difficulté
         * et ensuite, vérifie lequel à été cliqué et l'assigne à la variable nécessaire.
         * */
        radioGroupDifficulter.setOnCheckedChangeListener { group, checkedId ->

            val choixDifficulterBouton: RadioButton = findViewById(checkedId)
            val choixDifficulterSelectionner: String = choixDifficulterBouton.text.toString()

            when (checkedId) {
                R.id.btnDifficulterFacile    -> {
                    choixDifficulte = choixDifficulterSelectionner.lowercase()
                }
                R.id.btnDifficulterNormal    -> {
                    choixDifficulte = choixDifficulterSelectionner.lowercase()
                }
                R.id.btnDifficulterDifficile -> {
                    choixDifficulte = choixDifficulterSelectionner.lowercase()
                }
            }

        }

        /*
        * Un event listener sur le bouton retour à l'accueil qui crée une nouvelle bundle
        * et envoie les deux variables du choix de langue et de difficulté.
        * */
        btnRetourAccueil.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            val bundle = Bundle().apply {
                putString("choixLangue", choixLangue)
                putString("choixDifficulte", choixDifficulte)
            }
            intent.putExtras(bundle)

            startActivity(intent)
        }

        btnDictionnaire.setOnClickListener {
            val intent = Intent(this, DictionnaryActivity::class.java)

            val bundle = Bundle().apply {
                putString("choixLangue", choixLangue)

            }
            intent.putExtras(bundle)

            startActivity(intent)
        }
    }
}
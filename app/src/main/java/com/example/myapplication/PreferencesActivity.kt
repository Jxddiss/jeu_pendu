package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class PreferencesActivity : AppCompatActivity() {
    lateinit var btnRetourAccueil: Button
    lateinit var btnDictionnaire: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val bundle = intent.extras
        var choixLangue = bundle?.getString("choixLangue")
        var choixDifficulte = bundle?.getString("choixDifficulte")

        btnRetourAccueil = findViewById(R.id.btnRetourAccueil)
        btnDictionnaire = findViewById(R.id.btnDictionnaire)

        val radioGroupLangue: RadioGroup = findViewById(R.id.btnLangue)
        val radioGroupDifficulter: RadioGroup = findViewById(R.id.btnDifficulter)

        when (choixLangue?.lowercase()) {
            "français" -> radioGroupLangue.check(R.id.btnLangueFrancais)
            "anglais" -> radioGroupLangue.check(R.id.btnLangueAnglais)
            "french" -> radioGroupLangue.check(R.id.btnLangueFrancais)
            "english" -> radioGroupLangue.check(R.id.btnLangueAnglais)
        }

        when (choixDifficulte?.lowercase()) {
            "facile" -> radioGroupDifficulter.check(R.id.btnDifficulterFacile)
            "normal" -> radioGroupDifficulter.check(R.id.btnDifficulterNormal)
            "difficile" -> radioGroupDifficulter.check(R.id.btnDifficulterDifficile)
            "easy" -> radioGroupDifficulter.check(R.id.btnDifficulterFacile)
            "difficult" -> radioGroupDifficulter.check(R.id.btnDifficulterDifficile)
        }

        radioGroupLangue.setOnCheckedChangeListener { group, checkedId ->

            val choixLangueBouton: RadioButton = findViewById(checkedId)
            val choixLangueSelectionner: String = choixLangueBouton.text.toString()

            when (checkedId) {
                R.id.btnLangueFrancais -> {
                    choixLangue = choixLangueSelectionner
                }
                R.id.btnLangueAnglais  -> {
                    choixLangue = choixLangueSelectionner
                }
            }
        }

        radioGroupDifficulter.setOnCheckedChangeListener { group, checkedId ->

            val choixDifficulterBouton: RadioButton = findViewById(checkedId)
            val choixDifficulterSelectionner: String = choixDifficulterBouton.text.toString()

            when (checkedId) {
                R.id.btnDifficulterFacile    -> {
                    choixDifficulte = choixDifficulterSelectionner
                }
                R.id.btnDifficulterNormal    -> {
                    choixDifficulte = choixDifficulterSelectionner
                }
                R.id.btnDifficulterDifficile -> {
                    choixDifficulte = choixDifficulterSelectionner
                }
            }

        }

        btnRetourAccueil.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            val bundle = Bundle().apply {
                putString("choixLangue", choixLangue)
                putString("choixDifficulte", choixDifficulte)
            }
            intent.putExtras(bundle)

            startActivity(intent)
        }

    }
}
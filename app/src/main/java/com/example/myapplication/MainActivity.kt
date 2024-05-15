package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var btnStart: Button
    lateinit var btnPreference: Button

    companion object {
        var choixLangue: String = ""
        var choixDifficulte: String = ""
        var listeDeMot: ArrayList<String> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btnJeu)
        btnPreference = findViewById(R.id.btnPreference)

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
    }
}
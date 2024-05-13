package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity : AppCompatActivity() {
    lateinit var btnMenu : Button
    lateinit var btnRestart : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_over)

        btnMenu = findViewById(R.id.btnMenuGameOver)

        btnMenu.setOnClickListener {
            val intent : Intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        btnRestart = findViewById(R.id.btnRestartGameOver)
        btnRestart.setOnClickListener {
            val intent : Intent = Intent(this,PenduJeuActivity::class.java)
            startActivity(intent)
        }
    }
}
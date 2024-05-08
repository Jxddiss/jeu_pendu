package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class PenduJeuActivity : AppCompatActivity() {
    lateinit var gifImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendu_jeu)

        gifImageView = findViewById(R.id.animGifImageView)
    }


}
package com.example.myapplication

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide

class PenduJeuActivity : AppCompatActivity() {
    lateinit var gifImageView: ImageView
    lateinit var boutonsList : ArrayList<ImageButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendu_jeu)



        gifImageView = findViewById(R.id.animGifImageView)

        for (i in 0..25){
            val btn : ImageButton = findViewById(resources.getIdentifier("btn"+(i+1),"id",packageName))
            btn.setOnClickListener{
                blinkError(it as ImageButton)
            }
       }

        Glide.with(this).asGif().load(R.drawable.phase_one).into(gifImageView);
    }

    private fun blinkError(btn : ImageButton) {
        btn.isClickable = false

        val anim : ObjectAnimator = ObjectAnimator.ofInt(btn.background,
            "tint", Color.RED, Color.BLACK, Color.RED);
        anim.setDuration(300)
        anim.setEvaluator(ArgbEvaluator())
        anim.repeatMode = ValueAnimator.REVERSE
        anim.repeatCount = 4
        anim.start()

        btn.postDelayed({
            println("------------------ delayed")
            btn.background.setTint(Color.GRAY)
            btn.background.alpha = 100
        },400*5)
    }

}
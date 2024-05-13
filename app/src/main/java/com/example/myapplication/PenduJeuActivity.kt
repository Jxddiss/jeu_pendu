package com.example.myapplication

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.example.myapplication.model.Jeu

private const val NB_ERREURS_MAX = 6

class PenduJeuActivity : AppCompatActivity() {
    lateinit var gifImageView: ImageView
    lateinit var letterPlaceholder: LinearLayout
    lateinit var jeu: Jeu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendu_jeu)

        jeu = Jeu(mutableListOf("journee","biscuit", "orange", "pomme") as ArrayList)
        gifImageView = findViewById(R.id.animGifImageView)
        letterPlaceholder = findViewById(R.id.linearLayLettresMot)
        initialiserLetterPlaceHolder()

        /* Pour chaque position dans l'alphabet on charge le bouton qui
        * lui correspond et on associe un event listenner qui vas vérifier
        * si la lettre est dans le mot a deviner
        */
        for (i in 0..25){
            val btn : ImageButton = findViewById(resources.getIdentifier("btn"+(i+1),"id",packageName))

            btn.setOnClickListener{
                handleVerificationBouton(it as ImageButton)
            }
       }

        /*
        * Librairie qui permet d'animer des gifs
        *
        * source sur l'utilisation = https://stackoverflow.com/questions/3660209/display-animated-gif ==
        * */
        Glide.with(this)
            .asGif()
            .load(R.drawable.phase_1)
            .into(gifImageView)
    }

    /**
     * Méthode qui permet de verifier si le joueur à gagné le jeu et qui renvoie le joueur a une nouvelle activité
     *
     * @param btn  ImageButton qui vient d'être cliqué
     * */
    private fun handleVerificationBouton(btn : ImageButton){
        var resultPosition : ArrayList<Int>  = jeu.essayerUneLettre(btn.contentDescription[0])

        if (resultPosition.isEmpty()){
            blinkError(btn)
        }else{
            handleBonChoix(btn,resultPosition)
        }

        if (jeu.estRéussi()){
            btn.postDelayed({
                var intent : Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            },600)
        }else if(jeu.nbErreurs >= NB_ERREURS_MAX){
            btn.postDelayed({
                var intent : Intent = Intent(this, GameOverActivity::class.java)
                startActivity(intent)
            },2700)
        }
    }

    /**
     * Méthode qui permet d'animer le bouton si la lettre donnée n'est pas
     * dans le mot
     *
     * Source : https://stackoverflow.com/questions/2614545/animate-change-of-view-background-color-on-android
     *
     * @param btn  Bouton qui sera animé
     * */
    private fun blinkError(btn : ImageButton) {
        jeu.nbErreurs++
        btn.isClickable = false

        val anim : ObjectAnimator = ObjectAnimator.ofInt(btn.background,
            "tint", Color.RED, Color.BLACK, Color.RED)

        anim.setDuration(200)
        anim.setEvaluator(ArgbEvaluator())
        anim.repeatMode = ValueAnimator.REVERSE
        anim.repeatCount = 3
        anim.start()

        Glide.with(this)
            .asGif()
            .load(resources
            .getIdentifier("error_${jeu.nbErreurs}","drawable",packageName))
            .into(gifImageView)

        btn.postDelayed({
            btn.background.setTint(Color.GRAY)
            btn.background.alpha = 100
        },200*5)

        btn.postDelayed({
            Glide.with(this)
                .asGif()
                .load(resources
                .getIdentifier("phase_${jeu.nbErreurs+1}","drawable",packageName))
                .into(gifImageView)
        },400)
    }

    /**
     * Méthode qui permet d'initialiser le linear layout avec le nombre de charactère
     * du mot a deviné
     *
     * Aide : https://stackoverflow.com/questions/5042197/android-set-height-and-width-of-custom-view-programmatically
     * */
    private fun initialiserLetterPlaceHolder(){
        for (i in 0 until jeu.motADeviner.length){
            val image : ImageView = ImageView(this)
            image.layoutParams = ViewGroup.LayoutParams(100,100)

            if (jeu.motADeviner[i] != ' '){
                image.setBackgroundResource(R.drawable.btn_background)
            }

            letterPlaceholder.addView(image)
        }
    }

    /**
     * Méthode qui se lance dans le cas ou une lettre se trouve bel et bien dans le mot
     *
     * @param btn  ImageButton qui vient d'être cliqué
     * @param resultPosition  ArrayList<Int> qui représente les position ou la lettre se
     * trouve dans le mot
     * */
    private fun handleBonChoix(btn : ImageButton, resultPosition : ArrayList<Int> ){
        btn.isClickable = false
        for (result in resultPosition){
            val image = letterPlaceholder[result]
            btn.postDelayed({
                image.setBackgroundResource(resources
                    .getIdentifier(btn.contentDescription[0].lowercase(),"drawable",packageName))
            }, 150)
        }
        btn.postDelayed({
            btn.background.setTint(Color.GRAY)
            btn.background.alpha = 100
                        },200)
    }

}
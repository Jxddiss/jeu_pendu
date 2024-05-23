package com.example.myapplication

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.core.view.postDelayed
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity.Companion.choixDifficulte
import com.example.myapplication.MainActivity.Companion.choixLangue
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.databasehelper.MotDAO
import com.example.myapplication.databasehelper.PartieJoueeDAO
import com.example.myapplication.model.Jeu
import com.example.myapplication.model.PartieJouee

private const val NB_ERREURS_MAX = 6

class PenduJeuActivity : AppCompatActivity() {
    lateinit var gifImageView: ImageView
    lateinit var letterPlaceholder: LinearLayout
    lateinit var btnRecommencer: Button
    lateinit var btnPreference: Button
    lateinit var btnHistorique: Button
    lateinit var jeu: Jeu
    lateinit var btnLettreListe : ArrayList<ImageButton>
    lateinit var listeMotString : ArrayList<String>
    val databaseHelper = DatabaseHelper(this)
    val motDAO = MotDAO(databaseHelper)
    val partieJoueeDAO = PartieJoueeDAO(databaseHelper)
    var debutGame : Long = 0

    // source son : https://stackoverflow.com/questions/45870632/play-sounds-from-raw-file-in-kotlin
    lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendu_jeu)

        //Initialisation des variables
        listeMotString = motDAO.getAllMotStringByLangue(choixLangue, choixDifficulte) as ArrayList
        debutGame = System.currentTimeMillis()

        jeu = Jeu(listeMotString)
        gifImageView = findViewById(R.id.animGifImageView)
        letterPlaceholder = findViewById(R.id.linearLayLettresMot)
        initialiserLetterPlaceHolder()

        btnLettreListe = ArrayList()
        btnRecommencer = findViewById(R.id.btnRestartJeu)
        btnHistorique = findViewById(R.id.btnHistoriqueJeu)
        btnPreference = findViewById(R.id.btnPreferenceJeu)

        //OnclickListeners sur les bouttons
        btnRecommencer.setOnClickListener {
            it.background.setTint(Color.GRAY)
            it.background.alpha = 100
            this.recreate()
        }

        btnHistorique.setOnClickListener {
            val intent = Intent(this,HistoriqueActivity::class.java)
            startActivity(intent)
        }

        btnPreference.setOnClickListener {
            val intentPreference = Intent(this, PreferencesActivity::class.java)
            startActivity(intentPreference)
        }


        /* Pour chaque position dans l'alphabet on charge le bouton qui
        * lui correspond et on associe un event listenner qui vas vérifier
        * si la lettre est dans le mot a deviner
        */
        for (i in 0..25){
            val btn : ImageButton = findViewById(resources.getIdentifier("btn"+(i+1),"id",packageName))
            btnLettreListe.add(btn)
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
        // Désactivation des boutons
        for (btnLettre in btnLettreListe){
            btnLettre.isClickable = false
        }

        val resultPosition : ArrayList<Int>  = jeu.essayerUneLettre(btn.contentDescription[0])

        if (resultPosition.isEmpty()){
            blinkError(btn)
        }else{
            handleBonChoix(btn,resultPosition)
        }

        if (jeu.estRéussi()){
            btn.postDelayed({
                val intent  = Intent(this, GameOverActivity::class.java)
                val finGame = System.currentTimeMillis()
                val temp = finGame - debutGame
                val partieJouee = PartieJouee(jeu.motADeviner, choixDifficulte,temp,true)
                partieJoueeDAO.insertPartie(partieJouee)
                intent.putExtra("reussi",true)
                startActivity(intent)
                finish()
            },600)
        }else if(jeu.nbErreurs >= NB_ERREURS_MAX){
            btn.postDelayed({
                val intent  = Intent(this, GameOverActivity::class.java)
                val finGame = System.currentTimeMillis()
                val temp = finGame - debutGame
                val partieJouee = PartieJouee(jeu.motADeviner, choixDifficulte,temp,false)
                partieJoueeDAO.insertPartie(partieJouee)
                intent.putExtra("reussi",false)
                intent.putExtra("mot",jeu.motADeviner)
                startActivity(intent)
                finish()
            },2700)
        }else{
            // Réactivation des autres bouttons
            btn.postDelayed({
                for (btnLettre in btnLettreListe){
                    if (btnLettre.id != btn.id){
                        btnLettre.isClickable = true
                    }
                }
            },700)
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

        mediaPlayer = MediaPlayer.create(this,R.raw.pop)
        mediaPlayer.setVolume(0.5f,0.5f)
        mediaPlayer.start()

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

            mediaPlayer.stop()
            mediaPlayer = MediaPlayer.create(this,R.raw.error)
            mediaPlayer.setVolume(0.5f,0.5f)
            mediaPlayer.start()
        },400)

        if (jeu.nbErreurs == 6){
            btn.postDelayed({
                mediaPlayer.stop()
                mediaPlayer = MediaPlayer.create(this,R.raw.falling)
                mediaPlayer.setVolume(0.5f,0.5f)
                mediaPlayer.start()
            },500)
        }
    }

    /**
     * Méthode qui permet d'initialiser le linear layout avec le nombre de charactère
     * du mot a deviné
     *
     * Aide : https://stackoverflow.com/questions/5042197/android-set-height-and-width-of-custom-view-programmatically
     * */
    private fun initialiserLetterPlaceHolder(){
        for (i in 0 until jeu.motADeviner.length){
            val image = ImageView(this)
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
        mediaPlayer = MediaPlayer.create(this,R.raw.success)
        mediaPlayer.setVolume(0.5f,0.5f)
        mediaPlayer.start()

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
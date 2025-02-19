package com.example.myapplication

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.example.myapplication.MainActivity.Companion.choixDifficulte
import com.example.myapplication.MainActivity.Companion.choixLangue
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.databasehelper.MotDAO
import com.example.myapplication.databasehelper.PartieJoueeDAO
import com.example.myapplication.model.Jeu
import com.example.myapplication.model.PartieJouee

private const val NB_ERREURS_MAX = 6

class PenduJeuActivity : AppCompatActivity(), OnTouchListener {
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

    @SuppressLint("ClickableViewAccessibility")
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
            this.recreate()
        }

        btnRecommencer.setOnTouchListener(this)

        btnHistorique.setOnClickListener {
            val intent = Intent(this,HistoriqueActivity::class.java)
            startActivity(intent)
        }

        btnHistorique.setOnTouchListener(this)

        btnPreference.setOnClickListener {
            val intentPreference = Intent(this, PreferencesActivity::class.java)
            startActivity(intentPreference)
        }

        btnPreference.setOnTouchListener(this)


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

            btn.setOnTouchListener(this)
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
     * Méthode qui permet de verifier si le joueur à gagné le jeu et qui renvoie le joueur a une nouvelle activité
     *
     * @param btn  ImageButton qui vient d'être cliqué
     * */
    private fun handleVerificationBouton(btn : ImageButton){
        // Désactivation des boutons
        for (btnLettre in btnLettreListe){
            btnLettre.isClickable = false
        }

        btnLettreListe.remove(btn)

        val resultPosition : ArrayList<Int>  = jeu.essayerUneLettre(btn.contentDescription[0])

        if (resultPosition.isEmpty()){
            handleMauvaisChoix(btn)
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
                    btnLettre.isClickable = true
                }
            },700)
        }
    }

    /**
     * Méthode qui permet d'animer le bouton si la lettre donnée n'est pas
     * dans le mot avec la bonne couleur et qui change l'animation selon
     * le nombre d'erreurs
     *
     *
     * @param btn  Bouton qui sera animé
     * */
    private fun handleMauvaisChoix(btn : ImageButton) {
        jeu.nbErreurs++

        blink(btn,Color.RED)

        Glide.with(this)
            .asGif()
            .load(resources
                .getIdentifier("error_${jeu.nbErreurs}","drawable",packageName))
            .into(gifImageView)

        mediaPlayer = MediaPlayer.create(this,R.raw.pop)
        mediaPlayer.setVolume(0.5f,0.5f)
        mediaPlayer.start()

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
     * Méthode qui se lance dans le cas où une lettre se trouve bel et bien dans le mot
     *
     * @param btn  ImageButton qui vient d'être cliqué
     * @param resultPosition  ArrayList<Int> qui représente les positions où la lettre se
     * trouve dans le mot
     * */
    private fun handleBonChoix(btn : ImageButton, resultPosition : ArrayList<Int> ){
        mediaPlayer = MediaPlayer.create(this,R.raw.success)
        mediaPlayer.setVolume(0.5f,0.5f)
        mediaPlayer.start()

        blink(btn,Color.GREEN)

        for (result in resultPosition){
            val image = letterPlaceholder[result]
            btn.postDelayed({
                image.setBackgroundResource(resources
                    .getIdentifier(btn.contentDescription[0].lowercase(),"drawable",packageName))
            }, 150)
        }
    }

    /**
     * Méthode qui rajoute une animation qui fait clignoter le bouton selon
     * la couleur donnée en paramètre
     *
     * Source : https://stackoverflow.com/questions/2614545/animate-change-of-view-background-color-on-android
     *
     * @param btn ImageButton à faire clignoter
     * @param color Int couleur représentant la couleur du clignotement
     * */
    private fun blink(btn : ImageButton, color : Int){
        val anim : ObjectAnimator = ObjectAnimator.ofInt(btn.background,
            "tint", color, Color.BLACK, color)

        anim.setDuration(200)
        anim.setEvaluator(ArgbEvaluator())
        anim.repeatMode = ValueAnimator.REVERSE
        anim.repeatCount = 3
        anim.start()

        btn.postDelayed({
            btn.background.setTint(Color.GRAY)
            btn.background.alpha = 100
        },1000)
    }

    /**
     * override du onTouch pour permettre de mettre les boutons en gris s'il
     * sont appuyer longuement
     *
     * source : https://stackoverflow.com/questions/47170075/kotlin-ontouchlistener-called-but-it-does-not-override-performclick
     * */
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        var btn : Button? = null

        if (v is Button){
            btn = v
        }
        if(v?.isClickable == true){
            when (event?.action){
                MotionEvent.ACTION_DOWN -> {
                    v.background?.setTint(Color.GRAY)
                    btn?.setTextColor(Color.GRAY)
                    v.background?.alpha = 200
                }
                MotionEvent.ACTION_UP -> {

                        v.background.setTint(Color.BLACK)
                        btn?.setTextColor(Color.BLACK)
                        v.background.alpha = 255
                        v.performClick()
                }
            }
        }
        return true
    }
}
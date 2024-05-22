package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity.Companion.choixDifficulte
import com.example.myapplication.MainActivity.Companion.choixLangue
import com.example.myapplication.databasehelper.DatabaseHelper
import com.example.myapplication.databasehelper.MotDAO
import com.example.myapplication.model.Mot
import com.example.myapplication.recycleradapter.RecyclerAdapter
import java.util.stream.Collectors

class DictionnaryActivity : AppCompatActivity() {
    lateinit var motList : ArrayList<Mot> // Liste de tout les mots de la base de donnée
    lateinit var motListDisplayed : ArrayList<Mot> // Liste de mot utiliser pour afficher
    lateinit var recycler : RecyclerView

    lateinit var btnAjouter : Button
    lateinit var radioGroup: RadioGroup
    lateinit var spinnerRecherche : Spinner
    lateinit var spinnerAjout : Spinner
    lateinit var francaisAjout : EditText
    lateinit var anglaisAjout : EditText

    lateinit var adapter : RecyclerAdapter
    lateinit var motDAO : MotDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionnary)

        //premier spinner (menu déroulant) pour filtrer la base de données
        spinnerRecherche = findViewById(R.id.menu)

        recycler = findViewById(R.id.recycler)
        radioGroup = findViewById(R.id.choixLangue)
        motList = ArrayList()
        motListDisplayed = ArrayList()
        btnAjouter = findViewById(R.id.boutonAjouter)

        //deuxième spinner (menu déroulant) pour choisir la difficulté du mot ajouté
        spinnerAjout = findViewById(R.id.menuAjout)

        francaisAjout = findViewById(R.id.nouveauMotFrancais)
        anglaisAjout = findViewById(R.id.nouveauMotAnglais)

        val helper = DatabaseHelper(this)
        motDAO = MotDAO(helper)

        motList = motDAO.getAllMot() as ArrayList<Mot>
        motListDisplayed.addAll(motList)

        // Set le bouton selectionner selon le choix de langue
        when (choixLangue) {
            "français" -> {
                radioGroup.check(R.id.boutonFrancais)
                setInfoAdapter(true)
            }
            "anglais" -> {
                radioGroup.check(R.id.boutonAnglais)
                setInfoAdapter(false)
            }
            "french" -> {
                radioGroup.check(R.id.boutonFrancais)
                setInfoAdapter(true)
            }
            "english" -> {
                radioGroup.check(R.id.boutonAnglais)
                setInfoAdapter(false)
            }
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.boutonAnglais -> setInfoAdapter(false)
                R.id.boutonFrancais -> setInfoAdapter(true)
            }
        }

        setDiffRecherche(choixDifficulte)

        /*
        * Ajout d'un event listenner qui va appeler la méthode updateDif selon
        * la difficulter selectionner dans le spinner
        *
        * source : https://stackoverflow.com/questions/46447296/android-kotlin-onitemselectedlistener-for-spinner-not-working
        * */
        spinnerRecherche.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Switch case pour le choix de la difficulté
                when (position) {
                    0 -> updateDif("facile")
                    1 -> updateDif("normal")
                    2 -> updateDif("difficile")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //Ajout d'un mot dans la base de données
        btnAjouter.setOnClickListener {
            var message = ""

            val dao = MotDAO(DatabaseHelper(this))
            var motfr = francaisAjout.text.toString().lowercase()
            var moten = anglaisAjout.text.toString().lowercase()
            var diff = spinnerAjout.selectedItem.toString().lowercase()
            if (motfr.isNotEmpty() && moten.isNotEmpty()){
                dao.insertMot(motfr,moten,diff)
                motList.clear()
                motList.addAll(motDAO.getAllMot())
                motListDisplayed.clear()
                motListDisplayed.addAll(motList)
                updateDif(diff)
                setDiffRecherche(diff)
                message = getString(R.string.mot_ajoute)
            }else{
                message = getString(R.string.champ_vide)
            }
            francaisAjout.text.clear()
            anglaisAjout.text.clear()
            it.hideKeyboard()
            Toast.makeText(this,message,Toast.LENGTH_LONG).show()
        }
    }

    /**
     * change le display du recycler view selon la difficulté
     * sélectionnée en filtrant la liste de tout les mots selon la
     * difficultée et en mettant à jour la liste displayed
     *
     * @param difficulte Difficulté choisi
     * */
    fun updateDif(difficulte : String){
        var tradDifficulte = ""

        when(difficulte){
            "facile" -> tradDifficulte = "easy"
            "normal" -> tradDifficulte = "normal"
            "difficile" -> tradDifficulte = "hard"
            "easy" -> tradDifficulte = "facile"
            "hard" -> tradDifficulte = "difficile"
        }

        println(motList)
        motListDisplayed = motList
            .filter { mot -> mot.difficulte == difficulte
                    || mot.difficulte == tradDifficulte } as ArrayList

        println(motListDisplayed)
        setInfoAdapter(radioGroup.checkedRadioButtonId == R.id.boutonFrancais)
    }

    /**
     * Affiche les mots de la base de données selon la langue choisi
     *
     * @param francais indique si la langue choisi est français et la liste est mise
     * à jour si c'est le cas
     * */
    fun setInfoAdapter(francais : Boolean){
        adapter = RecyclerAdapter(this,motListDisplayed,francais)
        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)

        recycler.layoutManager = layoutManager
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.adapter = adapter
    }

    fun setDiffRecherche(difficulte: String){
        // Set l'option de spinner selectionner selon le choix de difficultée
        when(difficulte){
            "facile"-> spinnerRecherche.setSelection(0)
            "normal"-> spinnerRecherche.setSelection(1)
            "difficile"-> spinnerRecherche.setSelection(2)
            "easy"-> spinnerRecherche.setSelection(0)
            "hard"-> spinnerRecherche.setSelection(2)
        }
    }

    /**
     * Pour fermer le clavier après avoir insérer un mot
     *
     * source : https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin
     * */
    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onResume() {
        super.onResume()

        motList.clear()
        motList.addAll(motDAO.getAllMot())
        motListDisplayed.clear()
        motListDisplayed.addAll(motList)
        adapter.notifyDataSetChanged()
    }
}
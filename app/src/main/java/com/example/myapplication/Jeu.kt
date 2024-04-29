package com.example.myapplication

import kotlin.random.Random

class Jeu(jeu: ArrayList<String>) {
   var pointage: Int = 0
   var nbErreurs: Int = 0
   val motADeviner : String

   init {
       if (jeu.isEmpty()) {
           throw IllegalArgumentException("La liste est vide.")
       }
       val randomIndex = Random.nextInt(jeu.size);
       motADeviner = jeu[randomIndex]
   }

   fun essayerUneLettre(lettre : Char) : ArrayList<Int> {
       var uneListe: ArrayList<Int>
       for(uneLettre in jeu) {
            
       }
   }





}
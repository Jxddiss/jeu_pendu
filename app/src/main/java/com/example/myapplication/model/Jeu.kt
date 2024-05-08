package com.example.myapplication.model

import kotlin.random.Random

class Jeu(var jeu: ArrayList<String>) {
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
       var uneListe: ArrayList<Int> = ArrayList()

       for (i in 0..motADeviner.length){
           if (lettre == motADeviner[i]){
               pointage++
               uneListe.add(i);
           }
       }

       return uneListe
   }

    fun estRéussi() : Boolean{
        if(pointage == motADeviner.length){
            return true
        }

        return false
    }

}
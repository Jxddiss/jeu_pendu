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
       for (i in 0 until motADeviner.length){
           if (motADeviner[i].equals(' ')){
               pointage++
           }
       }
   }

   fun essayerUneLettre(lettre : Char) : ArrayList<Int> {
       var uneListe: ArrayList<Int> = ArrayList()

       for (i in 0 until motADeviner.length){
           if (lettre.equals(motADeviner[i],true)){
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
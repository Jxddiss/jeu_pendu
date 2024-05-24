package com.example.myapplication.model

import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class JeuTest {

    private lateinit var jeu: Jeu
    private lateinit var mots: ArrayList<String>

    @Before
    fun setUp() {
        mots = arrayListOf("orange", "guitare", "avion")
        jeu = Jeu(mots)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testListeVideThrowsException() {
        val listeVide = ArrayList<String>()
        Jeu(listeVide)
    }

    @Test
    fun testInitialisation() {
        assertTrue("un des mots de la liste devrait être un des mots a deviner", mots.contains(jeu.motADeviner))
    }

    @Test
    fun testPointageAvecEspace() {
        val motsAvecEspace = arrayListOf("hello world", "père noel")
        val jeuAvecEspace = Jeu(motsAvecEspace)
        val expectedPoints = jeuAvecEspace.motADeviner.count { it == ' ' }
        assertEquals("Pointage devrait comter le nombre d'espace comme points", expectedPoints, jeuAvecEspace.pointage)
    }

    @Test
    fun testEssayerUneLettre() {
        val motTest = "Automobile"
        jeu = Jeu(arrayListOf(motTest))

        val result = jeu.essayerUneLettre('b')
        assertEquals("Pointage devrait augmenter de 1", 1, jeu.pointage)

        val resultNonTrouver = jeu.essayerUneLettre('x')
        assertTrue("Liste résultat devrait être vide si la lettre est pas dans le mot", resultNonTrouver.isEmpty())
    }

    @Test
    fun testJeuReussie() {
        val motTest = "piste"
        jeu = Jeu(arrayListOf(motTest))

        val lettreArray = arrayListOf('p', 'i', 's', 't', 'e')
        for(lettre in lettreArray){
            jeu.essayerUneLettre(lettre)
        }
        assertTrue("Le jeu devrait être réussie", jeu.estRéussi())
    }

    @Test
    fun testJeuEchouer() {
        val motTest = "avion"
        jeu = Jeu(arrayListOf(motTest))

        val lettreArray = arrayListOf('a', 't', 's', 't', 'e')
        for(lettre in lettreArray){
            jeu.essayerUneLettre(lettre)
        }
        assertFalse("Le jeu devrait être non réussie (echoué, lol)", jeu.estRéussi())
    }

}
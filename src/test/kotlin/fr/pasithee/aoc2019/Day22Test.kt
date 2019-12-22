package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day22Test {
    @Test
    fun dealIntoNewStackTest() {
        val deck = CardShuffler(9)
        deck.dealIntoNewStack()
        assertEquals(listOf(9,8,7,6,5,4,3,2,1,0), deck.deck)
    }

    @Test
    fun cutNCardsPositive() {
        val deck = CardShuffler(9)
        deck.cutN(3)
        assertEquals(listOf(3,4,5,6,7,8,9,0,1,2), deck.deck)
    }

    @Test
    fun cutNCardsNegative() {
        val deck = CardShuffler(9)
        deck.cutN(-4)
        assertEquals(listOf(6,7,8,9,0,1,2,3,4,5), deck.deck)
    }

    @Test
    fun dealWithIncrement() {
        val deck = CardShuffler(9)
        deck.dealWithIncrement(3)
        assertEquals(listOf(0,7,4,1,8,5,2,9,6,3), deck.deck)
    }

    @Test
    fun doOperations() {
        val ops = listOf(
            "deal into new stack",
            "cut -2",
            "deal with increment 7",
            "cut 8",
            "cut -4",
            "deal with increment 7",
            "cut 3",
            "deal with increment 9",
            "deal with increment 3",
            "cut -1"
        )
        val deck = CardShuffler(9)
        deck.shuffle(ops)
        assertEquals(listOf(9,2,5,8,1,4,7,0,3,6), deck.deck)
    }
}
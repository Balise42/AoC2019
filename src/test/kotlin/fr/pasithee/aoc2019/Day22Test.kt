package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test
import java.math.BigInteger

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

    @Test
    fun largeDeckStack() {
        val deck = LargeCardShuffler(9)
        assertEquals(6, deck.cardDealStack(3))
    }

    @Test
    fun largeDeckCut() {
        val deck = LargeCardShuffler(9)
        assertEquals(0, deck.cardCutN(3, 7))
        assertEquals(8, deck.cardCutN(3, 5))
        assertEquals(8, deck.cardCutN(-4, 2))
        assertEquals(3, deck.cardCutN(-4, 7))
    }

    @Test
    fun largeDeckDeal() {
        val deck = LargeCardShuffler(9)
        assertEquals(7, deck.cardDealWithIncrement(3, 1))
        assertEquals(0, deck.cardDealWithIncrement(5, 0))
        assertEquals(1, deck.cardDealWithIncrement(3, 3))
        assertEquals(6, deck.cardDealWithIncrement(3, 8))
        assertEquals(8, deck.cardDealWithIncrement(3, 4))
    }

    @Test
    fun largeDeckOperations() {
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
        val deck = LargeCardShuffler(9)

        assertEquals(9, deck.shuffle(ops, 0))
        assertEquals(2, deck.shuffle(ops, 1))
        assertEquals(5, deck.shuffle(ops, 2))
        assertEquals(8, deck.shuffle(ops, 3))
        assertEquals(1, deck.shuffle(ops, 4))
        assertEquals(4, deck.shuffle(ops, 5))
        assertEquals(7, deck.shuffle(ops, 6))
        assertEquals(0, deck.shuffle(ops, 7))
        assertEquals(3, deck.shuffle(ops, 8))
        assertEquals(6, deck.shuffle(ops, 9))
    }

    @Test
    fun reducedDeckStack() {
        val ops = listOf("deal into new stack")
        val deck = ReducedCardShuffler(BigInteger.valueOf(10), ops)
        assertEquals(6, deck.apply(BigInteger.valueOf(3)))
    }

    @Test
    fun reducedDeckDealPositive() {
        val ops = listOf("deal with increment 3")
        val deck = ReducedCardShuffler(BigInteger.valueOf(10), ops)
        assertEquals(7, deck.apply(BigInteger.ONE))
    }

    @Test
    fun testInverseModulo() {
        assertEquals(4, modInverse(3, 11))
    }
}
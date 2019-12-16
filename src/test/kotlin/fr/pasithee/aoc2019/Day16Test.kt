package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day16Test {
    @Test
    fun patternGenerationShouldWork() {
        val expected = listOf(0, 0, 0, 1, 1, 1, 0,0,0,-1,-1, -1)
        assertEquals(expected, FFT().getPattern(3))
    }

    @Test
    fun computeDigitShouldWork() {
        val digits = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        assertEquals(4, FFT().computeDigit(digits, 0))
        assertEquals(8, FFT().computeDigit(digits, 1))
        assertEquals(5, FFT().computeDigit(digits, 6))
    }

    @Test
    fun computePhaseShouldWork() {
        val expected = listOf(3, 4, 0, 4, 0, 4, 3, 8)
        val digits = listOf(4,8,2,2,6,1,5,8)
        assertEquals(expected, FFT().computePhase(digits))
    }

    @Test
    fun numberToDigitShouldWork() {
        val expected = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        assertEquals(expected, FFT().numberToDigits("12345678"))
    }

    @Test
    fun computeFinalOutputShouldWork() {
        assertEquals(listOf(0, 1, 0, 2, 9, 4, 9, 8), FFT().computeFinalOutput(listOf(1, 2, 3, 4, 5, 6, 7, 8), 4))
        assertEquals(listOf(2, 4, 1, 7, 6, 1, 7, 6), FFT().computeFinalOutput(FFT().numberToDigits("80871224585914546619083218645595"), 100))
    }
}
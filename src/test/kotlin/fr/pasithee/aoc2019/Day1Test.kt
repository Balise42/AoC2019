package fr.pasithee.aoc2019

import org.junit.Test
import kotlin.test.assertEquals

class Day1Test {
    @Test
    fun computeFuelShouldReturnCorrectValues() {
        val inputs = arrayListOf(12, 14, 1969, 100756)
        val res = arrayListOf(2, 2, 654, 33583)

        for (i in 0..3) {
            assertEquals(res[i], computeFuel(inputs[i]))
        }
    }

    @Test
    fun computeTotalFuelShouldReturnCorrectValue() {
        val inputs = arrayListOf(12, 14, 1969, 100756)
        var res = 0
        for (i in 0..3) {
            res = res + computeFuel(inputs[i])
        }
        assertEquals(res, computeTotalFuel(inputs))
    }

    @Test
    fun computeFuelIncludingAdditionalShouldReturnCorrectValue() {
        val inputs = arrayListOf(14, 1969, 100756)
        val res = arrayListOf(2, 966, 50346)

        for (i in 0..2) {
            assertEquals(res[i], computeFuelIncludingAdditional(inputs[i]))
        }
    }

    @Test
    fun computeTotalFuelIncludingAdditionalShouldReturnCorrectValue() {
        val inputs = arrayListOf(12, 14, 1969, 100756)
        var res = 0
        for (i in 0..3) {
            res = res + computeFuelIncludingAdditional(inputs[i])
        }
        assertEquals(res, computeTotalFuelIncludingAdditional(inputs))
    }
}
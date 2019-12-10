package fr.pasithee.aoc2019

import org.junit.Test
import kotlin.test.assertEquals

class UtilsTest {
    @Test
    fun shouldParseIntegerFiles() {
        val expected = arrayListOf(1, 8, 12345, -9, 5)
        val res = readFileToIntegers(javaClass.getResource("integers.txt").path)
        assertEquals(expected, res)
    }

    @Test
    fun shouldParseCoordinates() {
        val expected = setOf(
            Pair(1, 0),
            Pair(4, 0),
            Pair(0, 2),
            Pair(1, 2),
            Pair(2, 2),
            Pair(3, 2),
            Pair(4, 2),
            Pair(4, 3),
            Pair(3, 4),
            Pair(4, 4)
        )
        val res = readFileToCoordinates(javaClass.getResource("points.txt").path, '#')
        assertEquals(expected, res.toSet())
    }
}
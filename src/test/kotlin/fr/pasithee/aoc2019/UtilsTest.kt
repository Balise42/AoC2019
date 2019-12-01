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
}
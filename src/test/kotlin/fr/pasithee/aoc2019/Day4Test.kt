package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day4Test {
    @Test
    fun isValidPassword1ShouldReturnCorrectResults() {
        assertTrue(Day4(111111).isValidPasswordForPart1())
        assertFalse(Day4(223450).isValidPasswordForPart1())
        assertFalse(Day4(123789).isValidPasswordForPart1())
        assertTrue(Day4(122345).isValidPasswordForPart1())
    }

    @Test
    fun isValidPassword2ShouldReturnCorrectResults() {
        assertFalse(Day4(111111).isValidPasswordForPart2())
        assertFalse(Day4(223450).isValidPasswordForPart2())
        assertFalse(Day4(123789).isValidPasswordForPart2())
        assertTrue(Day4(122345).isValidPasswordForPart2())
        assertTrue(Day4(112233).isValidPasswordForPart2())
        assertFalse(Day4(123444).isValidPasswordForPart2())
        assertTrue(Day4(111122).isValidPasswordForPart2())
        assertTrue(Day4(112345).isValidPasswordForPart2())
    }
}
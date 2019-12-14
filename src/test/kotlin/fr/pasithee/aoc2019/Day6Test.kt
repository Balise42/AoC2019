package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day6Test {
    @Test
    fun computeOrbitalChecksumShouldReturnCorrectResultOnExample() {
        val orbits = readFileToStrings(Day6Test().javaClass.getResource("day6example.txt").path)
        val om = OrbitalMap(orbits)
        assertEquals(42, om.checksum())
    }

    @Test
    fun computePathLengthShouldReturnCorrectResultOnExample() {
        val orbits = readFileToStrings(Day6Test().javaClass.getResource("day6-2example.txt").path)
        val om = OrbitalMap(orbits)
        assertEquals(4, om.transfers("SAN", "YOU"))
    }
}
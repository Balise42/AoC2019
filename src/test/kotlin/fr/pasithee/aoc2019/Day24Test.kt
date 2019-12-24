package fr.pasithee.aoc2019

import org.junit.Test
import kotlin.test.assertEquals

class Day24Test {
    @Test
    fun example1ShouldWork() {
        val map = readFileToCoordinates(Day24().javaClass.getResource("day24-1.txt").path, '#')
        assertEquals(2129920, ErisEvolution(map).findRepeatingLayoutBiodiversity())
    }

    @Test
    fun nextGen() {
        val map = readFileToCoordinates(Day24().javaClass.getResource("day24-1.txt").path, '#')
        val nextGen =  readFileToCoordinates(Day24().javaClass.getResource("day24-nextgen.txt").path, '#')
        assertEquals(ErisState(nextGen).getBugs(), ErisState(map).nextGen().getBugs())
    }

    @Test
    fun getNeighbors() {
        val map = readFileToCoordinates(Day24().javaClass.getResource("day24-1.txt").path, '#')
        val eris = ErisState(map)
        assertEquals(1, eris.getNeighbors(0, 0))
        assertEquals(0, eris.getNeighbors(1, 0))
        assertEquals(0, eris.getNeighbors(2, 0))
        assertEquals(2, eris.getNeighbors(3, 0))
        assertEquals(0, eris.getNeighbors(4, 0))
        assertEquals(1, eris.getNeighbors(0, 1))
        assertEquals(1, eris.getNeighbors(1, 1))
        assertEquals(1, eris.getNeighbors(2, 1))
        assertEquals(1, eris.getNeighbors(3, 1))
        assertEquals(3, eris.getNeighbors(4, 1))
    }

    @Test
    fun bioDiversityExample2ShouldWork() {
        val map = readFileToCoordinates(Day24().javaClass.getResource("day24-2.txt").path, '#')
        assertEquals(2129920, ErisState(map).biodiversity())
    }

    @Test
    fun countBugsInitialStateRecursive() {
        val map = readFileToCoordinates(Day24().javaClass.getResource("day24-1.txt").path, '#')
        assertEquals(8, RecursiveEris(map).getBugs())
    }

    @Test
    fun countBugsAfterTenSteps() {
        val map = readFileToCoordinates(Day24().javaClass.getResource("day24-1.txt").path, '#')
        val eris = RecursiveEris(map)
        for (i in 1..10) {
            eris.nextGen()
        }
        assertEquals(99, eris.getBugs())
    }
}
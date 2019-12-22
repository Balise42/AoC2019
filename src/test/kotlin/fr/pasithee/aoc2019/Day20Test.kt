package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day20Test {
    @Test
    fun maze1() {
        val input = readFileToStrings(Day20Test().javaClass.getResource("day20-1.txt").path)
        val pluto = Pluto(input)
        assertEquals(pluto.maze.getValue(Position(2, 8)).west!!.pos, Position(9, 6))
        assertEquals(pluto.maze.getValue(Position(9, 6)).south!!.pos, Position(2, 8))
        assertEquals(23, pluto.findShortestPath())
    }

    @Test
    fun maze2() {
        val input = readFileToStrings(Day20Test().javaClass.getResource("day20-2.txt").path)
        val pluto = Pluto(input)
        assertEquals(58, pluto.findShortestPath())
    }


}
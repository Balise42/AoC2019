package fr.pasithee.aoc2019

import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day18Test {
    @Test
    fun example1() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-1.txt").path)
        val graph = MapGraph(input)
        val res1 = graph.computeDistances('a', 'b', setOf('a'))
        assertEquals(6, res1.first)
        assertEquals(setOf('a', 'b'), graph.getKeysFromNodeList(res1.second))
        val res2 = graph.computeDistances('@', 'b', emptySet())
        assertEquals(setOf('a', 'b'), graph.getKeysFromNodeList(res2.second))
    }
}
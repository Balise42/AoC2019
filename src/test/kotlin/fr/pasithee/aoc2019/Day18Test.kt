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
        graph.getAllKeys(2)
    }

    @Test
    fun example2() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-4.txt").path)
        val graph = MapGraph(input)
        graph.getAllKeys(16)
    }
}
package fr.pasithee.aoc2019

import org.junit.Test

class Day18Test {
    @Test
    fun example1() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-1.txt").path)
        val graph = MapGraphPart1(input)
        graph.getAllKeys(2, '@')
    }

    @Test
    fun example2() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-4.txt").path)
        val graph = MapGraphPart1(input)
        graph.getAllKeys(16, '@')
    }


}
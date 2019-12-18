package fr.pasithee.aoc2019

import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day18Test {
    @Test
    fun example1FirstBfs() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-1.txt").path)
        val g = makeGraph(input)
        val explorer = MapExplorer(g, getPosFromMap(input))
        assertEquals(mapOf(Pair('a', 2)), explorer.bfs())
    }

    @Test
    fun example2FirstBfs() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-2.txt").path)
        val g = makeGraph(input)
        val explorer = MapExplorer(g, getPosFromMap(input))
        assertEquals(mapOf(Pair('a', 2)), explorer.bfs())
    }

    @Test
    fun example2LaterBfs() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-2-later.txt").path)
        val g = makeGraph(input)
        val explorer = MapExplorer(g, getPosFromMap(input))
        assertEquals(mapOf(Pair('e', 14), Pair('d', 24)), explorer.bfs())
    }

    @Test
    fun example3FirstBfs() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-3.txt").path)
        val g = makeGraph(input)
        val explorer = MapExplorer(g, getPosFromMap(input))
        assertEquals(mapOf(Pair('a', 2), Pair('b', 22)), explorer.bfs())
    }

    @Test
    fun example4FirstBfs() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-4.txt").path)
        val g = makeGraph(input)
        val explorer = MapExplorer(g, getPosFromMap(input))
        assertEquals(mapOf(Pair('a', 3), Pair('b', 3), Pair('f', 3), Pair('g', 3), Pair('c', 5), Pair('e', 5) ,Pair('d',5), Pair('h', 5)), explorer.bfs())
    }

    @Test
    fun example5FirstBfs() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-5.txt").path)
        val g = makeGraph(input)
        val explorer = MapExplorer(g, getPosFromMap(input))
        assertEquals(mapOf(Pair('a', 15), Pair('d', 3), Pair('e', 5), Pair('f', 7)), explorer.bfs())
    }

    @Test
    fun example2MultipleSteps() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-2.txt").path)
        val g = makeGraph(input)
        val initConfig = ConfigNode("", g, getPosFromMap(input), 0)
        var neighbors = initConfig.getOrComputeNeighbors()
        assertEquals(1, neighbors.size)
        neighbors = neighbors[0].getOrComputeNeighbors()
        assertEquals(1, neighbors.size)
        neighbors = neighbors[0].getOrComputeNeighbors()
        assertEquals(1, neighbors.size)
        neighbors = neighbors[0].getOrComputeNeighbors()
        assertEquals(2, neighbors.size)
        assertEquals(setOf("abcd", "abce"), setOf(neighbors[0].keys, neighbors[1].keys))
    }

    @Test
    fun example1Part1() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-1.txt").path)
        val g = makeGraph(input)
        val initConfig = ConfigNode("", g, getPosFromMap(input), 0)
        val solver = Part1Solver(initConfig)
        assertEquals(8, solver.currMin)
    }

    @Test
    fun example2Part1() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-2.txt").path)
        val g = makeGraph(input)
        val initConfig = ConfigNode("", g, getPosFromMap(input), 0)
        initConfig.numKeys = 6
        initConfig.avgPerKey = 12
        val solver = Part1Solver(initConfig)
        assertTrue(solver.currMin >= 86)
        solver.dfs(6)
        assertEquals(86, solver.currMin)
    }

    @Test
    fun example3Part1() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-3.txt").path)
        val g = makeGraph(input)
        val initConfig = ConfigNode("", g, getPosFromMap(input), 0)
        initConfig.numKeys = 7
        initConfig.avgPerKey = 19
        val solver = Part1Solver(initConfig)
        solver.dfs(7)
        assertEquals(132, solver.currMin)
    }

    @Test
    fun example4Part1() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-4.txt").path)
        val g = makeGraph(input)
        val initConfig = ConfigNode("", g, getPosFromMap(input), 0)
        initConfig.numKeys = 16
        initConfig.avgPerKey = 11
        val solver = Part1Solver(initConfig)
        solver.dfs(16)
        assertEquals(136, solver.currMin)
    }

    @Test
    fun example5Part1() {
        val input = readFileToStrings(Day6Test().javaClass.getResource("day18-5.txt").path)
        val g = makeGraph(input)
        val initConfig = ConfigNode("", g, getPosFromMap(input), 0)
        initConfig.numKeys = 9
        initConfig.avgPerKey = 9
        val solver = Part1Solver(initConfig)
        solver.dfs(9)
        assertEquals(81, solver.currMin)
    }
}
package fr.pasithee.aoc2019

class MapNode(val x : Int, val y : Int, val value : Char = '.') {
    fun getNeighbors(graph: Map<Pair<Int, Int>, MapNode>): List<Pair<Int, Int>> {
        val candidates = listOf(Pair(x-1,y), Pair(x+1,y), Pair(x, y-1), Pair(x, y+1))
        return candidates.filter {
            graph.containsKey(it) && isPassable(graph.getValue(it).value)
        }
    }

    private fun isPassable(value: Char): Boolean = (value == '.' || value in 'a'..'z')

}

class ConfigNode(val keys : String, val graph : Map<Pair<Int, Int>, MapNode>, val pos : Pair<Int, Int>, val moved : Int) {
    var neighbors : List<ConfigNode>? = null

    var numKeys = 26
    var avgPerKey = 221

    fun hstar() : Int {
        if (getOrComputeNeighbors().size > 0) {
            return avgPerKey * (numKeys - keys.length) - moved + (getOrComputeNeighbors()[0].moved - moved)
        } else {
            return 0
        }
    }

    fun getOrComputeNeighbors(): List<ConfigNode> {
        if (neighbors != null) {
            return neighbors!!
        }
        val res = mutableListOf<ConfigNode>()
        val availableKeys = MapExplorer(graph, pos).bfs()

        for (k in availableKeys.keys) {
            val newPos = graph.filter { it.value.value == k }.keys.first()
            val newMoved = moved + availableKeys.getValue(k)
            val newGraph = graph.toMutableMap()
            // remove the key from the graph
            newGraph[newPos] = MapNode(newPos.first, newPos.second, '.')
            // remove the door from the graph
            val door = graph.filter { it.value.value == k.toUpperCase() }.keys
            if (door.isNotEmpty()) {
                val doorPos = door.first()
                newGraph[doorPos] = MapNode(doorPos.first,doorPos.second, '.')
            }
            val newConfigNode = ConfigNode(keys + k,  newGraph, newPos, newMoved)
            newConfigNode.numKeys = numKeys
            newConfigNode.avgPerKey = avgPerKey
            res.add(newConfigNode)
        }
        neighbors = res.sortedBy { it.moved - moved }
        return neighbors!!
    }
}

class MapExplorer(val graph : Map<Pair<Int, Int>, MapNode>, val start : Pair<Int, Int>) {
    fun bfs(): Map<Char, Int> {
        val Q = mutableListOf(start)
        val discovered = mutableMapOf<Pair<Int, Int>, Int>()
        val keys = mutableMapOf<Char, Int>()
        discovered[start] = 0

        while (Q.isNotEmpty()) {
            val v = Q.removeAt(Q.lastIndex)
            if (graph.getValue(v).value in 'a'..'z') {
                keys[graph.getValue(v).value] = discovered.getValue(v)
                // we consider "new key" as "let's stop there and reinit the algo", otherwise it's messier. probably.
                continue
            }
            for (n in graph.getValue(v).getNeighbors(graph)) {
                if (!discovered.containsKey(n)) {
                    discovered[n] = discovered.getValue(v) + 1
                    Q.add(n)
                }
            }
        }
        return keys
    }
}

class Part1Solver(val config : ConfigNode) {
    var currMin = computeFirstPathLength(config)

    private fun computeFirstPathLength(config: ConfigNode): Int {
        var node = config
        while(true) {
            val neighbors = node.getOrComputeNeighbors()
            if (neighbors.isEmpty()) {
                return node.moved
            }
            node = neighbors[0]
        }
    }

    fun dfs(numKeys : Int) {
        val S = mutableListOf(config)
        val visited = mutableSetOf<String>()
        while (S.isNotEmpty()) {
            S.sortBy { - it.hstar() }
            val v = S.removeAt(0)
            if (visited.contains(v.keys)) {
                continue
            } else {
                visited.add(v.keys)
            }
            if (v.moved >= currMin) {
                // prune because we know this is not a good path
                continue
            }
            val neighbors = v.getOrComputeNeighbors()
            if (v.keys.length == numKeys) {
                currMin = v.moved
                println(currMin)
                continue
            }
            for (n in neighbors) {
                S.add(0, n)
            }
        }
    }
}

fun makeGraph(input : List<String>, keys : List<Char> = emptyList()) : Map<Pair<Int,Int>, MapNode> {
    val res = mutableMapOf<Pair<Int, Int>, MapNode>()
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] in keys || input[y][x] - ('A'-'a') in keys) {
                res[Pair(x, y)] = MapNode(x, y, '.')
            } else if (input[y][x] == '@'){
                res[Pair(x, y)] = MapNode(x, y, '.')
            } else {
                res[Pair(x, y)] = MapNode(x, y, input[y][x])
            }
        }
    }
    return res.toMap()
}

fun getPosFromMap(input : List<String>, search : Char= '@') : Pair<Int, Int> {
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == search) {
                return Pair(x, y)
            }
        }
    }
    throw(IllegalStateException("Can't find initial position"))
}

class Day18 {}

fun main() {
    val input = readFileToStrings(Day18().javaClass.getResource("day18.txt").path)
    val g = makeGraph(input)
    val initConfig = ConfigNode("", g, getPosFromMap(input), 0)
    val solver = Part1Solver(initConfig)
    solver.dfs(26)
    println(solver.currMin)
}
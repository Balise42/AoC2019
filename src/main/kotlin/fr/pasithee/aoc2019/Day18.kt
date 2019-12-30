package fr.pasithee.aoc2019

class MapNode(val x : Int, val y : Int, val value : Char = '.') {
    val coords = Pair(x, y)
    fun getNeighbors(graph: Map<Pair<Int, Int>, MapNode>): List<Pair<Int, Int>> {
        val candidates = listOf(Pair(x-1,y), Pair(x+1,y), Pair(x, y-1), Pair(x, y+1))
        return candidates.filter {
            graph.containsKey(it) && isPassable(graph.getValue(it).value)
        }
    }

    private fun isPassable(value: Char): Boolean = (value != '#')

}


class MapGraph(input : List<String>) {
    val graph : Map<Pair<Int,Int>, MapNode>
    val start : Pair<Int, Int>
    val namedNodes : Map<Char, MapNode>

    val dists : Map<Pair<Char, Char>, Int>

    init {
        val mutableGraph = mutableMapOf<Pair<Int, Int>, MapNode>()
        val mutableNamedNodes = mutableMapOf<Char, MapNode>()
        var mutableStart = Pair(-1, -1)
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (input[y][x] == '@'){
                    mutableStart = Pair(x, y)
                    mutableGraph[Pair(x, y)] = MapNode(x, y, '.')
                    mutableNamedNodes['@'] = mutableGraph.getValue(Pair(x, y))
                } else {
                    mutableGraph[Pair(x, y)] = MapNode(x, y, input[y][x])
                    if (input[y][x] != '.' && input[y][x] != '#') {
                        mutableNamedNodes[input[y][x]] = mutableGraph.getValue(Pair(x, y))
                    }
                }
            }
        }
        start = mutableStart
        graph = mutableGraph.toMap()
        namedNodes = mutableNamedNodes.toMap()
        dists = computeDistMap()
    }

    private fun computeDistMap(): Map<Pair<Char, Char>, Int> {
        val mutableDists = mutableMapOf<Pair<Char, Char>, Int>()
        for (k in namedNodes.keys) {
            computeDistances(k, mutableDists)
        }
        return mutableDists
    }

    private fun computeDistances(k: Char, mutableDists: MutableMap<Pair<Char, Char>, Int>) {
        val visitedDists = mutableMapOf<Pair<Int, Int>, Int>()
        visitedDists[namedNodes.getValue(k).coords] = 0

        val Q = mutableListOf(namedNodes.getValue(k))

        while (Q.isNotEmpty()) {
            Q.sortBy { visitedDists.getOrDefault(it.coords, Int.MAX_VALUE) }
            val u = Q.removeAt(0)
            for (neighbor in u.getNeighbors(graph)) {
                val alt = visitedDists.getValue(u.coords) + 1
                if (alt < visitedDists.getOrDefault(neighbor, Int.MAX_VALUE)) {
                    visitedDists[neighbor] = alt
                    if (graph.getValue(neighbor).value == '.') {
                        Q.add(graph.getValue(neighbor))
                    }
                }
            }
        }

        for (kv in visitedDists) {
            val key = kv.key
            if (graph.getValue(key).value != '.' && graph.getValue(key).value != k) {
                mutableDists[Pair(k, graph.getValue(key).value)] = kv.value
                mutableDists[Pair(graph.getValue(key).value, k)] = kv.value
            }
        }
    }

    fun getAllKeys(numKeys : Int) {
        val visitedDists = mutableMapOf<Pair<Char, Set<Char>>, Int>()
        visitedDists[Pair('@', emptySet())] = 0

        val Q = mutableListOf<Pair<Char, Set<Char>>>(Pair('@', emptySet()))
        while (Q.isNotEmpty()) {
            Q.sortBy { visitedDists.getOrDefault(it, Int.MAX_VALUE) }
            val u = Q.removeAt(0)
            if (u.second.size == numKeys) {
                println(visitedDists[u])
                break
            }
            for (neighbor in getNeighbors(u.first, u.second)) {
                val alt = visitedDists.getValue(u) + dists.getValue(Pair(neighbor, u.first))
                var shouldAddToQ = true

                for (key in visitedDists.keys) {
                    if (key.first == neighbor && alt >= visitedDists.getOrDefault(Pair(neighbor, key.second), Int.MAX_VALUE) && key.second.containsAll(u.second)) {
                        shouldAddToQ = false
                    }
                }
                if (shouldAddToQ) {
                    val keySet = if (neighbor in 'a'..'z') { u.second + neighbor } else {u.second}
                    Q.add(Pair(neighbor, keySet))
                    visitedDists[Pair(neighbor, keySet)] = alt
                }
            }
        }
    }

    fun getNeighbors(node: Char, keys: Set<Char>): List<Char> {
        val neighbors = mutableListOf<Char>()
        if (node in 'A'..'Z' && node.toLowerCase() !in keys) {
            return neighbors
        }
        for (k in dists) {
            if (k.key.first == node && k.value < Integer.MAX_VALUE) {
                neighbors.add(k.key.second)
            }
        }
        return neighbors
    }
}


class Day18 {}

fun main() {
    val input = readFileToStrings(Day18().javaClass.getResource("day18.txt").path)
    val graph = MapGraph(input)
    graph.getAllKeys(26)
}
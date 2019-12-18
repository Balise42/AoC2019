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

class MapGraph(input : List<String>) {
    val graph : Map<Pair<Int,Int>, MapNode>
    val namedNodes : Map<Char, MapNode>
    val start : Pair<Int, Int>

    private val knownDistances = mutableMapOf<Pair<Pair<Char, Char>, Set<Char>>, Pair<Int, List<MapNode>>>()

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
                    mutableNamedNodes[input[y][x]] = mutableGraph.getValue(Pair(x, y))
                }
            }
        }
        start = mutableStart
        graph = mutableGraph.toMap()
        namedNodes = mutableNamedNodes.toMap()
    }

    fun computeDistances(a: Char, b: Char, keys : Set<Char>) : Pair<Int, List<MapNode>> {
        if (knownDistances.containsKey(Pair(Pair(a, b), keys))) {
            return knownDistances.getValue(Pair(Pair(a, b), keys))
        }

        val Q = mutableListOf(namedNodes.getValue(a).coords)
        val distances = mutableMapOf<Pair<Int, Int>, Int>()
        for (v in graph.keys) {
            distances[v] = Int.MAX_VALUE
            Q.add(v)
        }
        val parent = mutableMapOf<Pair<Int, Int>, MapNode>()

        distances[namedNodes.getValue(a).coords] = 0

        while (Q.isNotEmpty()) {
            Q.sortBy { distances.getValue(it) }
            val u = Q.removeAt(0)

            val curr = graph.getValue(u).value
            if (curr in 'A'..'Z') {
                if (!keys.contains(curr.toLowerCase())) {
                    val aToKey = computeDistances(a, curr.toLowerCase(), keys)
                    val bToKey = computeDistances(
                        curr.toLowerCase(),
                        b,
                        keys.union(getKeysFromNodeList(aToKey.second))
                    )
                    val alt = aToKey.first + bToKey.first
                    if (alt < distances.getValue(u)) {
                        distances[u] = alt
                    }
                }
            }

            if (graph.getValue(u).value == b) {
                val parents = getParentLists(u, parent)
                knownDistances.put(Pair(Pair(a, b), keys), Pair(distances.getValue(u), parents))
                return (Pair(distances.getValue(u), parents))
            }

            for (v in graph.getValue(u).getNeighbors(graph)) {
                val neighbor = graph.getValue(v)
                var alt = Int.MAX_VALUE

                alt = distances.getValue(u) + 1

                if (alt < distances.getOrDefault(v, Int.MAX_VALUE)) {
                    distances[v] = alt
                    parent[v] = graph.getValue(u)
                }
            }
        }
        throw (IllegalStateException("Couldn't find path to node"))
    }

    fun getKeysFromNodeList(nodes: List<MapNode>): Set<Char> = nodes.filter { it.value in 'a'..'z' }.map {it.value}.toSet()

    private fun getParentLists(u: Pair<Int, Int>, parent: MutableMap<Pair<Int, Int>, MapNode>): List<MapNode> {
        val parents = mutableListOf(graph.getValue(u))
        var curr = u
        while (parent[curr] != null) {
            parents.add(parent[curr]!!)
            curr = parent[curr]!!.coords
        }
        return parents
    }
}

class Day18 {}

fun main() {
    val input = readFileToStrings(Day18().javaClass.getResource("day18.txt").path)

}
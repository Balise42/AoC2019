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


open class MapGraphPart1(input : List<String>) {
    val graph : MutableMap<Pair<Int,Int>, MapNode>
    val start : Pair<Int, Int>
    val namedNodes : MutableMap<Char, MapNode>

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
        graph = mutableGraph
        namedNodes = mutableNamedNodes
    }

    fun computeDistMap(): Map<Pair<Char, Char>, Int> {
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
        val dists = computeDistMap()
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
            for (neighbor in getNeighbors(u.first, u.second, dists)) {
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

    fun getNeighbors(
        node: Char,
        keys: Set<Char>,
        dists: Map<Pair<Char, Char>, Int>
    ): List<Char> {
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


class MapGraphPart2(input: List<String>) : MapGraphPart1(input) {
    init {
        val oldStart = namedNodes.remove('@')
        graph[oldStart!!.coords] = MapNode(oldStart.x, oldStart.y, '#')
        graph[Pair(oldStart.x-1, oldStart.y-1)] = MapNode(oldStart.x - 1, oldStart.y - 1, '1')
        graph[Pair(oldStart.x+1, oldStart.y-1)] = MapNode(oldStart.x + 1, oldStart.y - 1, '2')
        graph[Pair(oldStart.x-1, oldStart.y+1)] = MapNode(oldStart.x - 1, oldStart.y + 1, '3')
        graph[Pair(oldStart.x+1, oldStart.y+1)] = MapNode(oldStart.x + 1, oldStart.y + 1, '4')
        namedNodes['1'] = graph[Pair(oldStart.x-1, oldStart.y-1)]!!
        namedNodes['2'] = graph[Pair(oldStart.x+1, oldStart.y-1)]!!
        namedNodes['3'] = graph[Pair(oldStart.x-1, oldStart.y+1)]!!
        namedNodes['4'] = graph[Pair(oldStart.x+1, oldStart.y+1)]!!
        graph[Pair(oldStart.x, oldStart.y-1)] = MapNode(oldStart.x, oldStart.y - 1, '#')
        graph[Pair(oldStart.x-1, oldStart.y)] = MapNode(oldStart.x - 1, oldStart.y, '#')
        graph[Pair(oldStart.x+1, oldStart.y)] = MapNode(oldStart.x + 1, oldStart.y, '#')
        graph[Pair(oldStart.x, oldStart.y+1)] = MapNode(oldStart.x, oldStart.y + 1, '#')
    }




    fun run() {
        val dists = computeDistMap()
        val start = QuadrupleState(listOf('1', '2', '3', '4'), listOf(emptySet(), emptySet(), emptySet(), emptySet()), listOf(0,0,0,0))
        val neighbors = start.getQuadrupleStateNeighbors(dists)
        val neighbors2 = mutableSetOf<QuadrupleState>()
        for (n in neighbors) {
            neighbors2.addAll(n.getQuadrupleStateNeighbors(dists))
        }
        val neighbors3 = filterNeighbors(neighbors2)
        println(neighbors2.size)
        println(neighbors3.size)
    }

    private fun filterNeighbors(set: MutableSet<QuadrupleState>): MutableSet<QuadrupleState> {
        val filteredSet = mutableSetOf<QuadrupleState>()
        for (n in set) {
            val filteredSetCopy = mutableSetOf<QuadrupleState>()
            filteredSetCopy.addAll(filteredSet)
            val samePos = filteredSetCopy.filter { it.positions == n.positions }
            if (samePos.isEmpty()) {
                filteredSet.add(n)
                continue
            } else {
                for (candidate in samePos) {
                    if (n.allKeys.containsAll(candidate.allKeys) && candidate.totalElapsed >= n.totalElapsed) {
                        filteredSet.remove(candidate)
                        filteredSet.add(n)
                        continue
                    } else if (candidate.allKeys.containsAll(n.allKeys) && candidate.totalElapsed < n.totalElapsed) {
                        continue
                    }
                    filteredSet.add(n)
                }
            }
        }
        return filteredSet
    }
}

class QuadrupleState(val positions : List<Char>, val keys : List<Set<Char>>, val elapsed : List<Int>) {

    val allKeys = keys[0] + keys[1] + keys[2] + keys[3]
    val totalElapsed = elapsed.sum()

    fun getQuadrupleStateNeighbors(dists: Map<Pair<Char, Char>, Int>): Set<QuadrupleState> {
        val singleNeighbors = (0..3).map {
            getSingleStateNeighbors(positions[it], dists)
        }

        val res = mutableSetOf<QuadrupleState>()

        for (i in singleNeighbors[0]) {
            for (j in singleNeighbors[1]) {
                for (k in singleNeighbors[2]) {
                    for (l in singleNeighbors[3]) {
                        val quad = QuadrupleState(
                            listOf(i.key.first, j.key.first, k.key.first, l.key.first),
                            listOf(i.key.second, j.key.second, k.key.second, l.key.second),
                            listOf(i.value + elapsed[0], j.value + elapsed[1], k.value + elapsed[2], l.value + elapsed[3])
                        )
                        res.add(quad)
                    }
                }
            }
        }
        return res
    }

    fun getSingleStateNeighbors(start: Char, dists: Map<Pair<Char, Char>, Int>): Map<Pair<Char, Set<Char>>, Int> {
        val visitedDists = mutableMapOf<Pair<Char, Set<Char>>, Int>()
        visitedDists[Pair(start, emptySet())] = 0

        val Q = mutableListOf<Pair<Char, Set<Char>>>(Pair(start, emptySet()))
        while (Q.isNotEmpty()) {
            Q.sortBy { visitedDists.getOrDefault(it, Int.MAX_VALUE) }
            val u = Q.removeAt(0)
            for (neighbor in getNeighbors(u.first, u.second + allKeys, dists)) {
                val alt = visitedDists.getValue(u) + dists.getValue(Pair(neighbor, u.first))
                var shouldAddToQ = true

                for (key in visitedDists.keys) {
                    if (key.first == neighbor && alt >= visitedDists.getOrDefault(
                            Pair(neighbor, key.second),
                            Int.MAX_VALUE
                        ) && key.second.containsAll(u.second)
                    ) {
                        shouldAddToQ = false
                    }
                }
                if (shouldAddToQ) {
                    val keySet = if (neighbor in 'a'..'z') {
                        u.second + neighbor
                    } else {
                        u.second
                    }
                    Q.add(Pair(neighbor, keySet))
                    visitedDists[Pair(neighbor, keySet)] = alt
                }
            }
        }
        return visitedDists.filter { it.key.first in 'A'..'Z' }
    }

    fun getNeighbors(
        node: Char,
        keys: Set<Char>,
        dists: Map<Pair<Char, Char>, Int>
    ): List<Char> {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QuadrupleState

        if (positions != other.positions) return false
        if (keys != other.keys) return false
        if (elapsed != other.elapsed) return false
        if (allKeys != other.allKeys) return false

        return true
    }

    override fun hashCode(): Int {
        var result = positions.hashCode()
        result = 31 * result + keys.hashCode()
        result = 31 * result + elapsed.hashCode()
        result = 31 * result + allKeys.hashCode()
        return result
    }
}

class Day18 {}

fun main() {
    /*val input = readFileToStrings(Day18().javaClass.getResource("day18.txt").path)
    val graph = MapGraphPart1(input)
    graph.getAllKeys(26)*/
    val input = readFileToStrings(Day18().javaClass.getResource("day18.txt").path)
    val graph = MapGraphPart2(input)
    graph.run()
}
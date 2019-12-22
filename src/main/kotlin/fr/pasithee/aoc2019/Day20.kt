package fr.pasithee.aoc2019

import java.lang.IllegalStateException

class Day20 {

}

open class PlutoNode(val pos : Position) {
    fun getNeighbors(): List<PlutoNode> {
        val neighbors = mutableListOf<PlutoNode>()
        if (north != null) neighbors.add(north!!)
        if (south != null) neighbors.add(south!!)
        if (east != null) neighbors.add(east!!)
        if (west != null) neighbors.add(west!!)
        return neighbors
    }

    var north : PlutoNode? = null
    var south : PlutoNode? = null
    var east : PlutoNode? = null
    var west : PlutoNode? = null
}

class Portal(pos : Position, val id : String) : PlutoNode(pos) {
    var out : PlutoNode? = null
}

class Pluto(input : List<String>) {
    val maze : Map<Position, PlutoNode>
    init {
        val mazeInProgress = mutableMapOf<Position, PlutoNode>()
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (input[y][x] == '.') {
                    mazeInProgress[Position(x, y)] = PlutoNode(Position(x, y))
                }
                else if (input[y][x] in 'A'..'Z') {
                    if (y > 0 && input[y].length <= input[y-1].length && input[y-1][x] == '.') {
                        mazeInProgress[Position(x, y)] = Portal(Position(x, y), "" + input[y][x] + input[y+1][x])
                    } else if (y < input.lastIndex && input[y].length <= input[y+1].length && input[y+1][x] == '.') {
                        mazeInProgress[Position(x, y)] = Portal(Position(x, y), "" + input[y-1][x] + input[y][x])
                    } else if (x > 0 && input[y][x-1] == '.') {
                        mazeInProgress[Position(x, y)] = Portal(Position(x, y), "" + input[y][x] + input[y][x+1])
                    } else if (x < input[y].lastIndex && input[y][x+1] == '.') {
                        mazeInProgress[Position(x, y)] = Portal(Position(x, y), "" + input[y][x-1] + input[y][x])
                    }
                }
            }
        }

        for (node in mazeInProgress.values) {
            if (mazeInProgress.containsKey(Position(node.pos.x, node.pos.y - 1))) {
                var north = mazeInProgress.getValue(Position(node.pos.x, node.pos.y - 1))
                if (north is Portal) {
                    north = findOtherEnd(north, mazeInProgress)
                }
                node.north = north
            }
            if (mazeInProgress.containsKey(Position(node.pos.x, node.pos.y + 1))) {
                var south = mazeInProgress.getValue(Position(node.pos.x, node.pos.y + 1))
                if (south is Portal) {
                    south = findOtherEnd(south, mazeInProgress)
                }
                node.south = south
            }
            if (mazeInProgress.containsKey(Position(node.pos.x + 1, node.pos.y))) {
                var east = mazeInProgress.getValue(Position(node.pos.x + 1, node.pos.y))
                if (east is Portal) {
                    east = findOtherEnd(east, mazeInProgress)
                }
                node.east = east
            }

            if (mazeInProgress.containsKey(Position(node.pos.x - 1, node.pos.y))) {
                var west = mazeInProgress.getValue(Position(node.pos.x - 1, node.pos.y))
                if (west is Portal) {
                    west = findOtherEnd(west, mazeInProgress)
                }
                node.west = west
            }
        }


        maze = mazeInProgress
    }

    private fun findOtherEnd(
        portal: Portal,
        mazeInProgress: Map<Position, PlutoNode>
    ): PlutoNode {
        val portals = mazeInProgress.values.filterIsInstance<Portal>()
        for (p in portals) {
            if (p.id == portal.id && p.pos != portal.pos) {
                val x = p.pos.x
                val y = p.pos.y
                if (p.out == null) {
                    if (mazeInProgress.containsKey(Position(x+1, y))) {
                        p.out = mazeInProgress.getValue(Position(x+1, y))
                    } else if (mazeInProgress.containsKey(Position(x-1, y))) {
                        p.out = mazeInProgress.getValue(Position(x-1, y))
                    } else if (mazeInProgress.containsKey(Position(x, y-1))) {
                        p.out = mazeInProgress.getValue(Position(x, y-1))
                    } else if (mazeInProgress.containsKey(Position(x, y+1))) {
                        p.out = mazeInProgress.getValue(Position(x, y+1))
                    }
                }
                return p.out!!
            }
        }
        return portal
    }

    fun findShortestPath() : Int {
        val start = findOtherEnd(maze.values.filter { it is Portal && it.id == "AA" }[0] as Portal, maze)
        val end = findOtherEnd(maze.values.filter { it is Portal && it.id == "ZZ" }[0] as Portal, maze)

        val Q = mutableListOf(start.pos)
        val distances = mutableMapOf<Position, Int>()
        for (v in maze.values) {
            distances[v.pos] = Int.MAX_VALUE
            Q.add(v.pos)
        }

        distances[start.pos] = 0

        while (Q.isNotEmpty()) {
            Q.sortBy { distances.getValue(it) }
            val u = Q.removeAt(0)
            val curr = maze[u]
            for (neighbor in curr!!.getNeighbors()) {
                val alt = distances.getValue(u) + 1
                if (alt < distances.getValue(neighbor.pos)) {
                    distances[neighbor.pos] = alt
                }
            }
        }
        return distances.getValue(end.pos) - 2
    }
}

fun main() {
    val input = readFileToStrings(Day20().javaClass.getResource("day20.txt").path)
    val pluto = Pluto(input)
    println(pluto.findShortestPath())
}
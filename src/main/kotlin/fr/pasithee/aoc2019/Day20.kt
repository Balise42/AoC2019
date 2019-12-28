package fr.pasithee.aoc2019

class Day20 {

}

open class PlutoNode(val pos3D : Position3D) {
    val pos = Position(pos3D.x, pos3D.y)
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

class Portal(pos3D : Position3D, val id : String) : PlutoNode(pos3D) {
    var out : PlutoNode? = null
}

class Pluto(input : List<String>) {
    val maze : Map<Position, PlutoNode>
    init {
        val mazeInProgress = mutableMapOf<Position, PlutoNode>()
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (input[y][x] == '.') {
                    mazeInProgress[Position(x, y)] = PlutoNode(Position3D(x, y, 0))
                } else if (input[y][x] in 'A'..'Z') {
                    if (y > 0 && input[y].length <= input[y - 1].length && input[y - 1][x] == '.') {
                        mazeInProgress[Position(x, y)] = Portal(Position3D(x, y, 0), "" + input[y][x] + input[y + 1][x])
                    } else if (y < input.lastIndex && input[y].length <= input[y + 1].length && input[y + 1][x] == '.') {
                        mazeInProgress[Position(x, y)] = Portal(Position3D(x, y, 0), "" + input[y - 1][x] + input[y][x])
                    } else if (x > 0 && input[y][x - 1] == '.') {
                        mazeInProgress[Position(x, y)] = Portal(Position3D(x, y, 0), "" + input[y][x] + input[y][x + 1])
                    } else if (x < input[y].lastIndex && input[y][x + 1] == '.') {
                        mazeInProgress[Position(x, y)] = Portal(Position3D(x, y, 0), "" + input[y][x - 1] + input[y][x])
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

    fun findOtherEnd(
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
            val curr = maze[Position(u.x, u.y)]
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


class RecursivePluto(input: List<String>) {
    val maze = mutableMapOf<Position3D, PlutoNode>()
    private val maze2D = mutableMapOf<Position, PlutoNode>()
    val resolvedMaze2D = Pluto(input)
    var maxX = 0
    val maxY = input.size

    init {
        for (y in input.indices) {
            for (x in input[y].indices) {
                if (x > maxX) {
                    maxX = x
                }
                if (input[y][x] == '.') {
                    maze2D[Position(x, y)] = PlutoNode(Position3D(x, y, 0))
                } else if (input[y][x] in 'A'..'Z') {
                    if (y > 0 && input[y].length <= input[y - 1].length && input[y - 1][x] == '.') {
                        maze2D[Position(x, y)] = Portal(Position3D(x, y, 0), "" + input[y][x] + input[y + 1][x])
                    } else if (y < input.lastIndex && input[y].length <= input[y + 1].length && input[y + 1][x] == '.') {
                        maze2D[Position(x, y)] = Portal(Position3D(x, y, 0), "" + input[y - 1][x] + input[y][x])
                    } else if (x > 0 && input[y][x - 1] == '.') {
                        maze2D[Position(x, y)] = Portal(Position3D(x, y, 0), "" + input[y][x] + input[y][x + 1])
                    } else if (x < input[y].lastIndex && input[y][x + 1] == '.') {
                        maze2D[Position(x, y)] = Portal(Position3D(x, y,0), "" + input[y][x - 1] + input[y][x])
                    }
                }
            }
        }

        for (node in maze2D.values) {
            if (maze2D.containsKey(Position(node.pos.x, node.pos.y - 1))) {
                node.north = maze2D.getValue(Position(node.pos.x, node.pos.y - 1))
            }
            if (maze2D.containsKey(Position(node.pos.x, node.pos.y + 1))) {
                node.south =  maze2D.getValue(Position(node.pos.x, node.pos.y + 1))
            }
            if (maze2D.containsKey(Position(node.pos.x + 1, node.pos.y))) {
                node.east = maze2D.getValue(Position(node.pos.x + 1, node.pos.y))
            }
            if (maze2D.containsKey(Position(node.pos.x - 1, node.pos.y))) {
                node.west = maze2D.getValue(Position(node.pos.x - 1, node.pos.y))
            }
        }


        for (key in maze2D.keys) {
            get3DNode(key.x, key.y, 0)
        }
    }

    fun findShortestPath() : Int {
        val start = maze.values.filter { it is Portal && it.id == "AA" }[0] as Portal
        val end = maze.values.filter { it is Portal && it.id == "ZZ" }[0] as Portal

        val Q = mutableListOf(start.pos3D)
        val distances = mutableMapOf<Position3D, Int>()
        for (v in maze.values) {
            distances[v.pos3D] = Int.MAX_VALUE
        }

        distances[start.pos3D] = 0

        while (Q.isNotEmpty()) {
            Q.sortBy { distances.getValue(it) }
            val u = Q.removeAt(0)
            val curr = get3DNode(u.x, u.y, u.z)
            for (neighbor in recursiveNeighbors(curr!!)) {
                val alt = distances.getValue(u) + 1
                if (alt < distances.getOrPut(neighbor.pos3D ) {Int.MAX_VALUE} && alt < distances.getValue(end.pos3D)) {
                    distances[neighbor.pos3D] = alt
                    Q.add(neighbor.pos3D)
                }
            }
        }
        return distances.getValue(end.pos3D) - 2
    }

    fun get3DNode( x : Int,  y : Int,  z : Int) : PlutoNode {
        if (!maze.containsKey(Position3D(x, y, z))) {
            if (maze2D[Position(x, y)] is Portal) {
                maze[Position3D(x, y, z)] = Portal(Position3D(x, y, z), (maze2D[Position(x,y)] as Portal).id)
            } else {
                maze[Position3D(x, y, z)] = PlutoNode(Position3D(x, y, z))
            }
            val south2D = maze2D[Position(x, y)]!!.south
            val north2D = maze2D[Position(x, y)]!!.north
            val west2D = maze2D[Position(x, y)]!!.west
            val east2D = maze2D[Position(x, y)]!!.east

            if (south2D != null) {
                maze[Position3D(x, y, z)]!!.south = get3DNode(south2D.pos.x, south2D.pos.y, z)
            }
            if (north2D != null) {
                maze[Position3D(x, y, z)]!!.north = get3DNode(north2D.pos.x, north2D.pos.y, z)
            }
            if (west2D != null) {
                maze[Position3D(x, y, z)]!!.west = get3DNode(west2D.pos.x, west2D.pos.y, z)
            }
            if (east2D != null) {
                maze[Position3D(x, y, z)]!!.east = get3DNode(east2D.pos.x, east2D.pos.y, z)
            }
        }
        return maze[Position3D(x, y, z)]!!
    }

    fun recursiveNeighbors(curr: PlutoNode): List<PlutoNode> {
        val neighbors = curr.getNeighbors()
        return neighbors.mapNotNull { resolveNeighbors(it) }
    }

    private fun resolveNeighbors(p : PlutoNode) : PlutoNode? {
        if (p is Portal) {
            if ((p.id == "AA" || p.id == "ZZ") && p.pos3D.z != 0) {
                return null
            } else if (p.id != "AA" && p.id != "ZZ" && p.pos3D.z == 0 && isExternal(p)) {
                return null
            } else if (p.id == "AA" || p.id == "ZZ" && p.pos3D.z == 0) {
                return p
            } else {
                val otherEnd = resolvedMaze2D.findOtherEnd(p, resolvedMaze2D.maze)
                if (isExternal(p)) {
                    return get3DNode(otherEnd.pos.x, otherEnd.pos.y, p.pos3D.z - 1)
                } else {
                    return get3DNode(otherEnd.pos.x, otherEnd.pos.y, p.pos3D.z + 1)
                }
            }
        } else {
            return p
        }
    }

    private fun isExternal(p : Portal) : Boolean {
        return p.pos.x == 1 || p.pos.y == 1 || p.pos.x > maxX - 4 || p.pos.y > maxY - 4
    }

}

fun main() {
    val input = readFileToStrings(Day20().javaClass.getResource("day20.txt").path)
    val pluto = Pluto(input)
    println(pluto.findShortestPath())
    val recursivePluto = RecursivePluto(input)
    println(recursivePluto.findShortestPath())
}
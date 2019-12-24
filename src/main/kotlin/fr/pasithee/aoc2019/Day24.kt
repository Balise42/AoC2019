package fr.pasithee.aoc2019

import java.lang.Math.pow

class Day24 {}

class ErisState(val map: List<Pair<Int, Int>>) {
    var state: Map<Pair<Int, Int>, Int>

    init {
        val tmpState = mutableMapOf<Pair<Int, Int>, Int>()
        for (pair in map) {
            tmpState[pair] = 1
        }
        state = tmpState
    }

    fun getBugs(): Set<Pair<Int, Int>> {
        return state.keys.filter { state[it] == 1 }.toSet()
    }

    fun nextGen() : ErisState {
        val newState = mutableMapOf<Pair<Int, Int>, Int>()
        for (x in 0..4) {
            for (y in 0..4) {
                val neighbors = getNeighbors(x, y)
                newState[Pair(x, y)] = when {
                    shouldSpawn(x, y, neighbors) -> 1
                    shouldDie(x, y, neighbors) -> 0
                    else -> state.getOrDefault(Pair(x, y), 0)
                }
            }
        }
        state = newState
        return this
    }

    fun shouldSpawn(x: Int, y: Int, neighbors: Int) = state.getOrDefault(Pair(x, y), 0) == 0 && (neighbors == 1 || neighbors == 2)
    fun shouldDie(x: Int, y: Int, neighbors: Int) = state.getOrDefault(Pair(x, y), 0) == 1 && neighbors != 1

    fun getNeighbors(x: Int, y: Int) = listOf(Pair(x - 1, y), Pair(x + 1, y), Pair(x, y - 1), Pair(x, y + 1))
        .filter { state.getOrDefault(it, 0) == 1 }.size

    fun biodiversity(): Long {
        var biodiversity = 0L
        for (bug in getBugs()) {
            biodiversity += pow(2.0, bug.first.toDouble() + bug.second.toDouble() * 5).toLong()
        }
        return biodiversity
    }
}

class ErisEvolution(map: List<Pair<Int, Int>>) {
    private val eris = ErisState(map)
    private val states = mutableListOf(eris.getBugs())

    fun findRepeatingLayoutBiodiversity() : Long {
        var it = 0
        while (true) {
            it++
            eris.nextGen()
            val bugs = eris.getBugs()
            if (states.contains(bugs)) {
                println(it)
                println(bugs)
                return eris.biodiversity()
            } else {
                states.add(bugs)
            }
        }
    }
}


class RecursiveEris(map : List<Pair<Int, Int>>) {
    private var eris = mutableMapOf<Int, Array<Array<Int>>>()
    init {
        for (i in -200..200) {
            eris[i] = Array(5)  { Array(5) {0}}
        }

        for (bug in map) {
            eris.getValue(0)[bug.first][bug.second] = 1
        }
    }

    fun nextGen() : RecursiveEris {
        val newState = mutableMapOf<Int, Array<Array<Int>>>()
        for (i in eris.keys) {
            val newLevel = Array(5)  { Array(5) {0}}
            for (x in 0..4) {
                for (y in 0..4) {
                    if (x != 2 || y != 2) {
                        val neighbors = getNeighbors(x, y, i)
                        newLevel[x][y] = when {
                            shouldSpawn(i, x, y, neighbors) -> 1
                            shouldDie(i, x, y, neighbors) -> 0
                            else -> eris.getValue(i)[x][y]
                        }
                    }
                }
            }
            newState[i] = newLevel
        }
        eris = newState
        return this
    }

    fun getNeighbors(x: Int, y: Int, level : Int) : Int {
        val numNorth = computeNeighbors(x, y-1, x, y, level)
        val numSouth = computeNeighbors(x, y+1, x, y, level)
        val numEast = computeNeighbors(x+1, y, x, y, level)
        val numWest = computeNeighbors(x-1, y, x, y, level)

        return numNorth + numSouth + numEast + numWest
    }

    fun computeNeighbors(x: Int, y: Int, xorig: Int, yorig: Int, level: Int) : Int {
        if (x == -1) {
            return eris.getOrDefault(level - 1,  Array(5)  { Array(5) {0}})[1][2]
        } else if (x == 5) {
            return eris.getOrDefault(level - 1,  Array(5)  { Array(5) {0}})[3][2]
        } else if (y == -1) {
            return eris.getOrDefault(level - 1,  Array(5)  { Array(5) {0}})[2][1]
        } else if (y == 5) {
            return eris.getOrDefault(level - 1,  Array(5)  { Array(5) {0}})[2][3]
        } else if (x == 2 && y == 2) {
            var res = 0
            when {
                yorig == 1 -> for (i in 0..4) {
                    res += eris.getOrDefault(level + 1,  Array(5)  { Array(5) {0}})[i][0]
                }
                yorig == 3 -> for (i in 0..4) {
                    res += eris.getOrDefault(level + 1,  Array(5)  { Array(5) {0}})[i][4]
                }
                xorig == 1 -> for (i in 0..4) {
                    res += eris.getOrDefault(level + 1,  Array(5)  { Array(5) {0}})[0][i]
                }
                xorig == 3 -> for (i in 0..4) {
                    res += eris.getOrDefault(level + 1,  Array(5)  { Array(5) {0}})[4][i]
                }
            }
            return res
        }
        return eris.getValue(level)[x][y]
    }


    fun shouldSpawn(level: Int, x: Int, y: Int, neighbors: Int) = eris.getValue(level)[x][y] == 0 && (neighbors == 1 || neighbors == 2)
    fun shouldDie(level: Int, x: Int, y: Int, neighbors: Int) = eris.getValue(level)[x][y] == 1 && neighbors != 1

    fun getBugs() : Long {
        var res = 0L
        for (level in eris.values) {
            for (i in 0..4) {
                for (j in 0..4) {
                    res += level[i][j]
                }
            }
        }
        return res
    }

}

fun main() {
    val map = readFileToCoordinates(Day24().javaClass.getResource("day24.txt").path, '#')
    println(ErisEvolution(map).findRepeatingLayoutBiodiversity())
    val recursiveEris = RecursiveEris(map)
    for (i in 1..200) {
        recursiveEris.nextGen()
    }
    println(recursiveEris.getBugs())
}
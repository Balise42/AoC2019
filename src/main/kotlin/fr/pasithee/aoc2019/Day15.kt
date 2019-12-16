package fr.pasithee.aoc2019

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Collections.max
import kotlin.random.Random

val nodes = mutableMapOf<Pair<Int, Int>, Node>()
var foundO : Node? = null

class Node(val x: Int, val y: Int, var status : Char = '?') {
    fun getNeighbors() : List<Node> {
        nodes.putIfAbsent(Pair(x, y-1), Node(x, y-1))
        nodes.putIfAbsent(Pair(x, y+1), Node(x, y+1))
        nodes.putIfAbsent(Pair(x-1, y), Node(x-1, y))
        nodes.putIfAbsent(Pair(x+1, y), Node(x+1, y))

        val north = nodes[Pair(x, y-1)]!!
        val south = nodes[Pair(x, y+1)]!!
        val west = nodes[Pair(x-1, y)]!!
        val east = nodes[Pair(x+1, y)]!!

        return listOf(north, south, west, east)
    }

    fun availableMoves() : List<Int> {
        val res = mutableListOf<Int>()
        val dirs = getNeighbors()
        for (i in dirs.indices) {
            if (dirs[i]!!.status != '#') {
                res.add(i+1)
            }
        }
        return res
    }

}

class RepairDroid(source : List<Long>) {
    val commands = Channel<Long>(1)
    val status = Channel<Long>(1)
    val intcode = Intcode(source, source[1], source[2], commands, status)

    var posX = 0
    var posY = 0

    fun exploreMap(numSteps : Int) {

        GlobalScope.launch {
            intcode.runProgram()
        }

        nodes[Pair(0, 0)] = Node(0, 0, '.')
        for (i in 0..numSteps) {
            val availableMoves = nodes[Pair(posX, posY)]!!.availableMoves()
            val move = availableMoves[Random.nextInt(0, availableMoves.size)]
            runBlocking {
                commands.send(move.toLong())
                val response = status.receive()
                updatePositionAndKnowledge(move, response.toInt())
            }
        }
    }

    fun updatePositionAndKnowledge(move: Int, response: Int) {
        if (response != 0) {
            if (move == 1) {
                posY -= 1
            } else if (move == 2) {
                posY += 1
            } else if (move == 3) {
                posX -=1
            } else if (move == 4) {
                posX += 1
            }
            if (response == 1) {
                nodes.getOrDefault(Pair(posX, posY), Node(posX, posY, '.')).status = '.'
            } else if (response == 2) {
                nodes.getOrDefault(Pair(posX, posY), Node(posX, posY, '.')).status = 'O'
                foundO = nodes.get(Pair(posX, posY))
            }
        } else {
            if (move == 1) {
                nodes.getOrDefault(Pair(posX, posY - 1), Node(posX, posY - 1, '#')).status = '#'
            } else if (move == 2) {
                nodes.getOrDefault(Pair(posX, posY + 1), Node(posX, posY + 1, '#')).status = '#'
            } else if (move == 3) {
                nodes.getOrDefault(Pair(posX - 1, posY), Node(posX - 1, posY, '#')).status = '#'
            } else if (move == 4) {
                nodes.getOrDefault(Pair(posX + 1, posY), Node(posX + 1, posY, '#')).status = '#'
            }
        }
    }
}

class MazeSolver() {
    fun bfs() : Int {
        val visited = mutableMapOf<Pair<Int, Int>, Int>()
        visited[Pair(0, 0)] = 0

        val Q = mutableListOf(nodes[Pair(0, 0)]!!)
        var uncertainties = 0
        while (Q.isNotEmpty()) {
            val v = Q.removeAt(0)
            if (v.status == '?') {
                uncertainties++
                continue
            }
            if (v.status == 'O') {
                println("Uncertainties: " + uncertainties)
                return visited[Pair(v.x, v.y)]!!
            }
            for (n in v.getNeighbors()) {
                if (!visited.containsKey(Pair(n.x, n.y))) {
                    visited[Pair(n.x, n.y)] = visited[Pair(v.x, v.y)]!! + 1
                    if (n.status != '#') {
                        Q.add(n)
                    }
                }
            }
        }
        return -1
    }
}

class OxygenFiller() {
    fun bfs() : Int {
        val visited = mutableMapOf<Pair<Int, Int>, Int>()
        visited[Pair(foundO!!.x, foundO!!.y)] = 0

        val Q = mutableListOf(foundO!!)
        var uncertainties = 0
        while (Q.isNotEmpty()) {
            val v = Q.removeAt(0)
            if (v.status == '?') {
                uncertainties++
                continue
            }
            for (n in v.getNeighbors()) {
                if (!visited.containsKey(Pair(n.x, n.y))) {
                    if (n.status != '#') {
                        visited[Pair(n.x, n.y)] = visited[Pair(v.x, v.y)]!! + 1
                        Q.add(n)
                    }
                }
            }
        }
        println("Uncertainties: " + uncertainties)
        return max(visited.values)
    }
}

fun main() {
    val source = readFileToInccode(Day13().javaClass.getResource("day15.txt").path)
    val droid = RepairDroid(source)
    droid.exploreMap(1000000)
    println(foundO)
    println(MazeSolver().bfs())
    println(OxygenFiller().bfs())
}
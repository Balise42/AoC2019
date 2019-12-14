package fr.pasithee.aoc2019

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class Day13 {}

enum class ArcadeState { X, Y, VALUE}

class Arcade(program: List<Long>) : Intcode(program, program[1], program[2], Channel(), Channel())  {
    val board = Array(44) { Array(23) { 0 } }
    var currState = ArcadeState.X
    var x = -2
    var y = -2
    var score = -1L
    var ballPosX : Int? = null
    var paddlePos = 0

    init {
        this[0] = 2
    }

    override suspend fun save(op: Long, modes: List<Long>) : Long {
        val move =
            if (paddlePos < ballPosX!!) {
                1L
            } else if (paddlePos >  ballPosX!!) {
                -1L
            } else {
                0L
            }

        setValueAt(op, modes[0], move)
        return 2
    }

    override suspend fun out(op: Long, modes: List<Long>): Long {
        lastOutput = if (modes[0] == 1L) { op } else { getValueAt(op, modes[0]) }
        if (currState == ArcadeState.X) {
            x = lastOutput.toInt()
            currState = ArcadeState.Y
        } else if (currState == ArcadeState.Y) {
            y = lastOutput.toInt()
            currState = ArcadeState.VALUE
        } else if (currState == ArcadeState.VALUE) {
            if (x == -1 && y == 0) {
                score = lastOutput
            } else {
                board[x][y] = lastOutput.toInt()
                if (lastOutput == 3L) {
                    paddlePos = x
                } else if (lastOutput == 4L) {
                    ballPosX = x
                }
            }
            currState = ArcadeState.X
        }
        return 2
    }
}



fun main() {
    val source = readFileToInccode(Day13().javaClass.getResource("day13.txt").path)
    val intcode = Arcade(source)

    runBlocking {
        intcode.runProgram()
    }

    println(intcode.score)
}
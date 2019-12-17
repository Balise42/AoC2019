package fr.pasithee.aoc2019

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Day17{}

class CleanerBot(program: List<Long>) : Intcode(program, program[1], program[2], Channel(), Channel())  {
    var inputSpot = 0
    val prog = "A,B,A,B,C,A,B,C,A,C\nR,6,L,10,R,8\nR,8,R,12,L,8,L,8\nL,10,R,6,R,6,L,8\nn\n"

    override suspend fun save(op: Long, modes: List<Long>) : Long {

        setValueAt(op, modes[0], prog[inputSpot].toLong())
        inputSpot++
        return 2
    }

    override suspend fun out(op: Long, modes: List<Long>): Long {
        lastOutput = if (modes[0] == 1L) { op } else { getValueAt(op, modes[0]) }
        println(lastOutput)
        return 2
    }
}


fun main() {
    // getting the map
    /*
    val source = readFileToInccode(Day17().javaClass.getResource("day17.txt").path)
    val outChannel = Channel<Long>()
    val intcode = Intcode(source, source[1], source[2], Channel(), outChannel)
    GlobalScope.launch {
        intcode.runProgram()
    }
    runBlocking {
        while (true) {
            print(outChannel.receive().toChar())
        }
    }*/

    // part 1
    val map = readFileToCoordinates(Day17().javaClass.getResource("day17-map.txt").path, '#')
    var sum = 0
    for (pos in map) {
        if (map.contains(Pair(pos.first - 1, pos.second))
            && map.contains(Pair(pos.first + 1, pos.second))
            && map.contains(Pair(pos.first, pos.second - 1))
            && map.contains(Pair(pos.first, pos.second + 1))
        ) {
            sum += pos.first * pos.second
        }
    }
    println(sum)

    // part 2
    var pos = readFileToCoordinates(Day17().javaClass.getResource("day17-map.txt").path, '^')[0]
    var dir = Pair(0, -1)

    while (true) {
        var numSteps = 0
        while (map.contains(Pair(pos.first + dir.first, pos.second + dir.second))) {
            pos = Pair(pos.first + dir.first, pos.second + dir.second)
            numSteps++
        }
        print("" + numSteps + ",")

        if (dir == Pair(0, -1)) {
            if (map.contains(Pair(pos.first + 1, pos.second))) {
                dir = Pair(1, 0)
                print("R,")
            } else if (map.contains(Pair(pos.first - 1, pos.second))) {
                dir = Pair(-1, 0)
                print("L,")
            } else {
                break
            }
        } else if (dir == Pair(0, 1)) {
            if (map.contains(Pair(pos.first + 1, pos.second))) {
                dir = Pair(1, 0)
                print("L,")
            } else if (map.contains(Pair(pos.first - 1, pos.second))) {
                dir = Pair(-1, 0)
                print("R,")
            } else {
                break
            }
        } else if (dir == Pair(-1, 0)) {
            if (map.contains(Pair(pos.first, pos.second + 1))) {
                dir = Pair(0, 1)
                print("L,")
            } else if (map.contains(Pair(pos.first, pos.second - 1))) {
                dir = Pair(0, -1)
                print("R,")
            } else {
                break
            }
        } else if (dir == Pair(1, 0)) {
            if (map.contains(Pair(pos.first, pos.second + 1))) {
                dir = Pair(0, 1)
                print("R,")
            } else if (map.contains(Pair(pos.first, pos.second - 1))) {
                dir = Pair(0, -1)
                print("L,")
            } else {
                break
            }
        }
    }


    val source = readFileToInccode(Day17().javaClass.getResource("day17.txt").path)

    val intcode = CleanerBot(source)
    intcode[0] = 2
    runBlocking {
        intcode.runProgram()
    }

}
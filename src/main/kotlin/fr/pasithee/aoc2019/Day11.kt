package fr.pasithee.aoc2019

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

val up = Position(0, 1)
val right = Position(1, 0)
val down = Position(0, -1)
val left = Position(-1, 0)
val directions = arrayListOf(up, right, down, left)

enum class robotModes {MOVE, PAINT}

class RobotIntCode(program: List<Long>, val robot: Robot) : Intcode(program, program[1], program[2], Channel(), Channel()) {
    var mode = robotModes.PAINT

    override suspend fun save(op: Long, modes: List<Long>) : Long {
        setValueAt(op, modes[0], robot.getColor())
        return 2
    }

    override suspend fun out(op: Long, modes: List<Long>): Long {
        lastOutput = if (modes[0] == 1L) { op } else { getValueAt(op, modes[0]) }

        if (mode == robotModes.MOVE) {
            robot.move(lastOutput)
            mode = robotModes.PAINT
        } else {
            robot.paint(lastOutput)
            mode = robotModes.MOVE
        }
        return 2
    }
}

class Robot {
    var position = Position(0,0)
    var direction = directions.indexOf(up)
    val map = mutableMapOf<Position, Long>()

    fun getColor() : Long = map.getOrDefault(position, 0)
    fun move(movement: Long) {
        direction = if (movement == 1L) (direction + 1) % 4 else (direction - 1) % 4
        if (direction < 0) {
            direction += 4
        }
        position += directions[direction]
    }

    fun paint(color: Long) {
        map[position] = color
    }
}

fun main() {

    val intCode = arrayListOf(3,8,1005,8,330,1106,0,11,0,0,0,104,1,104,0,3,8,102,-1,8,10,101,1,10,10,4,10,1008,8,0,10,
        4,10,102,1,8,29,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,0,10,4,10,101,0,8,51,1,1103,2,10,1006,0,94,1006,0,11,
        1,1106,13,10,3,8,1002,8,-1,10,101,1,10,10,4,10,1008,8,1,10,4,10,1001,8,0,87,3,8,102,-1,8,10,101,1,10,10,4,10,
        1008,8,0,10,4,10,1001,8,0,109,2,1105,5,10,2,103,16,10,1,1103,12,10,2,105,2,10,3,8,102,-1,8,10,1001,10,1,10,4,
        10,108,1,8,10,4,10,1001,8,0,146,1006,0,49,2,1,12,10,2,1006,6,10,1,1101,4,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,
        108,0,8,10,4,10,1001,8,0,183,1,6,9,10,1006,0,32,3,8,102,-1,8,10,1001,10,1,10,4,10,1008,8,1,10,4,10,101,0,8,213,
        2,1101,9,10,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,1,10,4,10,101,0,8,239,1006,0,47,1006,0,4,2,6,0,10,1006,0,
        58,3,8,1002,8,-1,10,1001,10,1,10,4,10,1008,8,0,10,4,10,102,1,8,274,2,1005,14,10,1006,0,17,1,104,20,10,1006,0,28,
        3,8,102,-1,8,10,1001,10,1,10,4,10,108,1,8,10,4,10,1002,8,1,309,101,1,9,9,1007,9,928,10,1005,10,15,99,109,652,
        104,0,104,1,21101,0,937263411860,1,21102,347,1,0,1105,1,451,21101,932440724376,0,1,21102,1,358,0,1105,1,451,3,
        10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,3,10,104,0,104,1,3,10,104,0,104,0,3,10,104,0,104,1,21101,0,
        29015167015,1,21101,0,405,0,1106,0,451,21102,1,3422723163,1,21101,0,416,0,1106,0,451,3,10,104,0,104,0,3,10,104,
        0,104,0,21101,0,868389376360,1,21101,0,439,0,1105,1,451,21102,825544712960,1,1,21102,1,450,0,1106,0,451,99,109,
        2,21201,-1,0,1,21101,0,40,2,21102,482,1,3,21102,1,472,0,1106,0,515,109,-2,2106,0,0,0,1,0,0,1,109,2,3,10,204,-1,
        1001,477,478,493,4,0,1001,477,1,477,108,4,477,10,1006,10,509,1101,0,0,477,109,-2,2106,0,0,0,109,4,2101,0,-1,514,
        1207,-3,0,10,1006,10,532,21102,1,0,-3,22101,0,-3,1,22102,1,-2,2,21102,1,1,3,21101,551,0,0,1106,0,556,109,-4,
        2105,1,0,109,5,1207,-3,1,10,1006,10,579,2207,-4,-2,10,1006,10,579,22102,1,-4,-4,1106,0,647,21201,-4,0,1,21201,
        -3,-1,2,21202,-2,2,3,21102,1,598,0,1106,0,556,22101,0,1,-4,21101,1,0,-1,2207,-4,-2,10,1006,10,617,21102,0,1,-1,
        22202,-2,-1,-2,2107,0,-3,10,1006,10,639,21201,-1,0,1,21102,639,1,0,105,1,514,21202,-2,-1,-2,22201,-4,-2,-4,109,
        -5,2105,1,0)


    val robot = Robot()
    val program = RobotIntCode(intCode, robot)

    runBlocking {
        program.runProgram()
    }

    println(robot.map.size)

    val robotPart2 = Robot()
    val programPart2 = RobotIntCode(intCode, robotPart2)

    robotPart2.paint(1)
    runBlocking {
        programPart2.runProgram()
    }

    var minX = Int.MAX_VALUE
    var minY = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = Int.MIN_VALUE

    for (pos in robotPart2.map.keys) {
        if (pos.x < minX) {
            minX = pos.x
        }
        if (pos.y < minY) {
            minY = pos.y
        }
        if (pos.x > maxX) {
            maxX = pos.x
        }
        if (pos.y >  maxY) {
            maxY = pos.y
        }
    }

    for (y in maxY downTo minY) {
        for (x in maxX downTo minX) {
            if (robotPart2.map.getOrDefault(Position(maxX - x, y), 0L) == 0L) {
                print('.')
            } else {
                print('#')
            }
        }
        println("")
    }
}
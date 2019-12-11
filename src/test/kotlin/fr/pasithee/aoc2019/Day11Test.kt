package fr.pasithee.aoc2019

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Assert.*
import org.junit.Test

class Day11Test {
    @Test
    fun robotShouldPaint() {
        val intcode = arrayListOf(104L, 1, 104, 0, 104, 0, 104, 0, 104, 1, 104, 0, 104, 1, 104, 0, 104, 0, 104, 1, 104, 1, 104, 0, 104, 1, 104, 1, 99)
        val robot = Robot()
        val program = RobotIntCode(intcode, robot)

        runBlocking {
            program.runProgram()
        }

        assertEquals(6, robot.map.size)
    }

}
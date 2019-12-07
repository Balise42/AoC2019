package fr.pasithee.aoc2019

import org.junit.Test
import kotlin.test.assertEquals

class IntcodeTest {
    @Test
    fun runShouldWork() {
        val inputs = arrayListOf(
            arrayListOf(1, 0, 0, 0, 99),
            arrayListOf(2, 3, 0, 3, 99),
            arrayListOf(2, 4, 4, 5, 99, 9),
            arrayListOf(1, 1, 1, 4, 99, 5, 6, 0, 99),
            arrayListOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50),
            arrayListOf(1002,4,3,4,33)
        )
        val results = arrayListOf(
            arrayListOf(2, 0, 0, 0, 99),
            arrayListOf(2, 3, 0, 6, 99),
            arrayListOf(2, 4, 4, 5, 99, 9801),
            arrayListOf(30, 1, 1, 4, 2, 5, 6, 0, 99),
            arrayListOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50),
            arrayListOf(1002, 4, 3, 4, 99)
        )

        for (i in 0..4) {
            val program = Intcode(inputs[i], inputs[i][1], inputs[i][2])
            program.run()
            assertEquals(results[i], program.state())
        }
    }

    @Test
    fun ioShouldWork() {
        val program = Intcode(arrayListOf(3,0,4,0,99), 0, 4, listOf(78))
        program.run()
        assertEquals(78, program.output)
    }

    @Test
    fun equalPosShouldReturnFalse() {
        val program = Intcode(arrayListOf(3,9,8,9,10,9,4,9,99,-1,8), 9, 8, listOf(78))
        program.run()
        assertEquals(0, program.output)
    }

    @Test
    fun equalPosShouldReturnTrue() {
        val program = Intcode(arrayListOf(3,9,8,9,10,9,4,9,99,-1,8), 9, 8, listOf(8))
        program.run()
        assertEquals(1, program.output)
    }

    @Test
    fun equalImmShouldReturnFalse() {
        val program = Intcode(arrayListOf(3,3,1108,-1,8,3,4,3,99), 3, 1108, listOf(78))
        program.run()
        assertEquals(0, program.output)
    }

    @Test
    fun equalImmShouldReturnTrue() {
        val program = Intcode(arrayListOf(3,3,1108,-1,8,3,4,3,99), 3, 1108, listOf(8))
        program.run()
        assertEquals(1, program.output)
    }

    @Test
    fun ltPosShouldReturnFalse() {
        val program = Intcode(arrayListOf(3,9,7,9,10,9,4,9,99,-1,8), 9, 7, listOf(78))
        program.run()
        assertEquals(0, program.output)
    }

    @Test
    fun ltPosShouldReturnTrue() {
        val program = Intcode(arrayListOf(3,9,7,9,10,9,4,9,99,-1,8), 9, 7, listOf(7))
        program.run()
        assertEquals(1, program.output)
    }

    @Test
    fun ltImmShouldReturnFalse() {
        val program = Intcode(arrayListOf(3,9,7,9,10,9,4,9,99,-1,8), 3, 1107, listOf(78))
        program.run()
        assertEquals(0, program.output)
    }

    @Test
    fun ltImmShouldReturnTrue() {
        val program = Intcode(arrayListOf(3,3,1107,-1,8,3,4,3,99), 3, 1107, listOf(7))
        program.run()
        assertEquals(1, program.output)
    }

    @Test
    fun largerExampleShouldWorkLessThan8() {
        val program = Intcode(arrayListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 21, 1008, listOf(4))
        program.run()
        assertEquals(999, program.output)
    }

    @Test
    fun largerExampleShouldWorkEquals8() {
        val program = Intcode(arrayListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 21, 1008, listOf(8))
        program.run()
        assertEquals(1000, program.output)
    }

    @Test
    fun largerExampleShouldWorkLargerThan8() {
        val program = Intcode(arrayListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 21, 1008, listOf(45))
        program.run()
        assertEquals(1001, program.output)
    }
}
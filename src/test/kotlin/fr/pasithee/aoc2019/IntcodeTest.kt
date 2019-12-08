package fr.pasithee.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
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
            arrayListOf(1002, 4, 3, 4, 33)
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
            val program = Intcode(inputs[i], inputs[i][1], inputs[i][2], Channel(), Channel())
            runBlocking {
                program.runProgram()
            }
            assertEquals(results[i], program.state())
        }
    }

    @Test
    fun ioShouldWork() {
        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3, 0, 4, 0, 99), 0, 4, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(78, output.receive())
        }
    }

    @Test
    fun equalPosShouldReturnFalse() {
        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 9, 8, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(0, output.receive())
        }
    }

    @Test
    fun equalPosShouldReturnTrue() {
        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 9, 8, input, output)
        input.offer(8)
        runBlocking {
            program.runProgram()
            assertEquals(1, output.receive())
        }
    }

    @Test
    fun equalImmShouldReturnFalse() {
        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3, 3, 1108, -1, 8, 3, 4, 3, 99), 3, 1108, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(0, output.receive())
        }
    }

    @Test
    fun equalImmShouldReturnTrue() {
        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3, 3, 1108, -1, 8, 3, 4, 3, 99), 3, 1108, input, output)
        input.offer(8)
        runBlocking {
            program.runProgram()
            assertEquals(1, output.receive())
        }
    }


    @Test
    fun ltPosShouldReturnFalse() {

        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 9, 7, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(0, output.receive())
        }
    }

    @Test
    fun ltPosShouldReturnTrue() {

        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 9, 7, input, output)
        input.offer(7)
        runBlocking {
            program.runProgram()
            assertEquals(1, output.receive())
        }
    }

    @Test
    fun ltImmShouldReturnFalse() {

        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3,9,7,9,10,9,4,9,99,-1,8), 3, 1107, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(0, output.receive())
        }
    }

    @Test
    fun ltImmShouldReturnTrue() {

        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3,9,7,9,10,9,4,9,99,-1,8), 3, 1107, input, output)
        input.offer(7)
        runBlocking {
            program.runProgram()
            assertEquals(1, output.receive())
        }
    }



    @Test
    fun largerExampleShouldWorkLessThan8() {
        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 21, 1008, input, output)
        input.offer(4)
        runBlocking {
            program.runProgram()
            assertEquals(999, output.receive())
        }
    }

    @Test
    fun largerExampleShouldWorkEqualsThan8() {
        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 21, 1008, input, output)
        input.offer(8)
        runBlocking {
            program.runProgram()
            assertEquals(1000, output.receive())
        }
    }

    @Test
    fun largerExampleShouldWorkLargerThan8() {
        val input = Channel<Int>(1)
        val output = Channel<Int>(1)
        val program = Intcode(arrayListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 21, 1008, input, output)
        input.offer(45)
        runBlocking {
            program.runProgram()
            assertEquals(1001, output.receive())
        }
    }
}
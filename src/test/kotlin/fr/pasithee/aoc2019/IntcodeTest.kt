package fr.pasithee.aoc2019

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class IntcodeTest {
    @Test
    fun runShouldWork() {
        val inputs = arrayListOf(
            arrayListOf(1L, 0, 0, 0, 99),
            arrayListOf(2L, 3, 0, 3, 99),
            arrayListOf(2L, 4, 4, 5, 99, 9),
            arrayListOf(1L, 1, 1, 4, 99, 5, 6, 0, 99),
            arrayListOf(1L, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50),
            arrayListOf(1002L, 4, 3, 4, 33)
        )
        val results = arrayListOf(
            Memory(arrayListOf(2L, 0, 0, 0, 99)),
            Memory(arrayListOf(2L, 3, 0, 6, 99)),
            Memory(arrayListOf(2L, 4, 4, 5, 99, 9801)),
            Memory(arrayListOf(30L, 1, 1, 4, 2, 5, 6, 0, 99)),
            Memory(arrayListOf(3500L, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)),
            Memory(arrayListOf(1002L, 4, 3, 4, 99))
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
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3, 0, 4, 0, 99), 0, 4, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(78, output.receive())
        }
    }

    @Test
    fun equalPosShouldReturnFalse() {
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 9, 8, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(0, output.receive())
        }
    }

    @Test
    fun equalPosShouldReturnTrue() {
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 9, 8, input, output)
        input.offer(8)
        runBlocking {
            program.runProgram()
            assertEquals(1, output.receive())
        }
    }

    @Test
    fun equalImmShouldReturnFalse() {
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3, 3, 1108, -1, 8, 3, 4, 3, 99), 3, 1108, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(0, output.receive())
        }
    }

    @Test
    fun equalImmShouldReturnTrue() {
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3, 3, 1108, -1, 8, 3, 4, 3, 99), 3, 1108, input, output)
        input.offer(8)
        runBlocking {
            program.runProgram()
            assertEquals(1, output.receive())
        }
    }


    @Test
    fun ltPosShouldReturnFalse() {

        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 9, 7, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(0, output.receive())
        }
    }

    @Test
    fun ltPosShouldReturnTrue() {

        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 9, 7, input, output)
        input.offer(7)
        runBlocking {
            program.runProgram()
            assertEquals(1, output.receive())
        }
    }

    @Test
    fun ltImmShouldReturnFalse() {
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3,9,7,9,10,9,4,9,99,-1,8), 3, 1107, input, output)
        input.offer(78)
        runBlocking {
            program.runProgram()
            assertEquals(0, output.receive())
        }
    }

    @Test
    fun ltImmShouldReturnTrue() {

        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3,9,7,9,10,9,4,9,99,-1,8), 3, 1107, input, output)
        input.offer(7)
        runBlocking {
            program.runProgram()
            assertEquals(1, output.receive())
        }
    }

    @Test
    fun largerExampleShouldWorkLessThan8() {
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
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
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
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
        val input = Channel<Long>(1)
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99), 21, 1008, input, output)
        input.offer(45)
        runBlocking {
            program.runProgram()
            assertEquals(1001, output.receive())
        }
    }

    @Test
    fun longNumbersShouldWork() {
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(1102,34915192,34915192,7,4,7,99,0), 34915192, 34915192, Channel(), output)
        runBlocking {
            program.runProgram()
            assertEquals(16, output.receive().toString().length)
        }
    }

    @Test
    fun outputtingLongNumberShouldWork() {
        val output = Channel<Long>(1)
        val program = Intcode(arrayListOf(104,1125899906842624,99), 1125899906842624, 99, Channel(), output)
        runBlocking {
            program.runProgram()
            assertEquals(1125899906842624, output.receive())
        }
    }

    @Test
    fun quineShouldWork() {
        val output = Channel<Long>(16)
        val program = Intcode(arrayListOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99), 1, 204, Channel(), output)
        GlobalScope.launch {
            program.runProgram()
            val res = mutableListOf<Long>()
            for (i in 0..15) {
                res.add(output.receive())
            }
            assertEquals(arrayListOf(109L, 1, 204, -1, 1001, 100, 1, 100, 1008, 100, 16, 101, 1006, 101, 0, 99), res)
        }
    }
}
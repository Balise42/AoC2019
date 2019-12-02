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
            arrayListOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)
        )
        var results = arrayListOf(
            arrayListOf(2, 0, 0, 0, 99),
            arrayListOf(2, 3, 0, 6, 99),
            arrayListOf(2, 4, 4, 5, 99, 9801),
            arrayListOf(30, 1, 1, 4, 2, 5, 6, 0, 99),
            arrayListOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)
        )

        for (i in 0..4) {
            val program = Intcode(inputs[i], inputs[i][1], inputs[i][2])
            program.run()
            assertEquals(results[i], program.state())
        }
    }
}
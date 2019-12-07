package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day7Test {
    @Test
    fun permutationSizeShouldBeConsistent() {
        assertEquals(120, SignalComputer().permutations(listOf(0, 1, 2, 3, 4)).size)
    }

    @Test
    fun thrusterSignalComputationShouldWork() {
        assertEquals(43210, SignalComputer().getSignal(listOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0), listOf(4, 3, 2, 1, 0)))
        assertEquals(54321, SignalComputer().getSignal(listOf(3,23,3,24,1002,24,10,24,1002,23,-1,23,
            101,5,23,23,1,24,23,23,4,23,99,0,0), listOf(0, 1, 2, 3,4)))
        assertEquals(65210, SignalComputer().getSignal(listOf(3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,
            1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0), listOf(1,0,4,3,2)))
    }

    @Test
    fun strongestSignalShouldWork() {
        assertEquals(43210, SignalComputer().findStrongestSignal(listOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0), listOf(0, 1, 2, 3, 4)))
        assertEquals(54321, SignalComputer().findStrongestSignal(listOf(3,23,3,24,1002,24,10,24,1002,23,-1,23,
            101,5,23,23,1,24,23,23,4,23,99,0,0), listOf(0, 1, 2, 3, 4)))
        assertEquals(65210, SignalComputer().findStrongestSignal(listOf(3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,
            1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0), listOf(0, 1, 2, 3, 4)))
    }
}
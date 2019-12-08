package fr.pasithee.aoc2019

import kotlinx.coroutines.channels.Channel
import java.lang.UnsupportedOperationException


class Intcode(program: List<Int>, noun: Int, verb: Int, var input: Channel<Int>, var output: Channel<Int>) {

    private val program: MutableList<Int> = program.toMutableList()
    private var lastOutput = -1

    init {
        this.program[1] = noun
        this.program[2] = verb
    }

    operator fun get(i: Int) = program[i]

    suspend fun runProgram() : Int {
        var instPtr = 0
        while(true) {
            val modes = program[instPtr] / 100
            when(program[instPtr] % 100) {
                99 -> return lastOutput
                1 -> instPtr += add(program[instPtr + 1], program[instPtr + 2], program[instPtr + 3], modes)
                2 -> instPtr += mul(program[instPtr + 1], program[instPtr + 2], program[instPtr + 3], modes)
                3 -> instPtr += save(program[instPtr + 1])
                4 -> instPtr += out(program[instPtr + 1], modes)
                5 -> instPtr = jit(instPtr, program[instPtr+1], program[instPtr + 2], modes)
                6 -> instPtr = jif(instPtr, program[instPtr+1], program[instPtr + 2], modes)
                7 -> instPtr += lt(program[instPtr + 1], program[instPtr + 2], program[instPtr + 3], modes)
                8 -> instPtr += eq(program[instPtr + 1], program[instPtr + 2], program[instPtr + 3], modes)
                else -> throw(UnsupportedOperationException("instPtr: " + program[instPtr]))
            }
        }
    }

    private fun jit(instrPtr: Int, op1: Int, op2: Int, modes: Int): Int {
        val cond = if (modes % 10 == 0) program[op1] else op1
        val jumpVal = if (modes / 10 == 0) program[op2] else op2
        if (cond != 0) {
            return jumpVal
        } else {
            return instrPtr + 3
        }
    }

    private fun jif(instrPtr: Int, op1: Int, op2: Int, modes: Int): Int {
        val cond = if (modes % 10 == 0) program[op1] else op1
        val jumpVal = if (modes / 10 == 0) program[op2] else op2
        if (cond == 0) {
            return jumpVal
        } else {
            return instrPtr + 3
        }
    }

    private fun lt(op1: Int, op2: Int, res: Int, modes: Int) : Int {
        val v1 = if (modes % 10 == 0) program[op1] else op1
        val v2 = if (modes / 10 == 0) program[op2] else op2
        program[res] = if (v1 < v2) 1 else 0
        return 4
    }

    private fun eq(op1: Int, op2: Int, res: Int, modes: Int) : Int {
        val v1 = if (modes % 10 == 0) program[op1] else op1
        val v2 = if (modes / 10 == 0) program[op2] else op2
        program[res] = if (v1 == v2) 1 else 0
        return 4
    }

    suspend private fun out(op: Int, modes: Int): Byte {
        if (modes == 0) {
            lastOutput = program[op]
            output.send(program[op])
        } else {
            lastOutput = op
            output.send(op)
        }
        return 2
    }

    suspend private fun save(op: Int): Int {
        program[op] = input.receive()
        return 2
    }

    private fun add(op1: Int, op2: Int, res: Int, modes: Int) : Int {
        val v1 = if (modes % 10 == 0) program[op1] else op1
        val v2 = if (modes / 10 == 0) program[op2] else op2
        program[res] = v1 + v2
        return 4
    }

    private fun mul(op1: Int, op2: Int, res: Int, modes: Int) : Int {
        val v1 = if (modes % 10 == 0) program[op1] else op1
        val v2 = if (modes / 10 == 0) program[op2] else op2
        program[res] = v1 * v2
        return 4
    }

    fun state() : List<Int> {
        return program
    }
}
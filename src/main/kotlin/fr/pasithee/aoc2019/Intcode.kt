package fr.pasithee.aoc2019

class Intcode(program: List<Int>, noun: Int, verb: Int) {

    private val program: MutableList<Int> = program.toMutableList()

    init {
        this.program[1] = noun
        this.program[2] = verb
    }

    operator fun get(i: Int) = program[i]

    fun run() {
        var instPtr = 0
        while(true) {
            when(program[instPtr]) {
                99 -> return
                1 -> instPtr += addReferences(program[instPtr + 1], program[instPtr + 2], program[instPtr + 3])
                2 -> instPtr += mulReferences(program[instPtr + 1], program[instPtr + 2], program[instPtr + 3])
            }
        }
    }

    private fun addReferences(op1: Int, op2: Int, res: Int) : Int {
        program[res] = program[op1] + program[op2]
        return 4
    }

    private fun mulReferences(op1: Int, op2: Int, res: Int) : Int {
        program[res] = program[op1] * program[op2]
        return 4
    }

    fun state() : List<Int> {
        return program
    }
}
package fr.pasithee.aoc2019

class Intcode(private val program : MutableList<Int>) {
    operator fun get(i: Int) = program[i]
    operator fun set(i: Int, v: Int) {
        program[i] = v
    }

    fun run() {
        for (i in 0 until program.size step 4) {
            if (program[i] == 99) {
                return
            }
            if (program[i] == 1) {
                program[program[i+3]] = program[program[i+1]] + program[program[i+2]]
            }
            else if (program[i] == 2) {
                program[program[i+3]] = program[program[i+1]] * program[program[i+2]]
            }
        }
    }

    fun state() : List<Int> {
        return program
    }
}

fun main() {
    val program = Intcode(
        arrayListOf(1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,6,1,19,1,5,19,23,2,6,23,27,1,27,5,31,2,9,31,35,1,5,35,39,2,6,39,
            43,2,6,43,47,1,5,47,51,2,9,51,55,1,5,55,59,1,10,59,63,1,63,6,67,1,9,67,71,1,71,6,75,1,75,13,79,2,79,13,83,
            2,9,83,87,1,87,5,91,1,9,91,95,2,10,95,99,1,5,99,103,1,103,9,107,1,13,107,111,2,111,10,115,1,115,5,119,2,
            13,119,123,1,9,123,127,1,5,127,131,2,131,6,135,1,135,5,139,1,139,6,143,1,143,6,147,1,2,147,151,1,151,5,0,
            99,2,14,0,0)
    )
    program[1] = 12
    program[2] = 2

    program.run()
    println(program[0])

    for (i in 0..99) {
        for (j in 0..99) {
            val programCandidate = Intcode(
                arrayListOf(1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,6,1,19,1,5,19,23,2,6,23,27,1,27,5,31,2,9,31,35,1,5,35,39,2,6,39,
                43,2,6,43,47,1,5,47,51,2,9,51,55,1,5,55,59,1,10,59,63,1,63,6,67,1,9,67,71,1,71,6,75,1,75,13,79,2,79,13,83,
                2,9,83,87,1,87,5,91,1,9,91,95,2,10,95,99,1,5,99,103,1,103,9,107,1,13,107,111,2,111,10,115,1,115,5,119,2,
                13,119,123,1,9,123,127,1,5,127,131,2,131,6,135,1,135,5,139,1,139,6,143,1,143,6,147,1,2,147,151,1,151,5,0,
                99,2,14,0,0)
            )
            programCandidate[1] = i
            programCandidate[2] = j

            programCandidate.run()
            if (programCandidate[0] == 19690720) {
                println(i * 100 + j)
                return
            }
        }
    }
}
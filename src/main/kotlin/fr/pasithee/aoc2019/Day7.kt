package fr.pasithee.aoc2019

class SignalComputer {

    fun findStrongestSignal(program: List<Int>, elems: List<Int>) : Int {
        var sig = Int.MIN_VALUE
        for (candidate in permutations(elems)) {
            val out = getSignal(program, candidate)
            if (out > sig) {
                sig = out
            }
        }
        return sig
    }

    fun getSignal(program: List<Int>, candidate: List<Int>) : Int {
        val amps = candidate.map { i -> Intcode(program, program[1], program[2]) }

        for (i in 0 until amps.size) {
            amps[i].input = if (i == 0) listOf(candidate[0], 0) else listOf(candidate[i], amps[i-1].output!!)
            amps[i].run()
        }
        return amps.last().output!!
    }

    fun <T> permutations(elems: List<T>): List<List<T>> {
        if (elems.size == 1) {
            return arrayListOf(arrayListOf(elems.first()))
        } else {
            val res = mutableListOf<List<T>>()
            for (i in 0 until elems.size) {
                val elem = elems[i]
                val subperm = permutations(elems.subList(0, i) + elems.subList(i + 1, elems.size))
                for (l in subperm) {
                    res.add(l + listOf(elem))
                }
            }
            return res
        }
    }
}

fun main() {
    val program = listOf(3,8,1001,8,10,8,105,1,0,0,21,38,55,64,81,106,187,268,349,430,99999,3,9,101,2,9,9,1002,9,2,9,
        101,5,9,9,4,9,99,3,9,102,2,9,9,101,3,9,9,1002,9,4,9,4,9,99,3,9,102,2,9,9,4,9,99,3,9,1002,9,5,9,1001,9,4,9,102,
        4,9,9,4,9,99,3,9,102,2,9,9,1001,9,5,9,102,3,9,9,1001,9,4,9,102,5,9,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,
        9,3,9,1002,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,1001,9,2,9,
        4,9,3,9,101,1,9,9,4,9,3,9,1001,9,1,9,4,9,99,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,
        9,4,9,3,9,101,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,1,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,
        4,9,99,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,1002,9,2,
        9,4,9,3,9,1002,9,2,9,4,9,3,9,101,2,9,9,4,9,3,9,1001,9,2,9,4,9,3,9,101,1,9,9,4,9,99,3,9,102,2,9,9,4,9,3,9,1001,9,
        2,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,102,2,9,9,4,9,3,9,101,2,9,9,4,9,3,9,101,1,9,9,4,9,3,9,101,1,9,
        9,4,9,3,9,1001,9,1,9,4,9,3,9,102,2,9,9,4,9,99,3,9,101,1,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,
        2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,9,4,9,3,9,1002,9,2,9,4,9,3,9,1002,9,2,9,4,9,3,9,101,1,9,9,4,9,3,9,102,2,9,
        9,4,9,99)

    print(SignalComputer().findStrongestSignal(program, listOf(0, 1, 2, 3, 4)))
}
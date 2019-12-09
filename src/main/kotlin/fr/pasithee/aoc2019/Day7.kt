package fr.pasithee.aoc2019

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class SignalComputer {

    suspend fun findStrongestSignal(program: List<Long>, elems: List<Long>, loop : Boolean = false) : Long {
        var sig = Long.MIN_VALUE
        for (candidate in permutations(elems)) {
            val out = getSignal(program, candidate, loop)
            if (out > sig) {
                sig = out
            }
        }
        return sig
    }

    suspend fun getSignal(program: List<Long>, candidate: List<Long>, loop: Boolean = false) : Long {
        val channels = if(loop)
            listOf(Channel<Long>(10), Channel(10), Channel(10), Channel(10), Channel(10))
        else
            listOf(Channel<Long>(10), Channel(10), Channel(10), Channel(10), Channel(10), Channel(10))

        val amps = mutableListOf<Intcode>()

        for (i in candidate.indices) {
            amps.add(Intcode(program, program[1], program[2], channels[i], channels[(i+1)%channels.size]))
        }

        val deferred = amps.map { amp -> GlobalScope.async {
            amp.runProgram()
        } }

        for (i in candidate.indices) {
            channels[i].send(candidate[i])
        }

        channels[0].offer(0)

        deferred[4].await()
        if (loop) {
            return channels.first().receive()
        } else {
            return channels.last().receive()
        }
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
    val program = listOf(3L,8,1001,8,10,8,105,1,0,0,21,38,55,64,81,106,187,268,349,430,99999,3,9,101,2,9,9,1002,9,2,9,
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
    runBlocking {
        println(SignalComputer().findStrongestSignal(program, listOf(0, 1, 2, 3, 4)))
        println(SignalComputer().findStrongestSignal(program, listOf(5, 6, 7, 8, 9), true))
    }
}
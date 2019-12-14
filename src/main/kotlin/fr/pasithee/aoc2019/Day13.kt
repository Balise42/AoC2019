package fr.pasithee.aoc2019

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*


class Day13 {
}

fun main() {
    val source = readFileToInccode(Day3().javaClass.getResource("day13.txt").path)
    val out = Channel<Long>()
    val intcode = Intcode(source, source[1], source[2], Channel(), out)

    GlobalScope.launch {
        intcode.runProgram()
    }

    runBlocking {
        var i = 0
        var numBlocks = 0
        while (true) {
            val k = out.receive()
            if (i % 3 == 2 && k == 2L) {
                numBlocks++
            }
            println(numBlocks)
            i++
        }

    }
}
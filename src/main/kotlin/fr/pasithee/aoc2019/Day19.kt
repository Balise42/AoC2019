package fr.pasithee.aoc2019

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Day19 {

}

fun main() {
    val source = readFileToInccode(Day17().javaClass.getResource("day19.txt").path)
    val input = Channel<Long>()
    val output = Channel<Long>()


    var res = 0L
    for (y in 0..49) {
        for (x in 0..49) {
            GlobalScope.launch {
                val intcode =  Intcode(source, source[1], source[2], input, output)
                intcode.runProgram()
            }
            runBlocking {
                input.send(x.toLong())
                input.send(y.toLong())
                if (output.receive() == 0L) {
                    print('.')
                }
                else {
                    print('0')
                }
            }
        }
        println("")
    }

    println(res)
}
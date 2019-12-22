package fr.pasithee.aoc2019

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Day19 {

}

val source = readFileToInccode(Day17().javaClass.getResource("day19.txt").path)

fun main() {
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
                    res++
                }
            }
        }
        println("")
    }

    println(res)

    var guessX = 90L
    var guessY = 100L

    var san = sanityCheck(guessX, guessY)

    while(san!= 0) {
        guessX += adjustX(san)
        guessY += adjustY(san)
        san = sanityCheck(guessX, guessY)
    }
    println(10000*guessX + guessY)

    //9370819 too high
}

fun adjustX(san: Int) : Int = when (san) {
    1 -> 1
    3 -> 1
    7 -> -1
    5 -> -1
    else -> 0
}

fun adjustY(san: Int) : Int = when (san) {
    2 -> 1
    4 -> 1
    7 -> -1
    6 -> -1
    else -> 0
}

fun sanityCheck(x: Long, y: Long, recurse : Boolean = true) : Int {
    if(!sanityCheck(x, y, 1)) {
        if (recurse) {
            println("First coord wrong $x $y")
        }
        return 1
    }

    if (!sanityCheck(x+99, y, 1)) {
        if (recurse) {
            println("x+100 wrong $x $y")
        }
        return 2
    }

    if(!sanityCheck(x, y+99, 1)) {
        if (recurse) {
            println("y+100 wrong $x $y")
        }
        return 3
    }

    if(!sanityCheck(x+99, y+99, 1)) {
        if (recurse) {
            println("x+100, y+100 wrong $x $y")
        }
        return 4
    }

    if (recurse) {
        if (sanityCheck(x - 1, y-1, false) == 0) {
            println("x-1, y-1 works $x $y")
            return 7
        }
        if (sanityCheck(x - 1, y, false) == 0) {
            println("x-1 works $x $y")
            return 5
        }
        if (sanityCheck(x, y - 1, false) == 0) {
            println("y-1 works $x $y")
            return 6
        }
    }
    return 0

}

fun sanityCheck(x: Long, y: Long, v: Long): Boolean {
    val input = Channel<Long>()
    val output = Channel<Long>()
    val intcode =  Intcode(source, source[1], source[2], input, output)

    GlobalScope.launch {
        intcode.runProgram()
    }

    val out =
    runBlocking {
        input.send(x)
        input.send(y)
        output.receive()
    }
    return out == v
}

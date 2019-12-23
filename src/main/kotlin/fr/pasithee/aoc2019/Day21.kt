package fr.pasithee.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class SpringDroid(program: List<Long>, val prog: String) : Intcode(program, program[1], program[2], Channel(), Channel())  {
    var inputSpot = 0

    override suspend fun save(op: Long, modes: List<Long>) : Long {
        setValueAt(op, modes[0], prog[inputSpot].toLong())
        inputSpot++
        return 2
    }

    override suspend fun out(op: Long, modes: List<Long>): Long {
        lastOutput = if (modes[0] == 1L) { op } else { getValueAt(op, modes[0]) }
        print(if (lastOutput < 127) { lastOutput.toChar() } else { lastOutput } )
        return 2
    }
}

class Day21 {}

fun main() {

    val source = readFileToInccode(Day21().javaClass.getResource("day21.txt").path)
    val prog1 = readFileToString(Day21().javaClass.getResource("day21-sol-1.txt").path)


    val part1 = SpringDroid(source, prog1)
    runBlocking {
        part1.runProgram()
    }
    println("")

    val initialSet = (0..511).map {Integer.toBinaryString(it).padStart(9, '0')}
        .filter { !it.contains("0000")}
        .filter { !it.contains("010010")}
        .filter { !it.contains("001010" )}
        .filter { !(it in listOf("101100100", "100101100", "100101101", "101100101"))}
        .filter { it[0] == '1' || it[3] == '1' }
        .filter { !(it[0] == '0' && it[4] == '0' && it[7] == '0') }
        .filter { val k = it.indexOf("000") ; !(k >= 0 && ((k >= 4 && it[k-4] == '0') && (k >= 2 && it[k-2] == '0'))) }

    val noJump = initialSet
        .filter{(it[0] == '1' && it[1] == '1' && it[2] == '1')
                || it[3] == '0'
                || (it[4] == '0' && it[7] == '0')}

    val jumpNow = initialSet.filter{ !noJump.contains(it)}

    println("Y: " + jumpNow.filter {it[0] == '1'}.map { String(it.map {if (it == '0') '.' else '#'}.toCharArray())})
    println("N: " + noJump.filter {it[3] != '0' }.map { String(it.map {if (it == '0') '.' else '#'}.toCharArray())})

    val prog2 = readFileToString(Day21().javaClass.getResource("day21-sol-2.txt").path)

   val part2 = SpringDroid(source, prog2)
    runBlocking {
        part2.runProgram()
    }
}
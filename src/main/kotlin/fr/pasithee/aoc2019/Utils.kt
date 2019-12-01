package fr.pasithee.aoc2019

import java.io.File

fun readFileToIntegers(path: String): List<Int> {
    val f = File(path)
    val lines = f.readLines()
    return lines.map { l -> l.toInt() }
}


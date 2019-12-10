package fr.pasithee.aoc2019

import java.io.File

fun readFileToIntegers(path: String): List<Int> {
    val f = File(path)
    val lines = f.readLines()
    return lines.map { l -> l.toInt() }
}

fun readFileToStrings(path: String) : List<String> {
    val f = File(path)
    return f.readLines()
}

fun readFileToString(path: String) : String {
    val f = File(path)
    return f.readText()
}

fun readFileToCoordinates(path: String, dot: Char) : List<Pair<Int, Int>> {
    val f = File(path)
    val lines = f.readLines()

    val res = mutableListOf<Pair<Int, Int>>()
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            if (lines[y][x] == dot) {
                res.add(Pair(x, y))
            }
        }
    }
    return res
}


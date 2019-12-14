package fr.pasithee.aoc2019

import java.io.File
import kotlin.math.abs

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

fun readFileToInccode(path: String) : List<Long> {
    val f = File(path)
    return f.readText().split(",").map { it.toLong() }
}

class Position(val x: Int, val y: Int) {
    operator fun plus(p: Position): Position {
        return Position(this.x + p.x, this.y + p.y)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    fun manhattan() = abs(x) + abs(y)
}

class Position3D(val x: Int, val y: Int, val z: Int) {
    operator fun plus(p: Position3D): Position3D {
        return Position3D(this.x + p.x, this.y + p.y, this.z + p.z)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position3D

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

}


fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b
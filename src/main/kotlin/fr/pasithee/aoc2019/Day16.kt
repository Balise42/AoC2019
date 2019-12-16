package fr.pasithee.aoc2019

class Day16 {
}

class FFT {
    val basePattern = listOf(0, 1, 0, -1)
    val partialPattern = listOf(1, 0, -1, 0)

    fun getPattern(k: Int) : List<Int> {
        val pattern = mutableListOf<Int>()
        for (digit in basePattern) {
            for (i in 1..k) {
                pattern.add(digit)
            }
        }
        return pattern
    }

    fun computeDigit(digits : List<Int>, k : Int) : Int {
        val pattern = getPattern(k + 1)
        var res = 0
        for (i in digits.indices) {
            res += digits[i] * pattern[(i+1) % pattern.size]
        }
        return if (res >= 0) res % 10 else -res % 10
    }

    fun computePhase(digits : List<Int>) : List<Int> {
        val res = mutableListOf<Int>()
        for (i in digits.indices) {
            res.add(computeDigit(digits, i))
        }
        return res
    }

    fun computeFinalOutput(digits: List<Int>, n : Int) : List<Int> {
        println(digits.size)
        var state = digits
        for (i in 0 until n) {
            state = computePhase(state)
        }
        return state.subList(0, 8)
    }

    fun computeOutputPart2(digits: List<Int>) : List<Int> {
        val realInput = mutableListOf<Int>()
        for (i in 0 until 10000) {
            realInput.addAll(digits)
        }

        val offset = digitsToNumber(digits.subList(0, 7))

        var state = realInput.subList(offset, realInput.size)
        for (i in 0 until 100) {
            state = computePhasePart2(state, offset)
        }
        return state.subList(0, 8)

    }

    fun computePhasePart2(digits: List<Int>, offset : Int) : MutableList<Int> {
        var prevSum = digits.map { it.toLong() }.sum()
        val res = mutableListOf((prevSum % 10).toInt())

        for (k in digits.indices - offset) {
            prevSum -= digits[k]
            res.add((prevSum % 10).toInt())
        }
        return res
    }

    fun numberToDigits(s : String) : List<Int> {
        return s.map { it.toString().toInt() }
    }

    fun digitsToNumber(digits : List<Int>) : Int {
        var s = ""
        for (digit in digits) {
            s += digit.toString()
        }
        return s.toInt()
    }
}

fun main() {
    val input = FFT().numberToDigits(readFileToString(Day16().javaClass.getResource("day16.txt").path))
    println(FFT().computeFinalOutput(input, 100))
    println(FFT().computeOutputPart2(input))
}
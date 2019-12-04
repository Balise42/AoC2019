package fr.pasithee.aoc2019

class Day4(val password : Int) {
    fun isValidPasswordForPart1(shouldCheckForGroup : Boolean = false) : Boolean {
        val passStr = password.toString()
        var hasTwoAdjacentEqualDigits = false
        for (i in 0..passStr.length - 2) {
            if (passStr[i] > passStr[i+1]) {
                return false
            }
            if (passStr[i] == passStr[i+1]) {
                if (!shouldCheckForGroup) {
                    hasTwoAdjacentEqualDigits = true
                } else {
                    if ((i == 0  || passStr[i-1] != passStr[i]) && (i == passStr.length - 2 || passStr[i+2] != passStr[i+1])) {
                        hasTwoAdjacentEqualDigits = true
                    }
                }
            }
        }
        return hasTwoAdjacentEqualDigits
    }

    fun isValidPasswordForPart2() : Boolean {
        return isValidPasswordForPart1(true)
    }
}

fun main() {
    var resPart1 = 0
    var resPart2 = 0
    for (i in 359282..820401) {
        if (Day4(i).isValidPasswordForPart1()) {
            resPart1++
        }
        if (Day4(i).isValidPasswordForPart2()) {
            resPart2++
        }
    }
    println(resPart1)
    println(resPart2)
}
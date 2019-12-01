package fr.pasithee.aoc2019

class Day1{}

fun computeFuel(mass : Int) = mass / 3 - 2

fun computeTotalFuel(modules: List<Int>) = modules.map { m -> computeFuel(m) }.reduce(
    fun(sum, amount) : Int{ return sum + amount }
)

fun computeTotalFuelIncludingAdditional(modules: List<Int>) = modules.map { m -> computeFuelIncludingAdditional(m) }.reduce(
    fun(sum, amount) : Int{ return sum + amount }
)

fun computeFuelIncludingAdditional(m: Int): Int {
    var fuel = computeFuel(m)
    var shouldAdd = computeFuel(fuel)
    while (shouldAdd > 0) {
        fuel += shouldAdd
        shouldAdd = computeFuel(shouldAdd)
    }
    return fuel
}

fun main() {
    val modules = readFileToIntegers(Day1().javaClass.getResource("day1.txt").path)
    println(computeTotalFuel(modules))
    println(computeTotalFuelIncludingAdditional(modules))
}
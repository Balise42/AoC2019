package fr.pasithee.aoc2019

class Day14 {
}

class Reactor(input : List<String>) {
    val parents = mutableMapOf<String, List<String>>()
    val children = mutableMapOf<String, MutableList<String>>()
    val topologicalOrder : List<String>
    val productionUnit = mutableMapOf<String, Long>()
    val requiredReactant = mutableMapOf<Pair<String, String>, Long>()

    init {
        for (line in input) {
            parseLine(line)
        }
        topologicalOrder = computeTopologicalOrder()
    }

    private fun parseLine(line : String) {
        val sides = line.split(" => ")
        val toksRight = sides[1].split(" ")
        val reactants = sides[0].split(", ")
        val tmpParents = mutableListOf<String>()
        for (reactant in reactants) {
            val toksReactant = reactant.split(" ")
            tmpParents.add(toksReactant[1])
            if (children.containsKey(toksReactant[1])) {
                children[toksReactant[1]]!!.add(toksRight[1])
            } else {
                children[toksReactant[1]] = mutableListOf(toksRight[1])
            }
            requiredReactant[Pair(toksReactant[1], toksRight[1])] = toksReactant[0].toLong()
        }
        parents[toksRight[1]] = tmpParents
        productionUnit[toksRight[1]] = toksRight[0].toLong()

    }

    fun computeTopologicalOrder() : List<String> {
        val L = mutableListOf<String>()
        val S = mutableListOf("ORE")
        val parentsCopy =  mutableMapOf<String, MutableList<String>>()
        for (elem in parents) {
            parentsCopy[elem.key] = elem.value.toMutableList()
        }

        while (S.isNotEmpty()) {
            val n = S.removeAt(0)
            L.add(n)
            if (children.containsKey(n)) {
                for (child in children[n]!!) {
                    parentsCopy[child]!!.remove(n)
                    if (parentsCopy[child]!!.isEmpty()) {
                        S.add(child)
                    }
                }
            }
        }
        return L
    }

    fun computeRequiredQuantities(fuel : Long = 1) : Map<String, Long> {
        val requiredQuantities = mutableMapOf<String, Long>()
        requiredQuantities["FUEL"] = fuel
        for (element in topologicalOrder.reversed()) {
            if (element == "ORE") {
                break
            }
            for (reactant in parents[element]!!) {
                var quantity = requiredQuantities.getOrDefault(reactant, 0L)
                val numBatches = if (requiredQuantities[element]!! % productionUnit[element]!! == 0L) {
                    requiredQuantities[element]!! / productionUnit[element]!!
                } else {
                    requiredQuantities[element]!! / productionUnit[element]!! + 1
                }
                quantity += numBatches * requiredReactant[Pair(reactant, element)]!!
                requiredQuantities[reactant] = quantity
            }
        }
        return requiredQuantities
    }

    fun computeAvailableQuantities(ore : Long) : Long{
        var min = ore / computeRequiredQuantities()["ORE"]!!
        var max = min
        while (computeRequiredQuantities(max)["ORE"]!! < ore) {
            max *= 2
        }

        while (max > min + 1) {
            val mid = min + (max - min + 1) / 2
            if (computeRequiredQuantities(mid)["ORE"]!! < ore) {
                min = mid
            } else {
                max = mid
            }
        }
        return min
    }
}

fun main() {
    val lines = readFileToStrings(Day14().javaClass.getResource("day14.txt").path)
    val r = Reactor(lines)
    val quants = r.computeRequiredQuantities()
    println(quants["ORE"])

    println(r.computeAvailableQuantities(1000000000000))
}
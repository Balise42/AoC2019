package fr.pasithee.aoc2019

class Wires(private val wire1: String, private val wire2: String) {

    private val map: MutableMap<Position, MutableList<Int>> = mutableMapOf()
    private val dists: MutableMap<Position, Pair<Int, Int>> = mutableMapOf()

    init {
        parseWire(wire1, 0)
        parseWire(wire2, 1)
    }

    fun closestIntersectionDistance(): Int {
        val intersections = map.filterValues { l -> l.size > 1 }
        var dist = Int.MAX_VALUE
        for (intersection in intersections) {
            if (intersection.key.manhattan() in 1 until dist) {
                dist = intersection.key.manhattan()
            }
        }
        return dist
    }

    fun closestWireDistance(): Int {
        val intersections = dists.filterValues { v -> v.first != -1 && v.second != -1 }
        var dist = Int.MAX_VALUE
        for (intersection in intersections) {
            if (intersection.value.first + intersection.value.second in 1 until dist) {
                dist = intersection.value.first + intersection.value.second
            }
        }
        return dist
    }

    private fun parseWire(wire: String, index: Int) {
        var curPos = Position(0, 0)
        var curDist = 0
        val wireInsts = wire.split(",")
        for (inst in wireInsts) {
            val offset = getOffset(inst)
            val dist = getDist(inst)

            for (i in 0 until dist) {
                if (map[curPos] == null) {
                    map[curPos] = mutableListOf(index)
                } else if (!map[curPos]!!.contains(index)) {
                    map[curPos]!!.add(index)
                }

                if (dists[curPos] == null) {
                    dists[curPos] = if (index == 0) Pair(curDist, -1) else Pair(-1, curDist)
                } else {
                    if (index == 0) {
                        if (dists[curPos]!!.first == -1) {
                            dists[curPos] = Pair(curDist, dists[curPos]!!.second)
                        }
                    } else {
                        if (dists[curPos]!!.second == -1) {
                            dists[curPos] = Pair(dists[curPos]!!.first, curDist)
                        }
                    }
                }
                curPos += offset
                curDist += 1
            }
        }
    }


    private fun getOffset(inst: String): Position = when (inst[0]) {
        'U' -> Position(0, 1)
        'D' -> Position(0, -1)
        'R' -> Position(1, 0)
        else -> Position(-1, 0)
    }

    private fun getDist(inst: String): Int = inst.substring(1).toInt()
}

class Day3()

fun main() {
    val wires = readFileToStrings(Day3().javaClass.getResource("day3.txt").path)
    println(Wires(wires[0], wires[1]).closestIntersectionDistance())
    println(Wires(wires[0], wires[1]).closestWireDistance())
}
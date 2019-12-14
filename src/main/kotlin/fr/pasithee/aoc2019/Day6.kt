package fr.pasithee.aoc2019

class OrbitalMap(rawOrbits: List<String>) {

    val orbitedBy = mutableMapOf<String, MutableList<String>>()
    val orbits = mutableMapOf<String, String>()

    init {
        parseOrbits(rawOrbits)
    }

    fun checksum(): Int {
        var checksum = 0
        var currLayer = 0
        var layer = arrayListOf("COM")
        while (layer.isNotEmpty()) {
            checksum += currLayer * layer.size
            val prevLayer = layer
            layer = arrayListOf()
            for (e in prevLayer) {
                orbitedBy[e]?.let { layer.addAll(it) }
            }
            currLayer++
        }
        return checksum
    }

    private fun parseOrbits(rawOrbits: List<String>) {
        for (orbit in rawOrbits) {
            val parts = orbit.split(")")
            val orbited = parts[0]
            val orbiter = parts[1]
            orbits[orbiter] = orbited
            if (!orbitedBy.containsKey(orbited)) {
                orbitedBy[orbited] = arrayListOf(orbiter)
            } else {
                orbitedBy[orbited]!!.add(orbiter)
            }
        }
    }

    fun transfers(source: String, dest: String): Int {
        val commonParent = getCommonParent(source, dest)
        return dist(orbits[dest]!!, commonParent) + dist(orbits[source]!!, commonParent)
    }

    private fun dist(child: String, parent: String): Int {
        if (child == parent) {
            return 0
        }
        return 1 + dist(orbits[child]!!, parent)
    }

    private fun getCommonParent(p1: String, p2: String): String {
        val parents1 = hashSetOf(p1)
        val parents2 = hashSetOf(p2)

        var cur1 = p1
        var cur2 = p2

        while(parents1.intersect(parents2).isEmpty()) {
            orbits[cur1]?.let { parents1.add(it) }
            orbits[cur2]?.let { parents2.add(it) }
            cur1 = (if (orbits[cur1] != null) orbits[cur1] else cur1)!!
            cur2 = (if (orbits[cur2] != null) orbits[cur2] else cur2)!!
        }
        return parents1.intersect(parents2).first()
    }

}

class Day6 {}

fun main() {
    val orbits = readFileToStrings(Day6().javaClass.getResource("day6.txt").path)
    val om = OrbitalMap(orbits)
    println(om.checksum())
    println(om.transfers("YOU", "SAN"))
}
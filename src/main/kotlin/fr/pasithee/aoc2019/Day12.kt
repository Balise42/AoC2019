package fr.pasithee.aoc2019

import java.lang.StrictMath.abs

class Day12(val positions : MutableList<Position3D>) {
    val velocities = mutableListOf(Position3D(0,0,0), Position3D(0,0,0), Position3D(0,0,0), Position3D(0,0,0))

    fun makeStep() {
        for (i in positions.indices) {
            for (j in positions.indices) {
                if (i < j) {
                    updateVelocity(i, j)
                }
            }
        }
        for (i in positions.indices) {
            updatePosition(i)
        }
    }

    private fun updatePosition(i : Int) {
        positions[i] += velocities[i]
    }

    private fun updateVelocity(i : Int, j: Int) {
        val pos1 = positions[i]
        val pos2 = positions[j]
        val vel1 = velocities[i]
        val vel2 = velocities[j]

        val x1 = if (pos1.x > pos2.x) { vel1.x - 1 } else if (pos1.x < pos2.x ){ vel1.x + 1 } else vel1.x
        val y1 = if (pos1.y > pos2.y) { vel1.y - 1 } else if (pos1.y < pos2.y ){ vel1.y + 1 } else vel1.y
        val z1 = if (pos1.z > pos2.z) { vel1.z - 1 } else if (pos1.z < pos2.z ){ vel1.z + 1 } else vel1.z

        val x2 = if (pos1.x > pos2.x) { vel2.x + 1 } else if (pos1.x < pos2.x ){ vel2.x - 1 } else vel2.x
        val y2 = if (pos1.y > pos2.y) { vel2.y + 1 } else if (pos1.y < pos2.y ){ vel2.y - 1 } else vel2.y
        val z2 = if (pos1.z > pos2.z) { vel2.z + 1 } else if (pos1.z < pos2.z ){ vel2.z - 1 } else vel2.z

        velocities[i] = Position3D(x1, y1, z1)
        velocities[j] = Position3D(x2, y2, z2)
    }

    fun totalEnergy() : Int {
        var total = 0
        var potential = 0
        var kin = 0
        for (i in positions.indices) {
            val p = positions[i]
            val v = velocities[i]
            potential = abs(p.x) + abs(p.y) + abs(p.z)
            kin = abs(v.x) + abs(v.y) + abs(v.z)
            total += potential * kin
        }
        return total
    }
}

class Day121D(val pos : MutableList<Int>) {
    val vel = mutableListOf(0,0,0,0)

    fun makeStep() {
        for (i in pos.indices) {
            for (j in pos.indices) {
                if (i < j) {
                    updateVelocity(i, j)
                }
            }
        }
        for (i in pos.indices) {
            updatePosition(i)
        }
    }

    private fun updatePosition(i : Int) {
        pos[i] += vel[i]
    }

    private fun updateVelocity(i : Int, j: Int) {
        val pos1 = pos[i]
        val pos2 = pos[j]

        vel[i] = if (pos1 < pos2) { vel[i] + 1 } else if (pos1 > pos2) { vel[i] - 1 } else vel[i]
        vel[j] = if (pos1 < pos2) { vel[j] - 1 } else if (pos1 > pos2) { vel[j] + 1 } else vel[j]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Day121D

        if (pos != other.pos) return false
        if (vel != other.vel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pos.hashCode()
        result = 31 * result + vel.hashCode()
        return result
    }

    fun getState() : List<Int> {
        val state = mutableListOf<Int>()
        state.addAll(pos)
        state.addAll(vel)
        return state
    }
}

/*fun main() {
    val obj = Day12(mutableListOf(
        Position3D(-15, 1, 4),
        Position3D(1, -10, -8),
        Position3D(-5, 4, 9),
        Position3D(4, 6, -2)))
    for (i in 0..999) {
        obj.makeStep()
    }
    println(obj.totalEnergy())

    val objPart2 = Day12(mutableListOf(
        Position3D(-15, 1, 4),
        Position3D(1, -10, -8),
        Position3D(-5, 4, 9),
        Position3D(4, 6, -2)))
*/

fun main() {
   /* <x=-8, y=-10, z=0>
    <x=5, y=5, z=10>
    <x=2, y=-7, z=3>
    <x=9, y=-8, z=-3>
*/




    val x = Day121D(mutableListOf(-15, 1, -5,4))
    val stateX = x.getState()
    x.makeStep()
    var loopX = 1L
    while (stateX != x.getState()) {
        x.makeStep()
        loopX++
    }

    val y = Day121D(mutableListOf(1, -10, 4, 6))
    val stateY = y.getState()
    y.makeStep()
    var loopY = 1L
    while (stateY != y.getState()) {
        y.makeStep()
        loopY++
    }

    val z = Day121D(mutableListOf(4, -8, 9, -2))
    val stateZ = z.getState()
    var loopZ = 1L
    z.makeStep()
    while (stateZ != z.getState()) {
        z.makeStep()
        loopZ++
    }

    println(lcm(lcm(lcm(loopX, loopY), lcm(loopZ, loopX)), lcm(lcm(loopY, loopZ), lcm(loopX, loopZ))))
}

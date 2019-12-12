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
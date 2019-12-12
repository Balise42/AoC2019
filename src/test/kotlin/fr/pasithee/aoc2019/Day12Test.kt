package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day12Test {
    @Test
    fun velocitiesAndPositionsShouldUpdate() {
        val obj = Day12(mutableListOf(Position3D(-1, 0, 2), Position3D(2, -10, -7), Position3D(4, -8, 8), Position3D(3, 5, -1)))
        obj.makeStep()
        assertEquals(mutableListOf(Position3D(3, -1, -1), Position3D(1, 3, 3), Position3D(-3, 1, -3), Position3D(-1, -3, 1)), obj.velocities)
        assertEquals(mutableListOf(Position3D(2, -1, 1), Position3D(3, -7, -4), Position3D(1, -7, 5), Position3D(2, 2, 0)), obj.positions)

    }

    @Test
    fun energyShouldWork() {
        val obj = Day12(mutableListOf(Position3D(-1, 0, 2), Position3D(2, -10, -7), Position3D(4, -8, 8), Position3D(3, 5, -1)))
        for (i in 0..9) {
            obj.makeStep()
        }
        assertEquals(179, obj.totalEnergy())
    }
}
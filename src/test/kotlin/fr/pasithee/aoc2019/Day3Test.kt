package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day3Test {
    @Test
    fun closestIntersectionShouldComputeCorrectly() {
        assertEquals(6, Wires("R8,U5,L5,D3", "U7,R6,D4,L4").closestIntersectionDistance())
        assertEquals(
            159, Wires(
                "R75,D30,R83,U83,L12,D49,R71,U7,L72",
                "U62,R66,U55,R34,D71,R55,D58,R83"
            ).closestIntersectionDistance()
        )
        assertEquals(
            135, Wires(
                "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
                "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
            ).closestIntersectionDistance()
        )
    }

    @Test
    fun closestWireIntersectionShouldComputeCorrectly() {
        assertEquals(30, Wires("R8,U5,L5,D3", "U7,R6,D4,L4").closestWireDistance())
        assertEquals(
            610, Wires(
                "R75,D30,R83,U83,L12,D49,R71,U7,L72",
                "U62,R66,U55,R34,D71,R55,D58,R83"
            ).closestWireDistance()
        )
        assertEquals(
            410, Wires(
                "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",
                "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"
            ).closestWireDistance()
        )
    }
}
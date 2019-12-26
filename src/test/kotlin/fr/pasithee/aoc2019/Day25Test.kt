package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day25Test {
    @Test
    fun createRoomShouldWork() {
        val desc = "== Hot Chocolate Fountain ==\n" +
                "Somehow, it's still working.\n" +
                "\n" +
                "Doors here lead:\n" +
                "- south\n" +
                "\n" +
                "Items here:\n" +
                "- giant electromagnet\n" +
                "\n" +
                "Command?"
        val r = Room(desc)
        assertEquals("Hot Chocolate Fountain", r.id)
        assertEquals(listOf("giant electromagnet"), r.roomItems)
        assertNull(r.north)
        assertNull(r.west)
        assertNull(r.east)
        assertEquals(r.south, unknownRoom)
    }
}
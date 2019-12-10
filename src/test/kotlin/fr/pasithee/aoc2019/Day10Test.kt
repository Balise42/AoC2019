package fr.pasithee.aoc2019

import org.junit.Assert.*
import org.junit.Test

class Day10Test {
    @Test
    fun isCollinearIsTrue() {
        assertTrue(AsteroidMap(emptyList()).areCollinear(Point(3,4), Point(1,0), Point(2,2)))
        assertTrue(AsteroidMap(emptyList()).areCollinear(Point(3,4), Point(2,2), Point(1,0)))
        assertTrue(AsteroidMap(emptyList()).areCollinear(Point(2,2), Point(3,4), Point(1,0)))
        assertTrue(AsteroidMap(emptyList()).areCollinear(Point(2,2), Point(1,0), Point(3,4)))
        assertTrue(AsteroidMap(emptyList()).areCollinear(Point(1,0), Point(2,2), Point(3,4)))
        assertTrue(AsteroidMap(emptyList()).areCollinear(Point(1,0), Point(3,4), Point(2,2)))
    }

    @Test
    fun isCollinearIsFalse() {
        assertFalse(AsteroidMap(emptyList()).areCollinear(Point(3,4), Point(1,0), Point(2,1)))
        assertFalse(AsteroidMap(emptyList()).areCollinear(Point(3,4), Point(2,2), Point(1,1)))
        assertFalse(AsteroidMap(emptyList()).areCollinear(Point(2,2), Point(3,4), Point(1,1)))
        assertFalse(AsteroidMap(emptyList()).areCollinear(Point(2,2), Point(1,0), Point(3,3)))
        assertFalse(AsteroidMap(emptyList()).areCollinear(Point(1,0), Point(2,2), Point(3,3)))
        assertFalse(AsteroidMap(emptyList()).areCollinear(Point(1,0), Point(3,4), Point(2,1)))
    }

    @Test
    fun isBlockingIsTrue() {
        assertTrue(AsteroidMap(emptyList()).isBlocking(Point(2,2), Point(3,4), Point(1,0)))
        assertTrue(AsteroidMap(emptyList()).isBlocking(Point(2,2), Point(1,0), Point(3,4)))
    }

    @Test
    fun isBlockingIsFalse() {
        assertFalse(AsteroidMap(emptyList()).isBlocking(Point(3,4), Point(1,0), Point(2,2)))
        assertFalse(AsteroidMap(emptyList()).isBlocking(Point(3,4), Point(2,2), Point(1,0)))
        assertFalse(AsteroidMap(emptyList()).isBlocking(Point(1,0), Point(2,2), Point(3,4)))
        assertFalse(AsteroidMap(emptyList()).isBlocking(Point(1,0), Point(3,4), Point(2,2)))
    }

    @Test
    fun smallExampleShouldWork() {
        val map = AsteroidMap(listOf(
            Point(1,0),
            Point(4, 0),
            Point(0, 2),
            Point(1, 2),
            Point(2, 2),
            Point(3, 2),
            Point(4, 2),
            Point(4, 3),
            Point(3, 4),
            Point(4, 4)
        ))
        assertEquals(8, map.getMaxSeenAsteroids())
    }

    @Test
    fun example1() {
        val pairs = readFileToCoordinates(javaClass.getResource("day10ex1.txt").path, '#')
        val map = AsteroidMap(pairs.map { Point(it.first, it.second )})
        assertEquals(33, map.getMaxSeenAsteroids())
    }

    @Test
    fun example2() {
        val pairs = readFileToCoordinates(javaClass.getResource("day10ex2.txt").path, '#')
        val map = AsteroidMap(pairs.map { Point(it.first, it.second )})
        assertEquals(35, map.getMaxSeenAsteroids())
    }

    @Test
    fun example3() {
        val pairs = readFileToCoordinates(javaClass.getResource("day10ex3.txt").path, '#')
        val map = AsteroidMap(pairs.map { Point(it.first, it.second )})
        assertEquals(41, map.getMaxSeenAsteroids())
    }

    @Test
    fun example4() {
        val pairs = readFileToCoordinates(javaClass.getResource("day10ex4.txt").path, '#')
        val map = AsteroidMap(pairs.map { Point(it.first, it.second )})
        assertEquals(210, map.getMaxSeenAsteroids())
    }

    @Test
    fun asteroid200ShouldWork() {
        val pairs = readFileToCoordinates(javaClass.getResource("day10ex4.txt").path, '#')
        val map = AsteroidMap(pairs.map { Point(it.first, it.second )})
        val res = map.get200thAsteroid(Point(11, 13))
        println("" + res.x + " " + res.y)
    }
}
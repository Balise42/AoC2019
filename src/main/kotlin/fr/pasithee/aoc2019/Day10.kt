package fr.pasithee.aoc2019

import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

class Point(val x : Int, val y : Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

class AsteroidMap(val asteroids : List<Point>) {
    fun areCollinear(p1: Point, p2: Point, p3: Point) : Boolean {
        return ((p2.x - p1.x)*(p3.y-p1.y) - (p2.y - p1.y)*(p3.x - p1.x) == 0)
    }

    fun isBlocking(obstacle: Point, e1: Point, e2: Point) : Boolean {
        val dot1 = ((e2.x - e1.x) * (obstacle.x - e1.x) + (e2.y - e1.y) * (obstacle.y - e1.y))
        val dot2 = ((e2.x - e1.x) * (e2.x - e1.x) + (e2.y - e1.y) * (e2.y - e1.y))

        return (dot1 > 0 && dot2 > dot1)
    }

    fun getVisibleAsteroids(tower: Point) : List<Point> {
        val visibleAsteroids = asteroids.toMutableList()
        visibleAsteroids.remove(tower)
        for (asteroid in asteroids) {
            if (asteroid == tower) {
                continue
            }
            for (obstacle in asteroids) {
                if (obstacle == tower || obstacle == asteroid) {
                    continue
                }
                if (visibleAsteroids.contains(asteroid) && areCollinear(obstacle, tower, asteroid)
                    && isBlocking(obstacle, tower, asteroid)) {
                    visibleAsteroids.remove(asteroid)
                }
            }
        }
        return visibleAsteroids
    }

    // since there is more than 200 visible asteroids in the first round we actually don't care about the other layers!
    fun get200thAsteroid(tower: Point) : Point {
        val visibleAsteroids = getVisibleAsteroids(tower)
        val otherQuadrants = visibleAsteroids.filter {
            (it.x >= tower.x || it.y > tower.y)
        }

        val fourthQuadrant = visibleAsteroids.filter {
            (it.x < tower.x && it.y <= tower.y)
        }

        val sortedAsteroids = fourthQuadrant.sortedBy {
            -getAngle(tower, it)
        }
        return sortedAsteroids[199-otherQuadrants.size]
    }

    private fun getAngle(tower: Point, it: Point): Double {
        return atan2(it.x.toDouble() - tower.x, it.y.toDouble() - tower.y)
    }

    fun getMaxSeenAsteroids() : Int {
        var maxVisibleAsteroids = 0
        var point : Point? = null
        for (asteroid in asteroids) {
            val visibleAsteroids = getVisibleAsteroids(asteroid).size
            if (visibleAsteroids > maxVisibleAsteroids) {
                maxVisibleAsteroids = visibleAsteroids
                point = asteroid
            }
        }
        println("" + point!!.x + " " + point.y)
        return maxVisibleAsteroids
    }
}

class Day10 {}

fun main() {
    val pairs = readFileToCoordinates(Day10().javaClass.getResource("day10.txt").path, '#')
    val asteroids = pairs.map { Point(it.first, it.second )}.toMutableList()

    println(AsteroidMap(asteroids).getMaxSeenAsteroids())
    val asteroid200 = AsteroidMap(asteroids).get200thAsteroid(Point(11, 19))
    println(asteroid200.x * 100 + asteroid200.y)
}
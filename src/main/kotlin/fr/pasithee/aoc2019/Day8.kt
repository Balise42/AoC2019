package fr.pasithee.aoc2019

class Day8 {
}

class Sif(val data : String, val width : Int, val height : Int) {
    fun checksum() : Int {
        var minNum0 = width * height + 1
        var checksum = 0
        for (i in 0 until data.length - width*height step width * height) {
            val layer = data.subSequence(i, i+width*height)
            val num0 = layer.count { c -> c == '0' }
            if (num0 < minNum0) {
                minNum0 = num0
                checksum = layer.count { c -> c == '1' } * layer.count { c -> c == '2' }
            }
        }
        return checksum
    }

    fun printImage() {
        val layers = (0 until data.length - width * height step width * height).map {data.subSequence(it, it+width*height) }
        val imageData = (0 until width*height).map {
            var value = '2'
            for (i in layers.indices) {
                if (layers[i][it] < '2') {
                    value = layers[i][it]
                    break
                }
            }
            value
        }
        for (i in 0 until height) {
            for (j in 0 until width) {
                val char = imageData[width * i + j]
                print( if (char == '0') '.' else "#")
            }
            println("")
        }
    }
}

fun main() {
    val data = readFileToString(Day3().javaClass.getResource("day8.txt").path)
    val image = Sif(data, 25, 6)
    println(image.checksum())
    image.printImage()
}
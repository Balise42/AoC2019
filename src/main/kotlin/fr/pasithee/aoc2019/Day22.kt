package fr.pasithee.aoc2019

class Day22 {}

class CardShuffler(lastCard : Long) {

    var deck = (0..lastCard).toList()

    fun dealIntoNewStack() {
        deck = deck.reversed()
    }

    fun cutN(n : Int) {
        if (n > 0) {
            deck = deck.subList(n, deck.size) + deck.subList(0, n)
        } else {
            cutN(deck.size + n)
        }
    }

    fun dealWithIncrement(n : Int) {
        val tmpDeck = LongArray(deck.size)
        var pos = 0
        for (card in deck) {
            tmpDeck[pos] = card
            pos = (pos + n) % deck.size
        }
        deck = tmpDeck.toList()
    }

    fun doOperation(op : String) {
        when {
            op.startsWith("cut ") -> cutN(op.replace("cut ", "").toInt())
            op.startsWith("deal with increment ") -> dealWithIncrement(op.replace("deal with increment ", "").toInt())
            op.startsWith("deal into new stack") -> dealIntoNewStack()
        }
    }

    fun shuffle(ops : List<String>) {
        for (op in ops) {
            doOperation(op)
        }
    }
}

fun main() {
    val ops = readFileToStrings(Day22().javaClass.getResource("day22.txt").path)
    val deck = CardShuffler(10006)
    deck.shuffle(ops)
    println(deck.deck.indexOf(2019))
}


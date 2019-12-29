package fr.pasithee.aoc2019

import java.lang.IllegalArgumentException
import java.math.BigInteger

class Day22 {}

class CardShuffler(lastCard : Int) {

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
        val tmpDeck = IntArray(deck.size)
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

class LargeCardShuffler(val lastCard : Long) {
    fun cardCutN(n : Long, pos : Long) : Long {
        if (n > 0) {
           return (n + pos) % (lastCard + 1)
        } else {
            return cardCutN(lastCard + 1 + n, pos)
        }
    }

    fun cardDealWithIncrement(n: Long, pos : Long) : Long {
        if (pos == 0L) {
            return 0
        }
        val modInv = modInverse(n, lastCard + 1)
        val res = (BigInteger.valueOf(pos) * BigInteger.valueOf(modInv)) % (BigInteger.valueOf(lastCard + 1))
        return res.toLong()
    }

    fun cardDealStack(pos : Long) : Long {
        return lastCard - pos
    }

    fun shuffle(ops: List<String>, pos: Long) : Long {
        if (ops.isEmpty()) {
            return pos
        }
        val op = ops.last()
        val prevPos = doOperation(op, pos)
        return shuffle(ops.dropLast(1), prevPos)
    }

    fun doOperation(op : String, pos : Long) : Long {
        return when {
            op.startsWith("cut ") -> cardCutN(op.replace("cut ", "").toLong(), pos)
            op.startsWith("deal with increment ") -> cardDealWithIncrement((op.replace("deal with increment ", "").toLong()), pos)
            op.startsWith("deal into new stack") -> cardDealStack(pos)
            else -> throw(IllegalArgumentException("invalid operation"))
        }
    }

}

fun modInverse(aInit: Long, mInit: Long): Long {
    return BigInteger.valueOf(aInit).modPow(BigInteger.valueOf(-1), BigInteger.valueOf(mInit)).toLong()
}

class ReducedCardShuffler(val numCards : BigInteger, val ops: List<String>) {
    val a: BigInteger
    val b: BigInteger

    init {
        var tmpA = BigInteger.valueOf(1)
        var tmpB = BigInteger.valueOf(0)

        for (op in ops.reversed()) {
            val res = when {
                op.startsWith("cut ") -> cardCutN(op.replace("cut ", "").toLong(), tmpA, tmpB)
                op.startsWith("deal with increment ") -> cardDealWithIncrement(
                    (op.replace(
                        "deal with increment ",
                        ""
                    ).toLong()), tmpA, tmpB
                )
                op.startsWith("deal into new stack") -> cardDealStack(tmpA, tmpB)
                else -> throw(IllegalArgumentException("invalid operation"))
            }
            tmpA = res.first
            tmpB = res.second
        }

        a = tmpA
        b = tmpB
    }

    fun transform(a: BigInteger, b: BigInteger, a2: BigInteger, b2: BigInteger): Pair<BigInteger, BigInteger> {
        return Pair((a * a2) % numCards, (a2 * b + b2) % numCards)
    }

    fun cardCutN(n: Long, a: BigInteger, b: BigInteger): Pair<BigInteger, BigInteger> =
        transform(a, b, BigInteger.valueOf(1), BigInteger.valueOf(n))


    fun cardDealWithIncrement(n: Long, a: BigInteger, b: BigInteger): Pair<BigInteger, BigInteger> {
        val modInv = modInverse(n, numCards.toLong())
        return transform(a, b, BigInteger.valueOf(modInv), BigInteger.valueOf(0))
    }

    fun cardDealStack(a : BigInteger, b : BigInteger) = transform(a, b, BigInteger.valueOf(-1), BigInteger.valueOf(-1))

    fun apply(pos : BigInteger) : Long {
        val v = ((pos * a + b) % numCards)
        return if (v < BigInteger.ZERO) { (v + numCards).toLong() } else { v.toLong() }
    }

    fun apply(pos : BigInteger, numOps : BigInteger) : Long {
        val powA = a.modPow(numOps, numCards)
        val firstTerm = (powA * (pos % numCards)) % numCards

       /* var v = pos
        for (i in 1..numOps.toLong()) {
            v = BigInteger.valueOf(apply(v))
        }
        return v.toLong()*/
        val inverse1MinusA = (BigInteger.valueOf(1) - a).modPow(BigInteger.valueOf(-1), numCards)

        val geomSeq = ((BigInteger.valueOf(1) - powA)%numCards * inverse1MinusA%numCards)%numCards

        val secondTerm = (b % numCards) * (geomSeq % numCards) % numCards
        val sum = ((firstTerm + secondTerm) % numCards)
        return if (sum < BigInteger.ZERO) { (sum + numCards).toLong()} else {sum.toLong()}
    }
}

fun main() {
    val ops = readFileToStrings(Day22().javaClass.getResource("day22.txt").path)
    val deck = CardShuffler(10006)
    deck.shuffle(ops)
    println(deck.deck.indexOf(2019))

    val deckInLargeShuffler = LargeCardShuffler(10006)
    println(deckInLargeShuffler.shuffle(ops, 6431))

    val reducedCardShuffler = ReducedCardShuffler(BigInteger.valueOf(10007), ops)
    println(reducedCardShuffler.apply(BigInteger.valueOf(6431)))
    println(reducedCardShuffler.apply(BigInteger.valueOf(2019)))
    println(reducedCardShuffler.apply(BigInteger.valueOf(8420)))
    println(reducedCardShuffler.apply(BigInteger.valueOf(6431), BigInteger.valueOf(3)))

    val part2Shuffler = ReducedCardShuffler(BigInteger.valueOf(119315717514047), ops)
    println(part2Shuffler.apply(BigInteger.valueOf(2020), BigInteger.valueOf(101741582076661)))

}



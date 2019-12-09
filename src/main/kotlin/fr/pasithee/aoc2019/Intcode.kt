package fr.pasithee.aoc2019

import kotlinx.coroutines.channels.Channel
import java.lang.UnsupportedOperationException

class Memory(initMemory : List<Long> ) {
    val mem : MutableMap<Long, Long> = mutableMapOf()

    init {
        for (i in initMemory.indices) {
            mem[i.toLong()] = initMemory[i]
        }
    }

    operator fun get(i : Long) = if (mem.containsKey(i)) mem[i]!! else 0
    operator fun set(k: Long, v: Long) {
        mem[k] = v
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Memory

        if (mem != other.mem) return false

        return true
    }

    override fun hashCode(): Int {
        return mem.hashCode()
    }

}


class Intcode(prog: List<Long>, noun: Long, verb: Long, var input: Channel<Long>, var output: Channel<Long>) {

    private val memory = Memory(prog)
    private var lastOutput = -1L
    private var relativeBase = 0L

    /*init {
        this.memory[1] = noun
        this.memory[2] = verb
    }*/

    operator fun get(i: Long) = memory[i]

    suspend fun runProgram() : Long {
        var instPtr = 0L
        while(true) {
            val modes = getModes(memory[instPtr] / 100)
            when(memory[instPtr] % 100) {
                99L -> return lastOutput
                1L -> instPtr += add(memory[instPtr + 1], memory[instPtr + 2], memory[instPtr + 3], modes)
                2L -> instPtr += mul(memory[instPtr + 1], memory[instPtr + 2], memory[instPtr + 3], modes)
                3L -> instPtr += save(memory[instPtr + 1], modes)
                4L -> instPtr += out(memory[instPtr + 1], modes)
                5L -> instPtr = jit(instPtr, memory[instPtr+1], memory[instPtr + 2], modes)
                6L -> instPtr = jif(instPtr, memory[instPtr+1], memory[instPtr + 2], modes)
                7L -> instPtr += lt(memory[instPtr + 1], memory[instPtr + 2], memory[instPtr + 3], modes)
                8L -> instPtr += eq(memory[instPtr + 1], memory[instPtr + 2], memory[instPtr + 3], modes)
                9L -> instPtr += rel(memory[instPtr + 1], modes)
                else -> throw(UnsupportedOperationException("instPtr: " + memory[instPtr]))
            }
        }
    }

    private fun getModes(num: Long) : List<Long> {
        return arrayListOf(
            num % 10,
            (num / 10) % 10,
            num / 100
        )
    }

    private fun getValueAt(addr: Long, mode: Long) : Long {
        assert(mode == 0L || mode == 2L)
        if (mode == 0L) {
            return memory[addr]
        } else {
            return memory[addr + relativeBase]
        }
    }

    private fun setValueAt(addr: Long, mode: Long, value : Long) {
        assert(mode == 0L || mode == 2L)
        if (mode == 0L) {
            memory[addr] = value
        } else {
            memory[addr + relativeBase] = value
        }
    }

    private fun jit(instrPtr: Long, op1: Long, op2: Long, modes: List<Long>): Long {
        val cond = if (modes[0] == 1L) op1 else getValueAt(op1, modes[0])
        val jumpVal = if (modes[1] == 1L) op2 else getValueAt(op2, modes[1])
        if (cond != 0L) {
            return jumpVal
        } else {
            return instrPtr + 3
        }
    }

    private fun jif(instrPtr: Long, op1: Long, op2: Long, modes: List<Long>): Long {
        val cond = if (modes[0] == 1L) op1 else getValueAt(op1, modes[0])
        val jumpVal = if (modes[1] == 1L) op2 else getValueAt(op2, modes[1])
        if (cond == 0L) {
            return jumpVal
        } else {
            return instrPtr + 3
        }
    }

    private fun lt(op1: Long, op2: Long, res: Long, modes: List<Long>) : Long {
        val v1 = if (modes[0] == 1L) op1 else getValueAt(op1, modes[0])
        val v2 = if (modes[1] == 1L) op2 else getValueAt(op2, modes[1])
        setValueAt(res, modes[2], if (v1 < v2) 1 else 0)
        return 4
    }

    private fun eq(op1: Long, op2: Long, res: Long, modes: List<Long>) : Long {
        val v1 = if (modes[0] == 1L) op1 else getValueAt(op1, modes[0])
        val v2 = if (modes[1] == 1L) op2 else getValueAt(op2, modes[1])
        setValueAt(res, modes[2], if (v1 == v2) 1 else 0)
        return 4
    }

    private fun rel(op: Long, modes: List<Long>) : Long {
        relativeBase += if (modes[0] == 1L) op else getValueAt(op, modes[0])
        return 2
    }

    private suspend fun out(op: Long, modes: List<Long>): Long {
        if (modes[0] == 1L) {
            lastOutput = op
            output.send(op)
        } else {
            lastOutput = getValueAt(op, modes[0])
            output.send(getValueAt(op, modes[0]))
        }
        return 2
    }

    private suspend fun save(op: Long, modes: List<Long>): Long {
        setValueAt(op, modes[0], input.receive())
        return 2
    }

    private fun add(op1: Long, op2: Long, res: Long, modes: List<Long>) : Long {
        val v1 = if (modes[0] == 1L) op1 else getValueAt(op1, modes[0])
        val v2 = if (modes[1] == 1L) op2 else getValueAt(op2, modes[1])
        setValueAt(res, modes[2], v1 + v2)
        return 4
    }

    private fun mul(op1: Long, op2: Long, res: Long, modes: List<Long>) : Long {
        val v1 = if (modes[0] == 1L) op1 else getValueAt(op1, modes[0])
        val v2 = if (modes[1] == 1L) op2 else getValueAt(op2, modes[1])
        setValueAt(res, modes[2], v1 * v2)
        return 4
    }

    fun state() : Memory {
        return memory
    }
}
package fr.pasithee.aoc2019

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class Day23 {}

class Packet(val dest : Long, var x: Long?, var y: Long?) {

}

val channels = mutableMapOf<Long, Channel<Packet>>()

class NIC(program : List<Long>) : Intcode(program, program[1], program[2], Channel(Channel.UNLIMITED), Channel(1)) {
    var inputChannel : Channel<Packet>? = null
    var incomingPacket : Packet? = null
    var outGoingPacket : Packet? = null
    var nicNum : Long? = null
    var idle = false


    override suspend fun out(op: Long, modes: List<Long>): Long {
        idle = false
        val value = if (modes[0] == 1L) {
            op
        } else {
            getValueAt(op, modes[0])
        }

        if (outGoingPacket == null) {
            outGoingPacket = Packet(value, null, null)
        } else {
            if (outGoingPacket!!.x == null) {
                outGoingPacket!!.x = value
            } else {
                outGoingPacket!!.y = value
                if(channels.containsKey(outGoingPacket!!.dest)) {
                    channels.getValue(outGoingPacket!!.dest).send(outGoingPacket!!)
                }
                outGoingPacket = null
            }
        }
        return 2
    }

    @ExperimentalCoroutinesApi
    override suspend fun save(op: Long, modes: List<Long>): Long {
        if (inputChannel == null) {
            nicNum = input.receive()
            inputChannel = channels.getValue(nicNum!!)
            setValueAt(op, modes[0], nicNum!!)
            return 2
        }

        if (incomingPacket == null) {
            if (inputChannel!!.isEmpty) {
                setValueAt(op, modes[0], -1)
                idle = true
                delay(50)
            } else {
                incomingPacket = inputChannel!!.receive()
                setValueAt(op, modes[0], incomingPacket!!.x!!)
            }
        } else {
            setValueAt(op, modes[0], incomingPacket!!.y!!)
            incomingPacket = null
        }
        return 2
    }
}

fun main() {
    val source = readFileToInccode(Day23().javaClass.getResource("day23.txt").path)
    val nics = mutableListOf<NIC>()
    channels[255] = Channel(Channel.UNLIMITED)
    for (i in 0L..49) {
        channels[i] = Channel(Channel.UNLIMITED)
        val nic = NIC(source)
        nics.add(nic)

        GlobalScope.launch {
            nic.input.offer(i)
            nic.runProgram()
        }
    }

    var packet : Packet? = null

    GlobalScope.launch {
        while(true) {
            packet = channels.getValue(255).receive()
        }
    }

    runBlocking {
        val values = mutableSetOf<Long>()
        while (true) {
            var idle = true
            for (i in 0..10) {
                for (j in 0..49) {
                    if (!nics[i].idle) {
                        idle = false
                        break
                    }
                }
                if (!idle) {
                    break
                }
            }
            if (!idle) {
                delay(100)
                continue
            }
            delay(200)
            channels.getValue(0).send(Packet(0, packet!!.x!!, packet!!.y!!))
            if (values.contains(packet!!.y!!)) {
                println(packet!!.y)
                break
            } else {
                values.add(packet!!.y!!)
                println(packet!!.y!!)
            }
        }
    }
}


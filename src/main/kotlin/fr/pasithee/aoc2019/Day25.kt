package fr.pasithee.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import java.lang.IllegalStateException
import java.lang.Math.pow
import java.lang.System.exit

class ReindeerShip(program: List<Long>, val shipExplorer: ShipExplorer) : Intcode(program, program[1], program[2], Channel(), Channel())  {
    var nextCommand = ""
    var readBuffer = ""
    var inputSpot = 0
    var reading = true

    val commands =  readFileToString(Day21().javaClass.getResource("day25-sol.txt").path)


    override suspend fun save(op: Long, modes: List<Long>) : Long {
        if (reading) {
            nextCommand = shipExplorer.getNextCommand(readBuffer)
            inputSpot = 0
            reading = false
        }
        setValueAt(op, modes[0], nextCommand[inputSpot].toLong())
        inputSpot++
        return 2
    }

    override fun outputHook(): Long {
        if (readBuffer.contains("cockpit")) {
            exit(0)
        }
        return lastOutput
    }

    override suspend fun out(op: Long, modes: List<Long>): Long {
        if (!reading) {
            readBuffer = ""
            reading = true
        }
        lastOutput = if (modes[0] == 1L) { op } else { getValueAt(op, modes[0]) }
        readBuffer += lastOutput.toChar()
        print(lastOutput.toChar())
        return 2
    }
}

val titleRegex = Regex("== (.+) ==.*", RegexOption.MULTILINE)

private fun getIdFromDesc(desc : String): String? {
    return if (desc.contains("==")) {
        val splitdesc = desc.split("==")
        splitdesc.subList(splitdesc.lastIndex - 1, splitdesc.size)[0].drop(1).dropLast(1)
    } else {
        null
    }
}


fun getItemsFromString(s : String) : List<String> {
    val items = mutableListOf<String>()

    val itemParts = s.split('\n')
    for (line in itemParts) {
        if (line.startsWith("- ")) {
            items.add(line.substring(2, line.length))
        }
    }
    return items
}

class Room(private val desc : String) {
    var south : Room? = null
    var north : Room? = null
    var east : Room? = null
    var west : Room? = null

    val id : String
    val roomItems : List<String>
    var roomAvailableItems : MutableList<String>
    val itemsBlacklist = mutableListOf<String>()

    init {
        roomItems = getItemsFromDesc()
        roomAvailableItems = getItemsFromDesc().toMutableList()
        fillDirectionsFromDesc()
        id = getIdFromDesc(desc)!!
    }


    fun getItemsFromDesc() : List<String> {
        if (desc.contains("Items here:")) {
            val begin = desc.indexOf("Items here:")
            val end = desc.indexOf("Command?")
            return(getItemsFromString(desc.substring(begin, end)))
        }
        return emptyList()
    }

    fun fillDirectionsFromDesc() {
        val directionIndex = desc.indexOf("Doors here lead:")
        if (directionIndex < 0) {
            return
        }
        val nextBlockIndex =
            if (desc.contains("Items here:")) {
                desc.indexOf("Items here:")
            } else {
                desc.indexOf("Command?")
            }
        val directionString = desc.substring(directionIndex, nextBlockIndex)
        if (directionString.contains("south")) {
            south = unknownRoom
        }
        if (directionString.contains("east")) {
            east = unknownRoom
        }
        if (directionString.contains("west")) {
            west = unknownRoom
        }
        if (directionString.contains("north")){
            north = unknownRoom
        }
    }

    fun hasUnknowns(): Boolean {
        return north == unknownRoom || south == unknownRoom || east == unknownRoom || west == unknownRoom
    }
}

val unknownRoom = Room("== unknown bépoè ==")

enum class ExplorationState {EXPLORING, GATHERING, MOVING_TO_CHECKPOINT, HACKING_CHECKPOINT, DROPPING, TAKING }

class ShipExplorer() {
    val ship = mutableMapOf<String, Room>()
    var lastCommand = ""
    var room : Room = unknownRoom
    var prevRoom : Room = unknownRoom

    val shipAvailableItems = mutableMapOf<String, String>()

    var parents = mutableMapOf<String, Pair<String, String>>()

    var nextCheckpoint : String? = null
    var nextCheckpointDirection = ""
    var inventory = emptyList<String>()

    var state : ExplorationState = ExplorationState.EXPLORING

    val masksCheckpoints = mutableMapOf<String, Int>()


    init {
        unknownRoom.south = null
        unknownRoom.east = null
        unknownRoom.west = null
        unknownRoom.north = null
    }

    val source = readFileToInccode(Day21().javaClass.getResource("day25.txt").path)

    fun explore() {
        while(true) {
            reinitRun()
            val explore = ReindeerShip(source, this)
            runBlocking {
                explore.runProgram()
            }
        }
    }

    fun reinitRun() {
        prevRoom = unknownRoom
        if (lastCommand.startsWith("take ")) {
            val item = lastCommand.substring(5, lastCommand.lastIndex)
            ship.getValue(room!!.id).itemsBlacklist.add(item)
            shipAvailableItems.remove(item)
        }
        lastCommand = ""
        for (room in ship.values) {
            room.roomAvailableItems = room.roomItems.filterNot { room.itemsBlacklist.contains(it)}.toMutableList()
        }
        state = ExplorationState.EXPLORING
    }

    fun getNextCommand(readBuffer : String) : String {
        lastCommand = computeNextCommand(readBuffer)
        print(lastCommand)
        return lastCommand
    }

    private fun computeNextCommand(readBuffer: String): String {
        if (readBuffer.contains("you are ejected back to the checkpoint")) {
            nextCheckpoint = room.id
            nextCheckpointDirection = lastCommand
        }
        else if (lastCommand == "inv\n") {
            lastCommand = ""
            inventory = inventory(readBuffer)

            return getNextCommand(readBuffer)
        } else if (lastCommand.startsWith("take ") || lastCommand.startsWith("drop ")) {
            return "inv\n"
        }
        val id = getIdFromDesc(readBuffer)
        if (id != null) {
            prevRoom = room
            room = ship.getOrPut(id) { Room(readBuffer) }
            if (id == "Warp Drive Maintenance") {
                room.itemsBlacklist.add("infinite loop")
            } else if (id == "Hot Chocolate Fountain") {
                room.itemsBlacklist.add("giant electromagnet")
            }
            updateMap(lastCommand)
        }

        if (state == ExplorationState.EXPLORING || state == ExplorationState.GATHERING) {
            if (room.roomAvailableItems.isNotEmpty() && !room.itemsBlacklist.contains(room.roomAvailableItems[0])) {
                val item = room.roomAvailableItems.removeAt(0)
                shipAvailableItems.put(item, room.id)
                return "take $item\n"
            }
        }

        if (state == ExplorationState.EXPLORING) {
            val nextDirection = findUnknownChild()
            if (nextDirection != null) {
                return nextDirection
            } else if (parents.containsKey(room.id)) {
                return getOppositeDirection(parents[room.id]!!.second)
            } else {
                state = ExplorationState.GATHERING
            }
        }
        if (state == ExplorationState.GATHERING) {
            val nextDirection = findInventoryDirection()
            if (nextDirection != null) {
                return nextDirection
            } else if (parents.containsKey(room.id)) {
                return getOppositeDirection(parents[room.id]!!.second)
            } else {
                state = ExplorationState.MOVING_TO_CHECKPOINT
            }
        }
        if (state == ExplorationState.MOVING_TO_CHECKPOINT) {
            if (room.id == nextCheckpoint) {
                state = ExplorationState.DROPPING
                masksCheckpoints.putIfAbsent(room.id, pow(2.0, inventory.size.toDouble()).toInt() - 1)
            } else {
                return getNextDirection(nextCheckpoint!!)!!
            }
        }
        if (state == ExplorationState.DROPPING) {
            if (inventory.size > 0) {
                return "drop " + inventory[0] + "\n"
            } else {
                state = ExplorationState.TAKING
            }
        }
        if (state == ExplorationState.TAKING) {
            val totalObjects = shipAvailableItems.keys.sorted()
            val mask = Integer.toBinaryString(masksCheckpoints[room.id]!!).padStart(totalObjects.size, '0')
            for (c in mask.indices) {
                if (mask[c] == '1' && !inventory.contains(totalObjects[c])) {
                    return "take " + totalObjects[c] + "\n"
                }
            }
            masksCheckpoints[room.id] = masksCheckpoints[room.id]!! - 1
            state = ExplorationState.DROPPING
            return nextCheckpointDirection
        }
        return ""
    }

    private fun getOppositeDirection(direction: String): String {
        return when(direction) {
            "north\n" -> "south\n"
            "south\n" -> "north\n"
            "east\n" -> "west\n"
            "west\n" -> "east\n"
            else -> "inv\n"
        }
    }

    private fun inventory(readBuffer: String) : List<String> {
        if (readBuffer.startsWith("You aren't carrying any items.")) {
            return emptyList()
        } else {

        }
        return getItemsFromString(readBuffer)
    }

    private fun findInventoryDirection(): String? {
        val inventoryToFetch = shipAvailableItems.keys.filterNot { inventory.contains(it)}
        if (inventoryToFetch.isEmpty()) {
            return null
        } else {
            return getNextDirection(shipAvailableItems.getValue(inventoryToFetch.get(0)))
        }
    }

    private fun findUnknownChild(): String? {
        if (room.east == unknownRoom) {
            return "east\n"
        } else if (room.west == unknownRoom) {
            return "west\n"
        } else if (room.south == unknownRoom) {
            return "south\n"
        } else if (room.north == unknownRoom) {
            return "north\n"
        }
        for (toExplore in ship.values) {
            if (toExplore.hasUnknowns()) {
                val direction = getNextDirection(toExplore.id)
                if (direction != null) {
                    return direction
                }
            }
        }
        return null
    }

    private fun getNextDirection(toExplore: String): String? {
        if (toExplore == room.id) {
            return null
        }
         return if (!parents.containsKey(toExplore)) {
            null
        } else if (parents.getValue(toExplore).first == room.id) {
            parents.getValue(toExplore).second
        } else {
            getNextDirection(parents.getValue(toExplore).first)
        }
    }

    private fun updateMap(lastCommand: String) {
        if (prevRoom != unknownRoom) {
            when (lastCommand) {
                "north\n" -> {
                    prevRoom.north = room
                    room.south = prevRoom
                }
                "south\n" -> {
                    prevRoom.south = room
                    room.north == prevRoom
                }
                "east\n" -> {
                    prevRoom.east = room
                    room.west = prevRoom
                }
                "west\n" -> {
                    prevRoom.west = room
                    room.east = prevRoom
                }
            }
            if (lastCommand == "north\n" || lastCommand == "south\n" || lastCommand == "east\n" || lastCommand == "west\n") {
                if (!parents.containsKey(room.id) && room.id != "Hull Breach") {
                    parents[room.id] = Pair(prevRoom.id, lastCommand)
                }
            }
        }
    }
}

fun main() {
    ShipExplorer().explore()
}
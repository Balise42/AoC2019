package fr.pasithee.aoc2019

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class ReindeerShip(program: List<Long>, val shipExplorer: ShipExplorer) : Intcode(program, program[1], program[2], Channel(), Channel())  {
    var nextCommand = ""
    var readBuffer = ""
    var inputSpot = 0
    var reading = true

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
    return if (titleRegex.containsMatchIn(desc)) {
        titleRegex.find(desc)!!.groupValues[1]
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


class ShipExplorer() {
    val ship = mutableMapOf<String, Room>()
    var lastCommand = ""
    var id = ""
    var room : Room? = null
    var prevRoom : Room? = null

    var inventoryToFetch = listOf<String>()
    val shipAvailableItems = mutableMapOf<String, String>()

    var parents = mutableMapOf<String, Pair<String, String>>()

    var nextCheckpoint : String? = null
    var inventory = emptyList<String>()


    init {
        unknownRoom.south = null
        unknownRoom.east = null
        unknownRoom.west = null
        unknownRoom.north = null
    }

    val source = readFileToInccode(Day21().javaClass.getResource("day25.txt").path)

    fun explore() {
        while(true) {
            prevRoom = null
            room = ship["Hull Breach"]
            for (room in ship.values) {
                if (room.roomAvailableItems.count() + room.itemsBlacklist.count () < room.roomItems.count()) {
                    room.roomAvailableItems = room.roomItems.toMutableList()
                }
            }
            val explore = ReindeerShip(source, this)
            runBlocking {
                try {
                    explore.runProgram()
                } catch(e : IndexOutOfBoundsException) {
                }
            }
            if (lastCommand.startsWith("take ")) {
                val item = lastCommand.substring(5, lastCommand.lastIndex)
                ship.getValue(id).itemsBlacklist.add(item)
                ship.getValue(id).roomAvailableItems.remove(item)
                shipAvailableItems.remove(item)
                continue
            }
            if (lastCommand == "") {
                continue
            }
            break;
        }
    }



    fun getNextCommand(readBuffer: String): String {
        if (readBuffer.contains("you are ejected back to the checkpoint")) {
            nextCheckpoint = parents[room!!.id]!!.first
        }
        if (!inventory.isEmpty() && inventoryToFetch.isEmpty()) {
            if (room!!.id == nextCheckpoint) {
                return getNextDirection(nextCheckpoint!!)!!
            } else {
                return ""
            }
        }
        if (lastCommand == "inv\n") {
            lastCommand = ""
            inventory = inventory(readBuffer)
            inventoryToFetch = shipAvailableItems.keys.filterNot { inventory.contains(it) }

            return getNextCommand(readBuffer)
        } else if (lastCommand.startsWith("take ")) {
            lastCommand = "inv\n"
            return "inv\n"
        }
        val tmpId = getIdFromDesc(readBuffer)
        if (tmpId != null) {
            id = tmpId
            prevRoom = room
            room = ship.getOrPut(id) { Room(readBuffer) }
            if (id == "Warp Drive Maintenance") {
                room!!.itemsBlacklist.add("infinite loop")
            } else if (id == "Hot Chocolate Fountain") {
                room!!.itemsBlacklist.add("giant electromagnet")
            }
            updateMap(room!!, prevRoom, lastCommand)

        }

        val unknownChildDirection = findUnknownChild()
        val inventoryDirection = findInventoryDirection()

        lastCommand =
        if (room!!.roomAvailableItems.isNotEmpty() && !room!!.itemsBlacklist.contains(room!!.roomAvailableItems[0])) {
            val item = room!!.roomAvailableItems.removeAt(0)
            shipAvailableItems.put(item, room!!.id)
            "take $item\n"
        } else if (room!!.east == unknownRoom) {
             "east\n"
        } else if (room!!.west == unknownRoom) {
             "west\n"
        } else if (room!!.south == unknownRoom) {
             "south\n"
        } else if (room!!.north == unknownRoom) {
            "north\n"
        } else if (findUnknownChild() != null) {
            unknownChildDirection!!
        } else if (inventoryDirection != null) {
            inventoryDirection!!
        } else if (parents[id] == null) {
            "inv\n"
        } else {
            when (parents[id]!!.second) {
                        "north\n" -> "south\n"
                        "south\n" -> "north\n"
                        "east\n" -> "west\n"
                        "west\n" -> "east\n"
                        else -> ""
                    }
                }
        print(lastCommand)
        return lastCommand
    }

    private fun inventory(readBuffer: String) : List<String> {
        if (readBuffer.startsWith("You aren't carrying any items.")) {
            return emptyList()
        } else {

        }
        return getItemsFromString(readBuffer)
    }

    private fun findInventoryDirection(): String? {
        if (inventoryToFetch.isEmpty()) {
            return null
        } else {
            return getNextDirection(shipAvailableItems.getValue(inventoryToFetch.get(0)))
        }
    }

    private fun findUnknownChild(): String? {
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
        if (toExplore == room!!.id) {
            return null
        }
         return if (!parents.containsKey(toExplore)) {
            null
        } else if (parents.getValue(toExplore).first == room!!.id) {
            parents.getValue(toExplore).second
        } else {
            getNextDirection(parents.getValue(toExplore).first)
        }
    }

    private fun updateMap(room: Room, prevRoom: Room?, lastCommand: String) {
        if (prevRoom != null) {
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
                if (!parents.containsKey(id) && id != "Hull Breach") {
                    parents[room.id] = Pair(prevRoom.id, lastCommand)
                }
            }
        }
    }
}

fun main() {
    ShipExplorer().explore()
}
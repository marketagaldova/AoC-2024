import kotlin.io.path.Path
import kotlin.io.path.readText


val input = Path("06/input.txt").readText()


data class Vector(val x: Int = 0, val y: Int = 0) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)

    fun rotateRight() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
        else -> error("idk how")
    }

    companion object {
        val UP = Vector(0, -1)
        val RIGHT = Vector(1, 0)
        val DOWN = Vector(0, 1)
        val LEFT = Vector(-1, 0)
    }
}

sealed interface Cell {
    fun clone(): Cell

    data class Empty(
        val visitedDirections: MutableSet<Vector> = mutableSetOf(),
        var blocked: Boolean = false,
        var blockAttempted: Boolean = false,
        val isStart: Boolean = false,
    ) : Cell {
        override fun clone() = copy(visitedDirections = visitedDirections.toMutableSet())
    }

    data object Obstacle : Cell {
        override fun clone() = this
    }
}


var guardStartPosition = Vector()
var guardPosition = Vector()
var guardDirection = Vector.UP

typealias Map = List<List<Cell>>
val map: Map = input.lineSequence()
    .mapIndexed { y, line -> line.mapIndexed { x, char -> when (char) {
        '#' -> Cell.Obstacle
        '.' -> Cell.Empty()
        '^' -> {
            guardStartPosition = Vector(x, y)
            guardPosition = guardStartPosition
            Cell.Empty(isStart = true)
        }
        else -> error("unknown char: $char")
    } } }
    .toList()

operator fun Map.contains(position: Vector) = position.y in indices && position.x in this[position.y].indices
operator fun Map.get(position: Vector): Cell? = if (position !in this) null else this[position.y][position.x]
fun Map.clone(): Map = this.map { it.map { it.clone() } }


fun Map.print() {
    for (line in this) {
        for (cell in line) {
            when (cell) {
                is Cell.Obstacle -> print('#')
                is Cell.Empty -> when {
                    cell.isStart -> print('^')
                    cell.blockAttempted -> print('O')
                    else -> {
                        val vertical = Vector.UP in cell.visitedDirections || Vector.DOWN in cell.visitedDirections
                        val horizontal = Vector.LEFT in cell.visitedDirections || Vector.RIGHT in cell.visitedDirections
                        when {
                            vertical && horizontal -> print('+')
                            vertical -> print('|')
                            horizontal -> print('-')
                            else -> print('.')
                        }
                    }
                }
            }
        }
        println()
    }
}


fun checkForLoop(map: Map, startPosition: Vector, startDirection: Vector): Boolean {
    var guardPosition = startPosition
    var guardDirection = startDirection
    while (true) {
        (map[guardPosition] as Cell.Empty).visitedDirections.add(guardDirection)
        val nextPosition = guardPosition + guardDirection
        when (val cell = map[nextPosition]) {
            is Cell.Empty -> when {
                cell.blocked -> guardDirection = guardDirection.rotateRight()
                guardDirection in cell.visitedDirections -> return true
                else -> guardPosition = nextPosition
            }
            is Cell.Obstacle -> guardDirection = guardDirection.rotateRight()
            null -> return false
        }
    }
}


var loopCount = 0
while (true) {
    (map[guardPosition] as Cell.Empty).visitedDirections.add(guardDirection)
    val nextPosition = guardPosition + guardDirection
    when (val cell = map[nextPosition]) {
        is Cell.Empty -> {
            if (cell.visitedDirections.isEmpty() && nextPosition != guardStartPosition) {
                cell.blockAttempted = true
                cell.blocked = true
                if (checkForLoop(map.clone(), guardPosition, guardDirection)) loopCount++
                cell.blocked = false
            }
            guardPosition = nextPosition
        }
        is Cell.Obstacle -> guardDirection = guardDirection.rotateRight()
        null -> break
    }
}

map.print()
println(loopCount)

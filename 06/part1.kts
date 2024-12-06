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
    data class Empty(var visited: Boolean = false) : Cell
    data object Obstacle : Cell
}


var guardPosition = Vector()
var guardDirection = Vector.UP

typealias Map = List<List<Cell>>
val map: Map = input.lineSequence()
    .mapIndexed { y, line -> line.mapIndexed { x, char -> when (char) {
        '#' -> Cell.Obstacle
        '.' -> Cell.Empty()
        '^' -> {
            guardPosition = Vector(x, y)
            Cell.Empty()
        }
        else -> error("unknown char: $char")
    } } }
    .toList()

operator fun Map.contains(position: Vector) = position.y in indices && position.x in this[position.y].indices
operator fun Map.get(position: Vector): Cell? = if (position !in this) null else this[position.y][position.x]


var visitedCount = 0
while (true) {
    val cell = map[guardPosition] as Cell.Empty
    if (!cell.visited) visitedCount++
    cell.visited = true
    val nextPosition = guardPosition + guardDirection
    when (map[nextPosition]) {
        is Cell.Empty -> guardPosition = nextPosition
        is Cell.Obstacle -> guardDirection = guardDirection.rotateRight()
        null -> break
    }
}

println(visitedCount)

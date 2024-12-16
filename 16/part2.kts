import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.Path
import kotlin.io.path.readLines


data class Vector(val x: Int = 0, val y: Int = 0) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)

    fun rotateRight() = Vector(x = y * -1, y = x)
    fun rotateLeft() = Vector(x = y, y = x * -1)

    companion object {
        val UP = Vector(y = -1)
        val RIGHT = Vector(x = 1)
        val DOWN = Vector(y = 1)
        val LEFT = Vector(x = -1)
    }
}

sealed interface Cell {
    data object Wall : Cell
    data class Empty(val cheapestApproaches: MutableMap<Vector, Int> = mutableMapOf()) : Cell
    data object End : Cell
}


var start = Vector()
var end = Vector()

typealias Map = List<List<Cell>>
operator fun Map.contains(position: Vector) = position.y in indices && position.x in this[position.y].indices
operator fun Map.get(position: Vector) = this[position.y][position.x]

val map: Map = Path("16/input.txt").readLines()
    .mapIndexed { y, line -> line.mapIndexed { x, char -> when (char) {
        '#' -> Cell.Wall
        'S' -> {
            start = Vector(x, y)
            Cell.Empty()
        }
        'E' -> {
            end = Vector(x, y)
            Cell.End
        }
        else -> Cell.Empty()
    } } }


data class Step(val position: Vector, val previous: Step?)
data class Head(val position: Vector, val direction: Vector, val cost: Int, val path: Step) {
    fun forward() = Head(position + direction, direction, cost + 1, Step(position + direction, path))
    fun right() = Head(position + direction.rotateRight(), direction.rotateRight(), cost + 1001, Step(position + direction.rotateRight(), path))
    fun left() = Head(position + direction.rotateLeft(), direction.rotateLeft(), cost + 1001, Step(position + direction.rotateLeft(), path))
}

val heads = ArrayDeque<Head>().apply { add(Head(start, Vector.RIGHT, 0, Step(start, null))) }
val ends = mutableListOf<Head>()

while (heads.isNotEmpty()) {
    val head = heads.removeFirst()
    if (head.position !in map) continue

    val cell = when (val cell = map[head.position]) {
        is Cell.Wall -> continue
        is Cell.End -> {
            ends.add(head)
            continue
        }
        is Cell.Empty -> cell
    }

    val forwardCost = cell.cheapestApproaches[head.direction] ?: Int.MAX_VALUE
    if (forwardCost >= head.cost) {
        cell.cheapestApproaches[head.direction] = head.cost
        heads.add(head.forward())
    }

    val rightCost = cell.cheapestApproaches[head.direction.rotateRight()] ?: Int.MAX_VALUE
    if (rightCost >= head.cost + 1000) {
        cell.cheapestApproaches[head.direction.rotateRight()] = head.cost + 1000
        heads.add(head.right())
    }

    val leftCost = cell.cheapestApproaches[head.direction.rotateLeft()] ?: Int.MAX_VALUE
    if (leftCost >= head.cost + 1000) {
        cell.cheapestApproaches[head.direction.rotateLeft()] = head.cost + 1000
        heads.add(head.left())
    }
}

val spots = mutableSetOf<Vector>()
ends.asSequence()
    .filter { it.cost == ends.minOf { it.cost } }
    .forEach { head ->
        var step: Step? = head.path
        while (step != null) {
            spots.add(step.position)
            step = step.previous
        }
    }

println(spots.size)

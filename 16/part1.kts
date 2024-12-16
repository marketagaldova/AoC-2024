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
    data class Empty(
        var cheapestApproachCost: Int = Int.MAX_VALUE,
        var cheapestApproachDirection: Vector? = null,
    ) : Cell
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
            Cell.Empty()
        }
        else -> Cell.Empty()
    } } }


data class Head(val position: Vector, val direction: Vector, val cost: Int) {
    fun forward() = Head(position + direction, direction, cost + 1)
    fun right() = Head(position + direction.rotateRight(), direction.rotateRight(), cost + 1001)
    fun left() = Head(position + direction.rotateLeft(), direction.rotateLeft(), cost + 1001)
}
val heads = ArrayDeque<Head>().apply { add(Head(start, Vector.RIGHT, 0)) }

while (heads.isNotEmpty()) {
    val head = heads.removeFirst()
    if (head.position !in map) continue

    val cell = (map[head.position] as? Cell.Empty) ?: continue

    if (cell.cheapestApproachDirection != null && cell.cheapestApproachCost <= head.cost) continue
    cell.cheapestApproachDirection = head.direction
    cell.cheapestApproachCost = head.cost

    if (head.position == end) continue

    heads.add(head.forward())
    heads.add(head.right())
    heads.add(head.left())
}

println((map[end] as? Cell.Empty)?.cheapestApproachCost ?: "No path found")

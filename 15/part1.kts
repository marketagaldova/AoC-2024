import kotlin.io.path.Path
import kotlin.io.path.readText


val (mapInput, instructions) = Path("15/input.txt").readText().split("\n\n")

data class Vector(val x: Int = 0, val y: Int = 0) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vector) = Vector(this.x - other.x, this.y - other.y)

    companion object {
        val UP = Vector(y = -1)
        val RIGHT = Vector(x = 1)
        val DOWN = Vector(y = 1)
        val LEFT = Vector(x = -1)
    }
}

sealed interface Cell {
    data object Wall : Cell
    data object Box : Cell
}

typealias Map = List<MutableList<Cell?>>
operator fun Map.contains(position: Vector) = position.y in indices && position.x in this[position.y].indices
operator fun Map.get(position: Vector) = this[position.y][position.x]
operator fun Map.set(position: Vector, cell: Cell?) { this[position.y][position.x] = cell }

var robotPosition = Vector()
val map: Map = mapInput.lineSequence()
    .mapIndexed { y, line -> line.mapIndexedTo(mutableListOf()) { x, char ->
        when (char) {
            '#' -> Cell.Wall
            'O' -> Cell.Box
            '@' -> {
                robotPosition = Vector(x, y)
                null
            }
            else -> null
        }
    } }
    .toList()

fun execute(instruction: Char) {
    val direction = when (instruction) {
        '^' -> Vector.UP
        '>' -> Vector.RIGHT
        'v' -> Vector.DOWN
        '<' -> Vector.LEFT
        else -> return
    }

    var target = robotPosition + direction
    while (true) when (map[target]) {
        is Cell.Box -> target += direction
        is Cell.Wall -> return
        null -> break
    }

    map[target] = Cell.Box
    robotPosition += direction
    map[robotPosition] = null
}

for (instruction in instructions) execute(instruction)

val result = map.asSequence()
    .flatMapIndexed { y, line ->
        line.asSequence()
            .mapIndexed { x, cell -> if (cell is Cell.Box) y * 100 + x else 0 }
    }
    .sum()

println(result)

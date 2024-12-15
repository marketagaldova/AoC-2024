import kotlin.io.path.Path
import kotlin.io.path.readText


val (mapInput, instructions) = Path("15/input.txt").readText().split("\n\n")

data class Vector(val x: Int = 0, val y: Int = 0) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vector) = Vector(this.x - other.x, this.y - other.y)
    operator fun times(scale: Int) = Vector(x * scale, y * scale)

    companion object {
        val UP = Vector(y = -1)
        val RIGHT = Vector(x = 1)
        val DOWN = Vector(y = 1)
        val LEFT = Vector(x = -1)
    }
}

sealed interface Cell {
    data object Wall : Cell
    data object BoxLeft : Cell
    data object BoxRight : Cell
}

typealias Map = List<MutableList<Cell?>>
operator fun Map.contains(position: Vector) = position.y in indices && position.x in this[position.y].indices
operator fun Map.get(position: Vector) = this[position.y][position.x]
operator fun Map.set(position: Vector, cell: Cell?) { this[position.y][position.x] = cell }

var robotPosition = Vector()
val map: Map = mapInput.lineSequence()
    .mapIndexed { y, line ->
        val result = mutableListOf<Cell?>()
        for ((x, char) in line.withIndex()) when (char) {
            '#' -> { result.add(Cell.Wall); result.add(Cell.Wall) }
            'O' -> { result.add(Cell.BoxLeft); result.add(Cell.BoxRight) }
            '@' -> {
                robotPosition = Vector(x * 2, y)
                result.add(null); result.add(null)
            }
            else -> { result.add(null); result.add(null) }
        }
        result
    }
    .toList()

fun discoverBoxes(position: Vector, direction: Vector, output: MutableSet<Vector>): Boolean {
    if (position in output) return true
    when (map[position]) {
        null -> return true
        is Cell.BoxLeft -> {
            if (direction.y == 0) {
                if (!discoverBoxes(position + direction * 2, direction, output)) return false
                output.add(position + direction)
                output.add(position)
                return true
            }

            val leftPosition = position
            val rightPosition = position + Vector.RIGHT

            if (
                !discoverBoxes(leftPosition + direction, direction, output)
                || !discoverBoxes(rightPosition + direction, direction, output)
            ) return false

            output.add(leftPosition)
            output.add(rightPosition)
            return true
        }
        is Cell.BoxRight -> {
            if (direction.y == 0) {
                if (!discoverBoxes(position + direction * 2, direction, output)) return false
                output.add(position + direction)
                output.add(position)
                return true
            }

            val leftPosition = position + Vector.LEFT
            val rightPosition = position

            if (
                !discoverBoxes(leftPosition + direction, direction, output)
                || !discoverBoxes(rightPosition + direction, direction, output)
            ) return false

            output.add(leftPosition)
            output.add(rightPosition)
            return true
        }
        is Cell.Wall -> return false
    }
}

fun execute(instruction: Char) {
    val direction = when (instruction) {
        '^' -> Vector.UP
        '>' -> Vector.RIGHT
        'v' -> Vector.DOWN
        '<' -> Vector.LEFT
        else -> return
    }

    val boxPositions = mutableSetOf<Vector>()
    if (!discoverBoxes(robotPosition + direction, direction, boxPositions)) return

    for (position in boxPositions) {
        map[position + direction] = map[position]
        map[position] = null
    }

    robotPosition += direction
}

for (instruction in instructions) execute(instruction)

val result = map.asSequence()
    .flatMapIndexed { y, line ->
        line.asSequence()
            .mapIndexed { x, cell -> if (cell is Cell.BoxLeft) y * 100 + x else 0 }
    }
    .sum()

println(result)

import kotlin.io.path.Path
import kotlin.io.path.readLines


data class Vector(val x: Int, val y: Int) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vector) = Vector(this.x - other.x, this.y - other.y)

    fun rotateRight() = Vector(x = y * -1, y = x)

    companion object {
        val UP = Vector(0, -1)
        val RIGHT = Vector(1, 0)
        val DOWN = Vector(0, 1)
        val LEFT = Vector(-1, 0)

        val DIRECTIONS = listOf(UP, RIGHT, DOWN, LEFT)
    }
}

data class Cell(
    val position: Vector,
    val plant: Char,
    var consumed: Boolean = false,
    val consumedEdges: MutableSet<Vector> = mutableSetOf(),
)

typealias Map = List<List<Cell>>
val map: Map = Path("12/input.txt").readLines()
    .mapIndexed { y, line -> line.mapIndexed { x, plant -> Cell(Vector(x, y), plant) } }

operator fun Map.contains(position: Vector) = position.y in this.indices && position.x in this[position.y].indices
operator fun Map.get(position: Vector) = this[position.y][position.x]


val plots = mutableListOf<List<Vector>>()
fun MutableList<Vector>.discover(position: Vector, plant: Char) {
    if (position !in map) return
    val cell = map[position]
    if (cell.consumed || cell.plant != plant) return
    add(position)
    cell.consumed = true
    discover(position + Vector.UP, plant)
    discover(position + Vector.RIGHT, plant)
    discover(position + Vector.DOWN, plant)
    discover(position + Vector.LEFT, plant)
}

for (y in map.indices)
    for (x in map[y].indices) {
        val position = Vector(x, y)
        val cell = map[position]
        if (cell.consumed) continue
        plots.add(mutableListOf<Vector>().apply { discover(position, cell.plant) })
    }

fun Cell.isEdge(position: Vector) = position !in map || map[position].plant != this.plant

fun consumeSide(position: Vector, normal: Vector, plant: Char) {
    val direction = normal.rotateRight()
    var start = position
    while ((start - direction).let { position -> position in map && map[position].let { cell -> cell.plant == plant && cell.isEdge(position + normal) } })
        start -= direction

    var position = start
    while (position in map && map[position].let { cell -> cell.plant == plant && cell.isEdge(position + normal) }) {
        map[position].consumedEdges.add(normal)
        position += direction
    }
}

fun countSides(position: Vector): Int {
    val cell = map[position]
    var sides = 0
    for (direction in Vector.DIRECTIONS) {
        if (direction in cell.consumedEdges || !cell.isEdge(position + direction)) continue
        sides++
        consumeSide(position, direction, cell.plant)
    }
    return sides
}

val price = plots.sumOf { plot ->
    val sides = plot.sumOf { countSides(it) }
    plot.size * sides
}

println(price)

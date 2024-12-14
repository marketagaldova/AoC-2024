import kotlin.io.path.Path
import kotlin.io.path.readLines


data class Vector(val x: Int, val y: Int) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)

    companion object {
        val UP = Vector(0, -1)
        val RIGHT = Vector(1, 0)
        val DOWN = Vector(0, 1)
        val LEFT = Vector(-1, 0)
    }
}

data class Cell(val plant: Char, var consumed: Boolean = false)

typealias Map = List<List<Cell>>
val map: Map = Path("12/input.txt").readLines()
    .map { line -> line.map { Cell(it) } }

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
fun countEdges(position: Vector): Int {
    val cell = map[position]
    var edges = 0
    if (cell.isEdge(position + Vector.UP)) edges++
    if (cell.isEdge(position + Vector.RIGHT)) edges++
    if (cell.isEdge(position + Vector.DOWN)) edges++
    if (cell.isEdge(position + Vector.LEFT)) edges++
    return edges
}

val price = plots.sumOf { plot ->
    val perimeter = plot.sumOf { countEdges(it) }
    plot.size * perimeter
}

println(price)

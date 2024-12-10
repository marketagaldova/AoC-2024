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

typealias Map = List<String>
operator fun Map.contains(position: Vector) = position.y in indices && position.x in first().indices
operator fun Map.get(position: Vector) = this[position.y][position.x].digitToInt()

val map: Map = Path("10/input.txt").readLines()


fun Map.findDestinations(head: Vector, value: Int = 0): Set<Vector> {
    if (head !in this || this[head] != value) return emptySet()
    if (value == 9) return setOf(head)
    return findDestinations(head + Vector.UP, value + 1) +
        findDestinations(head + Vector.RIGHT, value + 1) +
        findDestinations(head + Vector.DOWN, value + 1) +
        findDestinations(head + Vector.LEFT, value + 1)
}


var score = 0
for (y in map.indices)
    for (x in map[y].indices)
        score += map.findDestinations(Vector(x, y)).size

println(score)

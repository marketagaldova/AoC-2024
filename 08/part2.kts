import kotlin.io.path.Path
import kotlin.io.path.readLines


val input = Path("08/input.txt").readLines()


data class Vector(val x: Int = 0, val y: Int = 0) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
    operator fun minus(other: Vector) = Vector(this.x - other.x, this.y - other.y)
}


val antennas = mutableMapOf<Char, MutableList<Vector>>()
for ((y, row) in input.withIndex())
    for ((x, char) in row.withIndex()) {
        if (char == '.') continue
        antennas.getOrPut(char, ::mutableListOf).add(Vector(x, y))
    }


typealias Map = List<BooleanArray>
operator fun Map.contains(position: Vector) = position.y in this.indices && position.x in first().indices
operator fun Map.get(position: Vector) = this[position.y][position.x]
operator fun Map.set(position: Vector, value: Boolean) { this[position.y][position.x] = value }

val map: Map = input.indices.map { BooleanArray(input.first().length) }


var result = 0
for ((_, positions) in antennas)
    for ((i, first) in positions.withIndex())
        for ((j, second) in positions.withIndex()) {
            if (i == j) continue
            val distance = first - second
            var antinode = first
            while (antinode in map) {
                if (!map[antinode]) result++
                map[antinode] = true
                antinode += distance
            }
        }


println(result)

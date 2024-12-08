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


val map = input.indices.map { BooleanArray(input.first().length) }

var result = 0
for ((_, positions) in antennas)
    for ((i, first) in positions.withIndex())
        for ((j, second) in positions.withIndex()) {
            if (i == j) continue
            val antinode = first + (first - second)
            if (antinode.y !in map.indices || antinode.x !in map.first().indices) continue
            if (!map[antinode.y][antinode.x]) result++
            map[antinode.y][antinode.x] = true
        }


println(result)

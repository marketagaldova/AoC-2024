import kotlin.io.path.Path
import kotlin.io.path.readLines


val input = Path("04/input.txt").readLines()
val width = input.first().length
val height = input.size

var result = 0

data class Direction(val dx: Int, val dy: Int)
val UP = Direction(0, -1)
val DOWN = Direction(0, 1)
val LEFT = Direction(-1, 0)
val RIGHT = Direction(1, 0)
val UP_LEFT = Direction(-1, -1)
val UP_RIGHT = Direction(1, -1)
val DOWN_LEFT = Direction(-1, 1)
val DOWN_RIGHT = Direction(1, 1)

val TARGET = "XMAS"

fun check(x: Int, y: Int, direction: Direction) {
    repeat(4) { i ->
        val x = x + direction.dx * i
        val y = y + direction.dy * i
        if (x !in 0 ..< width || y !in 0 ..< height) return
        if (input[y][x] != TARGET[i]) return
    }
    result++
}

for (y in input.indices)
    for (x in input[y].indices) {
        check(x, y, UP)
        check(x, y, DOWN)
        check(x, y, LEFT)
        check(x, y, RIGHT)
        check(x, y, UP_LEFT)
        check(x, y, UP_RIGHT)
        check(x, y, DOWN_LEFT)
        check(x, y, DOWN_RIGHT)
    }

println(result)

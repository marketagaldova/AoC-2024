import kotlin.io.path.Path
import kotlin.io.path.readLines


data class Vector(val x: Int, val y: Int) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
    operator fun times(scale: Int) = Vector(x * scale, y * scale)
    operator fun rem(modulo: Vector) = Vector(
        x = (this.x % modulo.x).let { if (it < 0) it + modulo.x else it },
        y = (this.y % modulo.y).let { if (it < 0) it + modulo.y else it },
    )
}

data class Robot(var position: Vector, val velocity: Vector)

val robotRegex = Regex("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)")

val MAP_SIZE = Vector(x = 101, y = 103)
val robots = Path("14/input.txt").readLines()
    .map { line ->
        val (_, px, py, vx, vy) = robotRegex.find(line)!!.groupValues
        Robot(position = Vector(px.toInt(), py.toInt()), velocity = Vector(vx.toInt(), vy.toInt()))
    }

for (robot in robots) robot.position = (robot.position + robot.velocity * 100) % MAP_SIZE

val middle = Vector(MAP_SIZE.x / 2, MAP_SIZE.y / 2)
var topLeft = 0
var topRight = 0
var bottomLeft = 0
var bottomRight = 0
for (robot in robots) when {
    robot.position.x < middle.x -> when {
        robot.position.y < middle.y -> topLeft++
        robot.position.y > middle.y -> bottomLeft++
    }
    robot.position.x > middle.x -> when {
        robot.position.y < middle.y -> topRight++
        robot.position.y > middle.y -> bottomRight++
    }
}

println(topLeft * topRight * bottomLeft * bottomRight)

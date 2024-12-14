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

var elapsed = 0
var buffer = CharArray(MAP_SIZE.x * MAP_SIZE.y)
while (true) {
    for (i in buffer.indices) buffer[i] = ' '
    var render = true
    for (robot in robots) {
        if (buffer[robot.position.y * MAP_SIZE.x + robot.position.x] != ' ') {
            render = false
            break
        }
        buffer[robot.position.y * MAP_SIZE.x + robot.position.x] = '*'
    }

    if (render) {
        println("$elapsed seconds elapsed")
        for (i in buffer.indices step MAP_SIZE.x) println(String(buffer, i, MAP_SIZE.x))
        readln()
    }

    for (robot in robots) robot.position = (robot.position + robot.velocity) % MAP_SIZE
    elapsed++
}

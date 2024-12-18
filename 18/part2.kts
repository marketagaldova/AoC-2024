import kotlin.io.path.Path
import kotlin.io.path.readLines


data class Vector(val x: Int = 0, val y: Int = 0) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
    override fun toString() = "$x,$y"

    companion object {
        val UP = Vector(y = -1)
        val RIGHT = Vector(x = 1)
        val DOWN = Vector(y = 1)
        val LEFT = Vector(x = -1)
        val DIRECTIONS = listOf(UP, RIGHT, DOWN, LEFT)
    }
}
data class Box(val topLeft: Vector = Vector(), val bottomRight: Vector = Vector()) {
    operator fun contains(position: Vector) = position.x in topLeft.x..bottomRight.x && position.y in topLeft.y..bottomRight.y
}


val WIDTH = 71
val HEIGHT = 71
val bytes = Path("18/input.txt").readLines()
    .asSequence()
    .map { it.split(',') }
    .map { (x, y) -> Vector(x.toInt(), y.toInt()) }
    .toList()

val BOUNDS = Box(bottomRight = Vector(WIDTH - 1, HEIGHT - 1))
val START = BOUNDS.topLeft
val GOAL = BOUNDS.bottomRight


data class Node(val position: Vector, val cost: Int) : Comparable<Node> {
    val heuristic get() = (WIDTH - position.x) + (HEIGHT - position.y)
    override fun compareTo(other: Node) = (this.cost + this.heuristic) - (other.cost + other.heuristic)
}

fun findPath(obstacles: Set<Vector>): Set<Vector>? {
    val nodes = ArrayDeque<Vector>()
    val costs = mutableMapOf<Vector, Int>()
    val cameFrom = mutableMapOf<Vector, Vector>()
    nodes.add(START)
    costs[START] = 0

    while (true) {
        val node = nodes.minByOrNull { costs[it] ?: 0 } ?: break
        nodes.remove(node)
        val cost = costs[node] ?: 0

        if (node == GOAL) {
            val path = mutableSetOf<Vector>()
            var step: Vector? = node
            while (step != null) {
                path.add(step)
                step = cameFrom[step]
            }
            return path
        }

        for (direction in Vector.DIRECTIONS) {
            val neighbor = node + direction
            if (neighbor !in BOUNDS || neighbor in obstacles) continue
            val neighborCost = cost + 1
            if (neighborCost < (costs[neighbor] ?: Int.MAX_VALUE)) {
                cameFrom[neighbor] = node
                costs[neighbor] = neighborCost
                nodes.add(neighbor)
            }
        }
    }

    return null
}


val obstacles = mutableSetOf<Vector>()
var path = findPath(obstacles)!!
var nextByte = 0

while (true) {
    val byte = bytes[nextByte++]
    obstacles.add(byte)
    if (byte in path) {
        val newPath = findPath(obstacles)
        if (newPath == null) {
            println(byte)
            break
        }
        path = newPath
    }
}

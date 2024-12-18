import java.util.PriorityQueue
import kotlin.io.path.Path
import kotlin.io.path.readLines


data class Vector(val x: Int = 0, val y: Int = 0) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)

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
val LIMIT = 1024
val bytes = Path("18/input.txt").readLines()
    .asSequence()
    .map { it.split(',') }
    .map { (x, y) -> Vector(x.toInt(), y.toInt()) }
    .take(LIMIT)
    .toSet()

val BOUNDS = Box(bottomRight = Vector(WIDTH - 1, HEIGHT - 1))

data class Node(val position: Vector, val cost: Int) : Comparable<Node> {
    val heuristic get() = (WIDTH - position.x) + (HEIGHT - position.y)
    override fun compareTo(other: Node) = (this.cost + this.heuristic) - (other.cost + other.heuristic)
}

val nodes = ArrayDeque<Vector>()
val costs = mutableMapOf<Vector, Int>()
nodes.add(Vector())

val GOAL = BOUNDS.bottomRight

while (true) {
    val node = nodes.minByOrNull { costs[it] ?: 0 } ?: break
    nodes.remove(node)
    val cost = costs[node] ?: 0

    if (node == GOAL) {
        println(cost)
        break
    }

    for (direction in Vector.DIRECTIONS) {
        val neighbor = node + direction
        if (neighbor !in BOUNDS || neighbor in bytes) continue
        val neighborCost = cost + 1
        if (neighborCost < (costs[neighbor] ?: Int.MAX_VALUE)) {
            costs[neighbor] = neighborCost
            nodes.add(neighbor)
        }
    }
}

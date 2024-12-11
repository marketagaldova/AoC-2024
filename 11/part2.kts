import kotlin.io.path.Path
import kotlin.io.path.readText


val stones = Path("11/input.txt").readText()
    .split(' ')
    .map { it.toLong() }

val cacheSize = 100000
val cache = (0..75).map { LongArray(cacheSize) }
fun simulate(stone: Long, depth: Int = 0): Long {
    if (depth == 75) return 1
    val cache = cache[depth]
    if (stone < cacheSize) cache[stone.toInt()].let { if (it != 0L) return it }
    if (stone == 0L) return simulate(1, depth + 1).also { if (stone < cacheSize) cache[stone.toInt()] = it }
    val string = stone.toString()
    if (string.length % 2 == 0) {
        val left = simulate(string.substring(0, string.length / 2).toLong(), depth + 1)
        val right = simulate(string.substring(string.length / 2, string.length).toLong(), depth + 1)
        return (left + right).also { if (stone < cacheSize) cache[stone.toInt()] = it }
    }
    return simulate(stone * 2024, depth + 1).also { if (stone < cacheSize) cache[stone.toInt()] = it }
}

val stoneCount = stones.sumOf { simulate(it) }
println(stoneCount)

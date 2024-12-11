import kotlin.io.path.Path
import kotlin.io.path.readText


var stones = Path("11/input.txt").readText()
    .split(' ')
    .map { it.toLong() }

repeat(25) {
    stones = buildList<Long>(stones.size) {
        for (stone in stones) {
            if (stone == 0L) {
                add(1)
                continue
            }
            val string = stone.toString()
            if (string.length % 2 == 0) {
                add(string.substring(0, string.length / 2).toLong())
                add(string.substring(string.length / 2, string.length).toLong())
                continue
            }
            add(stone * 2024)
        }
    }
}

println(stones.size)

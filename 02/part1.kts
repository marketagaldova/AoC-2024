import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs


val input = Path("02/input.txt").readText()

val reports = input
    .lineSequence()
    .filter { it.isNotBlank() }
    .map { it.split(" ").map { it.toInt() } }

val result = reports
    .count { level ->
        var previous = 0
        for ((a, b) in level.windowed(2, partialWindows = false)) {
            if (a == b) return@count false
            val diff = a - b
            if (abs(diff) > 3) return@count false
            when {
                previous < 0 -> if (diff > 0) return@count false
                previous > 0 -> if (diff < 0) return@count false
            }
            previous = diff
        }
        true
    }

println(result)

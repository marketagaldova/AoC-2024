import kotlin.io.path.Path
import kotlin.io.path.readText


val input = Path("03/input.txt").readText()

val regex = Regex("mul\\((\\d+),(\\d+)\\)")

val result = regex.findAll(input)
    .map { match ->
        val x = match.groupValues[1].toInt()
        val y = match.groupValues[2].toInt()
        x * y
    }
    .sum()

println(result)

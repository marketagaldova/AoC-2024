import kotlin.io.path.Path
import kotlin.io.path.readText


val input = Path("03/input.txt").readText()

val regex = Regex("mul\\((\\d+),(\\d+)\\)|(do\\(\\))|(don't\\(\\))")

var enabled = true
var result = 0
regex.findAll(input)
    .forEach { match ->
        val (_, x, y, enable, disable) = match.groupValues
        when {
            enable.isNotEmpty() -> enabled = true
            disable.isNotEmpty() -> enabled = false
            enabled -> result += x.toInt() * y.toInt()
        }
    }

println(result)

import kotlin.io.path.Path
import kotlin.io.path.readLines


val input = Path("07/input.txt").readLines()

fun check(operands: List<Long>, result: Long): Boolean {
    operands.singleOrNull()?.let { return it == result }
    if (check(listOf(operands[0] + operands[1]) + operands.subList(2, operands.size), result)) return true
    if (check(listOf(operands[0] * operands[1]) + operands.subList(2, operands.size), result)) return true
    if (check(listOf("${operands[0]}${operands[1]}".toLong()) + operands.subList(2, operands.size), result)) return true
    return false
}

var total = 0L
for (line in input) {
    val (result, operands) = line.split(": ")
        .let { (result, operands) -> result.toLong() to operands.split(' ').map { it.toLong() } }
    if (check(operands, result)) total += result
}

println(total)

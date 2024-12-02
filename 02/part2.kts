import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs


val input = Path("02/input.txt").readText()

val reports = input
    .lineSequence()
    .filter { it.isNotBlank() }
    .map { it.split(" ").map { it.toInt() } }

fun List<Int>.isSafe(): Boolean {
    var previous = 0
    for ((a, b) in windowed(2, partialWindows = false)) {
        if (a == b) return false
        val diff = a - b
        if (abs(diff) > 3) return false
        when {
            previous < 0 -> if (diff > 0) return false
            previous > 0 -> if (diff < 0) return false
        }
        previous = diff
    }
    return true
}

val (goodReports, badReports) = reports.partition { it.isSafe() }

val fixedReports = badReports.filter { report ->
    sequence {
        for (i in 0 .. report.lastIndex)
            yield(report.subList(0, i) + report.subList(i + 1, report.size))
    }.any { it.isSafe() }
}

println(goodReports.size + fixedReports.size)

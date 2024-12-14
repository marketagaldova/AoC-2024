import kotlin.io.path.Path
import kotlin.io.path.readText


data class Vector(val x: Int, val y: Int) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
    operator fun times(scale: Int) = Vector(x * scale, y * scale)
}

data class Machine(
    val a: Vector,
    val b: Vector,
    val prize: Vector,
)

fun Machine.findValidCombinations() = sequence {
    var da = minOf(prize.x / a.x, prize.y / a.y, 100)
    var db = 0

    while (da > 0) {
        while (true) {
            val target = a * da + b * db
            if (target == prize) yield(da to db)
            if (target.x > prize.x || target.y > prize.y) break
            db++
            if (db > 100) return@sequence
        }
        da--
    }
}

val buttonRegex = Regex("X\\+(\\d+), Y\\+(\\d+)")
val prizeRegex = Regex("X=(\\d+), Y=(\\d+)")

fun MatchResult.parse() = Vector(
    x = groupValues[1].toInt(),
    y = groupValues[2].toInt(),
)

val price = Path("13/input.txt").readText()
    .splitToSequence("\n\n")
    .map { input ->
        val (buttonA, buttonB, prize) = input.split('\n')
        Machine(
            a = buttonRegex.find(buttonA)!!.parse(),
            b = buttonRegex.find(buttonB)!!.parse(),
            prize = prizeRegex.find(prize)!!.parse(),
        )
    }
    .sumOf { machine ->
        machine.findValidCombinations()
            .minOfOrNull { (a, b) -> a*3 + b } ?: 0
    }

println(price)

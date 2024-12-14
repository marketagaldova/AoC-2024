import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.roundToLong


data class Vector(val x: Long, val y: Long) {
    operator fun plus(other: Vector) = Vector(this.x + other.x, this.y + other.y)
    operator fun times(scale: Long) = Vector(x * scale, y * scale)
}

data class Machine(
    val a: Vector,
    val b: Vector,
    val prize: Vector,
)

fun Machine.findValidCombinations() = sequence {
    var da = minOf(prize.x / a.x, prize.y / a.y)
    var db = 0L

    while (da > 0) {
        while (true) {
            val target = a * da + b * db
            if (target == prize) yield(da to db)
            if (target.x > prize.x || target.y > prize.y) break
            db++
        }
        da--
    }
}

val buttonRegex = Regex("X\\+(\\d+), Y\\+(\\d+)")
val prizeRegex = Regex("X=(\\d+), Y=(\\d+)")

fun MatchResult.parse() = Vector(
    x = groupValues[1].toLong(),
    y = groupValues[2].toLong(),
)

val PRIZE_OFFSET = Vector(10000000000000, 10000000000000)

Path("13/input.txt").readText()
    .splitToSequence("\n\n")
    .map { input ->
        val (buttonA, buttonB, prize) = input.split('\n')
        Machine(
            a = buttonRegex.find(buttonA)!!.parse(),
            b = buttonRegex.find(buttonB)!!.parse(),
            prize = prizeRegex.find(prize)!!.parse() + PRIZE_OFFSET,
        )
    }
    .sumOf { machine ->
        println(machine)

        val AX = machine.a.x.toDouble()
        val AY = machine.a.y.toDouble()
        val BX = machine.b.x.toDouble()
        val BY = machine.b.y.toDouble()
        val PX = machine.prize.x.toDouble()
        val PY = machine.prize.y.toDouble()

        val db = (PY-(AY*PX)/AX) / (BY-(AY*BX)/AX)
        val da = (PX - BX * db) / AX

        if (
            db.roundToLong() >= 0
            && da.roundToLong() >= 0
            && machine.a * da.roundToLong() + machine.b * db.roundToLong() == machine.prize
        ) da.roundToLong()*3 + db.roundToLong()
        else 0
    }
    .let { println(it) }


/*

AX * da + BX * db = PX
AY * da + BX * db = PY
----------------------

AX * da + BX * db = PX                    // - BX * db
          AX * da = PX - BX * db          // / AX
               da = (PX - BX * db) / AX
               ------------------------

    AY * ((PX - BX * db) / AX) + BY * db = PY
    (AY * (PX - BX * db)) / AX + BY * db = PY
     (AY*PX - AY*BX * db) / AX + BY * db = PY
(AY*PX)/AX - (AY*BX * db) / AX + BY * db = PY                    // - (AY*PX)/AX
          (-(AY*BX) * db) / AX + BY * db = PY - (AY*PX)/AX
            db * (-(AY*BX)/AX) + db * BY = PY - (AY*PX)/AX       // / db
                           BY-(AY*BX)/AX = (PY-(AY*PX)/AX) / db  // / PY-(AY*PX)/AX
       (BY-(AY*BX)/AX) / (PY-(AY*PX)/AX) = 1 / db
 1 / ((BY-(AY*BX)/AX) / (PY-(AY*PX)/AX)) = db
       (PY-(AY*PX)/AX) / (BY-(AY*BX)/AX) = db
       --------------------------------------

*/

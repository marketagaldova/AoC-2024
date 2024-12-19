import kotlin.io.path.Path
import kotlin.io.path.readText


val input = Path("19/input.txt").readText()
println(Regex("(?m)^(${input.lines()[0].replace(", ","|")})+$").findAll(input).count())

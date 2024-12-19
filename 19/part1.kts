import kotlin.io.path.Path
import kotlin.io.path.readText


val (towels, patterns) = Path("19/input.txt").readText().split("\n\n")
    .let { (towels, patterns) -> towels.split(", ") to patterns.split('\n') }

val regex = Regex("(${towels.joinToString("|") { "($it)" }})+")

var possible = 0
for (pattern in patterns)
    if (regex.matches(pattern)) possible++

println(possible)

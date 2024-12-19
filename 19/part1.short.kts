import kotlin.io.path.Path
import kotlin.io.path.readText


val (towels, patterns) = Path("19/input.txt").readText().split("\n\n")
println(Regex("^(${towels.replace(", ", "|")})+$", RegexOption.MULTILINE).findAll(patterns).count())

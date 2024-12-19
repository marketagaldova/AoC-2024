import kotlin.io.path.Path
import kotlin.io.path.readText


val (towels, patterns) = Path("19/input.txt").readText().split("\n\n")
    .let { (towels, patterns) -> towels.split(", ").groupBy { it.first() } to patterns.split('\n') }

fun check(pattern: String, offset: Int = 0, cache: MutableMap<Int, Long> = mutableMapOf()): Long {
    if (offset == pattern.length) return 1
    cache[offset]?.let { return it }

    val result = (towels[pattern[offset]] ?: return 0).sumOf { towel ->
        if (
            offset + towel.length > pattern.length
            || pattern.substring(offset, offset + towel.length) != towel
        ) 0
        else check(pattern, offset + towel.length, cache)
    }
    cache[offset] = result
    return result
}

println(patterns.sumOf { check(it) })

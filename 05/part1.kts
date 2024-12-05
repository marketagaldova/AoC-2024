import kotlin.io.path.Path
import kotlin.io.path.readText


val (ordering, updates) = Path("05/input.txt").readText().split("\n\n", limit = 2)

val rules = ordering.lineSequence()
    .map { it.split('|') }
    .map { (first, second) -> first.toInt() to second.toInt() }
    .groupBy { it.first }
    .mapValues { it.value.map { it.second } }


val result = updates.lineSequence()
    .map { it.split(',').map { it.toInt() } }
    .filter { pages ->
        for (i in pages.indices) {
            val rules = rules[pages[i]] ?: continue
            if (pages.subList(0, i).any { it in rules }) return@filter false
        }
        return@filter true
    }
    .map { pages -> pages[pages.size / 2] }
    .sum()

println(result)

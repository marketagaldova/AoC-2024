import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs


val input = Path("01/input.txt").readText()

val leftList = mutableListOf<Int>()
val rightList = mutableListOf<Int>()

input.lineSequence()
    .filter { it.isNotBlank() }
    .map { it.split(Regex("\\s+")) }
    .forEach { (left, right) ->
        leftList.add(left.toInt())
        rightList.add(right.toInt())
    }

val result = leftList
    .map { left -> left * rightList.count { right -> right == left } }
    .sum()

println(result)

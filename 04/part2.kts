import kotlin.io.path.Path
import kotlin.io.path.readLines


val input = Path("04/input.txt").readLines()
val width = input.first().length
val height = input.size

var result = 0

for (y in 1 ..< height - 1)
    for (x in 1 ..< width - 1) {
        if (input[y][x] != 'A') continue

        val left =
            input[y + 1][x - 1] == 'M' && input[y - 1][x + 1] == 'S'
            || input[y + 1][x - 1] == 'S' && input[y - 1][x + 1] == 'M'

        val right =
            input[y - 1][x - 1] == 'M' && input[y + 1][x + 1] == 'S'
            || input[y - 1][x - 1] == 'S' && input[y + 1][x + 1] == 'M'

        if (left && right) result++
    }

println(result)

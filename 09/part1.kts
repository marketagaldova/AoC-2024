import kotlin.io.path.Path
import kotlin.io.path.readText


val input = Path("09/input.txt").readText()

val driveData = mutableListOf<Int?>()

input.asSequence()
    .map { it.digitToInt() }
    .chunked(2)
    .withIndex()
    .map { Triple(it.index, it.value[0], it.value.getOrElse(1) { 0 }) }
    .forEach { (fileId, fileSize, freeSpace) ->
        repeat(fileSize) { driveData.add(fileId) }
        repeat(freeSpace) { driveData.add(null) }
    }

var startIndex = 0
var endIndex = driveData.lastIndex
while (startIndex < endIndex) {
    if (driveData[startIndex] != null) {
        startIndex++
        continue
    }
    while (startIndex < endIndex) {
        if (driveData[endIndex] == null) {
            endIndex--
            continue
        }
        driveData[startIndex] = driveData[endIndex]
        driveData[endIndex] = null
        startIndex++
        endIndex--
        break
    }
}

val result = driveData
    .filterNotNull()
    .withIndex()
    .sumOf { (index, fileId) -> index.toLong() * fileId }

println(result)

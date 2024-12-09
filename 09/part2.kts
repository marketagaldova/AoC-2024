import kotlin.io.path.Path
import kotlin.io.path.readText


val input = Path("09/input.txt").readText()

sealed interface Chunk {
    val size: Int
    fun checksum(position: Int): Long

    data class File(val id: Int, override val size: Int) : Chunk {
        override fun checksum(position: Int): Long {
            var result = 0L
            repeat(size) { i -> result += (position + i) * id }
            return result
        }
    }

    data class Empty(override val size: Int, val insertedFiles: MutableList<File> = mutableListOf()) : Chunk {
        var freeSpace = size
        fun insert(file: File) {
            freeSpace -= file.size
            insertedFiles.add(file)
        }

        override fun checksum(position: Int): Long {
            var result = 0L
            var offset = 0
            for (file in insertedFiles) {
                result += file.checksum(position + offset)
                offset += file.size
            }
            return result
        }
    }
}

val driveData = mutableListOf<Chunk>()
input.asSequence()
    .map { it.digitToInt() }
    .chunked(2)
    .withIndex()
    .map { Triple(it.index, it.value[0], it.value.getOrElse(1) { 0 }) }
    .forEach { (fileId, fileSize, freeSpace) ->
        driveData.add(Chunk.File(fileId, fileSize))
        if (freeSpace != 0) driveData.add(Chunk.Empty(freeSpace))
    }

for (i in driveData.indices.reversed()) {
    val file = driveData[i] as? Chunk.File ?: continue
    for (j in 0 ..< i) {
        val target = driveData[j] as? Chunk.Empty ?: continue
        if (target.freeSpace < file.size) continue
        target.insert(file)
        driveData[i] = Chunk.Empty(file.size)
        break
    }
}

var position = 0
var checksum = 0L
for (chunk in driveData) {
    checksum += chunk.checksum(position)
    position += chunk.size
}

println(checksum)

import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.pow


val input = Path("17/input.txt").readText()

var (A, B, C) = input.lines().take(3).map { it.split(": ")[1].toInt() }
var program = input.split("\n\n")[1].split(": ")[1].split(',').map { it.toInt() }
var ip = 0

fun readComboOperand(value: Int): Int = when (value) {
    in 0..3 -> value
    4 -> A
    5 -> B
    6 -> C
    else -> error("Invalid combo operand: $value")
}

while (ip in program.indices) {
    val instruction = program[ip]
    val operand = program[ip + 1]

    when (instruction) {
        0 -> { // adv
            A = (A / (2.0.pow(readComboOperand(operand)))).toInt()
            ip += 2
        }
        1 -> { // bxl
            B = B xor operand
            ip += 2
        }
        2 -> { // bst
            B = readComboOperand(operand) % 8
            ip += 2
        }
        3 -> { // jnz
            if (A == 0) ip += 2
            else ip = operand
        }
        4 -> { // bxc
            B = B xor C
            ip += 2
        }
        5 -> { // out
            print(readComboOperand(operand) % 8)
            print(',')
            ip += 2
        }
        6 -> { // bdv
            B = (A / (2.0.pow(readComboOperand(operand)))).toInt()
            ip += 2
        }
        7 -> { // cdv
            C = (A / (2.0.pow(readComboOperand(operand)))).toInt()
            ip += 2
        }
        else -> error("Unknown instruction $instruction")
    }
}

println("\b ")

fun main() {
    fun log(message: Any?) {
        println(message)
    }

    fun part1(input: List<String>): Int {
        var cycle = 1
        var x = 1
        var result = 0
        // shove an extra noop before every addx to make the cycle counts simpler
        input.flatMap {
            if (it == "noop") listOf(it) else listOf("noop", it)
        }.forEach { line ->
//            log("  $cycle -> $line -> $x")
            when {
                line.startsWith("noop") -> cycle++
                line.startsWith("addx") -> {
                    val value = line.split(" ")[1].toInt()
                    x += value
                    cycle++
                }
            }
            if (cycle == 20 || (cycle - 20) % 40 == 0) {
                val signalStrength = cycle * x
                result += signalStrength
//                log("* $cycle -> $x -> $signalStrength -> $result")
            }
        }
        return result
    }

    fun part2(input: List<String>): String {
        var cycle = 1
        var x = 1
        val crt = MutableList(241) { ' ' }

        // shove an extra noop before every addx to make the cycle counts simpler
        input.flatMap {
            if (it == "noop") listOf(it) else listOf("noop", it)
        }.forEach { line ->
            log("  $cycle -> $line -> $x")
            when {
                line.startsWith("noop") -> cycle++
                line.startsWith("addx") -> {
                    val value = line.split(" ")[1].toInt()
                    x += value
                    cycle++
                }
            }
            val offsetX = (cycle - 1) % 40
            crt[cycle - 1] = if (x in offsetX -1 .. offsetX + 1) '#' else ' '
        }
        return crt.joinToString("").chunked(40).joinToString("\n")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test.txt")
    check(part1(testInput) == 13140)
//    check(part2(testInput) == 999)


    val input = readInput("Day10.txt")
    println(part1(input))
    check(part1(input) == 10760)
    println(part2(input))
//    check(part2(input) == 99999)
}

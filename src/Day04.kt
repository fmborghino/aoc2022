fun main() {
    @Suppress("unused")
    fun log(message: Any?) {
        println(message)
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { record ->
            // BUG NOTE dammit, forgot to convert these to ints!
            val (a, b, x, y) = record.split(",", "-").map { it.toInt() }
            // hit https://youtrack.jetbrains.com/issue/KT-46360 here with sumOf() resolution for Int vs Long
            (if ((a in x..y && b in x..y) || (x in a..b && y in a..b)) 1 else 0).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { record ->
            val (a, b, x, y) = record.split(",", "-").map { it.toInt() }
            (if (a in x..y || b in x..y || x in a..b || y in a..b) 1 else 0).toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test.txt")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)


    val input = readInput("Day04.txt")
    println(part1(input))
    check(part1(input) == 595)
    println(part2(input))
    check(part2(input) == 952)
}

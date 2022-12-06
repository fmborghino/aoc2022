// four characters that are all different

fun main() {
    fun log(message: Any?) {
//        println(message)
    }

    fun part1(input: String): Int {
        // whoaa TIL indexOfFirst()
        // windowedSequence or not... need to benchmark really, not obvious
        return input.windowedSequence(4).indexOfFirst { it.toSet().size == 4 } + 4

// first try
//        log(input)
//        val match = input
//            .windowed(4, 1).map{ it.toSet() }.first { it.size == 4 }.joinToString("")
//        log(match)
//        return input.indexOf(match) + 4

// more noodling (based on youtube)
//        for((index, window) in input.windowedSequence(4).withIndex()) {
//            if (window.toSet().size == 4) return index + 4
//        }
//        return 0
    }

    fun part2(input: String): Int {
        val match = input
            .windowed(14, 1).map{ it.toSet() }.first { it.size == 14 }.joinToString("")
        log(match)
        return input.indexOf(match) + 14
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test.txt").first()
    check(part1(testInput) == 7)
//    check(part2(testInput) == 999)


    val input = readInput("Day06.txt").first()
    println(part1(input))
    check(part1(input) == 1651)
    println(part2(input))
    check(part2(input) == 3837)
}

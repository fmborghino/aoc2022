fun main() {
    fun log(message: Any?) {
        println(message)
    }

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("DayNN_test.txt")
    check(part1(testInput) == 1)
    //    check(part2(testInput) == 999)


    val input = readInput("DayNN.txt")
    println(part1(input))
//    check(part1(input) == 9999)
//    println(part2(input))
//    check(part2(input) == 9999)
}

fun main() {
    val _day_ = 16
    fun log(message: Any?) {
        println(message)
    }

    fun part1(input: List<String>): Int {
        return -1
    }

    fun part2(input: List<String>): Int {
        return -2
    }

    // test inputs
    val testInput = readInput("Day${_day_}_test.txt")

    // test part 1
    val test1 = part1(testInput)
    check(test1 == 111) { "!!! test part 1 failed with: $test1" }
    // test part 2
    val test2 = part2(testInput)
    check(part2(testInput) == 222) { "!!! test part 2 failed with: $test2" }

    // game inputs
    val gameInput = readInput("Day${_day_}.txt")

    // game part 1
    val game1 = part1(gameInput)
    println("*** game part 1: $game1")
    check(game1 == 111111)

    // game part 2
    val game2 = part2(gameInput)
    println("*** game part 2: $game2")
    check(game2 == 222222)
}

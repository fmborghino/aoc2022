fun main() {
    val _day_ = "00"
    fun log(message: Any?) {
        println(message)
    }

    fun part1(input: List<String>): Int {
        return -111111
    }

    fun part2(input: List<String>): Int {
        return -222222
    }

    // test inputs
    val testInput = readInput("Day${_day_}_test.txt")

    // test part 1
    val test1 = part1(testInput)
    check(test1 == 111111) { "!!! test part 1 failed with: $test1" }

    // game inputs
    val gameInput = readInput("Day${_day_}.txt")

    // game part 1
    val game1 = part1(gameInput)
    println("*** game part 1: $game1")
    check(game1 == 111111) { "!!! game part 1 failed with: $game1" }

    // test part 2
    val test2 = part2(testInput)
    check(test2 == 2222222) { "!!! test part 2 failed with: $test2" }

    // game part 2
    val game2 = part2(gameInput)
    println("*** game part 2: $game2")
    check(game2 == 222222) { "!!! game part 2 failed with: $game2" }
}
/*
 * Hindsight notes.
 * - There are much more concise ways to re-express this as chained functions.
 */
fun main() {
    fun log(message: Any?) {
//        println(message)
    }

    fun part1(input: List<String>): Int {
        var elfIndex = 1
        var maxElfIndex = 0
        var maxCalories = 0
        var calories = 0
        input.forEach {
            if (it.isNotEmpty()) {
                log(it)
                calories += it.toInt()
            } else {
                log("Done with elf #$elfIndex with $calories calories")
                if (calories > maxCalories) {
                    maxCalories = calories
                    maxElfIndex = elfIndex
                }
                calories = 0
                elfIndex++
            }
        }
        log("Done with elf #$elfIndex")
        log("Winner is elf $maxElfIndex with $maxCalories calories")
        return maxCalories
    }

    fun part2(input: List<String>): Int {
        var elfIndex = 1
        var calories = 0
        val caloriesList = mutableListOf<Int>()
        input.forEach {
            if (it.isNotEmpty()) {
                log(it)
                calories += it.toInt()
            } else {
                log("Done with elf #$elfIndex with $calories calories")
                caloriesList.add(calories)
                calories = 0
                elfIndex++
            }
        }
        caloriesList.add(calories)
        log("Done with elf #$elfIndex")

        val totalCalories = caloriesList.sorted().takeLast(3)
        log("take(3) ${caloriesList.sorted().take(3).joinToString()}")
        log("takeLast(3) ${totalCalories.joinToString()} for a total of ${totalCalories.sum()}")
        return totalCalories.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test.txt")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01.txt")
    println(part1(input))
    println(part2(input))
    check(part1(input) == 68467)
    check(part2(input) == 203420)
}

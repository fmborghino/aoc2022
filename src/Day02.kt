/*
 * Hindsight notes
 * - yeah too much string typing here :-/
 */
fun main() {
    fun getScore(key: String): Int {
        /*
         * Hindsight notes
         * `when` here would allow multiple matches to the same value, like
         * "AX", "BY", "CZ" -> 3 etc, which is more readable than this
         */
        val points = mapOf<String, Int>(
            "X" to 1, "Y" to 2, "Z" to 3,
            "AX" to 3, "AY" to 6, "AZ" to 0,
            "BX" to 0, "BY" to 3, "BZ" to 6,
            "CX" to 6, "CY" to 0, "CZ" to 3)
        return points.getOrDefault(key, 0) // silence the null
    }

    fun part1(input: List<String>): Int {
        // Rock A X, Paper B Y, Scissors C Z
//        var score = 0
//        input.forEach {
//            val (them: String, you: String) = it.split(' ')
//            score += getScore(you)
//            score += getScore(them + you)
//        }
//        return score
        // let's try this with sumOf()
        return input.sumOf {
            val (them: String, you: String) = it.split(' ')
            getScore(you) + getScore(them + you)
        }
    }

    /*
     * Hindsight notes.
     * -  Oops didn't need to calculate convertPlay here, because we're already told the desired outcome (lose, tie, win)
     *   and can infer the score directly from that.
     * - OTOH this allows us to re-use the key bots of part1
     */
    fun convertPlay(them: String, outcome: String): String {
        val convert = mapOf<String, String>(
            "AX" to "Z", "AY" to "X", "AZ" to "Y",
            "BX" to "X", "BY" to "Y", "BZ" to "Z",
            "CX" to "Y", "CY" to "Z", "CZ" to "X"
        )
        return convert.getOrDefault(them + outcome, "") // silence the null
    }

    fun part2(input: List<String>): Int {
        // Rock A, Paper B, Scissors C
        // Muse lose X, tie Y, win Z
        var score = 0
        input.forEach {
            val (them: String, outcome: String) = it.split(' ')
            val you = convertPlay(them, outcome)
            score += getScore(you)
            score += getScore(them + you)
        }
        return score
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test.txt")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02.txt")
    println(part1(input))
    check(part1(input) == 8890)
    println(part2(input))
    check(part2(input) == 10238)
}

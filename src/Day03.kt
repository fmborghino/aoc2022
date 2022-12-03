fun main() {
    fun log(message: Any?) {
//        println(message)
    }

    fun score(letter: Char): Int {
        return if (letter.isLowerCase()) {
            letter.code - 96
        } else {
            letter.code - 64 + 26
        }
    }
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val half = it.length / 2
            score(it.substring(0, half).toSet().intersect(it.substring(half).toSet()).first())
        }
    }

    fun part2(input: List<String>): Int {
        return input
            .chunked(3)
            .sumOf {
            score(it[0].toSet().intersect(it[1].toSet()).intersect(it[2].toSet()).first())
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test.txt")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)


    val input = readInput("Day03.txt")
    println(part1(input))
    check(part1(input) == 8243)
    println(part2(input))
    check(part2(input) == 2631)
}

// as an extension property, note ext props can't be local
val Char.scoreProp: Int
    get() = if (this.isLowerCase()) {
            this.code - 96
        } else {
            this.code - 64 + 26
        }

fun main() {
    fun log(message: Any?) {
//        println(message)
    }

    fun Char.score(): Int {
        return if (this.isLowerCase()) {
            this.code - 96
        } else {
            this.code - 64 + 26
        }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {
            val half = it.length / 2
            // kind of prefer the non infix for this (see part2)
            (it.substring(0, half).toSet() intersect it.substring(half).toSet()).first().score()// infix
        }
    }

    fun part2(input: List<String>): Int {
        return input
            .chunked(3)
            .sumOf {
            it[0].toSet().intersect(it[1].toSet()).intersect(it[2].toSet()).first().scoreProp // not infix :-)
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

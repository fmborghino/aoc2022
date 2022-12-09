import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    @Suppress("unused")
    fun log(message: Any?) {
        println(message)
    }

    fun parse(input: List<String>): List<Pair<Int, Int>> {
        val moves: MutableList<Pair<Int, Int>> = mutableListOf()
        var x = 0
        var y = 0
        moves.add(Pair(x, y))
        input.forEach { line ->
            val d = line[0]
            val c = line.drop(2).toInt()
            repeat(c) {
                when (d) {
                    'R' -> x++
                    'L' -> x--
                    'U' -> y++
                    'D' -> y--
                }
                moves.add(Pair(x, y))
            }
        }
        return moves.toList()
    }

    fun List<Pair<Int, Int>>.follow(): List<Pair<Int, Int>> {
        val follows: MutableList<Pair<Int, Int>> = mutableListOf()
        var tailX = 0
        var tailY = 0
        this.forEach {
            val (headX, headY) = it
            val deltaX = headX - tailX
            val deltaY = headY - tailY
            when {
                // within 1, don't move
                deltaX.absoluteValue <= 1 && deltaY.absoluteValue <= 1 -> {}
                // left / right move
                deltaY == 0 -> tailX += deltaX.sign
                deltaX == 0 -> tailY += deltaY.sign
                // diagonal
                deltaY.absoluteValue == 2 || deltaX.absoluteValue == 2 -> { tailY += deltaY.sign; tailX += deltaX.sign}
            }
            follows.add(Pair(tailX, tailY))
        }
        return follows.toList()
    }

    fun part1(input: List<String>): Int {
        return parse(input).follow().toSet().size
    }

    fun part2(input: List<String>): Int {
        return parse(input).follow().follow().follow().follow().follow().follow().follow().follow().follow()
            // ... the yalow brick road
            .toSet().size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test.txt")
    check(part1(testInput) == 13)
    check(part2(readInput("Day09_test2.txt")) == 36)


    val input = readInput("Day09.txt")
    println(part1(input))
    check(part1(input) == 6642)
    println(part2(input))
    check(part2(input) == 2765)
}

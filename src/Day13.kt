import org.json.JSONArray
import org.json.JSONTokener

fun main() {
    fun log(message: Any?) {
        println(message)
    }

    fun String.toJSONArray() = JSONTokener(this).nextValue() as JSONArray
    fun String.toListViaJSONArray() = this.toJSONArray().toList()

    val marker2 = "[[2]]".toListViaJSONArray()
    val marker6 = "[[6]]".toListViaJSONArray()

    // TODO: read up on Any vs *
    // wow this was hard to reason out...
    fun List<*>.wtfCompareTo(that: List<*>): Int {
        val compare = when {
            // check edge cases
            this.isNotEmpty() && that.isEmpty() -> return 1
            this.isEmpty() && that.isNotEmpty() -> return -1
            this.isEmpty() /*&& that.isEmpty()*/ -> return 0
            // then check heads
            this[0] is Int && that[0] is Int -> (this[0] as Int).compareTo((that[0] as Int)) // int <> int
            this[0] is List<*> && that[0] is Int -> (this[0] as List<*>).wtfCompareTo(listOf(that[0])) // list <> int
            this[0] is Int && that[0] is List<*> -> listOf(this[0]).wtfCompareTo(that[0] as List<*>) // int <> list
            else -> (this[0] as List<*>).wtfCompareTo(that[0] as List<*>) // list <> list
        }

        if (compare != 0) return compare // we're done, someone won

        return this.drop(1).wtfCompareTo(that.drop(1)) // drop heads, keep going
    }

    fun parse1(input: List<String>): List<Pair<List<Any>, List<Any>>> {
        val list = input.windowed(3, 3, partialWindows = true).map {// partial for the last pair
            val (left, right, _) = it
//            log("parse $left <> $right")
            Pair(left.toListViaJSONArray(), right.toListViaJSONArray())
        }.toList()
//        log(">>>\n$list")
        return list
    }
    fun parse2(input: List<String>): List<List<Any>> {
        val list = input.filter { it.isNotEmpty() }.mapIndexed { i, it ->
//            log("parse2 ${i + 1} -> $it")
            it.toListViaJSONArray()
        }.toMutableList().also {
            it.add(marker2)
            it.add(marker6)
        }.toList()
//        log(">>>\n$list")
        return list
    }

    fun part1(input: List<String>): Int {
        val pairs = parse1(input)
        val result: List<Int> = pairs.mapIndexed() { i, p ->
            val (left, right) = Pair(p.first, p.second)
            val isOrdered = left.wtfCompareTo(right) <= 0
//            log("$i -> $left <> $right -> $isOrdered")
            when {
                isOrdered -> i + 1
                else -> 0
            }
        }.toList()
//        log(">>> $result -> ${result.sum()}")
        return result.sum()
    }

    fun part2(input: List<String>): Int {
        val lists = parse2(input)
        val sorted = lists.sortedWith(List<Any>::wtfCompareTo)
//        log(" lists:\n${lists}\nsorted:\n${sorted}")
        val position2 = sorted.indexOfFirst { it.wtfCompareTo(marker2) == 0 } + 1
        val position6 = sorted.indexOfFirst { it.wtfCompareTo(marker6) == 0 } + 1
        return position2 * position6
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test.txt")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13.txt")
    println(part1(input))
    check(part1(input) == 5659)
    println(part2(input))
    check(part2(input) == 22110)
}

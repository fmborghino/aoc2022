// Monkey 0:
//  Starting items: 79, 98
//  Operation: new = old * 19
//  Test: divisible by 23
//    If true: throw to monkey 2
//    If false: throw to monkey 3
// round is all monkeys act on all items
// each item ALSO gets divide by 3 and round down before action
// new items go to the end of the list
//
// count number times each monkey inspects an item
// result is product of two top inspectors

fun main() {
    @Suppress("unused")
    fun log(message: Any?) {
        println(message)
    }

    class Operation(val op: Char, val value: Long?) // value = null when "old"
    class Test(val op: Char = 'd', val value: Long, var trueMonkey: Int = 0, var falseMonkey: Int = 0)
    class Monkey(var name: Int = 0, var inspections: Long = 0, var items: MutableList<Long>, val op: Operation?, val test: Test?)

    // Monkey 0:
//  Starting items: 79, 98
//  Operation: new = old * 19  // also + N also * old (oddly not + old)
//  Test: divisible by 23
//    If true: throw to monkey 2
//    If false: throw to monkey 3
    fun parseMonkeys(input: List<String>): List<Monkey> {
        val monkeys: MutableList<Monkey> = mutableListOf()
        var mNumber: Int = 0
        var items: MutableList<Long> = mutableListOf()
        var op: Operation? = null
        var test: Test? = null

        // CONSIDER input.removeAll { it.isBlank() }.windowed(size = 6, step = 6).split("\n")
        input.forEach {  l ->
//            log(l)
            when {
                // eh could just increment counter here... but hey what if part 2 is out of order?
                l.startsWith("Monkey ") ->
                    mNumber = l.substringAfter(" ").substringBefore(":").toInt()
                l.startsWith("  Starting items: ") ->
                    items = l.substringAfter("  Starting items: ").split(", ")
                        .map { it.toLong() }.toMutableList()
                l.startsWith("  Operation: new = old ") -> {
                    val args = l.substringAfter("  Operation: new = old ").split(" ")
                    op = Operation(op = args[0][0], value = args[1].toLongOrNull())
                }
                l.startsWith("  Test: divisible by ") ->
                    test = Test(value = l.substringAfter("  Test: divisible by ").toLong())
                l.startsWith("    If true:") -> test?.trueMonkey = l.split(" ").last().toInt()
                l.startsWith("    If false:") -> test?.falseMonkey = l.split(" ").last().toInt()
                l.isBlank() -> monkeys.add(Monkey(name = mNumber, items = items, op = op, test = test))
            }
        }
        monkeys.add(Monkey(name = mNumber, items = items, op = op, test = test)) // last monkey
        return monkeys
    }

    fun monkeyBusiness(monkey: Monkey, monkeys: List<Monkey>, part: Int = 1) {
        monkey.items.forEach { item ->
            val operand = monkey.op?.value ?: item // handles "old" which we stashed as Int? null
            var newItem = (if (monkey.op?.op == '*') (item * operand) else (item + operand))
            if (part == 1) { newItem /= 3 }
            else {
                val mod: Long = monkeys.map { it.test?.value!! }.reduce(Long::times)
                newItem %= mod
            }

            if (newItem % monkey.test?.value!! == 0L) {
                monkeys[monkey.test?.trueMonkey!!].items.add(newItem)
                log("monkey ${monkey.name}: $item -> $newItem to  true ${monkey.test?.trueMonkey!!}")
            } else {
                monkeys[monkey.test?.falseMonkey!!].items.add(newItem)
                log("monkey ${monkey.name}: $item -> $newItem to false ${monkey.test?.falseMonkey!!}")
            }
        }
        monkey.inspections += monkey.items.size
        monkey.items = mutableListOf()
        return
    }

    fun part1(input: List<String>): Long {
        val monkeys: List<Monkey> = parseMonkeys(input)
        log("STARTING\n${monkeys.map { it.items.joinToString(", ") }.joinToString("\n")}")
        repeat(20) { round ->
            monkeys.forEach { monkey ->
                monkeyBusiness(monkey, monkeys)
            }
            log("AFTER ROUND ${round + 1}\n${monkeys.map { it.items.joinToString(", ") }.joinToString("\n")}")
        }
        return monkeys.map { it.inspections }.sortedDescending().take(2).fold(1) { a, v -> a * v }
    }

    fun part2(input: List<String>): Long {
        val monkeys: List<Monkey> = parseMonkeys(input)
//        log("STARTING\n${monkeys.map { it.items.joinToString(", ") }.joinToString("\n")}")
        repeat(10_000) { round ->
            monkeys.forEach { monkey ->
                monkeyBusiness(monkey, monkeys, part = 2)
            }
//            log("AFTER ROUND ${round + 1}\n${monkeys.map { it.items.joinToString(", ") }.joinToString("\n")}")
        }
        return monkeys.map { it.inspections }.sortedDescending().take(2).fold(1) { a, v -> a * v }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test.txt")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)


    val input = readInput("Day11.txt")
    println(part1(input))
    check(part1(input) == 61503L)
    println(part2(input))
    check(part2(input) == 14081365540L)
}

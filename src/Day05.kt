fun main() {
    fun log(message: Any?) {
//        println(message)
    }

    // ugly-ish "matrix rotate" of the stacks, removing the junk columns and "leading" spaces
    // so the test stacks come back looking like...
    // NZ
    // DCM
    // P
    // TODO switch this to return Array<ArrayDeque<Char>>, but for now, jammin' with String < shrug >
    fun parseStacks(rawStacks: List<String>): Array<String> {
        // cuz of the pattern of the column counters (only works for single digit!)
        val stackCount = (rawStacks.last().length + 2) / 4
        log("# stacks $stackCount")
        val stacks = Array<String>(stackCount) { "" }

        rawStacks.take(rawStacks.size - 1).forEachIndexed() { index, row ->
            log("row $index: [$row]")
            for (i in 1 .. rawStacks.last().length step 4) {
                val stackNumber = (i - 1) / 4
                val content = row.getOrElse(i) { ' ' }
                log("row $index at $i: [$content] stackNumber: $stackNumber")
                if (content != ' ') {
                    stacks[stackNumber] = stacks[stackNumber] + content
                }
            }
        }
        return stacks
    }

    // doing this with strings is dumb as fuck but also a hoot!
    fun moveStacks(stacks: Array<String>, instructions: List<String>, is9001: Boolean = false) {
        instructions.forEach { instruction ->
            // move 1 from 2 to 1
            val (_, countStr, fromStr, toStr) = instruction.split("move ", " from ", " to ")
            val (count, from, to) = listOf(countStr.toInt(), fromStr.toInt() - 1, toStr.toInt() - 1)
            // what are we moving?
            val moving = if (is9001) {
                // this is for part2
                stacks[from].substring(0, count)
            } else {
                // lolz because "move one by one" (this is for part1)
                stacks[from].substring(0, count).reversed()
            }
            log("instructions [$instruction] -> $count $from $to (moving $moving)")
            // remove from the source
            stacks[from] = stacks[from].substring(count)
            // add to the destination
            stacks[to] = moving + stacks[to]
            log("*** stacks\n${stacks.joinToString("\n")}")
        }
    }

    fun stackTops(stacks: Array<String>): String {
        return stacks.map {
            it.getOrElse(0) { ' ' }
        }.joinToString("")
    }

    fun part1(input: List<String>, is9001: Boolean = false): String {
        // split up the stacks and the instructions, they are separated by an empty line
        val separator = input.indexOfFirst { it.isEmpty() }

        val rawStacks = input.slice(0 until separator)
        val instructions = input.slice(separator + 1 until input.size)

        log("*** rawStacks\n$rawStacks")
        log("*** instructions\n$instructions")

        val stacks: Array<String> = parseStacks(rawStacks)
        log("*** stacks\n${stacks.joinToString("\n")}")

        moveStacks(stacks, instructions, is9001 = is9001)
        log("*** moved stacks\n${stacks.joinToString("\n")}")

        return stackTops(stacks)
    }

    fun part2(input: List<String>): String {
        return part1(input, is9001 = true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test.txt")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")


    val input = readInput("Day05.txt")
    println(part1(input))
//    check(part1(input) == 9999)
    println(part2(input))
//    check(part2(input) == 99999)
}

// NOODLING BELOW FOR IMPROVEMENTS
private fun day05() {
    val input = """
    [D]
[N] [C]
[Z] [M] [P]
 1   2   3

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
"""

    val numberStacks = numberStacks(input)
    log("*** $numberStacks")
    log("*** regex ${buildRegex(3).pattern}")
    val stacks = parseStacks(input, numberStacks)
    log("*** dumpStacks\n${stacks.joinToString("\n")}")
}

private fun parseStacks(input: String, numberStacks: Int): Array<ArrayDeque<Char>> {
    val r = buildRegex(numberStacks)
    val stacks: Array<ArrayDeque<Char>> = Array(numberStacks) { ArrayDeque() }
    input.split("\n")
        .drop(1)
        .takeWhile { ! it.startsWith(" 1") }
        .forEachIndexed() { index, line ->
            val padded = line.padEnd((numberStacks * 4) - 1, ' ') // makes the regex easier
            log("parsing padded line $index -> [$padded]")
            val matches = r.find(padded)
            for (i in 1.. numberStacks) {
                val item = matches?.groups?.get(i)?.value
                log("matched group $i as [$item]")
                if(item != " ") {
                    stacks[i - 1].add(item?.get(0) ?: '*')
                }
            }
        }
    return stacks
}

private fun numberStacks(input: String): Int {
    return input.split("\n")
//        .dropWhile { it.contains("[") }
//        .first()
        .first() { it.startsWith(" 1")}
        .split(" ")
        .filter { it.isNotBlank() }
        .max()
        .toInt()
}

private fun buildRegex(numberStacks: Int): Regex {
    return ("." + """([A-Z ])...""".repeat(numberStacks - 1) + "([A-Z ]).").toRegex()
}

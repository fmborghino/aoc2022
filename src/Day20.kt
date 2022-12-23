fun main() {
    val _day_ = "20"
    fun log(message: Any?) {
        println(message)
    }

    data class Node(val start: Long, val value: Long)

    fun parse(input: List<String>, key: Long = 1): MutableList<Node> {
        return input.mapIndexed { index, s -> Node(index.toLong(), s.toLong() * key) }.toMutableList()
    }

    fun shuffle(ring: MutableList<Node>) {
        (0L until ring.size).forEach { origPos ->
            val nodeToMoveIndex = ring.indexOfFirst { it.start == origPos }
    //            log("$origPos -> $nodeToMoveIndex -> ${ring[nodeToMoveIndex]}")
            val nodeToMove = ring.removeAt(nodeToMoveIndex)
            val newPosition = (nodeToMoveIndex + nodeToMove.value).mod(ring.size)
    //            log("$origPos -> $nodeToMoveIndex + ${nodeToMove.value} mod(${ring.size}) = $newPosition")
            ring.add(newPosition, nodeToMove)
        }
    }

    fun result(ring: MutableList<Node>): Long {
        val pos0 = ring.indexOfFirst { it.value == 0L }
        return ring[(pos0 + 1000).mod(ring.size)].value +
                ring[(pos0 + 2000).mod(ring.size)].value +
                ring[(pos0 + 3000).mod(ring.size)].value
    }

    fun part1(input: List<String>): Long {
        val ring: MutableList<Node> = parse(input)
//        log("s -> ${ring.map { it.value }}")
        shuffle(ring)
        return result(ring)
    }

    fun part2(input: List<String>): Long {
        val ring: MutableList<Node> = parse(input, key = 811589153L)
//        log("s -> ${ring.map { it.value }}")
        repeat(10) {
            shuffle(ring)
        }
        return result(ring)
    }

    // test inputs
    val testInput = readInput("Day${_day_}_test.txt")

    // test part 1
    val test1 = part1(testInput)
    check(test1 == 3L) { "!!! test part 1 failed with: $test1" }

    // game inputs
    val gameInput = readInput("Day${_day_}.txt")

    // game part 1
    val game1 = part1(gameInput)
    println("*** game part 1: $game1")
    check(game1 == 5498L) { "!!! game part 1 failed with: $game1" }

    // test part 2
    val test2 = part2(testInput)
    check(test2 == 1623178306L) { "!!! test part 2 failed with: $test2" }

    // game part 2
    val game2 = part2(gameInput)
    println("*** game part 2: $game2")
    check(game2 == 3390007892081L) { "!!! game part 2 failed with: $game2" }
}
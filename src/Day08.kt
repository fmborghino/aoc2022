fun main() {
    fun log(message: Any?) {
//        println(message)
    }

    fun circumference(n: Int) = 4 * (n - 1)

    fun iterPairsInside(n: Int) = sequence {
        (1 until n - 1).forEach { x ->
            (1 until n - 1).forEach { y ->
                yield(Pair(x, y))
            }
        }
    }

    fun parse(input: List<String>): List<List<Int>> {
//        val n: Int = input.first().length
//        val grid: MutableList<MutableList<Int>> = MutableList(n) { mutableListOf(n) }
//        input.forEachIndexed { i, line ->
//            grid[i] = line.split("").drop(1).dropLast(1).map { it.toInt() }.toMutableList()
//        }
//        return grid
        // TIL... digitToInt()
        return input.map { row -> row.map { it.digitToInt() } }
    }

    fun part1(input: List<String>): Int {
        val n: Int = input.first().length
        val grid = parse(input)
        var countVisible = 0
        log("GRID of $n x $n\n$grid")
        (1 until n - 1).forEach { y ->
            (1 until n - 1).forEach { x ->
                val target = grid[y][x]

                val row = grid[y]
                val visibleFromLeft = row.take(x).all { it < target }
                val visibleFromRight = row.takeLast(n - x -1).all { it < target }

                val column = grid.map { it[x] }
                val visibleFromTop =  column.take(y).all { it < target }
                val visibleFromBottom = column.takeLast(n - y -1).all { it < target }

                val visible = (visibleFromLeft || visibleFromRight || visibleFromTop || visibleFromBottom)
                if (visible) { countVisible++ }
//                log("($x, $y) [${target}] -> $visible [$visibleFromLeft, $visibleFromRight, $visibleFromTop, $visibleFromBottom]")
            }
        }
        return circumference(n) + countVisible
    }

    fun scoreView(trees: List<Int>, target: Int): Int {
        return minOf(trees.takeWhile { it < target }.size + 1, trees.size)
    }

    fun part2(input: List<String>): Int {
        val n: Int = input.first().length
        val grid = parse(input)
        var bestScore = 0
//        log("GRID of $n x $n\n$grid")
        (0 until n).forEach { y ->
            (0 until n).forEach { x ->
                val target = grid[y][x]

                val row = grid[y]
                val visibleToLeft = scoreView(row.take(x).reversed(), target)
                val visibleToRight = scoreView(row.takeLast(n - x - 1), target)

                val column = grid.map { it[x] }
                val visibleToTop = scoreView(column.take(y).reversed(), target)
                val visibleToBottom = scoreView(column.takeLast(n - y - 1), target)

                val score = (visibleToLeft * visibleToRight * visibleToTop * visibleToBottom)
                bestScore = maxOf(bestScore, score)
                log("($x, $y) [$target] [${score}] -> [$visibleToLeft, $visibleToRight, $visibleToTop, $visibleToBottom]")
            }
        }
        return bestScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test.txt")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)


    val input = readInput("Day08.txt")
    val p1 = part1(input)
    println(">>> $p1")
    check(p1 == 1703)
    val p2 = part2(input)
    println(">>> $p2")
    check(p2 == 496650)
}

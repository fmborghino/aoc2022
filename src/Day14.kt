fun main() {
    fun log(message: Any?) {
        println(message)
    }

    data class Vec2(val x: Int, val y: Int)
    data class Node(val c: Char)
    class Grid(val grid: MutableMap<Vec2, Node>, val hasFloor: Boolean,
               val rangeX: Pair<Int, Int>, val rangeY: Pair<Int, Int>)

    fun parse(input: List<String>, hasFloor: Boolean = false): Grid {
        var rangeXmin = Int.MAX_VALUE
        var rangeXmax = Int.MIN_VALUE
        var rangeYmin = Int.MAX_VALUE
        var rangeYmax = Int.MIN_VALUE
        val grid = buildMap<Vec2, Node> {
            input.forEachIndexed() { i, line ->
                val xys = line.split(" -> ")
                xys.windowed(2) { xyPair ->
                    val (leftX, leftY) = xyPair.first().split(",").map { it.toInt() }
                    val (rightX, rightY) = xyPair.last().split(",").map { it.toInt() }
                    when {
                        leftX == rightX -> { // x same, y moves
                            // make sure we go small to large
                            val min = minOf(leftY, rightY)
                            val max = maxOf(leftY, rightY)
                            for (y in min..max) { this[Vec2(leftX, y)] = Node('#') }
                            rangeYmin = minOf(rangeYmin, min)
                            rangeYmax = maxOf(rangeYmax, max)
                        }
                        leftY == rightY -> { // y same, x moves
                            val min = minOf(leftX, rightX)
                            val max = maxOf(leftX, rightX)
                            for (x in min..max) { this[Vec2(x, leftY)] = Node('#') }
                            rangeXmin = minOf(rangeXmin, min)
                            rangeXmax = maxOf(rangeXmax, max)
                        }
                        else -> error("unexpected diagonal at line $i: $xyPair")
                    }
                }
            }
            if (hasFloor) {
                rangeYmax += 2
                val stretch = rangeYmax // not sure how much wider the floor needs to be but...
                rangeXmin -= stretch
                rangeXmax += stretch
                for (x in (rangeXmin - stretch)..(rangeXmax + stretch)) {
                    this[Vec2(x, rangeYmax)] = Node('#')
                }
            }
        }
        return Grid(grid.toMutableMap(), hasFloor, Pair(rangeXmin, rangeXmax), Pair(rangeYmin, rangeYmax))
    }

    fun printGrid(grid: Grid) {
        for (y in grid.rangeY.first - 3..grid.rangeY.second) { // y are the rows top to bottom
            print("${"%2d".format(y) } ")
            for(x in grid.rangeX.first..grid.rangeX.second) { // x are the columns left to right
                val node = grid.grid.getOrDefault(Vec2(x, y), Node('.'))
                print(node.c)
            }
            println()
        }
    }

    fun dropSand(grid: Grid): Int {
        fun g(x: Int, y: Int): Char = grid.grid.getOrDefault(Vec2(x, y), Node('.')).c
        val startX = 500
        val startY = 0
        var grainCount = 0
        var pouring = true
        while (pouring) {
            grainCount++
            var x = startX
            var y = startY
            var falling = true
            while (falling) {
                when {
                    g(x, y + 1) == '.' -> y++ // below is free, drop 1
                    g(x - 1, y + 1) == '.' -> { x--; y++ } // else below left is free, drop 1 and left 1
                    g(x + 1, y + 1) == '.' -> { x++; y++ } // else below right is free, drop 1 and right 1
                    else -> { grid.grid[Vec2(x, y)] = Node('o'); falling = false } // grain came to rest
                }
                when (grid.hasFloor) {
                    // grain fell past bottom, we're done
                    false -> if (y > grid.rangeY.second) { falling = false; pouring = false }
                    true -> if (g(startX, startY) == 'o') { falling = false; pouring = false }
                }

            }
//            log("grainCount $grainCount ${if (!pouring) " LAST GRAIN!" else ""}")
//            printGrid(grid)
        }

        return if (grid.hasFloor) grainCount else grainCount - 1
    }

    fun part1(input: List<String>): Int {
        val grid = parse(input)
//        printGrid(grid)
        val grains = dropSand(grid)
//        printGrid(grid)
        return grains
    }

    fun part2(input: List<String>): Int {
        val grid = parse(input, hasFloor = true)
//        printGrid(grid)
        val grains = dropSand(grid)
//        printGrid(grid)
        return grains
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test.txt")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)


    val input = readInput("Day14.txt")
    println(part1(input))
    check(part1(input) == 592)
    println(part2(input))
    check(part2(input) == 30367)
}

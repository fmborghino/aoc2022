import kotlin.math.abs

fun main() {
    fun log(message: Any?) {
        println(message)
    }

    data class Vec2(val x: Int, val y: Int)
    data class Node(val c: Char, val beacon: Vec2? = null, val manhattan: Int? = null)
    class Grid(val grid: MutableMap<Vec2, Node>, val rangeX: Pair<Int, Int>, val rangeY: Pair<Int, Int>)

    fun MutableMap<Vec2, Node>.markDiamond(scan: Int, sX: Int, sY: Int) {
        fun markScanned(x: Int, y: Int) {
            val node: Node? = this[Vec2(x, y)]
            if (node == null) {
                this[Vec2(x, y)] = Node('#')
            }
        }

        // paint in a diamond around the S
        for (x in 0..scan) {
            for (y in 0..(scan - x)) {
                if (x == 0 && y == 0) continue
                markScanned(sX + x, sY - y)
                markScanned(sX + x, sY + y)
                markScanned(sX - x, sY - y)
                markScanned(sX - x, sY + y)
            }
        }
    }

    // Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    fun parse(input: List<String>): Grid {
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE
        val signedIntRegex="""-?\d+""".toRegex()

        val grid = buildMap<Vec2, Node> {
            input.forEachIndexed() { i, line ->
                val (sX, sY, bX, bY)=signedIntRegex.findAll(line).map { it.value.toInt() }.toList()
                minX = minOf(minX, sX, bX)
                maxX = maxOf(maxX, sX, bX)
                minY = minOf(minY, sY, bY)
                maxY = maxOf(maxY, sY, bY)
                this[Vec2(bX, bY)] = Node('B')
                val manhattanDistance = abs(sX - bX) + abs(sY - bY)
                this[Vec2(sX, sY)] = Node('S', beacon = Vec2(bX, bY), manhattan = manhattanDistance)
            }
        }
        return Grid(grid.toMutableMap(), Pair(minX, maxX), Pair(minY, maxY))
    }

    fun printGrid(grid: Grid, expand: Int = 0, filter: (Node) -> Boolean = { true }) {
        for (y in (grid.rangeY.first - expand)..(grid.rangeY.second + expand)) {
            print("${"%3d".format(y) } ")
            for(x in (grid.rangeX.first - expand)..(grid.rangeX.second + expand)) {
                val node = grid.grid.getOrDefault(Vec2(x, y), Node('.'))
                if (filter(node)) print(node.c) else print('.')
            }
            println()
        }
    }

    fun check1(grid: Grid, y: Int): Int {
        return grid.grid.filter { it.key.y == y }.filter { it.value.c in listOf('#') }.count()
    }

    fun markScanned(grid: Grid, x: Int, y: Int) {
        val node: Node? = grid.grid[Vec2(x, y)]
        if (node == null) {
            grid.grid[Vec2(x, y)] = Node('#')
        }
    }

    fun markTargetRow(grid: Grid, targetY: Int) {
        grid.grid.filter { it.value.c == 'S' }.forEach { sensor ->
            val beacon = sensor.value.beacon!!
            val (sX, sY, bX, bY) = listOf(sensor.key.x, sensor.key.y, beacon.x, beacon.y)
            val manhattan = abs(sX - bX) + abs(sY - bY)
            val verticalDistance = abs(sY - targetY)
            val affects = manhattan - verticalDistance + 1
//            log("S $sX,$sY B $bX,$bY -> scan $scan -> vert $vert affects $affects")
            if (affects > 0) {
                for (i in 0 until affects) {
                    markScanned(grid,sX + i, targetY)
                    markScanned(grid,sX - i, targetY)
                }
            }
        }
    }


    fun part1(input: List<String>, targetY: Int): Int {
        val grid = parse(input)
        log("part1 >>> grid X -> ${grid.rangeX} Y -> ${grid.rangeY}")
        markTargetRow(grid, targetY)
//        printGrid(grid, expand = 2) // print every node

        return check1(grid, targetY)
    }

    fun part2(input: List<String>, scanMax: Int): Int {
        val grid = parse(input)
        log("part2 >>> scanMax $scanMax, grid X -> ${grid.rangeX} Y -> ${grid.rangeY}")
        val sensors = grid.grid.filter { it.value.c == 'S' }
        (0..scanMax).forEach { scanX ->
            (0..scanMax).forEach loopXY@{ scanY ->
                if (scanY % 100_000 == 0 && scanX % 100 == 0) {
                    log("$scanX, $scanY")
                }
                sensors.forEach { sensor ->
                    val manhattanSensorToHere: Int = abs(sensor.key.x - scanX) + abs(sensor.key.y - scanY)
                    if (sensor.value.manhattan!! >= manhattanSensorToHere) {
                        return@loopXY // this sensor touches this XY location
                    }
                }
                // if we get here, we found an XY location that is not touched by any sensor
                val result = (4_000_000 * scanX) + scanY
                log("part2 >>> result $result ($scanX, $scanY)")
                return result
            }
        }
        return -1 // didn't find anything, shouldn't happen!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test.txt")
    val test1 = part1(testInput, 10)
    check(test1 == 26)
    val test2 = part2(testInput, scanMax = 20)
    check(test2 == 56_000_011)


    val input = readInput("Day15.txt")
    val result1 = part1(input, 2_000_000)
    println(result1)
    check(result1 == 4_879_972)

    val result2 = part2(input, 4_000_000)
    println(result2)
//    check(part2(input, y2) == 99999)
}

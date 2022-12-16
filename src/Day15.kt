import kotlin.math.abs

fun main() {
    fun log(message: Any?) {
        println(message)
    }

    data class Vec2(val x: Int, val y: Int)
    data class Node(val c: Char, val beacon: Vec2? = null, val manhattan: Int? = null)
    class Grid(val grid: MutableMap<Vec2, Node>, val rangeX: Pair<Int, Int>, val rangeY: Pair<Int, Int>)

    // generate the positions 1 step outside the manhattan distance
    fun generateOutsideDiamond(x: Int, y: Int, distance: Int, scanMax: Int): List<Vec2> {
        // paint in a diamond around the S
        return buildList {
            (0..distance).forEach { i ->
                add(Vec2(x + i, y - (1 + distance - i)))
                add(Vec2(x - i, y + (1 + distance - i)))
                add(Vec2(x - distance - 1 + i, y - i))
                add(Vec2(x + distance + 1 - i, y + i))
            }
        }.filter { it.x in (0..scanMax) && it.y in (0..scanMax)} // wasteful adding and removing <shrug>
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

        return grid.grid.filter { it.key.y == targetY }.filter { it.value.c in listOf('#') }.count()
    }

    fun part2(input: List<String>, scanMax: Int): Long {
        val grid = parse(input)
        val sensors = grid.grid.filter { it.value.c == 'S' }
        val fenceNodes = buildSet<Vec2> {
             sensors.forEach { sensor ->
                addAll(generateOutsideDiamond(sensor.key.x, sensor.key.y, sensor.value.manhattan!!, scanMax))
             }
        }
        log("part2 >>> sensors ${sensors.size} fenceNodes ${fenceNodes.size}")

        // for each fence node, check the manhattan distance to each node
        fenceNodes.forEach loopFence@{ here ->
            sensors.forEach { sensor ->
                val manhattanSensorToHere = abs(here.x - sensor.key.x) + abs(here.y - sensor.key.y)
                if (sensor.value.manhattan!! >= manhattanSensorToHere) {
                    return@loopFence
                }
            }
            // if we get here, we found an XY location that is not touched by any sensor
            val result = (4_000_000L * here.x) + here.y
            log("part2 >>> result $result (${here.x}, ${here.y})")
            return result
        }
        return -1 // didn't find anything, shouldn't happen!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test.txt")
    val test1 = part1(testInput, targetY = 10)
    check(test1 == 26)
    val test2 = part2(testInput, scanMax = 20)
    check(test2 == 56_000_011L)

    // game inputs part1
    val input = readInput("Day15.txt")
    val result1 = part1(input, targetY = 2_000_000)
    println(result1)
    check(result1 == 4_879_972)

    // game inputs part2
    val result2 = part2(input, scanMax = 4_000_000)
    println(result2)
    check(result2 == 12_525_726_647_448L)
}

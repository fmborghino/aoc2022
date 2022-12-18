// cubes
// for N cubes, there are N * 6 faces maximum
// for x, y, z location of a cube, there is a common face IFF i
// any pair xy, yz, xz is the same AND the other coordinate is off by one
// so for example Node(1, 1, 1) has a shared face with Node(1, 1, 2)
// for each shared face, subtract 2 from the faces maximum

data class Node3D(val x: Int, val y: Int, val z: Int) {
    fun faceNeighbors(): Set<Node3D> {
        return setOf(
            Node3D(x -1 , y, z),
            Node3D(x + 1, y, z),
            Node3D(x, y - 1, z),
            Node3D(x, y + 1, z),
            Node3D(x, y, z - 1),
            Node3D(x, y, z + 1),
        )
    }

    companion object {
        fun of(input: String): Node3D =
            input.split(",").let { (x, y, z) -> Node3D(x.toInt(), y.toInt(), z.toInt()) }
    }
}

fun main() {
    val _day_ = 18
    fun log(message: Any?) {
        println(message)
    }

    fun parse(input: List<String>): Set<Node3D> {
         return buildSet {
            addAll(input.map { Node3D.of(it) })
         }.toSet()
    }

    // we'll use Node3D.faceNeighbors to spot neighbor nodes
    // we'll add BOTH "faces" as two pairs: Pair<nodeA, nodeB>, Pair<nodeB, nodeA> in a set
    // this will de-dup when we compare the pair the other way
    // this is a little wasteful, but we can optimize later if needed
    fun findSharedFaces(grid: Set<Node3D>): Set<Pair<Node3D, Node3D>> {
        val sharedFaces: Set<Pair<Node3D, Node3D>> = buildSet {
            grid.forEach { a ->
                a.faceNeighbors().forEach() { b ->
                    if (b in grid) {
                        add(Pair(a, b))
                        add(Pair(b, a))
                    }
                }
            }
        }
        return sharedFaces
    }

    fun part1(grid: Set<Node3D>): Int {
        // faces not covered is all the possible faces minus the shared faces
        return (grid.size * 6) - findSharedFaces(grid).size
    }

    // NOPE this doesn't account for empty inside nodes in adjacent groups
    fun countEnclosedNodes(grid: Set<Node3D>): Int {
        var count = 0
        (grid.minOf { it.x } - 1..grid.maxOf { it.x } + 1).forEach { x ->
            (grid.minOf { it.y } - 1..grid.maxOf { it.y } + 1).forEach { y ->
                (grid.minOf { it.z } - 1..grid.maxOf { it.z } + 1).forEach { z ->
                    val n = Node3D(x, y, z)
                    if (n !in grid) { // it's a empty spot
                        if (n.faceNeighbors().filter { it in grid }.size == 6 ) { // and it's surrounded
                            count++
                        }
                    }

                }
            }
        }
        return count
    }

    fun findOutsideNodes(grid: Set<Node3D>): Set<Node3D> {
        val minX = grid.minOf { it.x } - 1
        val minY = grid.minOf { it.y } - 1
        val minZ = grid.minOf { it.z } - 1
        val maxX = grid.maxOf { it.x } + 1
        val maxY = grid.maxOf { it.y } + 1
        val maxZ = grid.maxOf { it.z } + 1
        val startNode = Node3D(minX, minY, minZ)
        val frontier = mutableListOf<Node3D>(startNode)
        val visited: MutableSet<Node3D> = mutableSetOf()
        val outside = mutableListOf<Node3D>(startNode)
        while (frontier.isNotEmpty()) {
            val n = frontier.removeAt(0)
            if (n !in visited) {
                visited.add(n)
                n.faceNeighbors().forEach {
                    if (it.x in minX..maxX &&
                        it.y in minY..maxY &&
                        it.z in minZ..maxZ &&
                        it !in grid) {
                        frontier.add(it)
                        outside.add(it)
                    }
                }
            }
        }
        return outside.toSet()
    }

    fun part2(grid: Set<Node3D>): Int {
        // 1. BFS to generate all outside nodes starting next to minX, minY, minZ, within maximum cube
        // 2. iterate over the grid and count all that have outside node neighbors (each neighbor is an outside face)
        val outsideNodes: Set<Node3D> = findOutsideNodes(grid)
        return grid.sumOf { node -> node.faceNeighbors().count { it in outsideNodes } }
    }

    // test inputs
    val testInput = readInput("Day${_day_}_test.txt")
    val testGrid = parse(testInput)

    // test part 1
    val test1 = part1(testGrid)
    check(test1 == 64) { "!!! test part 1 failed with: $test1" }

    // game inputs
    val gameInput = readInput("Day${_day_}.txt")
    val gameGrid = parse(gameInput)

    // game part 1
    val game1 = part1(gameGrid)
    println("*** game part 1: $game1")
    check(game1 == 4604) { "!!! game part 1 failed with: $game1" }

    // test part 2
    val test2 = part2(testGrid)
    check(test2 == 58) { "!!! test part 2 failed with: $test2" }

    // game part 2
    val game2 = part2(gameGrid)
    println("*** game part 2: $game2")
    check(game2 == 2604) { "!!! game part 2 failed with: $game2" }
}

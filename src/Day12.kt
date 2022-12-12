data class Node(val row: Int, val col: Int, var char: Char = '?', var distance: Int = -1, var visited: Boolean = false) {
    val height get(): Int = when {
        char.isLowerCase() -> char.code - 'a'.code // 0 - 25
        char == 'E' -> 26 // end point is 1 higher than 'z'
        else -> Int.MAX_VALUE // everything else is out of reach
    }
}
fun Node.isCandidateNeighborOf(node: Node): Boolean = this.height - 1 <= node.height

typealias Grid = Array<Array<Node>> // eh, marginal value, maybe less readable?

fun main() {
    fun log(message: Any?) {
        println(message)
    }

    fun bfs(nodes: Grid, start: Node): Int {
        // Create a queue and add start to represent the index of the first node
        start.distance = 0 // make sure we start at 0, not sure if I need the default of -1 yet
        val queue: MutableList<Node> = mutableListOf(start)
        while (queue.isNotEmpty()) {
            // Dequeue a node from queue
            val node = queue.removeAt(0)
            // Add all the node's unvisited neighbors to the queue
            if (!node.visited) {
                val candidates = listOfNotNull(
                    nodes[node.row].getOrNull(node.col - 1), // left
                    nodes[node.row].getOrNull(node.col + 1), // right
                    nodes.getOrNull(node.row - 1)?.getOrNull(node.col), // up
                    nodes.getOrNull(node.row + 1)?.getOrNull(node.col), // down
                ).filter { it.isCandidateNeighborOf(node) } // cannot be more than + 1 height
                candidates.forEach { candidate -> candidate.distance = node.distance + 1 }
                candidates.singleOrNull() { candidate -> candidate.char == 'E' }
                    ?.let { endNode -> return endNode.distance } // we found the end! return the result
                queue.addAll(candidates)
                node.visited = true
            }
        }
        return -1
    }

    fun parse(input: List<String>): Grid {
        // List<List<Node>> would be easier to init (none needed) but maybe slower? need to bench test
        val nodes = Grid(input.size) { row -> Array<Node>(input[0].length) { col ->
            Node(row, col)}
        }
        // is there a way to do this on the initializer without slurping this into another nested array/list
        input.forEachIndexed { row, s ->
            s.forEachIndexed { col, c ->
                nodes[row][col].char = c
            }
        }
        return nodes
    }

    fun findStart(nodes: Array<Array<Node>>, char: Char): Node? {
        // hm should do this on init too, but the parse return type gets ugly
        nodes.forEachIndexed { row, s ->
            s.forEachIndexed { col, c ->
                if (c.char == char) return nodes[row][col]
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {
        val grid = parse(input)
        val start = findStart(grid, 'S')
//        log("S is at $start")
        return bfs(grid, start!!)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test.txt")
    check(part1(testInput) == 31)
//    check(part2(testInput) == 29)


    val input = readInput("Day12.txt")
    println(part1(input))
//    check(part1(input) == 9999)
//    println(part2(input))
//    check(part2(input) == 99999)
}

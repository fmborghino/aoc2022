data class Node(val row: Int, val col: Int, var char: Char = '?', var distance: Int = -1, var visited: Boolean = false) {
    val height get(): Int = when {
        char.isLowerCase() -> char.code - 'a'.code // 0 - 25
        char == 'E' -> 26 // end point is 1 higher than 'z'
        else -> Int.MAX_VALUE // everything else is out of reach
    }
}

class Grid(val nodes: Array<Array<Node>>, val start: Node, val end: Node )

fun main() {
    fun log(message: Any?) {
        println(message)
    }

    fun bfs(
        nodes: Array<Array<Node>>, start: Node,
        isCandidate: (from: Node, to: Node) -> Boolean,
        isTarget: (Node) -> Boolean
    ): Int {
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
                ).filter { isCandidate(node, it) } // fn param checks the height diffs
                candidates.forEach { candidate -> candidate.distance = node.distance + 1 }
                candidates.singleOrNull() { candidate -> isTarget(candidate) }
                    ?.let { endNode -> return endNode.distance } // we found the target! return the result
                queue.addAll(candidates)
                node.visited = true
            }
        }
        return -1
    }

    fun parse(input: List<String>): Grid {
        val nodes = Array<Array<Node>>(input.size) { row: Int ->
            Array<Node>(input[0].length) { col ->
                Node(row, col)
            }
        }
        var start: Node? = null
        var end: Node? = null

        input.forEachIndexed { row, s ->
            s.forEachIndexed { col, c ->
                nodes[row][col].char = c
                if (c == 'S') { start = nodes[row][col] }
                if (c == 'E') { end = nodes[row][col] }
            }
        }
        return Grid(nodes, start!!, end!!)
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
        return bfs(grid.nodes, start = grid.start,
            isCandidate = { from, to -> to.height - from.height <= 1 }, // cannot be more than + 1 height from->to
            isTarget = { node -> node.char == 'E' } // stop at the end
        )
    }

    fun part2(input: List<String>): Int {
        val grid = parse(input)
        return bfs(grid.nodes, start = grid.end,
            isCandidate = { from, to -> from.height - to.height <= 1 }, // cannot be more than + 1 height to->from
            isTarget = { node -> node.char == 'a' } // stop at any 'a' height
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test.txt")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)


    val input = readInput("Day12.txt")
    println(part1(input))
    check(part1(input) == 423)
    println(part2(input))
    check(part2(input) == 416)
}

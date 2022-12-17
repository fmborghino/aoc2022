/*
1. parse into Node(name: String, flow: Int, edgesOne: List<Edge>, edgesAll: List<Edge>)
where Edge(to: Node, hop: Int)
and edgesOne is the directly connected graph with hop=1 each time

2. derive edgesAll, all possible (N * N) - N connections with the hops in that edge

3. walk every possible non-cyclic path to visit all nodes accumulating a flow total
 */

fun main() {
    val _day_ = 16
    fun log(message: Any?) {
        println(message)
    }

    data class Edge(val to: String, val hops: Int)
    data class Node(val name:String, val flow: Int,
               val edgeOne: List<Edge> = listOf(), val edgeAll: MutableList<Edge> = mutableListOf()
    )
    data class Graph(val g: List<Node>) {
        val byName get() = g.associateBy { it.name }
        val names get() = g.map { it.name }
    }

    // Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
    fun parse(input: List<String>): Graph {
        val r = """Valve (..) has flow rate=(\d+); tunnels? leads? to valves? (.+)""".toRegex()
        val graph: List<Node> = input.map { r.find(it)!!.destructured }.map { (name, flow, edges) ->
            Node(name, flow.toInt(), edges.split(", ").map { Edge(it, 1) })
        }
        return Graph(graph)
    }

    fun shortestDistance(graph: Graph, from: String, to: String): Int {
        val g = graph.byName
        val frontier: MutableList<Pair<String, Int>> = mutableListOf(from to 0)
        val visited: MutableSet<String> = mutableSetOf()
        while (frontier.isNotEmpty()) {
            val (name, hops) = frontier.removeAt(0)
            if (name == to) { return hops }
            if (name !in visited) {
                visited.add(name)
                g[name]?.edgeOne?.forEach { frontier.add(it.to to hops + 1) }
            }
        }
        error("no route from $from to $to")
    }

    // for each node, find the shortest path to every other node (ie how many hops)
    fun calculateAllRoutes(graph: Graph) {
        graph.byName.values.forEach { v ->
            val edges = graph.names.filter { it != v.name }.map { Edge(it, shortestDistance(graph, v.name, it)) }
            v.edgeAll.addAll(edges)
        }
    }

    // for the NxN routes, remove any destinations that have zero flow, no point checking those later
    fun pruneZeroFlow(graph: Graph) {
        val zeroFlowNodes = graph.g.filter { it.flow == 0 }.map { it.name }
        graph.g.forEach { node ->
            node.edgeAll.removeIf { it.to in zeroFlowNodes }
        }
    }

    fun printGraph(graph: Graph) {
        graph.g.forEachIndexed { i, v ->
            log("$i -> Node(${v.name}, ${v.flow}, " +
                    "[${v.edgeOne.joinToString { it.to }}], ${v.edgeAll.size} -> [" +
                    "${v.edgeAll.joinToString { "${it.to}:${it.hops}" }}]")
        }
    }

    fun longestDistance(graph: Any, name: String, it: String): Int {
        return -1
    }

    fun calculateAllScores(graph: Graph) {
        graph.byName.values.forEach { v ->
            val edges = graph.names.filter { it != v.name }.map { Edge(it, longestDistance(graph, v.name, it)) }
            v.edgeAll.addAll(edges)
        }
    }

    fun part1(input: List<String>): Int {
        val graph = parse(input)
        calculateAllRoutes(graph)
        pruneZeroFlow(graph)
        printGraph(graph)
        calculateAllScores(graph) // TODO: yeah hmm
        return -1
    }

    fun part2(input: List<String>): Int {
        return -2
    }

    // test inputs
    val testInput = readInput("Day${_day_}_test.txt")

    // test part 1
    val test1 = part1(testInput)
    check(test1 == 1651) { "!!! test part 1 failed with: $test1" }
    // test part 2
    val test2 = part2(testInput)
    check(part2(testInput) == 222) { "!!! test part 2 failed with: $test2" }

    // game inputs
    val gameInput = readInput("Day${_day_}.txt")

    // game part 1
    val game1 = part1(gameInput)
    println("*** game part 1: $game1")
    check(game1 == 111111)

    // game part 2
    val game2 = part2(gameInput)
    println("*** game part 2: $game2")
    check(game2 == 222222)
}

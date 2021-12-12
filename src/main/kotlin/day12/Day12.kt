package day12

import BaseDay
import getInputLines
import java.lang.IllegalArgumentException
import java.util.*

class Day12 : BaseDay(12, "Passage Pathing") {
    override suspend fun partOne(input: String) = solveDay12(input) {
        it.findPaths().size
    }

    override suspend fun partTwo(input: String) = solveDay12(input) {
        it.findPaths(allowedExtraSmallCaves = 1).size
    }

    private fun solveDay12(input: String, solver: (CaveGraph) -> Int) =
        input.getInputLines().parseCaveGraph().let(solver)
}

object LowerCase {
    operator fun contains(string: String) = string.all { it.isLowerCase() }
}

object UpperCase {
    operator fun contains(string: String) = string.all { it.isUpperCase() }
}

fun List<String>.parseCaveGraph(): CaveGraph {
    val connections = mutableMapOf<Node, MutableSet<Node>>()
    forEach {
        val conn = it.split("-")
        val curr = parseNode(conn[0])
        val other = parseNode(conn[1])
        connections.putIfAbsent(curr, mutableSetOf())
        connections.putIfAbsent(other, mutableSetOf())
        connections[curr]?.add(other)
        connections[other]?.add(curr)
    }

    return CaveGraph(connections)
}

fun parseNode(id: String) = when (id) {
    "start" -> StartNode()
    "end" -> EndNode()
    in LowerCase -> SmallCaveNode(id)
    in UpperCase -> BigCaveNode(id)
    else -> {
        throw IllegalArgumentException("Could not parse node for: $id")
    }
}

sealed class Node(val id: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Node) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return id
    }
}

open class CaveNode(id: String) : Node(id)
class BigCaveNode(id: String) : CaveNode(id)
class SmallCaveNode(id: String) : CaveNode(id)
class StartNode : Node("start")
class EndNode : Node("end")

class Path(
    val nodes: List<Node>,
    val allowedExtraSmallCaves: Int = 0,
    val extraSmallCaves: Int = 0,
    val seenSmallCaves: MutableMap<SmallCaveNode, Int> = mutableMapOf()
) {
    val latestNode = nodes.last()
    fun addNode(node: Node): Path? = when {
        node is StartNode -> null
        seenSmallCaves.containsKey(node) -> {
            if (extraSmallCaves < allowedExtraSmallCaves) {
                Path(mutableListOf<Node>().apply {
                    nodes.forEach { add(it) }
                    add(node)
                },
                    allowedExtraSmallCaves,
                    extraSmallCaves + 1,
                    mutableMapOf<SmallCaveNode, Int>().apply {
                        seenSmallCaves.forEach { put(it.key, it.value) }
                        put(node as SmallCaveNode, get(node) ?: 0 + 1)
                    })
            } else {
                null
            }
        }
        node is SmallCaveNode -> {
            Path(mutableListOf<Node>().apply {
                nodes.forEach { add(it) }
                add(node)
            },
                allowedExtraSmallCaves,
                extraSmallCaves,
                mutableMapOf<SmallCaveNode, Int>().apply {
                    seenSmallCaves.forEach { put(it.key, it.value) }
                    put(node, 0)
                })
        }
        else -> {
            Path(
                mutableListOf<Node>().apply {
                    nodes.forEach { add(it) }
                    add(node)
                },
                allowedExtraSmallCaves,
                extraSmallCaves,
                mutableMapOf<SmallCaveNode, Int>().apply { seenSmallCaves.forEach { put(it.key, it.value) }}
            )
        }
    }
}

class CaveGraph(val connections: Map<Node, Set<Node>>) {
    fun findPaths(allowedExtraSmallCaves: Int = 0): List<Path> {
        val paths = mutableListOf<Path>()
        val pathStack = Stack<Path>()
        pathStack.push(Path(mutableListOf(StartNode()), allowedExtraSmallCaves = allowedExtraSmallCaves))

        while (pathStack.isNotEmpty()) {
            val currPath = pathStack.pop()
            if (currPath.latestNode is EndNode) {
                paths.add(currPath)
                continue
            }
            connections[currPath.latestNode]?.forEach { node ->
                currPath.addNode(node)?.let {
                    pathStack.add(it)
                }
            }
        }

        return paths
    }
}
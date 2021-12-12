package day12

import org.junit.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CaveGraphTest {

    @Test
    fun `parse graph correctly`() {
        val graph = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent().split("\n").parseCaveGraph()

        assertEquals(6, graph.connections.size)
        val startNode = graph.connections[StartNode()]!!
        val endNode = graph.connections[EndNode()]!!
        val bigANode = graph.connections[BigCaveNode("A")]!!
        val cNode = graph.connections[SmallCaveNode("c")]!!
        val bNode = graph.connections[SmallCaveNode("b")]!!
        val dNode = graph.connections[SmallCaveNode("d")]!!

        assertEquals(2, startNode.size)
        assertTrue(startNode.contains(BigCaveNode("A")))
        assertTrue(startNode.contains(SmallCaveNode("b")))

        assertEquals(2, endNode.size)
        assertTrue(endNode.contains(BigCaveNode("A")))
        assertTrue(endNode.contains(SmallCaveNode("b")))

        assertEquals(4, bigANode.size)
        assertTrue(bigANode.contains(StartNode()))
        assertTrue(bigANode.contains(EndNode()))
        assertTrue(bigANode.contains(SmallCaveNode("c")))
        assertTrue(bigANode.contains(SmallCaveNode("b")))

        assertEquals(1, cNode.size)
        assertTrue(cNode.contains(BigCaveNode("A")))

        assertEquals(4, bNode.size)
        assertTrue(bNode.contains(StartNode()))
        assertTrue(bNode.contains(EndNode()))
        assertTrue(bNode.contains(BigCaveNode("A")))
        assertTrue(bNode.contains(SmallCaveNode("d")))

        assertEquals(1, dNode.size)
        assertTrue(dNode.contains(SmallCaveNode("b")))
    }

    @Test
    fun `check number of paths`() {
        val graph = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent().split("\n").parseCaveGraph()

        assertEquals(10, graph.findPaths().size)
    }
    @Test
    fun `check number of paths extra small cave`() {
        val graph = """
            start-A
            start-b
            A-c
            A-b
            b-d
            A-end
            b-end
        """.trimIndent().split("\n").parseCaveGraph()

        assertEquals(36, graph.findPaths(allowedExtraSmallCaves = 1).size)
    }


    @Test
    fun `check slightly larger graph number of paths`() {
        val graph = """
            dc-end
            HN-start
            start-kj
            dc-start
            dc-HN
            LN-dc
            HN-end
            kj-sa
            kj-HN
            kj-dc
        """.trimIndent().split("\n").parseCaveGraph()

        assertEquals(19, graph.findPaths().size)
    }

    @Test
    fun `check even larger graph number of paths`() {
        val graph = """
            fs-end
            he-DX
            fs-he
            start-DX
            pj-DX
            end-zg
            zg-sl
            zg-pj
            pj-he
            RW-he
            fs-DX
            pj-RW
            zg-RW
            start-pj
            he-WI
            zg-he
            pj-fs
            start-RW
        """.trimIndent().split("\n").parseCaveGraph()

        assertEquals(226, graph.findPaths().size)
    }
}
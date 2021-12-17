package day16

import BaseDayObjectTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day16Test : BaseDayObjectTest(Day16(), partOneExpected = 960, partTwoExpected = 12301926782560) {
    val binaryOne = "110100101111111000101000"

    val hexOne = "8A004A801A8002F478"
    val hexTwo = "620080001611562C8802118E34"
    val hexThree = "520080001611562C8802118E34"

    @Test
    fun `check hex to binary`() {
        val binaryString = "0123456789ABCDEF".hexToBinary()
        assertEquals("0000000100100011010001010110011110001001101010111100110111101111", binaryString)
    }

    @Test
    fun `check extract version from binary`() {
        assertEquals(1, "2".hexToBinary().extractVersion())
        assertEquals(1, "3".hexToBinary().extractVersion())
        assertEquals(2, "4".hexToBinary().extractVersion())
        assertEquals(2, "5".hexToBinary().extractVersion())
        assertEquals(3, "6".hexToBinary().extractVersion())
        assertEquals(3, "7".hexToBinary().extractVersion())
        assertEquals(4, "8".hexToBinary().extractVersion())
        assertEquals(4, "9".hexToBinary().extractVersion())
        assertEquals(5, "A".hexToBinary().extractVersion())
        assertEquals(5, "B".hexToBinary().extractVersion())
        assertEquals(6, "C".hexToBinary().extractVersion())
        assertEquals(6, "D".hexToBinary().extractVersion())
        assertEquals(7, "E".hexToBinary().extractVersion())
        assertEquals(7, "F".hexToBinary().extractVersion())
    }

    @Test
    fun `check extract header from binary`() {
        assertEquals(Header(6, PacketType.LITERAL), binaryOne.extractHeader())
        assertEquals(Header(4, PacketType.MIN), hexOne.hexToBinary().extractHeader())
        assertEquals(Header(3, PacketType.SUM), hexTwo.hexToBinary().extractHeader())
    }

    @Test
    fun `parses literal packet`() {
        val (parsed, _) = binaryOne.parsePacket()
        assertTrue(parsed is LiteralPacket)
        assertEquals(2021, parsed.value)

        val (parsed2, _) = "11010001010".parsePacket()
        assertTrue(parsed2 is LiteralPacket)
        assertEquals(10, parsed2.value)

        val (parsed3, _) = "0101001000100100".parsePacket()
        assertTrue(parsed3 is LiteralPacket)
        assertEquals(20, parsed3.value)

        val (parsed4, _) = "00111000000000000110111101000101001010010001001000000000".parsePacket(22)
        assertTrue(parsed4 is LiteralPacket)
        assertEquals(10, parsed4.value)
    }

    @Test
    fun `parses operator packet with 0 length type`() {
        val (parsed, _) = "00111000000000000110111101000101001010010001001000000000".parsePacket()
        assertTrue(parsed is OperatorPacket)
        assertEquals(Header(1, PacketType.LESS_THAN), parsed.header)
        assertEquals(2, parsed.subPackets.size)
        assertTrue(parsed.subPackets[0] is LiteralPacket)
        assertEquals(10, (parsed.subPackets[0] as LiteralPacket).value)
        assertTrue(parsed.subPackets[1] is LiteralPacket)
        assertEquals(20, (parsed.subPackets[1] as LiteralPacket).value)
    }

    @Test
    fun `parses operator packet with 1 length type`() {
        val (parsed, _) = "11101110000000001101010000001100100000100011000001100000".parsePacket()
        assertTrue(parsed is OperatorPacket)
        assertEquals(Header(7, PacketType.MAX), parsed.header)
        assertEquals(3, parsed.subPackets.size)
        assertTrue(parsed.subPackets[0] is LiteralPacket)
        assertEquals(1, (parsed.subPackets[0] as LiteralPacket).value)
        assertTrue(parsed.subPackets[1] is LiteralPacket)
        assertEquals(2, (parsed.subPackets[1] as LiteralPacket).value)
        assertTrue(parsed.subPackets[2] is LiteralPacket)
        assertEquals(3, (parsed.subPackets[2] as LiteralPacket).value)
    }

    @Test
    fun `check part one impl`() {
        val day16 = Day16()
        runBlocking {
            val count1 = day16.partOne("8A004A801A8002F478")
            assertEquals(16, count1)

            val count2 = day16.partOne("620080001611562C8802118E34")
            assertEquals(12, count2)

            val count3 = day16.partOne("C0015000016115A2E0802F182340")
            assertEquals(23, count3)

            val count4 = day16.partOne("A0016C880162017C3686B18A3D4780")
            assertEquals(31, count4)
        }
    }

    @Test
    fun `apply sums`() {
        val day16 = Day16()
        runBlocking {
            val sums = day16.partTwo("C200B40A82")
            assertEquals(3, sums)
        }
    }

    @Test
    fun `apply products`() {
        with(Day16()) {
            runBlocking {
                val products = partTwo("04005AC33890")
                assertEquals(54, products)
            }
        }
    }

    @Test
    fun `apply mins`() {
        with(Day16()) {
            runBlocking {
                val mins = partTwo("880086C3E88112")
                assertEquals(7, mins)
            }
        }
    }

    @Test
    fun `apply maxes`() {
        with(Day16()) {
            runBlocking {
                val maxes = partTwo("CE00C43D881120")
                assertEquals(9, maxes)
            }
        }
    }

    @Test
    fun `apply less than`() {
        with(Day16()) {
            runBlocking {
                val lessThanValue = partTwo("D8005AC2A8F0")
                assertEquals(1, lessThanValue)
            }
        }
    }

    @Test
    fun `apply greater than`() {
        with(Day16()) {
            runBlocking {
                val greaterThan = partTwo("F600BC2D8F")
                assertEquals(0, greaterThan)
            }
        }
    }

    @Test
    fun `apply equal to`() {
        with(Day16()) {
            runBlocking {
                val equalTo = partTwo("9C005AC2F8F0")
                assertEquals(0, equalTo)
            }
        }
    }

    @Test
    fun `apply sum equalTo product`() {
        with(Day16()) {
            runBlocking {
                val sumEqualToProduct = partTwo("9C0141080250320F1802104A08")
                assertEquals(1, sumEqualToProduct)
            }
        }
    }
}
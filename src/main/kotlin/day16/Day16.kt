package day16

import BaseDay
import java.util.*

class Day16 : BaseDay(16, "Packet Decoder") {
    override suspend fun partOne(input: String) = input.hexToBinary()
        .parsePacket()
        .let {
            countVersions(it.first)
        }

    private fun countVersions(root: Packet): Int {
        return when (root) {
            is LiteralPacket -> {
                root.header.version
            }
            is OperatorPacket -> {
                root.header.version + root.subPackets.sumOf { countVersions(it) }
            }
        }
    }

    override suspend fun partTwo(input: String) = input.hexToBinary().parsePacket().first.value
}

fun String.parsePacket(currentPosition: Int = 0, currentLevel: Int = 0): Pair<Packet, Int> {
    var position = currentPosition
    val header = extractHeader(position).also { position += 6 }
    val packet: Packet

    if (header.type == PacketType.LITERAL) {
        val (parsedPacket, newPosition) = parseLiteralPacket(header, position)
        packet = parsedPacket
        position = newPosition
    } else {
        val lengthType = get(position).digitToInt().also { position++ }
        if (lengthType == 0) {
            val totalLengthOfSubPackets = substring(position, position + 15).toInt(2).also { position += 15 }
            var currentParsedLength = 0
            val subPackets = mutableListOf<Packet>().apply {
                while (currentParsedLength < totalLengthOfSubPackets) {
                    val (subPacket, newPosition) = parsePacket(position, currentLevel + 1)
                    add(subPacket)
                    currentParsedLength += (newPosition - position)
                    position = newPosition
                }
            }

            packet = createOperatorPacket(header, subPackets)
        } else {
            val numberOfSubPackets = substring(position, position + 11).toInt(2).also { position += 11 }
            val subPackets = mutableListOf<Packet>().apply {
                repeat(numberOfSubPackets) {
                    val (subPacket, newPosition) = parsePacket(position, currentLevel + 1)
                    add(subPacket)
                    position = newPosition
                }
            }

            packet = createOperatorPacket(header, subPackets)
        }
    }

    return Pair(packet.apply { level = currentLevel }, position)
}

fun createOperatorPacket(header: Header, subPackets: List<Packet>) = when(header.type) {
    PacketType.SUM -> SumPacket(header, subPackets)
    PacketType.PRODUCT -> ProductPacket(header, subPackets)
    PacketType.MIN -> MinPacket(header, subPackets)
    PacketType.MAX -> MaxPacket(header, subPackets)
    PacketType.GREATER_THAN -> GreaterThanPacket(header, subPackets)
    PacketType.LESS_THAN -> LessThanPacket(header, subPackets)
    PacketType.EQUAL -> EqualToPacket(header, subPackets)
    else -> throw IllegalArgumentException("Cannot create operator packet from type: ${header.type}")
}

fun String.parseLiteralPacket(header: Header, currentPosition: Int): Pair<LiteralPacket, Int> {
    val chunks = mutableListOf<String>()
    var position = currentPosition
    while (get(position) != '0') {
        chunks.add(slice(position + 1..position + 4))
        position += 5
    }

    chunks.add(slice(position + 1..position + 4))
    position += 5

    return Pair(LiteralPacket(header, chunks.joinToString("").toLong(2)), position)
}

fun String.extractHeader(position: Int = 0) = Header(extractVersion(position), extractType(position + 3))

fun String.extractVersion(position: Int = 0) = slice(position..(position + 2)).toInt(2)
fun String.extractType(position: Int = 0) = slice(position..(position + 2)).toInt(2).toPacketType()

fun String.hexToBinary() = map { it.toBinary() }.joinToString("")

fun Char.toBinary() = when (this) {
    '0' -> "0000"
    '1' -> "0001"
    '2' -> "0010"
    '3' -> "0011"
    '4' -> "0100"
    '5' -> "0101"
    '6' -> "0110"
    '7' -> "0111"
    '8' -> "1000"
    '9' -> "1001"
    'A' -> "1010"
    'B' -> "1011"
    'C' -> "1100"
    'D' -> "1101"
    'E' -> "1110"
    'F' -> "1111"
    else -> throw IllegalArgumentException("Non hex character representation: $this")
}

fun Int.toPacketType() = PacketType.values().find { it.typeValue == this } ?: PacketType.OTHER

enum class PacketType(val typeValue: Int) {
    SUM(0),
    PRODUCT(1),
    MIN(2),
    MAX(3),
    LITERAL(4),
    GREATER_THAN(5),
    LESS_THAN(6),
    EQUAL(7),
    OTHER(999);
}

data class Header(val version: Int, val type: PacketType)

sealed class Packet(val header: Header) {
    var level = 0
    abstract val value: Long
    abstract val height: Int

    val width: Int
    get() {
        val queue = LinkedList<Packet>().apply { add(this@Packet) }
        var maxWidth = 1
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current is OperatorPacket) {
                if (current.subPackets.size > maxWidth) {
                    maxWidth = current.subPackets.size
                }
                queue.addAll(current.subPackets)
            }
        }
        return maxWidth
    }
}
class LiteralPacket(header: Header, override val value: Long) : Packet(header) {
    override val height
    get() = level
}
abstract class OperatorPacket(header: Header, val subPackets: List<Packet>) : Packet(header) {
    override val height = subPackets.maxOf { it.height }
}

class SumPacket(header: Header, subPackets: List<Packet>): OperatorPacket(header, subPackets) {
    override val value = subPackets.sumOf { it.value }
}

class ProductPacket(header: Header, subPackets: List<Packet>): OperatorPacket(header, subPackets) {
    override val value = subPackets.fold(1L) { acc, subPacket -> acc * subPacket.value }
}

class MinPacket(header: Header, subPackets: List<Packet>): OperatorPacket(header, subPackets) {
    override val value = subPackets.minOf { it.value }
}

class MaxPacket(header: Header, subPackets: List<Packet>): OperatorPacket(header, subPackets) {
    override val value = subPackets.maxOf { it.value }
}

class GreaterThanPacket(header: Header, subPackets: List<Packet>): OperatorPacket(header, subPackets) {
    override val value = if (subPackets[0].value > subPackets[1].value) 1L else 0L
}

class LessThanPacket(header: Header, subPackets: List<Packet>): OperatorPacket(header, subPackets) {
    override val value = if (subPackets[0].value < subPackets[1].value) 1L else 0L
}

class EqualToPacket(header: Header, subPackets: List<Packet>): OperatorPacket(header, subPackets) {
    override val value = if (subPackets[0].value == subPackets[1].value) 1L else 0L
}